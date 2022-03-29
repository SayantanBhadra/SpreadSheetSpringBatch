package com.bubai.ssp.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.bubai.ssp.batch.listener.CustomJobExecutionListener;
import com.bubai.ssp.batch.processor.CustomEmployeeProcessor;
import com.bubai.ssp.batch.writer.CustomEmployeeWriter;
import com.bubai.ssp.entity.Employee;
import com.bubai.ssp.model.EmployeeDTO;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Bean
	public ItemReader<EmployeeDTO> excelEmployeeReader(){
		PoiItemReader<EmployeeDTO> reader = new PoiItemReader<EmployeeDTO>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("data/Employee.xlsx"));
		reader.setRowMapper(excelRowMapper());
		return reader;
	}
	@Bean
	public ItemProcessor<EmployeeDTO, Employee> customEmployeeProcessor(){
		return new CustomEmployeeProcessor();
	}
	@Bean
	public ItemWriter<Employee> customEmployeeWriter(){
		return new CustomEmployeeWriter();
	}
	private RowMapper<EmployeeDTO> excelRowMapper(){
		BeanWrapperRowMapper<EmployeeDTO> rowMapper = new BeanWrapperRowMapper<>();
		rowMapper.setTargetType(EmployeeDTO.class);
		return rowMapper;
	}
	
	public JobExecutionDecider eachFileDecider() {
		return (JobExecution jobExecution,StepExecution stepExecution)->{
			Boolean filePresent="YES".equalsIgnoreCase(jobExecution.getExecutionContext().getString("FILEPRESENT"))?true:false;
			return filePresent?new FlowExecutionStatus("CONTINUE"):new FlowExecutionStatus("COMPLETED");
		};
	}
	@Bean
	public Step employeeExcelToDatabaseStep(ItemReader<EmployeeDTO> excelEmployeeReader,
			ItemProcessor<EmployeeDTO,Employee> customEmployeeProcessor,
			ItemWriter<Employee> customEmployeeWriter,StepBuilderFactory sdf) {
		return sdf.get("employeeExcelToDatabaseStep").<EmployeeDTO,Employee>chunk(10)
				.reader(excelEmployeeReader)
				.processor(customEmployeeProcessor)
				.writer(customEmployeeWriter)
				.build();
	}
	@Bean
	public Job employeeExcelToDatabaseJob(JobBuilderFactory jbf,@Qualifier("employeeExcelToDatabaseStep")Step employeeExcelToDatabaseStep) {
		return jbf
				.get("employeeExcelToDatabaseJob")
				.incrementer(new RunIdIncrementer())
				.listener(new CustomJobExecutionListener())
				.flow(employeeExcelToDatabaseStep)
				.end().build();
	}
}
