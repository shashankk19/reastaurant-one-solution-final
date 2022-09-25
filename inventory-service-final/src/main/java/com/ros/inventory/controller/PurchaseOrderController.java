package com.ros.inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.ros.inventory.exception.PurchaseOrderNotFoundException;
import com.ros.inventory.controller.dto.PurchasedProductDto;
import com.ros.inventory.exception.InCorrectStatusOFOrderException;
import com.ros.inventory.exception.ItsAlreadySubmittedException;
import com.ros.inventory.exception.PurchaseOrderInvalidException;
import com.ros.inventory.service.PurchaseOrderService;
import com.ros.inventory.controller.dto.PurchaseOrderDto;
import com.ros.inventory.exception.EmptyProductsListException;
import com.ros.inventory.exception.PurchaseOrderAlreadyExistsException;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/purchaseOrder")
public class PurchaseOrderController {

	@Autowired
	PurchaseOrderService purchaseOrderService;

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "update purchase order from rejected status to submitted status")
	@PutMapping("/rejectToSubmit/{id}")
	public ResponseEntity<?> updatePurchaseOrderFromrejectToSubmit(@PathVariable(value = "id") UUID id) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.reSubmitRejectedOrder(id), HttpStatus.OK);
		} catch (InCorrectStatusOFOrderException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Ajay
	 */
	@Operation(summary = "update purchase order from draft status to submitted status")
	@PutMapping("/submit/{poId}")
	public ResponseEntity<?> updatePurchaseOrderFromDraftToSubmit(@PathVariable(value = "poId") UUID poId) {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.submitDraft(poId), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "bulk reject purchase orders")
	@PutMapping("/rejectAll")
	public ResponseEntity<?> rejectAll() {
		ResponseEntity<?> response = null;
		response = new ResponseEntity<>(purchaseOrderService.bulkRejectPurchaseOrders(), HttpStatus.OK);
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "reject purchase order from submitted status")
	@PutMapping("/reject/{id}")
	public ResponseEntity<?> rejectSubmittedOrder(@PathVariable(value = "id") UUID id) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.rejectSubmittedPurchaseOrder(id), HttpStatus.OK);
		} catch (InCorrectStatusOFOrderException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Ajay
	 */
	@Operation(summary = "bulk update purchase order from draft status to submitted status")
	@PutMapping("/bulk/submit/")
	public ResponseEntity<?> updatePurchaseOrderFromDraftToSubmit() {

		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.updatePurchaseOrdersFromDraftToSubmitted(),
					HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "update the status of purchase order from submitted to approved")
	@PutMapping("/approve/{id}")
	public ResponseEntity<?> approveSubmittedOrder(@PathVariable(value = "id") UUID id) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.approveSubmittedOrder(id), HttpStatus.OK);
		} catch (InCorrectStatusOFOrderException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "edit purchase order's products quantity by id")
	@PutMapping("/edit/quantites/{id}")
	public ResponseEntity<?> editPurchaseOrderQuantities(@RequestBody List<PurchasedProductDto> purchasedproducts,
			@PathVariable(value = "id") UUID id) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.editPurchaseOrder(purchasedproducts, id),
					HttpStatus.OK);
		} catch (ItsAlreadySubmittedException | PurchaseOrderInvalidException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Sai Shashank
	 */
	@Operation(summary = "add reason for rejection")
	@PutMapping("/addComment/{id}/{message}")
	public ResponseEntity<?> putReasonForRejection(@PathVariable(value = "id") UUID id,
			@PathVariable(value = "message") String message) {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.addReasonForRejection(id, message), HttpStatus.OK);
		} catch (InCorrectStatusOFOrderException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "list view of purchase orders with draft status")
	@GetMapping("/view/draft")
	public ResponseEntity<?> getDraftPurchaseOrders() {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.findDrafts(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "list view of purchase orders with submitted status")
	@GetMapping("/view/submitted")
	public ResponseEntity<?> getSubmittedPurchasedOrders() {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.findSubmittedPurchaseOrders(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;

	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "list view of purchase orders with approved status")
	@GetMapping("/view/approved")
	public ResponseEntity<?> getApprovedPurchaseOrders() {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.findApprovedPurchaseOrders(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;

	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "list view of purchase orders with rejected status")
	@GetMapping("/view/rejected")
	public ResponseEntity<?> getRejectedPurchaseOrders() {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.findRejectedPurchaseOrders(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;

	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "delete draft purchase order by id")
	@DeleteMapping("/delete/draft/{id}")
	public ResponseEntity<?> deletePurchaseOrderById(@PathVariable(value = "id") UUID id)
			throws PurchaseOrderNotFoundException {
		ResponseEntity<?> response = null;
		try {

			response = new ResponseEntity<>(purchaseOrderService.deleteDraftPurchaseOrderById(id), HttpStatus.OK);

		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;

	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "delete all draft purchase orders")
	@DeleteMapping("/deleteAll/")
	public ResponseEntity<?> deleteAllDraftPurchaseOrders() {
		ResponseEntity<?> response = null;
		try {

			response = new ResponseEntity<>(purchaseOrderService.deleteAllDrafts(), HttpStatus.OK);

		} catch (PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);

		}

		return response;
	}

	/**
	 * 
	 * @return ResponseEntity
	 * @author Jhansi
	 */
	@Operation(summary = "update purchase order status from submitted to rejected")
	@PutMapping("/update/{id}/reject")
	public ResponseEntity<?> updatePurchaseOrderFromSubmitToReject(@PathVariable(value = "id") UUID id)
			throws PurchaseOrderNotFoundException {
		ResponseEntity<?> response = null;
		try {
			response = new ResponseEntity<>(purchaseOrderService.updatePurchaseOrderFromSubmitToReject(id),
					HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
		return response;

	}

	@PostMapping("/createorder-and-submit")
	public ResponseEntity<?> addPurchaseOrderAndSubmit(@RequestBody PurchaseOrderDto purchaseorder) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.createOrderAndSubmit(purchaseorder), HttpStatus.OK);
		} catch (PurchaseOrderAlreadyExistsException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	@PostMapping("/createorder-and-draft")
	public ResponseEntity<?> addPurchaseOrderAndDraft(@RequestBody PurchaseOrderDto purchaseorder) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.createOrderAndDraft(purchaseorder), HttpStatus.OK);
		} catch (PurchaseOrderAlreadyExistsException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/draftdetails/{poid}")
	public ResponseEntity<?> viewdraftproducts(@PathVariable(value = "poid") UUID id) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.viewDetailsOfDraft(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/submitdetails/{poid}")
	public ResponseEntity<?> viewsubmitproducts(@PathVariable(value = "poid") UUID id) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.viewDetailsOfSubmit(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/approvedetails/{poid}")
	public ResponseEntity<?> viewapproveproducts(@PathVariable(value = "poid") UUID id) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.viewDetailsOfApprove(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Adhway
	 */
	@Operation(summary = "bulk update purchase orders status to approve")
	@PutMapping("/approveall")
	public ResponseEntity<?> putBulkApprove() {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.approveAll(), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Adhway
	 */
	@Operation(summary = "get purchase order product details which are in reject")
	@GetMapping("/view/{poid}")
	public ResponseEntity<?> getPurchaseOrderDetails(@PathVariable(value = "poid") UUID id) {
		ResponseEntity<?> response;

		try {
			response = new ResponseEntity<>(purchaseOrderService.getPurchaseOrderDetails(id), HttpStatus.OK);
		} catch (PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	/**
	 * @param UUID id
	 * @return ResponseEntity
	 * @author Adhway
	 */
	@Operation(summary = "get purchase order product details")
	@GetMapping("/ApprovedDetails/{poid}")
	public ResponseEntity<?> viewapprovedproducts(@PathVariable(value = "poid") UUID id)
			throws EmptyProductsListException, PurchaseOrderNotFoundException {
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(purchaseOrderService.viewDetailsOfApproved(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

	@PutMapping("/ApprovedToReceived/{poid}")
	public ResponseEntity<?> updateStatus(@PathVariable(value = "poid") UUID id)
			throws EmptyProductsListException, PurchaseOrderNotFoundException {
		ResponseEntity<?> response;
		try {
			response = new ResponseEntity<>(purchaseOrderService.updateStatus(id), HttpStatus.OK);
		} catch (EmptyProductsListException | PurchaseOrderNotFoundException e) {

			response = new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return response;
	}

}
