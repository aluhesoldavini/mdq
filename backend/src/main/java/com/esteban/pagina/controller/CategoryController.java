package com.esteban.pagina.controller;

import com.esteban.pagina.model.Category;
import com.esteban.pagina.model.Product;
import com.esteban.pagina.service.CategoryService;
import com.esteban.pagina.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ErrorHandler errorHandler;

    @Autowired
    public CategoryController(CategoryService categoryService, ErrorHandler errorHandler, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
        this.errorHandler = errorHandler;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Category> categories = categoryService.getAll();

        if(categories == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se extraían categorías de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(categories.isEmpty()){
            return errorHandler.createErrorResponse("No hay categorías en la base de datos", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(categories), HttpStatus.OK);
    }

    @GetMapping("/{categoryIdOrName}")
    public ResponseEntity<Object> getCategoryById(@PathVariable("categoryIdOrName") String categoryIdOrName){
        Optional<Category> optCategory;

        if(categoryIdOrName.matches("\\d+")){
            Long categoryId = Long.valueOf(categoryIdOrName);

            if(categoryId < 1) {
                return errorHandler.createErrorResponse("Identificador de categoría inválido. Solo se permiten números enteros positivos", HttpStatus.BAD_REQUEST);
            }

            optCategory = categoryService.getCategoryById(categoryId);
        } else {
            optCategory = categoryService.getCategoryByName(categoryIdOrName);
        }

        if(optCategory.isEmpty()){
            return errorHandler.createErrorResponse("No se encontró la categoría", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optCategory, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> saveCategory(@RequestBody Category category){
        if(category.getIdCategoria() != null){
            return errorHandler.createErrorResponse("El identificador de la categoría se genera automáticamente", HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Category> optCategory = categoryService.getCategoryByName(category.getNombre());

        if(optCategory.isPresent()){
            return errorHandler.createErrorResponse("Ya existe una categoría con ese nombre", HttpStatus.NOT_ACCEPTABLE);
        }

        String errorMessage = validateCategory(category);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Category createdCategory = categoryService.saveOrUpdate(category);

        if(createdCategory == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se creaba la categoría", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCategory(@RequestBody Category category){
        final Long newCatId = category.getIdCategoria();

        if(newCatId == null){
            return errorHandler.createErrorResponse("Falta el identificador de la categoría", HttpStatus.BAD_REQUEST);
        }

        Optional<Category> optExistingCategory = categoryService.getCategoryById(newCatId);

        if(optExistingCategory.isEmpty()){
            return errorHandler.createErrorResponse("No existe una categoría con ese identificador", HttpStatus.NOT_FOUND);
        }

        if(category.getNombre() == null || category.getNombre().isEmpty()){
            category.setNombre(optExistingCategory.get().getNombre());
        }

        if(category.equals(optExistingCategory.get())){
            return errorHandler.createErrorResponse("La categoría no tiene cambios", HttpStatus.BAD_REQUEST);
        }

        if(!category.getNombre().equals(optExistingCategory.get().getNombre())){
            Optional<Category> optCategory = categoryService.getCategoryByName(category.getNombre());

            if(optCategory.isPresent()){
                return errorHandler.createErrorResponse("Ya existe una categoría con ese nombre", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        String errorMessage = validateCategory(category);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Category updatedCategory = categoryService.saveOrUpdate(category);

        if(updatedCategory == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se actualizaba la categoría", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId:[0-9]+}")
    public ResponseEntity<Object> deleteCategoryById(@PathVariable("categoryId") Long categoryId){
        Optional<Category> optCatToDel = categoryService.getCategoryById(categoryId);

        if(optCatToDel.isEmpty()){
            return errorHandler.createErrorResponse("No se encontro la categoría", HttpStatus.BAD_REQUEST);
        }

        List<Product> products = productService.getAll();

        if(products == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se leian productos de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getCategory().getIdCategoria() == categoryId)
                .toList();

        if(!filteredProducts.isEmpty()){
            return errorHandler.createErrorResponse("No se puede eliminar. Esta categoría tiene productos asociados", HttpStatus.NOT_ACCEPTABLE);
        }

        String delResponse = categoryService.deleteCategoryById(categoryId);

        if(!delResponse.isEmpty()){
            return errorHandler.createErrorResponse(delResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(optCatToDel, HttpStatus.OK);
    }



    private String validateCategory(Category newCategory){
        final String newCatName = newCategory.getNombre();
        final boolean newCatMinor = newCategory.isMayorista();
        final boolean newCatMayor = newCategory.isMinorista();

        if(!(newCatMinor || newCatMayor)){
            return "La categoría debe ser minorista o mayorista";
        } if(newCatName.length() < 3 || newCatName.length() > 15){
            return "El nombre debe contener entre 3 y 15 caractéres";
        } if(!newCatName.matches("^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\d]+$")) {
            return "El nombre de la categoría solo puede contener letras, numeros y espacios";
        } if(newCatName.matches("\\d+")){
            return "El nombre de la categoría debe contener al menos una letra";
        }

        return "";
    }
}
