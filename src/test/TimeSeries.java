package test;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimeSeries {

	HashMap<String, ArrayList<Float>> HM= new HashMap <String, ArrayList<Float>>();
	int dataRowSize;

	public int getRowSize() {
		return dataRowSize;
	}

	public TimeSeries(String csvFileName) {
		int i;
		String line="";
		BufferedReader lineRead=null;
		try{
			lineRead=new BufferedReader(new FileReader(csvFileName));
			line=lineRead.readLine();
			String []check=line.split(",");
			for (String str:check) {
				HM.put(str, new ArrayList<Float>());//create the new array base on the abcd..
			}
			while ((line=lineRead.readLine())!=null){
				String[] row=line.split(",");
				for ( i=0;i<check.length;i++) {
					HM.get(check[i]).add(Float.parseFloat(row[i]));//put in every array(abcd) value
				}
			}
			dataRowSize=HM.get(String.valueOf((char) 65)).size();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				lineRead.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
