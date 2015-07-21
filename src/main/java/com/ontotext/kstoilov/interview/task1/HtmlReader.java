package com.ontotext.kstoilov.interview.task1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;

/**
 * Reads an HTML file and stores table columns with headers <th>First Name</th> and <th>Last Name</th>.
 * @author kstoilov
 *
 */

public class HtmlReader {
	
	private String file;
	
	private List<String> people;
	
	public HtmlReader(String file) throws MalformedURLException, IOException, SAXException {
		this.file = file;
		people = parseFile(getInputStream(file));
	}
	
	private InputStream getInputStream(String argument) throws MalformedURLException, IOException {
		
		UrlValidator urlValidator = new UrlValidator();
		
		if (urlValidator.isValid(argument)) {
			return new URL(argument).openConnection().getInputStream();	
		}
		
		return new FileInputStream(argument);
	}
	
	private List<String> parseFile(InputStream input) throws SAXException, IOException {
		
		HtmlTableParser parser = new HtmlTableParser();
		SAXParserImpl.newInstance(null).parse(input, parser);
		
		List<String> names = new ArrayList<String>(100);
		
		for (HtmlTable table : parser.getTables()) {
			List<String> firstNames = table.getColumnByHeader("first name");
			List<String> lastNames = table.getColumnByHeader("last name");
			
			int maxSize = Math.max(firstNames.size(), lastNames.size());
			for (int i = 0; i < maxSize; i++) {
				String name = "";
				if (i < firstNames.size()) {
					name += firstNames.get(i);
				}
				if (i < lastNames.size()) {
					name += " " + lastNames.get(i);
				}
				names.add(name);
			}
		}
		
		return names;
	}
	
	public List<String> getPeople() {
		
		return people;
	}
	
	

}
