package com.example.demo;

import com.example.CustomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomStarterDemoApplication implements CommandLineRunner {

  @Autowired(required = false)
  private CustomService customService;

  public static void main(String[] args) {
    SpringApplication.run(CustomStarterDemoApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (customService != null) {
      customService.printMessage();
    } else {
      System.out.println("No service present");
    }
  }
}