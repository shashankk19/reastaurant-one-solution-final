package com.ros.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ros.inventory.controller.dto.SupplierDto;
import com.ros.inventory.controller.dto.supplier.ExternalSupplierDto;
import com.ros.inventory.controller.dto.supplier.InternalSupplierDto;
import com.ros.inventory.controller.dto.supplier.ProductMasterDto;
import com.ros.inventory.exception.ProductNotFoundException;
import com.ros.inventory.exception.RestaurantNotFoundException;
import com.ros.inventory.exception.SupplierAlreadyExistsException;
import com.ros.inventory.exception.SupplierNotFoundException;
import com.ros.inventory.mapper.ProductMasterMapper;
import com.ros.inventory.mapper.SupplierMapper;
import com.ros.inventory.model.supplier.BankDetails;
import com.ros.inventory.model.supplier.ProductMaster;
import com.ros.inventory.model.supplier.Restaurant;
import com.ros.inventory.model.supplier.Supplier;
import com.ros.inventory.model.supplier.SupplierType;
import com.ros.inventory.repository.ProductMasterRepository;
import com.ros.inventory.repository.ProductRepository;
import com.ros.inventory.repository.RestaurantRepository;
import com.ros.inventory.repository.SupplierRepository;
import com.ros.inventory.service.SupplierService;

/**
 * @author kalyan.prathapaneni
 *
 */
@Service
public class SupplierServiceImpl implements SupplierService {

	private static final int pageSize = 5;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private SupplierMapper supplierMapper;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private ProductMasterMapper productMasterMapper;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Supplier addSupplier(ExternalSupplierDto externalSupplierDto, UUID restaurantId)
			throws SupplierAlreadyExistsException, RestaurantNotFoundException {

		Supplier supplier = new Supplier();

		UUID dtoId = externalSupplierDto.getId();

		if (dtoId != null) {

			if (supplierRepository.existsById(dtoId)) {

				throw new SupplierAlreadyExistsException(Messages.getString("SupplierAlreadyExists"));
			}
		}

		if (!restaurantRepository.findById(restaurantId).isPresent()) {

			throw new RestaurantNotFoundException(Messages.getString("RestaurantNotFound"));
		}

		supplier = supplierMapper.updateSupplierFromDto(externalSupplierDto, supplier);

		supplier.setSupplierName(supplier.getBasicInformation().getBusinessName());

		supplier.setPhoneNumber(supplier.getBasicInformation().getMobileNumber());

		supplier.setEmail(supplier.getBasicInformation().getEmail());

		supplier.setType(SupplierType.EXTERNAL);

		List<ProductMaster> productMasters = supplier.getProducts();

		if (productMasters != null && !productMasters.isEmpty()) {

			for (ProductMaster productMaster : productMasters) {

				productMaster.setSupplier(supplier);
			}
		}

		Restaurant restaurant = restaurantRepository.getOne(restaurantId);

		if (restaurant.getSupplier() == null) {
			restaurant.setSupplier(new ArrayList<Supplier>());
		}

		restaurant.getSupplier().add(supplier);

		restaurantRepository.save(restaurant);

		return restaurant.getSupplier().get(restaurant.getSupplier().size() - 1);
	}

	@Override
	public Supplier addSupplier(InternalSupplierDto internalSupplierDto, UUID restaurantId)
			throws SupplierAlreadyExistsException, RestaurantNotFoundException {

		Supplier supplier = new Supplier();
		UUID dtoId = internalSupplierDto.getId();

		if (dtoId != null) {
			if (supplierRepository.existsById(internalSupplierDto.getId())) {

				throw new SupplierAlreadyExistsException(Messages.getString("SupplierAlreadyExists"));
			}
		}

		if (!restaurantRepository.existsById(restaurantId)) {
			throw new RestaurantNotFoundException(Messages.getString("RestaurantNotFound"));
		}

		supplier = supplierMapper.updateSupplierFromDto(internalSupplierDto, supplier);

		supplier.setSupplierName(supplier.getBasicInformation().getBusinessName());

		supplier.setPhoneNumber(supplier.getBasicInformation().getMobileNumber());

		supplier.setEmail(supplier.getBasicInformation().getEmail());

		supplier.setType(SupplierType.INTERNAL);

		List<ProductMaster> productMasters = supplier.getProducts();
		if (productMasters != null && !productMasters.isEmpty()) {

			for (ProductMaster product : productMasters) {

				product.setSupplier(supplier);
			}
		}

		Restaurant restaurant = restaurantRepository.getOne(restaurantId);

		if (restaurant.getSupplier() == null) {
			restaurant.setSupplier(new ArrayList<Supplier>());
		}

		restaurant.getSupplier().add(supplier);

		restaurantRepository.save(restaurant);

		return restaurant.getSupplier().get(restaurant.getSupplier().size() - 1);
	}

	@Override
	public Supplier editSupplier(SupplierDto supplierDto) throws SupplierNotFoundException {

		UUID supplierDtoId = supplierDto.getId();

		if (supplierDtoId != null) {
			if (!supplierRepository.existsById(supplierDtoId)) {

				throw new SupplierNotFoundException(Messages.getString("SupplierNotFound")); //$NON-NLS-1$
			}
		}

		Supplier supplier = supplierRepository.getOne(supplierDto.getId());

		checkSupplierChildIdValidations(supplier, supplierDto);

		if (supplierDto.getType() == SupplierType.EXTERNAL) {

			supplier = editExternalSupplier(supplierDto);
		} else if (supplierDto.getType() == SupplierType.INTERNAL) {

			supplier = editInternalSupplier(supplierDto);
		}
		return supplier;
	}

	private void checkSupplierChildIdValidations(Supplier supplier, SupplierDto supplierDto) {

	}

	private Supplier editExternalSupplier(SupplierDto supplierDto) {

		Supplier supplier = supplierRepository.getOne(supplierDto.getId());

		ExternalSupplierDto externalDto = new ExternalSupplierDto();

		externalDto = supplierMapper.convertToExternalSupplierDto(supplierDto);

		supplierMapper.updateSupplierFromDto(externalDto, supplier);

		supplier.getProducts().forEach(product -> product.setSupplier(supplier));

		supplierRepository.save(supplier);

		return supplier;
	}

	private Supplier editInternalSupplier(SupplierDto supplierDto) {

		Supplier supplier = supplierRepository.getOne(supplierDto.getId());

		InternalSupplierDto internalDto = supplierMapper.convertToInternalSupplierDto(supplierDto);

		supplierMapper.updateSupplierFromDto(internalDto, supplier);

		supplier.getProducts().forEach(product -> product.setSupplier(supplier));

		supplierRepository.save(supplier);

		return supplier;
	}

	@Override
	public List<ProductMaster> updateProductMaster(UUID supplierId, List<ProductMasterDto> productMastersDtos)
			throws SupplierNotFoundException {
		if (supplierId != null) {
			if (!supplierRepository.existsById(supplierId)) {

				throw new SupplierNotFoundException(Messages.getString("SupplierNotFound")); //$NON-NLS-1$
			}
		}

		Supplier supplierFromDB = supplierRepository.getOne(supplierId);

		List<ProductMaster> productMasters = new ArrayList<ProductMaster>();

		for (ProductMasterDto dto : productMastersDtos) {

			productMasters.add(supplierMapper.convertToProductMaster(dto));

		}

		productMasters.forEach(product -> product.setSupplier(supplierFromDB));

		supplierFromDB.setProducts(productMasters);

		supplierRepository.saveAndFlush(supplierFromDB);

		return productMasters;
	}


	// Method that returns bank details of supplier
	@Override
	public BankDetails viewSupplierBankDetailss(UUID supplierId) throws SupplierNotFoundException {

		if (supplierRepository.existsById(supplierId)) {
			Supplier supplier = supplierRepository.getOne(supplierId);
			BankDetails bankDetails = supplier.getBankDetails();
			return bankDetails;
		} else
			throw new SupplierNotFoundException(Messages.getString("SupplierNotFound"));

	}

	// delete supplier from list
	@Override
	public Supplier removeSupplier(UUID supplierId) throws SupplierNotFoundException {

		if (!supplierRepository.existsById(supplierId))
			throw new SupplierNotFoundException(Messages.getString("SupplierNotFound"));
		else {
			Supplier supplier = supplierRepository.getOne(supplierId);
			supplierRepository.delete(supplier);
			return supplier;
		}

	}

//Method that returns product master 
	@Override
	public List<ProductMasterDto> viewSupplierProductMaster(UUID supplierId) throws SupplierNotFoundException {
		List<ProductMasterDto> productMasterDtoList = new ArrayList<>();
		ProductMasterDto productMasterDto = new ProductMasterDto();
		if (supplierRepository.existsById(supplierId)) {
			List<ProductMaster> productMasterList = supplierRepository.getOne(supplierId).getProducts();
			for (ProductMaster pm : productMasterList) {
				productMasterDto = productMasterMapper.convertToProductMasterDto(pm);
				productMasterDtoList.add(productMasterDto);
			}
			return productMasterDtoList;
		} else {
			throw new SupplierNotFoundException(Messages.getString("SupplierNotFound")); //$NON-NLS-1$
		}
	}

	// add product master list for external supplier
	@Override
	public ProductMasterDto addProductMasterList(ProductMasterDto productMasterDto)
			throws SupplierNotFoundException, ProductNotFoundException {
		ProductMaster productMaster = new ProductMaster();
		productMaster = productMasterMapper.updateProductMaster(productMasterDto, productMaster);

		productMasterRepository.save(productMaster);

		return productMasterDto;
	}

	// edit productMaster list
	@Override
	public boolean updateProductMaster(ProductMasterDto productMasterDto)
			throws SupplierNotFoundException, ProductNotFoundException {
		if (!supplierRepository.existsById(productMasterDto.getId().getSupplierId())) {
			throw new SupplierNotFoundException();
		}
		if (!productRepository.existsById(productMasterDto.getId().getProductId())) {
			throw new ProductNotFoundException();
		}
		ProductMaster productMasterFromRepo = productMasterRepository.getOne(productMasterDto.getId());
		productMasterFromRepo = productMasterMapper.updateProductMaster(productMasterDto, productMasterFromRepo);
		productMasterFromRepo.calculateActualPrice();
		productMasterRepository.saveAndFlush(productMasterFromRepo);
		return true;
	}

	@Override
	public List<SupplierDto> getAllSuppliers(UUID restaurantId, int page)
			throws SupplierNotFoundException, IllegalArgumentException {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Supplier> suppliers = supplierRepository.getSupplierList(restaurantId, pageable);

		if (suppliers.isEmpty()) {
			throw new SupplierNotFoundException();
		}

		List<SupplierDto> supplierDtos = suppliers.stream()
				.map((supplier) -> supplierMapper.convertToSupplierDto(supplier)).collect(Collectors.toList());

		return supplierDtos;
	}
	
	
	
	@Override
	public List<SupplierDto> getAllSuppliersByName(UUID restaurantId, int page, String supplierName) throws SupplierNotFoundException{
		int supplierNameLength = supplierName.length();
		if(supplierNameLength < 3 || supplierNameLength > 50){
			throw new IllegalArgumentException(Messages.getString("CharacterRangeExceeded")); //$NON-NLS-1$
		}
		
		Pageable pageable = PageRequest.of(page, pageSize);
		List<Supplier> suppliers = supplierRepository.getSupplierList(restaurantId, pageable, supplierName);

		if (suppliers.isEmpty()) {
			throw new SupplierNotFoundException();
		}

		List<SupplierDto> supplierDtos = suppliers.stream()
				.map((supplier) -> supplierMapper.convertToSupplierDto(supplier)).collect(Collectors.toList());

		return supplierDtos;
	}

}
