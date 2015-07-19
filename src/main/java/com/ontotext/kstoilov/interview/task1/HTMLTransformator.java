package com.ontotext.kstoilov.interview.task1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;

public class HTMLTransformator {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.out.println("The tool requires at leat two arguments.");
			return;
		}
		
		InputStream stream1 = new FileInputStream(args[0]);
		final Stack<Table> tables = new Stack<Table>();
		
		SAXParserImpl.newInstance(null).parse(stream1, new DefaultHandler() {
			
			private StringBuilder currentString = null;
			
			private int columnIndex = 0;
			
			@Override
			public void startElement(String uri, String localName, String name, Attributes attributes)
					throws SAXException {
				 
				if (name.equalsIgnoreCase("table")) {
					tables.push(new Table());
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
		
		for (int i = 0; i < firstNames.size(); i++) {
			System.out.println(firstNames.get(i) + " " + lastNames.get(i));
		}
		
	}

}
