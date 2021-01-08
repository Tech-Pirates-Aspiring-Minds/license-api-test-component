package com.kg.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kg.web.model.KeyStore;

@Repository
public interface KeyStoreRepository extends JpaRepository<KeyStore, String> {

}
