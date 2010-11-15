package af.statguitoolkit.gui.datatable;

import java.util.List;
import java.util.Vector;


public class RDataFrameRef {

	List<String> rcNames = new Vector<String>();
	Vector<Vector<Double>> data = new Vector<Vector<Double>>();  
	
	public RDataFrameRef () {
		addRowCol("H1");
	}

	public String getColName(int col) {
		return rcNames.get(col);
	}
	
	public Object getRowName(int row) {
		return rcNames.get(row);
	}

	public void setVarName(String oldName, String newName) {
		
	}

	public void setValue(int row, int col, Double value) {
		data.get(row).set(col, value);
	}

	public double[] getCol(int col) {
		double[] value = new double[getRowCount()];
		for (int i=0; i < getRowCount(); i++) {
			value[i] = data.get(i).get(col);
		}
		return value;
	}

	public double getElement(int row, int col) {
		return data.get(row).get(col);
	}


	
	public void delRowCol(int col) {
		rcNames.remove(col); 
		data.remove(col);
		for (Vector<Double> v: data) {
			v.remove(col);
		}
	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return data.size();
	}

	public void addRowCol(String name) {
		rcNames.add(name);
		Vector<Double> row = new Vector<Double>();
		for (int i=0; i < getColumnCount(); i++) {row.add(0.0);}
		data.add(row);
		for (int i=0; i < getRowCount(); i++) {data.get(i).add(0.0);}
		System.out.println("Data has "+getRowCount()+" rows and "+getColumnCount()+" columns now.");
	}



}
