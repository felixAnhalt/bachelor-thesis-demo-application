package com.berlin.felix.anhalt;

public class Constants {
  
  private static final String BASIC_FILEPATH_ = "static/bpmn/";
  
  public static final String FILEPATH_ONBOARDING = BASIC_FILEPATH_ + "onboarding.bpmn"; 
  public static final String FILEPATH_EMPLOYEE_HIRING = BASIC_FILEPATH_ + "employeeHiring.bpmn"; 
  public static final String FILEPATH_APPLICATION_DEV = BASIC_FILEPATH_ + "applicationDev.bpmn"; 
  public static final String FILEPATH_APPLICATION_NON_DEV = BASIC_FILEPATH_ + "applicationNonDev.bpmn"; 
 

  public static final String PROCESS_KEY_ONBOARDING = "employeeOnboarding";
  public static final String PROCESS_KEY_EMPLOYEE_HIRING = "employeeHiring";
  public static final String PROCESS_KEY_APPLICATION_DEV = "hiring_process_for_dev";
  public static final String PROCESS_KEY_APPLICATION_NON_DEV = "hiring_process_for_candidate";

  public static final String DEPLOYMENT_NAME = "Deployment";
  
  /*
   * Payload constants for data generator.
   */
  
  public static final String goodFit = "goodFit";
  public static final String onsiteInterviewPassedFirst = "onsiteInterviewPassedFirst";
  public static final String onsiteInterviewPassedSecond = "onsiteInterviewPassedSecond";
  public static final String solutionGood = "solutionGood";
  public static final String videoCallPassed = "videoCallPassed";
  public static final String remoteSessionPassed = "remoteSessionPassed";
  public static final String devOnsiteInterviewPassed = "devOnsiteInterviewPassed";
  public static final String contractNegotiation = "contractNegotiation";
  
  /*
   * 
   */
  
  public static final String bewerbungUeberpruefen = "bewerbungUeberpruefen";
  public static final String loesungErhalten = "loesungErhalten";
  public static final String loesungUeberpruefen = "loesungUeberpruefen";
  public static final String videoAnruf = "videoAnruf";
  public static final String onsiteRemoteHackSession = "onsiteRemoteHackSession";
  public static final String onsiteInterview = "onsiteInterview";
  public static final String vertragsverhandlungen = "vertragsverhandlungen";
  public static final String onsiteInterviewNondev = "onsiteInterviewNondev";
  public static final String onsiteInterviewNondevSecond = "onsiteInterviewNondevSecond";
  public static final String personalStammbogen = "personalStammbogen";
  public static final String abstimmungArbeitsmittel = "abstimmungArbeitsmittel";
  public static final String weitereVorbereitungen = "weitereVorbereitungen";
  public static final String arbeitsmittelBeschaffen = "arbeitsmittelBeschaffen";
  public static final String maSchulungAnmelden = "maSchulungAnmelden";
  public static final String maEinweisen = "maEinweisen";
  public static final String einweisungBestaetigung = "einweisungBestaetigung"; 
  
  /*
   * Gender.
   */
  
  public static final String MAENNLICH = "Männlich";
  public static final String WEIBLICH = "Weiblich";
  public static final String DIVERS = "Divers";
  
}
