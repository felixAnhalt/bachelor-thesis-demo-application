package com.berlin.felix.anhalt.utils;

import static com.berlin.felix.anhalt.Constants.abstimmungArbeitsmittel;
import static com.berlin.felix.anhalt.Constants.arbeitsmittelBeschaffen;
import static com.berlin.felix.anhalt.Constants.bewerbungUeberpruefen;
import static com.berlin.felix.anhalt.Constants.einweisungBestaetigung;
import static com.berlin.felix.anhalt.Constants.loesungErhalten;
import static com.berlin.felix.anhalt.Constants.loesungUeberpruefen;
import static com.berlin.felix.anhalt.Constants.maEinweisen;
import static com.berlin.felix.anhalt.Constants.maSchulungAnmelden;
import static com.berlin.felix.anhalt.Constants.onsiteInterview;
import static com.berlin.felix.anhalt.Constants.onsiteInterviewNondev;
import static com.berlin.felix.anhalt.Constants.onsiteInterviewNondevSecond;
import static com.berlin.felix.anhalt.Constants.onsiteRemoteHackSession;
import static com.berlin.felix.anhalt.Constants.personalStammbogen;
import static com.berlin.felix.anhalt.Constants.vertragsverhandlungen;
import static com.berlin.felix.anhalt.Constants.videoAnruf;
import static com.berlin.felix.anhalt.Constants.weitereVorbereitungen;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.berlin.felix.anhalt.Constants;
import com.berlin.felix.anhalt.model.EducationalLevel;
import com.berlin.felix.anhalt.model.Employee;
import com.berlin.felix.anhalt.model.Person;

@Component
public class UserTaskProcessingTimeCalculator {
	
	private ProcessEngine processEngine;
	private HashMap<String, Employee> employeeMap;
	private HashMap<String, Float>attributeBaseMultiplicatorValues;
	private Collection<Employee> employees;
	private int totalEmployees;
	
	{
		attributeBaseMultiplicatorValues = new HashMap<>();
		attributeBaseMultiplicatorValues.put(EducationalLevel.BACHELOR.name(), 0f);
		attributeBaseMultiplicatorValues.put(EducationalLevel.DOCTORAL.name(), -0.1f);
		attributeBaseMultiplicatorValues.put(EducationalLevel.MASTER.name(), 0.05f);
		attributeBaseMultiplicatorValues.put(EducationalLevel.SELF_TAUGHT.name(), -0.025f);
		attributeBaseMultiplicatorValues.put(EducationalLevel.VOCATIONAL_TRAINING.name(), 0.025f);
		attributeBaseMultiplicatorValues.put(Constants.MAENNLICH, 0f);
		attributeBaseMultiplicatorValues.put(Constants.WEIBLICH, -0.025f);
		attributeBaseMultiplicatorValues.put(Constants.DIVERS, 0.01f);		
	}
	
	@Autowired
	public UserTaskProcessingTimeCalculator (ProcessEngine processEngine) {
		this.processEngine = processEngine;
		employeeMap = new HashMap<>();
		setUpFirstWorkers();
		employees = employeeMap.values();
		totalEmployees = employees.size();
	}
	
	public Integer getTimeNeededForTask (String userTaskId, String employeeId) {
		return employeeMap.get(employeeId).getTaskCompletetionTime().get(userTaskId);
	}
	
	public void addEmployee(Employee employee) {
		this.employeeMap.put(employee.getEmployeeId(), employee);
		totalEmployees += 1;
	}
	
	private void setUpFirstWorkers() {
		createEmployee("1996-07-23", "some@mail.com", "Männlich", "Hubert Klauber");
		createEmployee("1983-02-12", "other@mail.com", "Weiblich", "Christina Schneider");
		createEmployee("1975-12-24", "otherest@mail.com", "Divers", "Concheetah Hurst");
	}
	
	private void createEmployee(String birthDate, String mail, String gender, String name) {
		Person person = new Person();
		Date firstBirthday = Date.from(LocalDate.parse("1996-07-23").atStartOfDay().toInstant(ZoneOffset.UTC));
		person.setBirthday(firstBirthday);
		person.setEmail("some@mail.com");
		person.setGender("Männlich");
		person.setName("Hubert Klauber");
		Employee employee = new Employee(person, getRandomTaskCompletitionTime());
		employeeMap.put(employee.getEmployeeId(), employee);
	}
	
	private HashMap<String, Integer> getRandomTaskCompletitionTime(){
		HashMap<String, Integer> taskCompletetionTime = new HashMap<>();
		taskCompletetionTime.put(bewerbungUeberpruefen, uniformInt(48, 72));
		taskCompletetionTime.put(loesungErhalten, uniformInt(12, 168));
		taskCompletetionTime.put(loesungUeberpruefen, uniformInt(48, 72));
		taskCompletetionTime.put(videoAnruf, uniformInt(24, 48));
		taskCompletetionTime.put(onsiteRemoteHackSession, uniformInt(24, 48));
		taskCompletetionTime.put(onsiteInterview, uniformInt(24, 48));
		taskCompletetionTime.put(vertragsverhandlungen, uniformInt(24, 48));
		taskCompletetionTime.put(onsiteInterviewNondev, uniformInt(24, 168));
		taskCompletetionTime.put(onsiteInterviewNondevSecond, uniformInt(24, 168));
		taskCompletetionTime.put(personalStammbogen, uniformInt(24, 72));
		taskCompletetionTime.put(abstimmungArbeitsmittel, uniformInt(24, 72));
		taskCompletetionTime.put(weitereVorbereitungen, uniformInt(48, 168));
		taskCompletetionTime.put(arbeitsmittelBeschaffen, uniformInt(48, 168));
		taskCompletetionTime.put(maSchulungAnmelden, uniformInt(24, 168));
		taskCompletetionTime.put(maEinweisen, uniformInt(5, 15));
		taskCompletetionTime.put(einweisungBestaetigung, uniformInt(1, 168)); 
		return taskCompletetionTime;
	}
	
	private int uniformInt(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}
	
	public String getWorker() {
		return employees.stream()
	        .skip((int) (totalEmployees * Math.random()))
	        .findFirst()
	        .get()
	        .getEmployeeId();
	}

	public HashMap<String, Float> getAttributeBaseMultiplicatorValues() {
		return attributeBaseMultiplicatorValues;
	}

}
