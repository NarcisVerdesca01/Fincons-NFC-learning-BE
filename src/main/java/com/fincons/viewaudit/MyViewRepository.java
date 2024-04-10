package com.fincons.viewaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;



@NoRepositoryBean
public interface MyViewRepository<T, K> extends JpaRepository<T, K> {

    long count();

    List<T> findAll();

}
