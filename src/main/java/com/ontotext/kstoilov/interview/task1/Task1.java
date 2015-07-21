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
	
	private static final String NAMESPACE = "http://ontotext.com/kstoilov/interview/task1/";

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
	
	public static void writeRdf(List<String> content, File file, RDFFormat format) {
		
		RdfBuilder builder;
		FileOutputStream fileStream;
		try {
			fileStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			System.err.println("Could not open file for writing " + file);
			return;
		}

		try {
			builder = new RdfBuilder(NAMESPACE, content);
			builder.write(fileStream, format);
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
						
			RDFFormat format = RDFFormat.TURTLE;
			
			if (outputFormat.equalsIgnoreCase("rdf.ntriples")) {
				format = RDFFormat.NTRIPLES;
			}
			else if (outputFormat.equalsIgnoreCase("rdf.xml")) {
				format = RDFFormat.RDFXML;
			}
			else if (outputFormat.equalsIgnoreCase("rdf.turtle")) {
				format = RDFFormat.TURTLE;
			}
			else {
				System.out.println("Unknown/Unsupported rdf format, defaulting to rdf.turtle");
			}
			
			writeRdf(r.getPeople(), file, format);
			return;
		}
		
		writePlain(r.getPeople(), file);
	}
	
	public static void main(String[] args) {
		
		String format = "plain";
		
		if (args.length < 2) {
			System.out.println("The tool requires at leat two arguments.");
			return;
		}
		
		else if (args.length == 3) {
			if (args[2].startsWith("rdf")) {
				format = args[2];			
			}
			else if (! args[2].equalsIgnoreCase(format)) {
				System.out.println("Unknown/Unsupported output format " + args[3] + " defaulting to 'plain'");
			}
		}
		
		handleFile(args[0], format);
		handleFile(args[1], format);
	}

}
