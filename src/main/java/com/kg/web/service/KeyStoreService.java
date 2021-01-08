package com.kg.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.web.model.KeyStore;
import com.kg.web.repository.KeyStoreRepository;

@Service
public class KeyStoreService implements IKeyStoreService {

	@Autowired
	private KeyStoreRepository repository;

	public KeyStore getKey(String appName) {

		Optional<KeyStore> store = repository.findById(appName);
		if (store.isPresent()) {
			return store.get();
		}
		return null;
	}

	public boolean save(KeyStore store) {

		Optional<KeyStore> exist = repository.findById(store.getName());
		if (exist.isPresent()) {
			KeyStore data = exist.get();
			data.setKey(store.getKey());
			repository.save(data);
		} else {
			repository.save(store);
		}
		return true;
	}

}
