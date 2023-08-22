package com.jspider.car_dekho_casestudy_hibernate.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="carinfo")
public class Car {

	@Id
	private int id;
	private String name;
	private String model;
	private String brand;
	private String fuletype;
	private double price;
	
	@Override
	public String toString() {
		
		return ""+id+"\t\t"+name+"\t\t"+model+"\t\t"+brand+"\t\t"+fuletype+"\t\t"+price+"";
		
	}

}
