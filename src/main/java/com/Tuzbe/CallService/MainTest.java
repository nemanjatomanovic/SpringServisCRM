package com.Tuzbe.CallService;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class MainTest {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		File file1 = new File("/opt/apache-tomcat-8.0.41/webapps/crm_files/test1.txt");
		//PDDocument doc1 = PDDocument.load(file1);

		File file2 = new File("C:\\Users\\Nemanja\\Desktop\\test2.pdf");
		//PDDocument doc2 = PDDocument.load(file2);

		PDFMergerUtility PDFmerger = new PDFMergerUtility();

		PDFmerger.setDestinationFileName("C:\\Users\\Nemanja\\Desktop\\sastavljeno.pdf");

		// adding the source files
		PDFmerger.addSource(file1);
		PDFmerger.addSource(file2);

		// Merging the two documents
		PDFmerger.mergeDocuments();

		System.out.println("Documents merged");
		// Closing the documents
//		doc1.close();
//		doc2.close();
	}
}
