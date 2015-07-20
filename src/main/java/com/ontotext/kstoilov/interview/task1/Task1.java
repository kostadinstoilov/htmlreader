package com.ontotext.kstoilov.interview.task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.List;

import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.xml.sax.SAXException;

public class Task1 {

	public static void writePlain(List<String> content, File file) {
		
		Writer fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			for (String line : content) {
				line += System.getProperty("line.separator");
				bufferedWriter.write(line);
			}

		} catch (IOException e) {
			System.err.println("Error writing the to file: " + file.getName());
		} 
		finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();					
				}
				if (fileWriter != null) {
					fileWriter.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public static void writeRdf(List<String> content, File file) {
		
		RdfBuilder builder;
		FileOutputStream fileStream;
		try {
			fileStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			System.err.println("Could not open file for writing " + file);
			return;
		}

		try {
			builder = new RdfBuilder("http://ontotext.com/kstoilov/interview/task1", content);
			builder.write(fileStream, RDFFormat.TURTLE);
		} catch (RepositoryException e) {
			System.err.println("RDF Repository Exception " + e.getMessage());
		} catch (RDFHandlerException e) {
			System.err.println("RDF Handler Exception " + e.getMessage());
		} finally {
			try {
				fileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void handleFile(String filename, String outputFormat) {

		HtmlReader r;
		try {
			r = new HtmlReader(filename);
		} catch (MalformedURLException e1) {
			System.err.println("Malformed URL specified: " + filename);
			return;
		} catch (IOException e1) {
			System.err.println("Could not read from resource: " + filename);
			return;
		} catch (SAXException e1) {
			System.err.println("Could not parse HTML from: " + filename);
			return;
		}
		
		File file = new File(new File(filename).getName() + "." + outputFormat);
		
		if (outputFormat.startsWith("rdf")) {
			writeRdf(r.getPeople(), file);
			return;
		}
		
		writePlain(r.getPeople(), file);
	}
	
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("The tool requires at leat two arguments.");
			return;
		}
		
		handleFile(args[1], "rdf");
	}

}
