package com.berlin.felix.anhalt;

import static com.berlin.felix.anhalt.Constants.FILEPATH_APPLICATION_DEV;
import static com.berlin.felix.anhalt.Constants.FILEPATH_APPLICATION_NON_DEV;
import static com.berlin.felix.anhalt.Constants.FILEPATH_EMPLOYEE_HIRING;
import static com.berlin.felix.anhalt.Constants.FILEPATH_ONBOARDING;

import java.util.Timer;
import java.util.TimerTask;

import org.camunda.bpm.application.AbstractProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ResumePreviousBy;
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationStartedEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.camunda.consulting.simulator.SimulationExecutor;
import com.camunda.consulting.simulator.SimulatorPlugin;

@Component
public class DemoData {

	private ProcessEngine processEngine;
	private AbstractProcessApplication processApp;

	@Autowired
	public DemoData(ProcessEngine processEngine, AbstractProcessApplication processApp) {
		this.processEngine = processEngine;
		this.processApp = processApp;
	}

	private void deployProcessDiagramms(String... classpathresourceNames) {
		DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
				.createDeployment(processApp.getReference()).name(Constants.DEPLOYMENT_NAME).resumePreviousVersions()
				.resumePreviousVersionsBy(ResumePreviousBy.RESUME_BY_PROCESS_DEFINITION_KEY);

		for (String resource : classpathresourceNames) {
			deploymentBuilder = deploymentBuilder.addClasspathResource(resource);
		}
		Deployment deployment = deploymentBuilder.deploy();

		processEngine.getManagementService().registerProcessApplication(deployment.getId(), processApp.getReference());
	}

	public void deployProcesses() {
		deployProcessDiagramms(FILEPATH_APPLICATION_DEV, FILEPATH_EMPLOYEE_HIRING, FILEPATH_ONBOARDING,
				FILEPATH_APPLICATION_NON_DEV);
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(DemoData.class);

	private void runSimulationAndStopAfterwards() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				SimulationExecutor.execute(DateTime.now().minusMonths(9).toDate(), DateTime.now().toDate());
				SimulatorPlugin.resetProcessEngine();
				deployProcesses();
			}
		}, 10_000);
	}

	@EventListener
	public void notify(final ProcessApplicationStartedEvent processApplicationStartedEvent) {
		deployProcesses();
		runSimulationAndStopAfterwards();
	}

}
