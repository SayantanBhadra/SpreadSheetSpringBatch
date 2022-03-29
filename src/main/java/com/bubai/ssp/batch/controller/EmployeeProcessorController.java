package com.bubai.ssp.batch.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bubai.ssp.batch.service.EmployeeProcessorService;

@RestController
@CrossOrigin
@RequestMapping("/hello")
public class EmployeeProcessorController {
	@Autowired
	private EmployeeProcessorService service;
	@GetMapping("/get")
	public String getHelloUser() {
		service.launchEmployeeToExcelDatabaseJob();
		return "Hi!!! From User Service from port number:";
	}
	@PostMapping("/upload/excel")
	public String uploadExcelFile(@RequestBody MultipartFile file) {
		try {
			service.splitExcelFile(file.getOriginalFilename(),file.getInputStream());
			service.launchEmployeeToExcelDatabaseJob();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "File Uploaded Successfully";
	}
}
