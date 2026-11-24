package com.excursiones.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T, K> {
    K insert(T entity) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(K id) throws SQLException;
}