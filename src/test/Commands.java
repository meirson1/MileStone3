package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);

		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		int sharedstate;
		public SharedState(){
			this.sharedstate=0;
		}

		public int getSharedstate() {
			return sharedstate;
		}

		public void setSharedstate() {
			this.sharedstate++;
		}
	}
	
	private  SharedState sharedState=new SharedState();

	
	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}
	
	// Command class for example:
	public class ExampleCommand extends Command{

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}		
	}
	
	// implement here all other commands

	public class UploadCsvFile extends Command{

		public UploadCsvFile() {
			super("Please upload your local train CSV file.\n");
			try {
				PrintWriter train = new PrintWriter(new FileWriter("trainFile.cvs"));//create train cvs file
				dio.readText();//read from A to done
				TimeSeries ts = new TimeSeries("input.txt");//send train cvs file to time series
				dio.write("Upload complete.\n" + "Please upload your local test CSV file.\n");
				//read again from A to done
				PrintWriter test = new PrintWriter(new FileWriter("tesFile.cvs"));//create test cvs file
				//sent it to TS
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}
	
}
