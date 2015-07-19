package com.ontotext.kstoilov.interview.task1;

import java.util.ArrayList;

public class Table {
	
	private static final int COLUMNSIZE = 10;
	
	private static final int ROWSIZE = 100;
	
	private ArrayList<String> headers;
	
	private ArrayList<ArrayList<String>> columns;
	
	public Table() {
		headers = new ArrayList<String>(COLUMNSIZE);
		columns = new ArrayList<ArrayList<String>>(COLUMNSIZE);
	}
	
	public void addHeader(String header) {
		headers.add(header);
	}
	
	public void createColumn(int index) {
		columns.set(index, new ArrayList<String>(ROWSIZE));
	}
	
	public void addToColumn(int index, String value) {
		columns.get(index).add(value);
	}
	
	public ArrayList<String> getColumnByHeader(String headerName) {
		
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).equalsIgnoreCase(headerName)) {
				return columns.get(i);
			}
		}
		
		return null;
		
	}
	
}
