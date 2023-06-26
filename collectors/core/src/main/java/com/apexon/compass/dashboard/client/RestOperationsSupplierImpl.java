package com.apexon.compass.dashboard.client;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Component
public class RestOperationsSupplierImpl implements RestOperationsSupplier {

	@Override
	public RestOperations get() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(20000);
		return new RestTemplate(requestFactory);
	}

}
