package com.berlin.felix.anhalt.model;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;

public class Employee extends Person {
	
	private String employeeId;
	private HashMap<String, Integer> taskCompletetionTime;
	
	public Employee (Person person, HashMap<String, Integer> taskCompletetionTime) {
		this.setBirthday(person.getBirthday());
		this.setEmail(person.getEmail());
		this.setGender(person.getGender());
		this.setName(person.getName());
		this.employeeId = RandomStringUtils.random(8);
		this.taskCompletetionTime = taskCompletetionTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public HashMap<String, Integer> getTaskCompletetionTime() {
		return taskCompletetionTime;
	}

	public void setTaskCompletetionTime(HashMap<String, Integer> taskCompletetionTime) {
		this.taskCompletetionTime = taskCompletetionTime;
	}

}
