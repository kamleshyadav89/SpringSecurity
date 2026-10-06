package com.itvedant.securityRepositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itvedant.Security_d.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
