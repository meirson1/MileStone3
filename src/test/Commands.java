package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		//imp milestone 2
		TimeSeries ts;//send train cvs file to time series
		SimpleAnomalyDetector ad;

		private void ts(String csvFileName){
			ts=new TimeSeries(csvFileName);
		}

		private List<CorrelatedFeatures> ad(){
			ad=new SimpleAnomalyDetector();
			ad.learnNormal(ts);
			List<CorrelatedFeatures> cf=ad.getNormalModel();
			return cf;
		}
	}
	
	private  SharedState sharedState=new SharedState();//data member

	
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
	
	// implement here all others commands

	public class UploadCsvFile extends Command{

		public UploadCsvFile() {
			super("Please upload your local train CSV file.\n");
		}

		@Override
		public void execute() {
			dio.write(description);
			try {
				PrintWriter train = new PrintWriter(new FileWriter("trainFile.csv"));//create train cvs file
				String line;
				dio.readText();
				while (!((line=dio.readText()).equals("done"))) {
					train.println(line);//read from A to done
				}
				train.close();
				dio.write("Upload complete.\n" + "Please upload your local test CSV file.\n");
				PrintWriter test = new PrintWriter(new FileWriter("tesFile.csv"));//create test cvs file
				while (!((line=dio.readText()).equals("done"))) {
					test.println(line);//read from A to done
				}
				test.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public class AlgorithmSettings extends Command{

		public AlgorithmSettings() {
			super("The current correlation threshold is");
		}

		@Override
		public void execute() {
			sharedState.ts("trainFile.csv");
			List<CorrelatedFeatures> cf=sharedState.ad();
			for (CorrelatedFeatures c:cf) {
				//dio.write(c.);
			}
		}
	}

	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("anomaly detection complete.");
		}

		@Override
		public void execute() {

		}
	}

	public class DisplayResults extends Command{

		public DisplayResults() {
			super("display results");
		}

		@Override
		public void execute() {

		}
	}

	public class UploadAAResults extends Command{

		public UploadAAResults() {
			super("upload anomalies and analyze results");
		}

		@Override
		public void execute() {

		}
	}
	
}
