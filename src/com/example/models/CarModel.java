package com.example.models;

import java.io.Serializable;
import java.util.List;

public class CarModel implements Serializable{

	private int id;
	private String brand;
	private CustomerModel owner;
	private String registrationNumber;
	private String yearOfManufactury;
	private String model;
	private String typeOfFuel;
	private String colour;
	public List<CarServiceModel> serviceList;
	
	public CarModel(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public CustomerModel getOwner() {
		return owner;
	}

	public void setOwner(CustomerModel owner) {
		this.owner = owner;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getYearOfManufactury() {
		return yearOfManufactury;
	}
	public void setYearOfManufactury(String yearOfManufactury) {
		this.yearOfManufactury = yearOfManufactury;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getTypeOfFuel() {
		return typeOfFuel;
	}
	public void setTypeOfFuel(String typeOfFuel) {
		this.typeOfFuel = typeOfFuel;
	}
	
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public List<CarServiceModel> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<CarServiceModel> serviceList) {
		this.serviceList = serviceList;
	}

}
