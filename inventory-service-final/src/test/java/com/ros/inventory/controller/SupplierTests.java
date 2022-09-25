package com.ros.inventory.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.inventory.InventoryServiceApplication;
import com.ros.inventory.mapper.SupplierMapper;
import com.ros.inventory.service.SupplierService;


@ContextConfiguration(classes = InventoryServiceApplication.class)
@SpringBootTest
public class SupplierTests {

	private MockMvc mockMvc;

	@MockBean
	private SupplierService supplierService;

	@MockBean
	private SupplierMapper supplierMapper;

	private static ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
    private WebApplicationContext wac;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    }
    
    @Test
    public void testAddExternalSupplier() throws Exception {
    	mockMvc.perform(post("/supplier/external/3fa85f64-5717-4562-b3fc-2c963f66afa6")
    			.content("{\r\n"
    					+ "  \"type\": \"INTERNAL\",\r\n"
    					+ "  \"profilePic\": \"Profile\",\r\n"
    					+ "  \"basicInformation\": {\r\n"
    					+ "    \"businessName\": \"string\",\r\n"
    					+ "    \"legalTradeName\": \"string\",\r\n"
    					+ "    \"mobileNumber\": 0,\r\n"
    					+ "    \"telephone\": 0,\r\n"
    					+ "    \"email\": \"string\",\r\n"
    					+ "    \"fax\": \"string\"\r\n"
    					+ "  },\r\n"
    					+ "  \"generalAddress\": {\r\n"
    					+ "    \"address\": \"string\",\r\n"
    					+ "    \"zipcode\": 0,\r\n"
    					+ "    \"city\": \"string\",\r\n"
    					+ "    \"state\": \"string\",\r\n"
    					+ "    \"country\": \"string\"\r\n"
    					+ "  },\r\n"
    					+ "  \"bankDetails\": {\r\n"
    					+ "    \"bankName\": \"string\",\r\n"
    					+ "    \"branchAccount\": \"string\",\r\n"
    					+ "    \"accountHolderName\": \"string\",\r\n"
    					+ "    \"bankCode\": 0,\r\n"
    					+ "    \"accountNumber\": 0\r\n"
    					+ "  },\r\n"
    					+ "  \"products\": [\r\n"
    					+ "    {\r\n"
    					+ "      \"id\": {\r\n"
    					+ "        \"productId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\r\n"
    					+ "        \"supplierId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"\r\n"
    					+ "      },\r\n"
    					+ "      \"product\": {\r\n"
    					+ "        \"name\": \"string\",\r\n"
    					+ "        \"productCode\": 0,\r\n"
    					+ "        \"type\": \"FOOD\"\r\n"
    					+ "      },\r\n"
    					+ "      \"unitMeasurement\": \"string\",\r\n"
    					+ "      \"pricePerUnit\": 0,\r\n"
    					+ "      \"vat\": 0,\r\n"
    					+ "      \"effectiveDate\": \"2022-01-11T10:56:21.826Z\"\r\n"
    					+ "    }\r\n"
    					+ "  ]\r\n"
    					+ "}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
				.andExpect(status().isOk()).
				andExpect(jsonPath("$.profilePic", Matchers.is("Profile")));
    }
//
//	@Test
//	public void testAddExternalSupplierV01() throws Exception {
//
//		ExternalSupplierDto externalSupplierDto = new ExternalSupplierDto();
//
//		BasicInformationDto basicInformationDto = new BasicInformationDto();
//		basicInformationDto.setBusinessName("TestBusiness");
//		basicInformationDto.setLegalTradeName("TestLegalName");
//		basicInformationDto.setEmail("testEmail");
//		basicInformationDto.setMobileNumber(94464987542L);
//		AddressDto addressDto = new AddressDto();
//		BankDetailsDto bankDetailsDto = new BankDetailsDto();
//
//		externalSupplierDto.setBasicInformation(basicInformationDto);
//		externalSupplierDto.setGeneralAddress(addressDto);
//		externalSupplierDto.setBankDetails(bankDetailsDto);
//
//		Supplier supplier = new Supplier();
//		supplier.setId(UUID.fromString("79b804ed-765b-44ed-8e55-59b200aa536a"));
//
//		String json = mapper.writeValueAsString(externalSupplierDto);
//
//		when(supplierService.addSupplier(externalSupplierDto,ArgumentMatchers.any())).thenReturn(supplier);
//
//		mockMvc.perform(post("/supplier/external/79b804ed-765b-44ed-8e55-59b200aa536a").content(json)
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.businessName", Matchers.equalTo("TestBusiness")));
//
//	}

}
