package com.example.upPointsTest;

//import java.net.URI;
//import java.util.Map;
//import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SellingPoint {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private String fone;
	private String address;
	private String workingHours;
	
	protected SellingPoint(){}
	
	protected SellingPoint(String name, String fone, String address, String workingHours){
		this.name = name;
		this.fone = fone;
		this.address = address;
		this.workingHours = workingHours;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFone() {
		return fone;
	}
	
	public void setFone(String fone) {
		this.fone = fone;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getWorkingHours() {
		return workingHours;
	}
	
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	
	@Override
	public String toString() {
		return String.format("Customer[id=%d, name='%s', fone='%s', address='%s', workingHours='%s']", id,
				name, fone, address, workingHours);
	}

}
