package com.camunda.academy.services;

public class PDFService {

	
	private static final int PDF_CREATION_TIME = 1000;

	public void createPDF() throws InterruptedException {
		
		System.out.println("Creating PDF...");

		Thread.sleep(PDF_CREATION_TIME);

		System.out.println("...PDF created");

	}
}