package com.esteban.pagina.service;

import com.esteban.pagina.model.Product;
import com.esteban.pagina.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthenticationType authType;

    @Autowired
    public ProductService(ProductRepository categoryRepository, AuthenticationType authType){
        this.productRepository = categoryRepository;
        this.authType = authType;
    }


    public List<Product> getAll() {
        try {
            List<Product> products = new ArrayList<>();
            if(authType.type() == 0){
                products.addAll(productRepository.findByMinoristaTrue());
                products.forEach(product -> {
                    product.setMayorista(false);
                    product.getCategory().setMayorista(false);
                });
            } else {
                productRepository.findAll().forEach(products::add);
            }
            return products;
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Product> getProductById(Long productId) {
        try {
            if(authType.type() == 0){
                Optional<Product> optProd = productRepository.findByidProductoAndMinoristaTrue(productId);
                if(optProd.isPresent()) {
                    Product product = optProd.get();
                    product.setMayorista(false);
                    product.getCategory().setMayorista(false);
                    return Optional.of(product);
                }
                return Optional.empty();
            }
            return productRepository.findById(productId);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Product> getProductByName(String name) {
        try {
            if(authType.type() == 0){
                Optional<Product> optProd = productRepository.findByNombreAndMinoristaTrue(name);
                if(optProd.isPresent()) {
                    Product product = optProd.get();
                    product.setMayorista(false);
                    product.getCategory().setMayorista(false);
                    return Optional.of(product);
                }
                return Optional.empty();
            }
            return productRepository.findByNombre(name);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public List<Product> getProductsByNameContains(String name) {
        try {
            List<Product> products = new ArrayList<>();
            if(authType.type() == 0){
                products.addAll(productRepository.findByNombreContainingAndMinoristaTrue(name));
                products.forEach(product -> {
                    product.setMayorista(false);
                    product.getCategory().setMayorista(false);
                });
            } else {
                products.addAll(productRepository.findByNombreContaining(name));
            }
            return products;
        } catch (Exception e) {
            return null;
        }
    }

    public Product saveOrUpdate(Product product) {
        try {
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteProductById(Long productId) {
        try {
            productRepository.deleteById(productId);
            return "";
        } catch (Exception e) {
            return "Error interno del servidor mientras se eliminaba el producto";
        }
    }
}
