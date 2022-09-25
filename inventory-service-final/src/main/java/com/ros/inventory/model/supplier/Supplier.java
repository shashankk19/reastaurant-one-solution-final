package com.ros.inventory.model.supplier;

import java.util.List;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Enumerated(EnumType.STRING)
	private SupplierType type;

	private String supplierName;

	private String profilePic;

	private String email;

	private long phoneNumber;

	private String restaurantName;

	@OneToOne(cascade = CascadeType.ALL)
	private Address generalAddress;

	@OneToOne(cascade = CascadeType.ALL)
	private BankDetails bankDetails;

	@OneToOne(cascade = CascadeType.ALL)
	private BasicInformation basicInformation;

	@OneToOne(cascade = CascadeType.ALL)
	private PrimaryContact primaryContact;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_id")
	@JsonBackReference
	private List<ProductMaster> products;

}
