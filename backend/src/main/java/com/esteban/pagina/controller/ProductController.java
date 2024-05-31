package com.esteban.pagina.controller;

import com.esteban.pagina.model.Category;
import com.esteban.pagina.model.Product;
import com.esteban.pagina.service.CategoryService;
import com.esteban.pagina.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ErrorHandler errorHandler;

    public ProductController(ProductService productService, CategoryService categoryService, ErrorHandler errorHandler){
        this.productService = productService;
        this.categoryService = categoryService;
        this.errorHandler = errorHandler;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Product> products = productService.getAll();

        if(products == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se extraían productos de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(products.isEmpty()){
            return errorHandler.createErrorResponse("No hay productos en la base de datos", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(products), HttpStatus.OK);
    }

    @GetMapping("/{productIdOrName}")
    public ResponseEntity<Object> getProductById(@PathVariable("productIdOrName") String productIdOrName){
        Optional<Product> optProduct;

        if(productIdOrName.matches("\\d+")){
            Long productId = Long.valueOf(productIdOrName);

            if(productId < 1) {
                return errorHandler.createErrorResponse("Identificador de producto inválido. Solo se permiten números enteros positivos", HttpStatus.BAD_REQUEST);
            }

            optProduct = productService.getProductById(productId);
        } else {
            optProduct = productService.getProductByName(productIdOrName);
        }

        if(optProduct.isEmpty()){
            return errorHandler.createErrorResponse("No se encontró el producto", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optProduct, HttpStatus.OK);
    }

    @GetMapping("/contains/{productName}")
    public ResponseEntity<Object> getProductByNameContains(@PathVariable("productName") String productName){
        if(productName.isEmpty()) {
            return errorHandler.createErrorResponse("Falta el nombre del producto", HttpStatus.BAD_REQUEST);
        }

        List<Product> products = productService.getProductsByNameContains(productName);

        if(products == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se extraían productos de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(products.isEmpty()){
            return errorHandler.createErrorResponse("No hay productos que coincidan con ese nombre", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(products), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> saveProduct(@RequestBody Product product){
        if(product.getIdProducto() != null){
            return errorHandler.createErrorResponse("El identificador del producto se genera automáticamente", HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Product> optProduct = productService.getProductByName(product.getNombre());

        if(optProduct.isPresent()){
            return errorHandler.createErrorResponse("Ya existe un producto con ese nombre", HttpStatus.NOT_ACCEPTABLE);
        }

        Category category = product.getCategory();

        if(category == null || category.getIdCategoria() == null){
            return errorHandler.createErrorResponse("Falta la categoría del producto", HttpStatus.BAD_REQUEST);
        }

        Optional<Category> existingCategoryOpt = categoryService.getCategoryById(category.getIdCategoria());

        if(existingCategoryOpt.isEmpty()){
            return errorHandler.createErrorResponse("No se encontro una categoría con ese identificador", HttpStatus.NOT_FOUND);
        }

        product.setCategory(existingCategoryOpt.get());

        String errorMessage = validateProduct(product);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Product createdProduct = productService.saveOrUpdate(product);

        if(createdProduct == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se creaba el producto", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product){
        final Long newProdId = product.getIdProducto();

        if(newProdId == null){
            return errorHandler.createErrorResponse("Falta el identificador del producto", HttpStatus.BAD_REQUEST);
        }

        Optional<Product> optExistingProduct = productService.getProductById(newProdId);

        if(optExistingProduct.isEmpty()){
            return errorHandler.createErrorResponse("No existe un producto con ese identificador", HttpStatus.NOT_FOUND);
        }

        if(product.getNombre() == null || product.getNombre().isEmpty()){
            product.setNombre(optExistingProduct.get().getNombre());
        }

        Category category = product.getCategory();

        if(category == null || category.getIdCategoria() == null){
            return errorHandler.createErrorResponse("Falta la categoría del producto", HttpStatus.BAD_REQUEST);
        }

        Optional<Category> existingCategoryOpt = categoryService.getCategoryById(category.getIdCategoria());

        if(existingCategoryOpt.isEmpty()){
            return errorHandler.createErrorResponse("No se encontro una categoría con ese identificador", HttpStatus.NOT_FOUND);
        }

        product.setCategory(existingCategoryOpt.get());

        if(product.equals(optExistingProduct.get())){
            return errorHandler.createErrorResponse("El producto no tiene cambios", HttpStatus.BAD_REQUEST);
        }

        if(!product.getNombre().equals(optExistingProduct.get().getNombre())){
            Optional<Product> optProduct = productService.getProductByName(product.getNombre());

            if(optProduct.isPresent()){
                return errorHandler.createErrorResponse("Ya existe un producto con ese nombre", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        String errorMessage = validateProduct(product);

        if(!errorMessage.isEmpty()){
            return errorHandler.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Product updatedProduct = productService.saveOrUpdate(product);

        if(updatedProduct == null){
            return errorHandler.createErrorResponse("Error interno del servidor mientras se actualizaba el producto", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId:[0-9]+}")
    public ResponseEntity<Object> deleteProductById(@PathVariable("productId") Long productId){
        Optional<Product> optProductToDel = productService.getProductById(productId);

        if(optProductToDel.isEmpty()){
            return errorHandler.createErrorResponse("No se encontro el producto", HttpStatus.BAD_REQUEST);
        }

        String delResponse = productService.deleteProductById(productId);

        if(!delResponse.isEmpty()){
            return errorHandler.createErrorResponse(delResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(optProductToDel, HttpStatus.OK);
    }



    private String validateProduct(Product product){
        final String newProdName = product.getNombre();
        final boolean newProdMinor = product.isMinorista();
        final boolean newProdMayor = product.isMayorista();
        final String newProdImg = product.getImagenes();
        final String newProdDesc = product.getDescripcion();

        if(newProdName.length() < 3 || newProdName.length() > 30){
            return "El nombre debe contener entre 3 y 30 caractéres";
        } if(!(newProdMinor || newProdMayor)){
            return "El producto debe ser mayorista o minorista";
        } if(newProdImg != null){
            if (newProdImg.length() < 10 || newProdImg.length() > 500){
                return "Las imágenes deben contener entre 10 y 500 caractéres";
            } if(!newProdImg.contains(";")){
                return "Las imágenes deben contener ';'";
            }
        } if(newProdDesc.length() < 3 || newProdDesc.length() > 2000){
            return "La descripción debe contener entre 3 y 2000 caractéres";
        } if(!product.isMayorista() && !product.getCategory().isMinorista()){
            return "El producto solo puede ir en una categoría minorista";
        } if(!product.isMinorista() && !product.getCategory().isMayorista()){
            return "El producto solo puede ir en una categoría mayorista";
        } if(product.isMinorista() && product.isMayorista()){
            if(!product.getCategory().isMinorista() || !product.getCategory().isMayorista()){
                return "El producto solo puede ir en una categoría minorista y mayorista";
            }
        }

        return "";
    }
}
