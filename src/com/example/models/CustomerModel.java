package com.example.models;

import java.io.Serializable;
import java.util.List;

public class CustomerModel implements Serializable {

	private int id;
	private String name;
	private String title;
	private String age;
	private String phoneNumber;
	private String address;
	private String email;
	public List<CarModel> carList;

	public CustomerModel() {
		this.setTitle("Customer");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<CarModel> getCarList() {
		return carList;
	}

	public void setCarList(List<CarModel> carList) {
		this.carList = carList;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
