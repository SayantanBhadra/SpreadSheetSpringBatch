package com.bubai.ssp.batch.launcher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExcelToDatabaseJobLauncher {
	
	private JobLauncher jobLauncher;
	private Job job;
	
	@Autowired
	ExcelToDatabaseJobLauncher(@Qualifier("employeeExcelToDatabaseJob") Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }
	
	public void launchExcelToDataJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		jobLauncher.run(job, this.getJobParameters());
	}
	
	private JobParameters getJobParameters() {
		Map<String,JobParameter> param = new HashMap<>();
		JobParameter dateParam = new JobParameter(new Date());
		param.put("JobCreationTime", dateParam);
		return new JobParameters(param);
	}
}
