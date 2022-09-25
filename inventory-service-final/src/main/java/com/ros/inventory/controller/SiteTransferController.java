package com.ros.inventory.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ros.inventory.exception.EmptyProductsListException;
import com.ros.inventory.exception.PurchaseOrderNotFoundException;
import com.ros.inventory.exception.SiteTransferNotFoundException;
import com.ros.inventory.service.SiteTransferService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sitetransfer")
public class SiteTransferController {

	@Autowired
	public SiteTransferService siteTransferService;

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "get all site transfers")
	@GetMapping("/siteTransfers")
	public ResponseEntity<?> getAllSiteTransfers() {

		ResponseEntity<?> response = null;

		try {

			response = new ResponseEntity<>(siteTransferService.findAllSiteTransfers(), HttpStatus.OK);

		} catch (SiteTransferNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);

		}
		return response;

	}

	@GetMapping("/sitetransferdetails/{id}")
	public ResponseEntity<?> detailsOfSiteTransfer(@PathVariable(value = "id") UUID id)
			throws EmptyProductsListException, PurchaseOrderNotFoundException {
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(siteTransferService.detailsOfSiteTransfers(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

}
