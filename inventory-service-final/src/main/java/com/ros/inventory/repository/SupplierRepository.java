package com.ros.inventory.repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ros.inventory.model.supplier.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
	
	@Query(value = "select s from Restaurant r inner join r.supplier s where r.restaurantId =:id "
			+ "order by s.supplierName desc")
	Page<Supplier> getSupplierList(@Param(value = "id") UUID restaurantId, Pageable page);
	
	@Query(value = "select s from Restaurant r inner join r.supplier s "
			+ "where r.restaurantId =:id and "
			+ "UPPER(s.supplierName) like UPPER(concat('%', concat(:supplierName, '%'))) ")
	public List<Supplier> getSupplierList(@Param(value = "id") UUID restaurantId, Pageable pageable, @Param(value = "supplierName") String supplierName);

}
