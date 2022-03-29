package com.bubai.ssp.batch.service.tasklet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ParameterManagerTasklet implements Tasklet {
	private Integer count=0;
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		if("NO".equals(contribution.getStepExecution().getJobExecution().getExecutionContext().getString("TRIGGERBATCH"))) {
			String fileNames=new String();
			List<String> filesPresent=Arrays.asList(contribution.getStepExecution().getJobExecution().getExecutionContext().getString("INPUTFILES").split("\\|"));
			if(filesPresent.size()>1) {
				filesPresent.remove(contribution.getStepExecution().getJobExecution().getExecutionContext().getString("CURRENTFILENAME"));
				fileNames = filesPresent.stream().collect(Collectors.joining("|"));
			}
			else {
				fileNames = filesPresent.stream().collect(Collectors.joining("|"));
				if(fileNames.equalsIgnoreCase(contribution.getStepExecution().getJobExecution().getExecutionContext().getString("CURRENTFILENAME"))) {
					fileNames="|";
				}
			}
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("INPUTFILES",fileNames);
		}
		String[] inputFiles=contribution.getStepExecution().getJobExecution().getExecutionContext().getString("INPUTFILES").split("\\|");
		if(count>inputFiles.length) {
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("FILEPRESENT","NO");
		}
		else {
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("FILEPRESENT","YES");
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("ERRORPRESENT","NO");
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("TRIGGERBATCH","NO");
			contribution.getStepExecution().getJobExecution().getExecutionContext().putString("CURRENTFILENAME",inputFiles[count++]);
		}
		return RepeatStatus.FINISHED;
	}

}
