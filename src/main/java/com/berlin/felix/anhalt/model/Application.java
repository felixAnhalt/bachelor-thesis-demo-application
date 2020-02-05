package com.berlin.felix.anhalt.model;

import java.util.Calendar;

import com.fasterxml.uuid.UUIDGenerator;

public class Application {

	private String applicationNumber;

	private Person applicant;
	private String role;
	private String educationalLevel;

	public Person getApplicant() {
		return applicant;
	}

	public void setApplicant(Person applicant) {
		this.applicant = applicant;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEducationalLevel() {
		return educationalLevel;
	}

	public void setEducationalLevel(String educationalLevel) {
		this.educationalLevel = educationalLevel;
	}

}
