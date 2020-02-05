package com.berlin.felix.anhalt;

import static com.berlin.felix.anhalt.Constants.DIVERS;
import static com.berlin.felix.anhalt.Constants.MAENNLICH;
import static com.berlin.felix.anhalt.Constants.WEIBLICH;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berlin.felix.anhalt.model.EducationalLevel;
import com.berlin.felix.anhalt.model.Employee;
import com.berlin.felix.anhalt.model.Application;
import com.berlin.felix.anhalt.model.Person;
import com.berlin.felix.anhalt.model.Role;
import com.berlin.felix.anhalt.utils.UserTaskProcessingTimeCalculator;
import com.camunda.consulting.simulator.PayloadGenerator;
import com.camunda.consulting.simulator.SimulationExecutor;

public class ContentGenerator extends PayloadGenerator {

	private final static Logger LOGGER = LoggerFactory.getLogger(ContentGenerator.class);
	
	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static long instances = 0;
	
	private UserTaskProcessingTimeCalculator userTaskProcessingTimeCalculator;
	
	public ContentGenerator(UserTaskProcessingTimeCalculator userTaskProcessingTimeCalculator) {
		this.userTaskProcessingTimeCalculator = userTaskProcessingTimeCalculator;
	}

	@SuppressWarnings("unchecked")
	public <T> T uniformFromArrayTypeless(T... elements) {
		return (T) super.uniformFromArray(elements);
	}
	
	public Employee getEmployeeFromApplication(Application application) {
		float multiplicator = calculateTimeMultiplicator(application);

		HashMap<String, Integer> taskCompletetionTime = new HashMap<>();
		taskCompletetionTime.put(bewerbungUeberpruefen, uniformInt(48 , 72, multiplicator));
		taskCompletetionTime.put(loesungErhalten, uniformInt(12, 168, multiplicator));
		taskCompletetionTime.put(loesungUeberpruefen, uniformInt(48, 72, multiplicator));
		taskCompletetionTime.put(videoAnruf, uniformInt(24, 48, multiplicator));
		taskCompletetionTime.put(onsiteRemoteHackSession, uniformInt(24, 48, multiplicator));
		taskCompletetionTime.put(onsiteInterview, uniformInt(24, 48, multiplicator));
		taskCompletetionTime.put(vertragsverhandlungen, uniformInt(24, 48, multiplicator));
		taskCompletetionTime.put(onsiteInterviewNondev, uniformInt(24, 168, multiplicator));
		taskCompletetionTime.put(onsiteInterviewNondevSecond, uniformInt(24, 168, multiplicator));
		taskCompletetionTime.put(personalStammbogen, uniformInt(24, 72, multiplicator));
		taskCompletetionTime.put(abstimmungArbeitsmittel, uniformInt(24, 72, multiplicator));
		taskCompletetionTime.put(weitereVorbereitungen, uniformInt(48, 168, multiplicator));
		taskCompletetionTime.put(arbeitsmittelBeschaffen, uniformInt(48, 168, multiplicator));
		taskCompletetionTime.put(maSchulungAnmelden, uniformInt(24, 168, multiplicator));
		taskCompletetionTime.put(maEinweisen, uniformInt(5, 15, multiplicator));
		taskCompletetionTime.put(einweisungBestaetigung, uniformInt(1, 168, multiplicator)); 
		Employee employee = new Employee(application.getApplicant(), taskCompletetionTime);
		return employee;
	}
	
	private float calculateTimeMultiplicator(Application application) {
		float multiplicator = 1f;
		if(application.getApplicant().getAge() > 27) {
			multiplicator += (application.getApplicant().getAge() - 27f) / 100f;
		}
		multiplicator += userTaskProcessingTimeCalculator.getAttributeBaseMultiplicatorValues().get(application.getEducationalLevel());
		multiplicator += userTaskProcessingTimeCalculator.getAttributeBaseMultiplicatorValues().get(application.getApplicant().getGender());
		
		return multiplicator;
	}
	
	public Integer uniformInt(int min, int max, float multiplicator) {
		return (int) (uniformInt(min, max) * multiplicator);
	}

	public Application newApplication() {
		Application application = null;

		String gender = uniformFromArrayTypeless(MAENNLICH, WEIBLICH, DIVERS);

		boolean male = gender == MAENNLICH;
		boolean female = gender == WEIBLICH;

		String name;

		if (male) {
			name = getMaleName();
		} else if (female) {
			name = getFemaleName();
		} else {
			name = getDiversName();
		}

		String email = email(name, uniformFromArgs3("GoogleMail", "Hotmail", "Yahoo"));
		Date birthdate = uniformBirthdate(18, 64);

		EducationalLevel educationalLevel = uniformFromArrayTypeless(EducationalLevel.values());
		Role role = uniformFromArrayTypeless(Role.values());

		application = createApplication(birthdate, name, email, gender, role, educationalLevel);
		instances++;

		if (instances % 100 == 0)
			LOGGER.info("Created " + new DecimalFormat("0000").format(instances) + " Instances and progress: "
					+ new DecimalFormat("000.##").format(SimulationExecutor.getProgress() * 100) + "!");

		return application;
	}
	
	public String workerId() {
		return userTaskProcessingTimeCalculator.getWorker();
	}

	private String getMaleName() {
		return firstnameMale() + " " + surnameGerman();
	}

	private String getFemaleName() {
		return firstnameFemale() + " " + surnameGerman();
	}

	private String getDiversName() {
		return firstname() + " " + surnameGerman();
	}

	public static Application createApplication(Date birthDate, String name, String email, String gender, Role role,
			EducationalLevel educationalLevel) {
		Application newApplication = new Application();
		newApplication.setApplicant(new Person());
		newApplication.setRole(role.name());
		newApplication.setEducationalLevel(educationalLevel.name());
		newApplication.getApplicant().setBirthday(birthDate);
		newApplication.getApplicant().setName(name);
		newApplication.getApplicant().setEmail(email);
		newApplication.getApplicant().setGender(gender);
		return newApplication;
	}

	public Date getCompletitionDate(int hoursNeeded) {
		return nowPlusHours(hoursNeeded);
	}

	public Date bewerbungUeberpruefen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(bewerbungUeberpruefen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date loesungErhalten(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(loesungErhalten, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date loesungUeberpruefen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(loesungUeberpruefen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date videoAnruf(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(videoAnruf, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date onsiteRemoteHackSession(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(onsiteRemoteHackSession, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date onsiteInterview(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(onsiteInterview, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date vertragsverhandlungen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(vertragsverhandlungen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date onsiteInterviewNondev(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(onsiteInterviewNondev, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date onsiteInterviewNondevSecond(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(onsiteInterviewNondevSecond, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date personalStammbogen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(personalStammbogen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date abstimmungArbeitsmittel(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(abstimmungArbeitsmittel, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}
	
	public Date weitereVorbereitungen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(weitereVorbereitungen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date arbeitsmittelBeschaffen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(arbeitsmittelBeschaffen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date arbeitsplatzVorbereiten(String firstWorkingDay) {
		return LocalDate.parse(firstWorkingDay).minusDays(1).toDate();
	}

	public Date maSchulungAnmelden(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(maSchulungAnmelden, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date maEinweisen(String employeeId) {
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(maEinweisen, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Date einweisungBestaetigung(String employeeId, Application application) {
		if(application.getRole().equalsIgnoreCase(Role.CLERK.name()) && uniformBoolean()) {
			userTaskProcessingTimeCalculator.addEmployee(getEmployeeFromApplication(application));			
		}
		int userTaskProcessingTime = userTaskProcessingTimeCalculator.getTimeNeededForTask(einweisungBestaetigung, employeeId);
		return getCompletitionDate(userTaskProcessingTime);
	}

	public Boolean goodFit() {
		return true;
	}

	public boolean onsiteInterviewPassedFirst() {
		return true;
	}

	public boolean onsiteInterviewPassedSecond() {
		return true;
	}

	public boolean solutionGood() {
		return true;
	}

	public boolean videoCallPassed() {
		return true;
	}

	public boolean remoteSessionPassed() {
		return true;
	}

	public boolean devOnsiteInterviewPassed() {
		return true;
	}

	public String contractNegotiation() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowPlusHours(uniformInt(168, 336)));
		
		String firstWorkingDay = formatter.format(cal.getTime());
		return firstWorkingDay;
	}

}
