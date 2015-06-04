package models;

import javax.swing.table.AbstractTableModel;

public class LobbyModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;
	private String[][] tableData;
	
	private String[] columnNames = {
			"Title",
			"Players",
			"Map"
	};
	public LobbyModel(String[][] tableData){
		this.tableData = tableData;
	}
		 
	private Object[][] data = {
		{"N/A", "N/A", "N/A"}
	};
	
	public void rereadData(String args){
		fireTableDataChanged();
	}

	public int getColumnCount() {
		System.out.println(tableData.length);
		if(tableData.length == 0){
			return 0;
		}
		return tableData[0].length;
	}
		
	public int getRowCount() {
		return tableData.length;
	}
		
	public String getColumnName(int col) {
		return columnNames[col];
	}
		
	public Object getValueAt(int row, int col) {
		return tableData[row][col];
	}
	
	/*
	* JTable uses this method to determine the default renderer/
	* editor for each cell.  If we didn't implement this method,
	* then the last column would contain text ("true"/"false"),
	* rather than a check box.
	*/
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	* Don't need to implement this method unless your table's
	* editable.
	*/
	public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
		if (col < 2) {
		return false;
		} else {
		return true;
		}
	}

	/*
	* Don't need to implement this method unless your table's
	* data can change.
	*/
	public void setValueAt(String value, int row, int col) {
		if (DEBUG) {
		System.out.println("Setting value at " + row + "," + col
		            + " to " + value
		            + " (an instance of "
		            + value.getClass() + ")");
		}

		tableData[row][col] = value;
		fireTableCellUpdated(row, col);

		if (DEBUG) {
		System.out.println("New value of data:");
		printDebugData();
		}
	}

	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i=0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j=0; j < numCols; j++) {
				System.out.print("  " + tableData[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}
}