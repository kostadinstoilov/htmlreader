package com.ontotext.kstoilov.interview.task1;

import java.util.ArrayList;
import java.util.List;

public class HTMLTable {
	
	private static final int COLUMNSIZE = 10;
	
	private static final int ROWSIZE = 100;
	
	private List<String> headers;
	
	private List<ArrayList<String>> columns;
	
	public HTMLTable() {
		headers = new ArrayList<String>(COLUMNSIZE);
		columns = new ArrayList<ArrayList<String>>(COLUMNSIZE);
	}
	
	public void addHeader(String header) {
		headers.add(header);
	}
	
	public void createColumn() {
		columns.add(new ArrayList<String>(ROWSIZE));
	}
	
	public void addToColumn(int index, String value) {
		columns.get(index).add(value);
	}
	
	public List<String> getColumnByHeader(String headerName) {
		
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).equalsIgnoreCase(headerName)) {
				return columns.get(i);
			}
		}
		
		return null;
		
	}
	
}
