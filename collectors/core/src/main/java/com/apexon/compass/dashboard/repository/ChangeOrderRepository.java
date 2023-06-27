package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.ChangeOrder;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link ChangeOrder} data.
 */
public interface ChangeOrderRepository extends CrudRepository<ChangeOrder, ObjectId> {

	ChangeOrder findByChangeID(String changeItem);

}
