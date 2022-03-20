package com.bubai.ssp.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.bubai.ssp.entity.Employee;
import com.bubai.ssp.model.EmployeeDTO;

public class CustomEmployeeProcessor implements ItemProcessor<EmployeeDTO, Employee> {

	@Override
	public Employee process(EmployeeDTO item) throws Exception {
		// TODO Auto-generated method stub
		Employee employee = new Employee();
		employee.setFirstName(item.getFirstName());
		employee.setLastName(item.getLastName());
		employee.setDoj(item.getDoj());
		System.out.println("Converting EmployeeDTO Object"+item.toString()+" to Employee Object"+employee.toString());
		return employee;
	}

}
