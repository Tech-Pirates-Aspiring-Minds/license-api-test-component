package com.kg.web.service;

import com.kg.web.model.KeyStore;

public interface IKeyStoreService {

	KeyStore getKey(String appName);

	boolean save(KeyStore data);

}
