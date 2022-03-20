package com.bubai.ssp.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.bubai.ssp.entity.Employee;

public class CustomEmployeeWriter implements ItemWriter<Employee> {

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		// TODO Auto-generated method stub
		for(Employee employee:items) {
			System.out.println("Writing Employee:"+employee.toString());
		}
	}

}
