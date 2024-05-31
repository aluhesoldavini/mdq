package com.esteban.pagina.repository;

import com.esteban.pagina.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findByMinoristaTrue();

    Optional<Category> findByidCategoriaAndMinoristaTrue(Long id);

    Optional<Category> findByNombreAndMinoristaTrue(String name);

    Optional<Category> findByNombre(String name);
}
