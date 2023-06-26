package com.apexon.compass.dashboard.client;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;

/**
 * Supplier that returns an instance of RestOperations
 */
public interface RestOperationsSupplier {

	RestOperations get();

}
