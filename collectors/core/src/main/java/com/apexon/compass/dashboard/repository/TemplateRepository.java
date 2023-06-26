package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Template;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * {@link Template} repository.
 */
public interface TemplateRepository
		extends PagingAndSortingRepository<Template, ObjectId>, CrudRepository<Template, ObjectId> {

	Template findByTemplate(String template);

}
