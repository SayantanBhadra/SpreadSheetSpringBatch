package com.bubai.ssp.batch.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/hello")
public class EmployeeProcessorController {
	@GetMapping("/get")
	public String getHelloUser() {
		return "Hi!!! From User Service from port number:";
	}
}
