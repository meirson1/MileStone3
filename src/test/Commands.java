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
		TimeSeries tsTrain;//send train cvs file to time series
		TimeSeries tsTest;//send test cvs file to time series
		SimpleAnomalyDetector ad;
		List<AnomalyReport> reports;

		private void tsTrain(String csvFileName){
			tsTrain=new TimeSeries(csvFileName);
		}
		private void tsTest(String csvFileName){
			tsTest=new TimeSeries(csvFileName);
		}

		private void adThresh(){
			ad=new SimpleAnomalyDetector();
			ad.learnNormal(tsTrain);
		}

		private void ad(String csvFileName){
			tsTest(csvFileName);
			reports=ad.detect(tsTest);
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
				PrintWriter test = new PrintWriter(new FileWriter("testFile.csv"));//create test cvs file
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
			super("The current correlation threshold is ");
		}

		@Override
		public void execute() {
			sharedState.tsTrain("trainFile.csv");
			sharedState.adThresh();
			dio.write(description+sharedState.ad.geThreshold()+"\n");
			dio.write("Type a new threshold\n");
			float threshold=0;
			do {
				threshold=dio.readVal();
			}while (threshold<0||threshold>1);
			sharedState.ad.seThreshold(threshold);
		}
	}

	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("anomaly detection complete.\n");
		}

		@Override
		public void execute() {
			sharedState.ad("testFile.csv");
			dio.write(description);
		}
	}

	public class DisplayResults extends Command{

		public DisplayResults() {
			super("done");
		}

		@Override
		public void execute() {
			for (AnomalyReport ar:sharedState.reports) {
				dio.write(ar.description+ar.timeStep);
			}
			dio.write(description);

		}
	}

	public class UploadAAResults extends Command{

		public UploadAAResults() {
			super("upload anomalies and analyze results");
		}

		@Override
		public void execute() {
			dio.write(description);

		}
	}
	
}
