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
		commands.add(c.new AlgorithmSettings());
		commands.add(c.new DetectAnomalies());
		commands.add(c.new DisplayResults());
		commands.add(c.new UploadAAResults());
	}
	
	public void start() {
		boolean exit=false;
		int choice=0;
		while (!exit){
			dio.write("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
					"3. detect anomalies\n" +
					"4. display results\n" +
					"5. upload anomalies and analyze results\n" +
					"6. exit\n");
			do {
				choice=(int) dio.readVal();
			}while (choice>6||choice<1);

			switch (choice) {
				case 1:
					commands.get(0).execute();
					break;
				case 2:
					commands.get(1).execute();
					break;
				case 3:
					commands.get(2).execute();
					break;
				case 4:
					commands.get(3).execute();
					break;
				case 5:
					commands.get(4).execute();
					break;
				case 6:
					exit=true;
					break;
			}
		}
	}
}
