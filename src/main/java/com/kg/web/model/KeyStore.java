package com.kg.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "key_store")
@Entity
public class KeyStore {

	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "licence_key")
	private String key;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
