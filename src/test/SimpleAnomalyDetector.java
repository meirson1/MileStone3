package test;
import java.util.ArrayList;
import java.util.List;
public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {

	List<CorrelatedFeatures> CF=new ArrayList<CorrelatedFeatures>();
	float threshold=0.9f;

	public void seThreshold(float thresh){
		this.threshold=thresh;
	}
	public float geThreshold(){
		return this.threshold;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		float SendPearsonCorrelated;
		CorrelatedFeatures c=null;
		for (int i = 1; i <= ts.HM.size(); i++) {
			SendPearsonCorrelated=geThreshold();
			for (int j = i + 1; j <= ts.HM.size(); j++) {
				float[] f1=new float[ts.HM.get(String.valueOf((char) (i+64))).size()];
				float[] f2=new float[ts.HM.get(String.valueOf((char) (i+64))).size()];
				for (int s=0;s<f1.length;s++) {
					f1[s]=(float)(ts.HM.get(String.valueOf((char) (i+64))).get(s));
					f2[s]=(float)(ts.HM.get(String.valueOf((char) (j+64))).get(s));
				}

				if (Math.abs(StatLib.pearson(f1,f2)) >SendPearsonCorrelated) {
					SendPearsonCorrelated = Math.abs(StatLib.pearson(f1,f2));
					Line line=StatLib.linear_reg(f1,f2);
					float MaxThreshold=this.CheckMaxThreshold(line,f1,f2)*(float)1.1;//10%
					c=new CorrelatedFeatures(String.valueOf((char)(i+64)),String.valueOf((char)(j+64)),SendPearsonCorrelated,line,MaxThreshold);
				}

			}
			if (this.CF==null)
				this.CF=new ArrayList<CorrelatedFeatures>();
			if (SendPearsonCorrelated>0.9f)
				CF.add(c);
		}
	}

	private float CheckMaxThreshold(Line line, float[] f1, float[] f2) {
		float MaxThreshold=0;
		for (int k = 0; k < 100; k++) {
			Point point = new Point(f1[k], f2[k]);
			float Threshold = StatLib.dev(point, line);//check max threshold
			if (MaxThreshold < Threshold)
				MaxThreshold = Threshold;
		}
		return MaxThreshold;
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> ListAn=new ArrayList<AnomalyReport>();
		for (CorrelatedFeatures c:this.CF){
			float[] f1=new float[ts.HM.get(c.feature1).size()];
			float[] f2=new float[ts.HM.get(c.feature2).size()];
			for (int i=0;i<f1.length;i++){
				f1[i]=ts.HM.get(c.feature1).get(i);
				f2[i]=ts.HM.get(c.feature2).get(i);
			}
			Line line=StatLib.linear_reg(f1,f2);
			float threshold=this.CheckMaxThreshold(line,f1,f2);
			if (threshold>c.threshold) {
				long timeStep=reverseThreshold(threshold,line,f1,f2);
				AnomalyReport an = new AnomalyReport(c.feature1+"-"+c.feature2, timeStep);
				ListAn.add(an);
			}
		}
		return ListAn;
	}

	private long reverseThreshold(float threshold, Line line, float[] x_floats, float[] y_floats) {

		int i=0;
		while (i!=x_floats.length){
			Point p=new Point(x_floats[i],y_floats[i]);
			if (StatLib.dev(p,line)==threshold)
				return i+1;
			i++;
		}
		return i;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return CF;
	}
}
