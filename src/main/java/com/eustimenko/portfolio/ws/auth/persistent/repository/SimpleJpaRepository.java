package com.eustimenko.portfolio.ws.auth.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
interface SimpleJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
}
