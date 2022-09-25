package com.ros.inventory.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ros.inventory.exception.InvoiceNotFoundException;
import com.ros.inventory.service.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired
	public InvoiceService invoiceService;

	@GetMapping("/monthlyinvoice/{year}/{month}")
	public ResponseEntity<?> viewmonthlyInvoice(@PathVariable(value = "year") int year,
			@PathVariable(value = "month") int month) {
		ResponseEntity<?> response;
		response = new ResponseEntity<>(invoiceService.getMonthlyInvoice(year, month), HttpStatus.OK);
		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Ajay
	 */
	@Operation(summary = "To get Invoice")
	@GetMapping("/{id}")
	public ResponseEntity<?> getInvoiceById(@PathVariable(value = "id") UUID id) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(invoiceService.viewInvoice(id), HttpStatus.OK);
		} catch (InvoiceNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param int year
	 * @return ResponseEntity
	 * @author Ajay
	 */
	@GetMapping("/getyearlyInvoice/{year}")
	@Operation(summary = "To get Yearly Invoice")
	public ResponseEntity<?> getInvoiceByyear(@PathVariable(value = "year") int year) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(invoiceService.getYearlyInvoice(year), HttpStatus.OK);
		} catch (InvoiceNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

}
