package com.bubai.ssp.batch.service;

import java.io.InputStream;

public interface EmployeeProcessorService {

	public void launchEmployeeToExcelDatabaseJob();

	public void splitExcelFile(String name,InputStream in);

}
