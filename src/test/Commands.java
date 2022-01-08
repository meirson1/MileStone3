package test;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
				dio.write("Upload complete.\n");
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
			super("Done.\n");
		}

		@Override
		public void execute() {
			for (AnomalyReport ar:sharedState.reports) {
				dio.write(ar.timeStep+" "+ar.description+"\n");
			}

			dio.write(description);
		}
	}

	public class UploadAAResults extends Command {

		public UploadAAResults() {
			super("Please upload your local anomalies file.\n");
		}

		private class Pair {

			int start;
			int end;

			public Pair(int start, int end) {
				this.start = start;
				this.end = end;
			}

			public int getStart() {
				return start;
			}

			public void setStart(int start) {
				this.start = start;
			}

			public int getEnd() {
				return end;
			}

			public void setEnd(int end) {
				this.end = end;
			}
		}

		@Override
		public void execute() {
			dio.write(description);
			List<List<String>> SE = new ArrayList<>();
			String line;

			dio.readText();
			while (!((line = dio.readText()).equals("done"))) {
				String[] values = line.split(",");
				SE.add(Arrays.asList(values));
			}

			float P = SE.size();
			float N = sharedState.tsTrain.getRowSize();
			float FP = 0;
			float TP = 0;
			List<Float> time = new ArrayList<>();

			for (int i = 0; i < SE.size(); i++) {
				for (int j = 0; j < SE.get(i).size() - 1; j++) {
					time.add(Float.parseFloat(SE.get(i).get(j + 1)) - Float.parseFloat(SE.get(i).get(j)) + 1);
				}
			}

			String des = sharedState.reports.get(0).description;
			List<Pair> Par = new ArrayList<>();
			Pair p = new Pair((int) sharedState.reports.get(0).timeStep, 0);

			for (int i = 0; i < sharedState.reports.size() - 1; i++) {//group by description of anomaly reports
				if ((des.equals(sharedState.reports.get(i).description)) && (!des.equals(sharedState.reports.get(i + 1).description))) {
					p.setEnd((int) sharedState.reports.get(i).timeStep);
					Par.add(p);
					p = new Pair((int) sharedState.reports.get(i + 1).timeStep, 0);
					if (!(sharedState.reports.get(i + 1).equals(null)))
						des = sharedState.reports.get(i + 1).description;
				} else if (i == sharedState.reports.size() - 2) {
					p.setEnd((int) sharedState.reports.get(i + 1).timeStep);
					Par.add(p);
				}
			}

			for (int i = 0; i <Par.size(); i++) {
				N-=(Par.get(i).getEnd()-Par.get(i).getStart()+1);
			}

			for (Pair pa : Par) {
				boolean flag = false;
				for (int i = 0; i < SE.size(); i++) {
					if (((pa.getStart() > (Float.parseFloat(SE.get(i).get(0)))) && (pa.getStart() > (Float.parseFloat(SE.get(i).get(1))))) && ((pa.getEnd() > (Float.parseFloat(SE.get(i).get(0)))) && (pa.getEnd() > (Float.parseFloat(SE.get(i).get(1))))))//calc TP FP
						flag = false;
					else {
						TP++;
						flag = true;
						break;
					}
				}
				if (!flag) {
					FP++;
				}
			}

			float TruePositiveRate = (TP / P);//TruePositiveRate
			float FalseAlarmRate = (FP / N);//FalseAlarmRate
			DecimalFormat df = new DecimalFormat("#0.0");
			df.setMaximumFractionDigits(3);
			df.setRoundingMode(RoundingMode.DOWN);

			dio.write("Upload complete.\n");
			dio.write("True Positive Rate: " + df.format(TruePositiveRate) + "\n");
			dio.write("False Positive Rate: " + df.format(FalseAlarmRate) + "\n");
		}
	}
}
