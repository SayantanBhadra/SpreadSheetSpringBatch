package com.bubai.ssp.batch.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bubai.ssp.batch.launcher.ExcelToDatabaseJobLauncher;
import com.bubai.ssp.batch.service.EmployeeProcessorService;

@Service
public class EmployeeProcessorServiceImpl implements EmployeeProcessorService {
	@Autowired
	private ExcelToDatabaseJobLauncher jobLauncher;
	@Value("${data.file.location}")
	private String destination;

	@Override
	public void launchEmployeeToExcelDatabaseJob() {
		try {
			jobLauncher.launchExcelToDataJob();
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void splitExcelFile(String name, InputStream in) {
		// TODO Auto-generated method stub
		try {
			this.saveExcelFile(name, in);
			int noOfSheets = this.getNoOfSheets(name);
			for (int i = 0; i < noOfSheets; i++) {
				this.writeSplitedExcelFile(noOfSheets, i, name);
			}

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveExcelFile(String fileName, InputStream in) {
		try(XSSFWorkbook workbook = new XSSFWorkbook(in)) {
			FileOutputStream outFile = new FileOutputStream(new File(destination + fileName));
			workbook.write(outFile);
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getNoOfSheets(String fileName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(destination + fileName)));
			return workbook.getNumberOfSheets();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private void writeSplitedExcelFile(int noOfSheets, int sheetIndex, String fileName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(destination + fileName)));
			for (int i = 0; i < noOfSheets; i++) {
				if (i != sheetIndex) {
					workbook.removeSheetAt(noOfSheets - (i + 1));
				}
			}
			FileOutputStream outFile = new FileOutputStream(
					new File(destination + (noOfSheets - (sheetIndex + 1)) + "_" + fileName));
			workbook.write(outFile);
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
