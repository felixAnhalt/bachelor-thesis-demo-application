package com.berlin.felix.anhalt;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableProcessApplication("demo-data-application")
public class CamundaDemoDataApplication {

  public static void main(String[] args) {
    SpringApplication.run(CamundaDemoDataApplication.class, args);
  }
  
}
