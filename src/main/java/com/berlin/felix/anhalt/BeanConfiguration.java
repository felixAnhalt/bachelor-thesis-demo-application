package com.berlin.felix.anhalt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.berlin.felix.anhalt.utils.UserTaskProcessingTimeCalculator;
import com.camunda.consulting.simulator.PayloadGenerator;
import com.camunda.consulting.simulator.SimulatorPlugin;

@Configuration
public class BeanConfiguration {
  
  @Bean
  public SimulatorPlugin simulatorPlugin() {
        return new SimulatorPlugin();
  }
  
  @Bean
  @Primary
  public PayloadGenerator generator(UserTaskProcessingTimeCalculator userTaskProcessingTimeCalculator) {
	  return new ContentGenerator(userTaskProcessingTimeCalculator);
  }

}
