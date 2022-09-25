package com.ros.inventory.model.supplier;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ros.inventory.model.invoice.Invoice;
import com.ros.inventory.model.purchaseorder.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Restaurant implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "restaurant_id")
	private UUID restaurantId;
	
	@Column(name ="restaurant_name" )
	private String restaurantName;
	
	 @OneToMany(targetEntity =Supplier.class , cascade=CascadeType.ALL )
     @JoinColumn(name= "restaurant_fk" ,referencedColumnName = "restaurant_id")
	 private List<Supplier> supplier;

	 @OneToMany(targetEntity =PurchaseOrder.class , cascade=CascadeType.ALL )
     @JoinColumn(name= "restaurant_fk" ,referencedColumnName = "restaurant_id")
	 private List<PurchaseOrder>  purchase;
	 
	 @OneToMany(mappedBy = "restaurant" , cascade=CascadeType.ALL )
	 private List<Invoice>  invoice;
	 
	 @OneToOne(fetch = FetchType.LAZY ,cascade=CascadeType.ALL)
	 @JoinColumn(name ="restaurantaddress_id")
	 private ResturantAddress resturantAddress;

}

