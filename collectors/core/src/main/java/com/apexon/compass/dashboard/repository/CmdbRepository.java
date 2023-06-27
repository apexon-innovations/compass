package com.apexon.compass.dashboard.repository;

import com.apexon.compass.dashboard.model.Cmdb;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for {@link Cmdb} data.
 */
public interface CmdbRepository extends CrudRepository<Cmdb, ObjectId> {

	Cmdb findByConfigurationItem(String configurationItem);

	List<Cmdb> findAllByConfigurationItemContainingOrCommonNameContainingAllIgnoreCase(String configItemFilter,
			String commonNameFilter);

	List<Cmdb> findAllByConfigurationItemContainingOrConfigurationKeyContainingOrLegacyServiceManagerNameContainingOrCommonNameContainingAllIgnoreCase(
			String configItemFilter, String configKeyFilter, String legacyNameFilter, String commonNameFilter);

	Page<Cmdb> findAllByItemTypeAndValidConfigItemAndIdIn(String itemType, boolean valid, List<ObjectId> idList,
			Pageable pageable);

	Page<Cmdb> findAllByItemTypeAndConfigurationItemContainingIgnoreCaseAndValidConfigItem(String itemType,
			String configurationItem, Pageable pageable, boolean valid);

	Cmdb findByConfigurationItemIgnoreCase(String configurationItem);

	List<Cmdb> findAllByItemType(String type);

	List<Cmdb> findAllByItemTypeAndValidConfigItem(String type, boolean valid);

	Cmdb findByConfigurationItemAndItemType(String confiugrationItem, String itemType);

	Iterable<Cmdb> findAllByValidConfigItem(boolean validFlag);

	Cmdb findByConfigurationItemIgnoreCaseOrCommonNameIgnoreCase(String configurationItem, String commonName);

	Cmdb findByItemTypeAndCommonNameContainingIgnoreCase(String itemType, String commonName);

	Cmdb findByItemTypeAndConfigurationItemIgnoreCase(String itemType, String configurationItem);

	List<Cmdb> findByItemTypeAndOwnerDeptAndValidConfigItem(String itemType, String ownerDept, boolean isValid);

	Cmdb findByConfigurationItemAndItemTypeAndValidConfigItem(String configurationItem, String itemType,
			boolean isValid);

	List<Cmdb> findByBusinessOwnerAndItemTypeAndValidConfigItem(String businessOwner, String itemType, boolean isValid);

	List<Cmdb> findByCommonNameOrderByTimestamp(String commonName);

}
