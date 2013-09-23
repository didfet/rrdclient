package info.fetter.rrdclient;

/*
 * Copyright 2013 Didier Fetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * Bidimensional table. Used by the fetch command to return the query results.
 * 
 * The rows are indexed by date and the columns by column name.
 * 
 * @author Didier Fetter
 *
 * @param <T>
 */
public class DataTable<T> {
	private static Logger logger = Logger.getLogger(DataTable.class);
	private List<String> columnNames = new ArrayList<String>();
	private List<List<T>> columns = new ArrayList<List<T>>();
	private List<Date> dateColumn = new ArrayList<Date>();
	private boolean columnAddForbidden = false;
	
	/**
	 * Add a column with the name columnName.
	 * 
	 * @param columnName
	 * @return this DataTable instance
	 * @throws IllegalStateException if rows have already been added.
	 */
	public DataTable<T> addColumn(String columnName) {
		if(columnAddForbidden)
			throw new IllegalStateException("Can't create a new column after rows have been added.");
		if(logger.isTraceEnabled())
			logger.trace("Adding column " + columnName);
		columnNames.add(columnName);
		columns.add(new ArrayList<T>());
		return this;
	}
	
	/**
	 * Add a row of data for the given date.
	 * 
	 * @param date
	 * @param data
	 * @throws IllegalArgumentException if the data list does not contain as many elements as the column list.
	 */
	public void addRow(Date date, List<T> data) {
		if(data.size() != columns.size()) {
			throw new IllegalArgumentException("Data list must contain " + columns.size() + " elements");
		}
		if(logger.isTraceEnabled()) {
			String dataString = ": ";
			for(T value : data) {
				dataString += " " + value;
			}
			logger.trace("Adding row " + date + dataString);
		}
		dateColumn.add(date);
		for(int i = 0; i < data.size(); i++) {
			List<T> column = columns.get(i);
			column.add(data.get(i));
		}
		columnAddForbidden = true;
	}
	
	/**
	 * Get a list containing all the values for a given column.
	 * 
	 * @param columnName the name of the data column
	 * @return the data rows corresponding to the column
	 * @throws IllegalArgumentException if the column name doesn't exist
	 */
	public List<T> getColumn(String columnName) {
		int index = columnNames.indexOf(columnName);
		if(index < 0)
			throw new IllegalArgumentException("Column " + columnName + " not found.");
		return columns.get(index);
	}
	
	/**
	 * Get the data for a given column and date. 
	 * 
	 * @param columnName the name of the data column
	 * @param date
	 * @return the requested data
	 */
	public T getData(String columnName, Date date) {
		int dateIndex = dateColumn.indexOf(date);
		return getData(columnName, dateIndex);
	}
	
	/**
	 * Get the data for a given column and index. 
	 * 
	 * @param columnName the name of the data column
	 * @param index
	 * @return the requested data
	 */
	public T getData(String columnName, int index) {
		List<T> column = getColumn(columnName);
		return column.get(index);
	}
	
	public Date getDateAtIndex(int index) {
		return dateColumn.get(index);
	}
	
	public int getNumberOfRows() {
		return dateColumn.size();
	}
	
	public int getNumberOfColumns() {
		return columns.size();
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}
	
	public List<Date> getDates() {
		return dateColumn;
	}
}
