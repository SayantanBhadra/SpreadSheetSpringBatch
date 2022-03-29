package com.bubai.ssp.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecutionListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		String triggerBatch=jobExecution.getJobParameters().getString("TRIGEERBATCH");
		String inputPath=jobExecution.getJobParameters().getString("INPUTPATH");
		String inputFiles=jobExecution.getJobParameters().getString("INPUTFILES");
		
		jobExecution.getExecutionContext().putString("TRIGEERBATCH", triggerBatch);
		jobExecution.getExecutionContext().putString("INPUTPATH", inputPath);
		jobExecution.getExecutionContext().putString("INPUTFILES", inputFiles);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub

	}

}
