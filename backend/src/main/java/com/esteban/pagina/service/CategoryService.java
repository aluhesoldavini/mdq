package com.esteban.pagina.service;

import com.esteban.pagina.model.Category;
import com.esteban.pagina.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthenticationType authType;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, AuthenticationType authType){
        this.categoryRepository = categoryRepository;
        this.authType = authType;
    }


    public List<Category> getAll() {
        try {
            List<Category> categories = new ArrayList<>();
            if(authType.type() == 0){
                categories.addAll(categoryRepository.findByMinoristaTrue());
                categories.forEach(category -> category.setMayorista(false));
            } else {
                categoryRepository.findAll().forEach(categories::add);
            }
            return categories;
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        try {
            if(authType.type() == 0){
                Optional<Category> optCat = categoryRepository.findByidCategoriaAndMinoristaTrue(categoryId);
                if(optCat.isPresent()) {
                    Category category = optCat.get();
                    category.setMayorista(false);
                    return Optional.of(category);
                }
                return Optional.empty();
            }
            return categoryRepository.findById(categoryId);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Category> getCategoryByName(String name) {
        try {
            if(authType.type() == 0){
                Optional<Category> optCat = categoryRepository.findByNombreAndMinoristaTrue(name);
                if(optCat.isPresent()) {
                    Category category = optCat.get();
                    category.setMayorista(false);
                    return Optional.of(category);
                }
                return Optional.empty();
            }
            return categoryRepository.findByNombre(name);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Category saveOrUpdate(Category category) {
        try {
            categoryRepository.save(category);
            return category;
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteCategoryById(Long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return "";
        } catch (Exception e) {
            return "Error interno del servidor mientras se eliminaba la categoria";
        }
    }
}
