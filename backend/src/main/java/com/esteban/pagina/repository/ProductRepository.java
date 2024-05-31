package com.esteban.pagina.repository;

import com.esteban.pagina.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByMinoristaTrue();

    Optional<Product> findByidProductoAndMinoristaTrue(Long id);

    Optional<Product> findByNombreAndMinoristaTrue(String name);
    Optional<Product> findByNombre(String name);

    List<Product> findByNombreContainingAndMinoristaTrue(String name);
    List<Product> findByNombreContaining(String name);
}
