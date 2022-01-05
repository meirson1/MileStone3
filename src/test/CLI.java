package test;

import java.util.ArrayList;
import java.util.Scanner;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();
		// example: commands.add(c.new ExampleCommand());
		// implement
		commands.add(c.new UploadCsvFile());
	}
	
	public void start() {
		boolean exit=false;

		while (!exit){
			dio.write("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
					"3. detect anomalies\n" +
					"4. display results\n" +
					"5. upload anomalies and analyze results\n" +
					"6. exit\n");
			int choice= (int) dio.readVal();
			switch (choice) {
				case 1:
					commands.add(c.new UploadCsvFile());
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					exit=true;
					break;
			}
		}
	}
}
