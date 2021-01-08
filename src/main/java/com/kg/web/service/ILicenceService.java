package com.kg.web.service;

import com.kg.web.model.Licence;

public interface ILicenceService {

	boolean register(Licence data);

	String getKey();
	
	boolean isValid();

}
