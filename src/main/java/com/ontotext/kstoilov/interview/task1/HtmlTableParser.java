package com.ontotext.kstoilov.interview.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler implementation for reading HTML Table columns.
 * @author kstoilov
 */

public class HtmlTableParser extends DefaultHandler {
	
	private Stack<HtmlTable> tableStack;
	
	private List<HtmlTable> processedTables;
	
	private StringBuilder currentString = null;
	
	private int columnIndex = 0;
	
	public HtmlTableParser() {
		tableStack = new Stack<HtmlTable>();
		processedTables = new ArrayList<HtmlTable>();
	}
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes)
			throws SAXException {
		 	
		if (name.equalsIgnoreCase("table")) {
			tableStack.push(new HtmlTable());
		}
		
		else if (name.equalsIgnoreCase("th") || name.equalsIgnoreCase("td")) {
			currentString = new StringBuilder();
			if (name.equalsIgnoreCase("th")) {
				tableStack.peek().createColumn();
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		if (name.equalsIgnoreCase("th")) {
			tableStack.peek().addHeader(currentString.toString());
			columnIndex++;
		}
		else if (name.equalsIgnoreCase("td")) {
			tableStack.peek().addToColumn(columnIndex, currentString.toString());
			columnIndex++;
		}
		else if (name.equalsIgnoreCase("tr")) {
			columnIndex = 0;
		}
		else if (name.equalsIgnoreCase("table")){
			processedTables.add(tableStack.pop());
		}
		
		currentString = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(currentString != null) {
			currentString.append(new String(ch, start, length));					
		}
	}
	
	public List<HtmlTable> getTables() {
		return processedTables;
	}
}
