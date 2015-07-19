package com.ontotext.kstoilov.interview.task1;

import java.util.ArrayList;

public class Table {
	
	private static int defaultSize = 10;
	
	private ArrayList<String> headers;
	
	private ArrayList<ArrayList<String>> columns;
	
	public Table() {
		headers = new ArrayList<String>(defaultSize);
		columns = new ArrayList<ArrayList<String>>(defaultSize);
	}
	
	public void addHeader(String header) {
		headers.add(header);
	}
	
	public void createColumn(int index) {
		columns.set(index, new ArrayList<String>(100));
	}
	
	public void addToColumn(int index, String value) {
		columns.get(index).add(value);
	}
	
	public ArrayList<String> getColumnByHeader(String headerName) {
		
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).equals(headerName)) {
				return columns.get(i);
			}
		}
		
		return null;
		
	}
	
}
