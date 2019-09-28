package edu.refactor.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GenericCrudRepository<T> extends CrudRepository<T, Long> {

    @Override
    List<T> findAll();

    @Override
    List<T> findAllById(Iterable<Long> iterable);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> iterable);
}
