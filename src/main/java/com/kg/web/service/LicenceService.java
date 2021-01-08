package com.kg.web.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.kg.web.model.KeyStore;
import com.kg.web.model.Licence;
import com.kg.web.model.RegisterData;
import com.kg.web.util.KeyUtil;

@Service
public class LicenceService implements ILicenceService {

	public static final String APP_NAME = "app001";

	@Autowired
	private IKeyStoreService keyStoreService;

	@Value("${licence.url:#{null}}")
	private String url;

	private boolean validated = false;

	private String key = null;

	@PostConstruct
	private void postConstruct() {

		validated = false;
		KeyStore store = keyStoreService.getKey(APP_NAME);

		if (store != null) {
			key = store.getKey();
		}
		if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(url)) {
			if (validateToken(key)) {
				validated = true;
			} else {
				key = null;
			}
		}
	}

	public boolean isValid() {
		return validated;
	}

	@Override
	public boolean register(Licence licence) {
		if (!StringUtils.isEmpty(licence.getKey())) {
			RestTemplate restTemplate = new RestTemplate();
			String resourceUrl = url;
			RegisterData reqData = new RegisterData();
			reqData.setKey(licence.getKey());

			ResponseEntity<Boolean> response = restTemplate.postForEntity(resourceUrl, reqData, Boolean.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				boolean validKey = response.getBody();
				if (validKey) {
					KeyStore store = new KeyStore();
					store.setKey(licence.getKey());
					store.setName(APP_NAME);
					key = licence.getKey();
					keyStoreService.save(store);
					validated = true;
				}

			}
		}

		return validated;
	}

	private boolean validateToken(String key) {

		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = url + "/" + KeyUtil.getURIKey(key) + "/key-expiry";

		ResponseEntity<Boolean> response = restTemplate.getForEntity(resourceUrl, Boolean.class);
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			return response.getBody();
		}

		return false;

	}

	public String getKey() {
		return this.key;
	}

}
