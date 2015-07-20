package com.ontotext.kstoilov.interview.task1;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import info.aduna.iteration.Iterations;

public class HTMLTransformator {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.out.println("The tool requires at leat two arguments.");
			return;
		}
		
		InputStream stream1 = new FileInputStream(args[0]);
		final Stack<HTMLTable> tables = new Stack<HTMLTable>();
		
		SAXParserImpl.newInstance(null).parse(stream1, new DefaultHandler() {
			
			private StringBuilder currentString = null;
			
			private int columnIndex = 0;
			
			@Override
			public void startElement(String uri, String localName, String name, Attributes attributes)
					throws SAXException {
				 	
				if (name.equalsIgnoreCase("table")) {
					tables.push(new HTMLTable());
				}
				
				else if (name.equalsIgnoreCase("th") || name.equalsIgnoreCase("td")) {
					currentString = new StringBuilder();
					if (name.equalsIgnoreCase("th")) {
						tables.peek().createColumn();
					}
				}
			}

			@Override
			public void endElement(String uri, String localName, String name) throws SAXException {
	
				if (name.equalsIgnoreCase("th")) {
					tables.peek().addHeader(currentString.toString());
					columnIndex++;
				}
				else if (name.equalsIgnoreCase("td")) {
					tables.peek().addToColumn(columnIndex, currentString.toString());
					columnIndex++;
				}
				else if (name.equalsIgnoreCase("tr")) {
					columnIndex = 0;
				}
				currentString = null;
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				if(currentString != null) {
					currentString.append(new String(ch, start, length));					
				}
			}
		});
		
		ArrayList<String> firstNames = tables.peek().getColumnByHeader("First Name");
		ArrayList<String> lastNames = tables.peek().getColumnByHeader("Last Name");

		String namespace = "http://ontotext.com/kstoilov/interview/task1";
		Repository rep = new SailRepository(new MemoryStore());
		rep.initialize();
		ValueFactory f  = rep.getValueFactory();
		
		RepositoryConnection conn = rep.getConnection();
		
		try {
			for (int i = 0; i < firstNames.size(); i++) {
				String person = firstNames.get(i) + " " + lastNames.get(i);
				URI personURI= f.createURI(namespace, person);
				conn.add(personURI, RDF.TYPE, FOAF.PERSON);
				conn.add(personURI, RDFS.LABEL, f.createLiteral(person, XMLSchema.STRING));
				RepositoryResult<Statement> statements =  conn.getStatements(null, null, null, true);
				Model model = Iterations.addAll(statements, new LinkedHashModel());
				Rio.write(model, System.out, RDFFormat.TURTLE);
			}
		}
		finally {
			conn.close();
		}
		
		
		
		
		
	}

}
