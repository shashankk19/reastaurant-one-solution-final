package com.ros.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ros.inventory.controller.dto.SupplierDto;
import com.ros.inventory.controller.dto.supplier.ExternalSupplierDto;
import com.ros.inventory.controller.dto.supplier.InternalSupplierDto;
import com.ros.inventory.controller.dto.supplier.ProductMasterDto;
import com.ros.inventory.exception.SupplierAlreadyExistsException;
import com.ros.inventory.exception.SupplierNotFoundException;
import com.ros.inventory.service.SupplierService;
import com.ros.inventory.exception.ProductNotFoundException;
import com.ros.inventory.exception.RestaurantNotFoundException;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author kalyan.prathapaneni
 *
 */
@RestController
@RequestMapping("/supplier")
@CrossOrigin("*")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	/**
	 * @param externalSupplierDto
	 * @param restaurantId
	 * @return
	 * @throws SupplierAlreadyExistsException
	 * @throws RestaurantNotFoundException
	 */
	@Operation(summary = "Adding External Supplier")
	@PostMapping("/external/{restaurandId}")
	public ResponseEntity<?> postSupplier(@RequestBody ExternalSupplierDto externalSupplierDto,
			@PathVariable(value = "restaurandId") UUID restaurantId)
			throws SupplierAlreadyExistsException, RestaurantNotFoundException {

		ResponseEntity<?> response = new ResponseEntity<>(
				supplierService.addSupplier(externalSupplierDto, restaurantId), HttpStatus.OK);

		return response;
	}

	/**
	 * @param internalSupplierDto
	 * @param restaurantId
	 * @return
	 * @throws RestaurantNotFoundException
	 * @throws SupplierAlreadyExistsException
	 */
	@Operation(summary = "Adding Internal Supplier")
	@PostMapping("/internal/{restaurandId}")
	public ResponseEntity<?> postSupplier(@RequestBody InternalSupplierDto internalSupplierDto,
			@PathVariable(value = "restaurandId") UUID restaurantId)
			throws RestaurantNotFoundException, SupplierAlreadyExistsException {

		ResponseEntity<?> response = new ResponseEntity<>(
				supplierService.addSupplier(internalSupplierDto, restaurantId), HttpStatus.OK);

		return response;
	}

	/**
	 * @param supplierDto
	 * @return
	 * @throws SupplierNotFoundException
	 */
	@Operation(summary = "Editing Supplier")
	@PutMapping("/")
	public ResponseEntity<?> putSupplier(@RequestBody SupplierDto supplierDto) throws SupplierNotFoundException {
		ResponseEntity<?> response = new ResponseEntity<>(supplierService.editSupplier(supplierDto), HttpStatus.OK);

		return response;
	}

	/**
	 * @param supplierId
	 * @param productMasterDto
	 * @return
	 * @throws SupplierNotFoundException
	 */
	@Operation(summary = "Editing Product Master")
	@PutMapping("/{supplierId}/products")
	public ResponseEntity<?> putProductMasters(@PathVariable(value = "supplierId") UUID supplierId,
			@RequestBody List<ProductMasterDto> productMasterDto) throws SupplierNotFoundException {

		ResponseEntity<?> response = new ResponseEntity<>(
				supplierService.updateProductMaster(supplierId, productMasterDto), HttpStatus.OK);

		return response;
	}

	@Operation(summary = "Getting Bank details")
	@GetMapping("/viewSuppliersBankDetails/{supplierId}")
	public ResponseEntity<?> getSuppliersBankDetails(@PathVariable(value = "supplierId") UUID supplierId)
			throws SupplierNotFoundException {
		ResponseEntity<?> response = new ResponseEntity<>(supplierService.viewSupplierBankDetailss(supplierId),
				HttpStatus.OK);

		return response;
	}

	@Operation(summary = "Delete supplier by supplierId")
	@DeleteMapping("/deleteSupplier/{supplierId}")
	public ResponseEntity<?> deleteSupplier(@PathVariable(value = "supplierId") UUID supplierId)
			throws SupplierNotFoundException {
		ResponseEntity<?> response = null;

		response = new ResponseEntity<>(supplierService.removeSupplier(supplierId), HttpStatus.OK);

		return response;
	}

	@Operation(summary = "view Supplier ProductMasterDetails")
	@GetMapping("/viewSuppliersProductMasterDetails/{supplierId}")
	public ResponseEntity<?> getSuppliersProductMaster(@PathVariable(value = "supplierId") UUID supplierId)
			throws SupplierNotFoundException {
		ResponseEntity<?> response = new ResponseEntity<>(supplierService.viewSupplierProductMaster(supplierId),
				HttpStatus.OK);

		return response;
	}

	@Operation(summary = "update Product master details")
	@PutMapping("/productmaster")
	public ResponseEntity<?> putProductMaster(@RequestBody ProductMasterDto productMasterDto)
			throws SupplierNotFoundException, ProductNotFoundException {
		ResponseEntity<?> response = new ResponseEntity<>(supplierService.updateProductMaster(productMasterDto),
				HttpStatus.OK);

		return response;
	}

	/**
	 * @param restaurantId
	 * @param page
	 * @return
	 * @throws SupplierNotFoundException
	 * @throws IllegalArgumentException
	 */
	@Operation(summary = "get all suppliers")
	@GetMapping("/")
	public ResponseEntity<?> getAllSuppliers(@RequestParam UUID restaurantId, int page)
			throws SupplierNotFoundException, IllegalArgumentException {
		ResponseEntity<?> response = new ResponseEntity<>(supplierService.getAllSuppliers(restaurantId, page),
				HttpStatus.OK);
		return response;

	}

	@Operation(summary = "search supplier by name")
	@GetMapping("/search/{supplierName}")
	public ResponseEntity<?> getAllSuppliers(@RequestParam UUID restaurantId, int page,
			@PathVariable(value = "supplierName") String supplierName)
			throws SupplierNotFoundException, IllegalArgumentException {
		ResponseEntity<?> response = new ResponseEntity<>(
				supplierService.getAllSuppliersByName(restaurantId, page, supplierName), HttpStatus.OK);
		return response;

	}
}
