package com.ros.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ros.inventory.controller.dto.AddStockDto;
import com.ros.inventory.controller.dto.EditWastageDto;
import com.ros.inventory.controller.dto.WastageDto;
import com.ros.inventory.exception.SiteTransferNotFoundException;
import com.ros.inventory.exception.StockAlreadyClosedException;
import com.ros.inventory.exception.StockAlreadyExistsException;
import com.ros.inventory.exception.StockNotClosedException;
import com.ros.inventory.exception.StockNotFoundException;
import com.ros.inventory.service.StockService;
import com.ros.inventory.exception.PurchaseOrderNotFoundException;
import com.ros.inventory.exception.StockEmptyException;
import com.ros.inventory.exception.WastageNotFoundException;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/stock")
public class StockController {

	@Autowired
	private StockService stockService;

	@PostMapping("/addStock")
	public ResponseEntity<?> addstock(@RequestBody AddStockDto stockDto) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.insertStock(stockDto), HttpStatus.OK);
		} catch (StockAlreadyExistsException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	@PutMapping("/addclosedstock/{id}")
	public ResponseEntity<?> addClosedStock(@PathVariable(value = "id") UUID stockId) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.insertClosedStock(stockId), HttpStatus.OK);
		} catch (StockAlreadyClosedException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		} catch (StockNotFoundException e) {
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	@PostMapping("/addwastage/{id}")
	public ResponseEntity<?> addWastage(@PathVariable(value = "id") UUID stockId, @RequestBody WastageDto wastage) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.insertWastage(wastage, stockId), HttpStatus.OK);
		} catch (StockNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	@PutMapping("/editclosedstock/{id}")
	public ResponseEntity<?> editClosedStock(@PathVariable(value = "id") UUID stockId,
			@RequestBody List<EditWastageDto> wastageDtoList) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.editClosedStock(stockId, wastageDtoList), HttpStatus.OK);
		} catch (StockNotClosedException | StockNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/getSiteTransfers/{id}")
	public ResponseEntity<?> getSiteTransfers(@PathVariable(value = "id") UUID stockId) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.fetchSiteTransfers(stockId), HttpStatus.OK);
		} catch (StockNotFoundException | SiteTransferNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/getWastages/{id}")
	public ResponseEntity<?> getWastages(@PathVariable(value = "id") UUID stockId) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.fetchWastages(stockId), HttpStatus.OK);
		} catch (StockNotFoundException | WastageNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	// View the list of closed stock periods
	@GetMapping("/getClosedStock")
	@Operation(summary = "To find all clsoing stocks")
	public ResponseEntity<?> viewClosedstocks() {
		ResponseEntity<?> response = null;

		try {
			response = new ResponseEntity<>(stockService.findClosedStock(), HttpStatus.OK);
		} catch (StockEmptyException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	// Delete a wastage product details from a stock period
	@DeleteMapping("/deleteFromWastage/{WastageId}")
	@Operation(summary = "To delete Wastage using")

	public ResponseEntity<?> deleteProduct(@PathVariable(value = "WastageId") UUID wastageId) {
		ResponseEntity<?> response = null;
		try {
			stockService.deleteWasteProduct(wastageId);

			response = new ResponseEntity<>("Deleted", HttpStatus.OK);
		} catch (WastageNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);

		}

		return response;
	}

	// Approve a stock period for a closed stock
	@PutMapping("/ApproveClosedStocks/{stockId}")
	@Operation(summary = "approve closed stock using stock id")

	public ResponseEntity<?> updateStock(@PathVariable(value = "stockId") UUID stockId) {
		ResponseEntity<?> response = null;

		try {
			response = new ResponseEntity<>(stockService.aproveStock(stockId), HttpStatus.OK);
		} catch (StockNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	// View the purchase order list in a stock period
	@GetMapping("/GetStockPurchaseOrderList")
	@Operation(summary = "To find purchase order list from opening stocks period")

	public ResponseEntity<?> viewExported() {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.findPurchasedOrderList(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param UUID purchaseOrderId
	 * @return ResponseEntity
	 * @author Ranjith
	 */
	@Operation(summary = "bulk approve stock status")
	@GetMapping("/getPurchaseorder/{purchaseOrderid}")
	public ResponseEntity<?> viewtPurchaseOrder(@PathVariable(value = "purchaseOrderid") UUID poId) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.getPurchaseOrder(poId), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Ranjith
	 */
	@Operation(summary = "bulk approve stock status")
	@PutMapping("/bulkApproveClosedStock")
	public ResponseEntity<?> bulkApproveClosedStock() {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.bulkApproveStock(), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param UUID sitetransferId
	 * @return ResponseEntity
	 * @author Ranjith
	 */
	@Operation(summary = "detailed view of site transfer")
	@GetMapping("/getSiteTransfer/{siteTransfersId}")
	public ResponseEntity<?> getSiteTransfer(@PathVariable(value = "siteTransfersId") UUID id) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.viewSiteTransfer(id), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Ranjith
	 */
	@Operation(summary = "update stock status by id to approved")
	@PutMapping("/ApproveStock/{stockid}")
	public ResponseEntity<?> AprroveStock(@PathVariable(value = "stockid") UUID stockId) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(stockService.approveStock(stockId), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

}
