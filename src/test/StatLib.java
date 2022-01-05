package test;


import java.lang.Math;


public class StatLib {


    public static float avg(float[] x){
        float av =0;
        for(float i : x)
            av+=i;
        return av/x.length;
    }

    // returns the variance of X and Y
    public static float var(float[] x){
        float va , av;
        av = avg(x);
        float y[]=new float[x.length];
        for (int i=0;i<x.length;i++)
            y[i]=(float)Math.pow(x[i],2);
        va=avg(y)-(float)Math.pow(av,2);
        return va;
    }

    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y){
        float cv;
        float z[]=new float[x.length];
        for (int i=0;i<x.length;i++)
            z[i]=x[i]*y[i];
        cv=avg(z)-avg(x)*avg(y);
        return cv;
    }


    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y){
        float pcc;
        pcc=cov(x,y)/(float)(Math.sqrt(var(x))*Math.sqrt(var(y)));
        return pcc;
    }

    // performs a linear regression and returns the line equation
    public static Line linear_reg(Point[] points){

        int arr_len=points.length;
        float x_point[], y_point[];
        x_point=new float[arr_len];
        y_point=new float[arr_len];
        for (int i=0;i<points.length;i++)
        {
            x_point[i]=points[i].x;
            y_point[i]=points[i].y;
        }
        return linear_reg(x_point,y_point);
    }

    public static Line linear_reg(float[] x_point,float[] y_point){
        float a_temp,b_temp;
        a_temp=cov(x_point,y_point)/var(x_point);
        b_temp=avg(y_point)-a_temp*avg(x_point);
        Line line=new Line(a_temp,b_temp);
        return line;
    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p,Point[] points){
        Line line=linear_reg(points);
        return dev(p,line);
    }

    // returns the deviation between point p and the line
    public static float dev(Point p,Line l){
        return (float)(Math.sqrt(Math.pow((l.a*p.x+l.b)-p.y,2)));
        //Math.abs((l.a*p.x+l.b)-p.y);
    }   //threshold=l.a*p.x+l.b-p.y
        //threshold-l.b=l.a*p.x-p.y

    /*// simple average
    //@org.jetbrains.annotations.Contract(pure = true)
    public static float avg(float [] x) {
        float sum = 0;
        int i = 0;
        for (i = 0; i < x.length; i++) {
            sum += x[i];
        }
        sum /= i;
        return sum;
    }

    // returns the variance of X and Y
    public static float var(float[] x) {
        float u=avg(x);//By formula u=avg
        float[] x2=new float[x.length];//creating new list of x^2 to prevent lost of data original x.
        for (int i=0;i<x2.length;i++){
            x2[i]=x[i];
        }
        for (int i=0;i<x.length;i++) {//By the formula of x^2
            x2[i]= (float) Math.pow(x2[i],2);
        }
        float u2= (float) Math.pow(u,2);//by formula u^2
        float z=avg(x2)-u2;
        z*=100;
        int Z=(int)z;
        z=(float) Z;
        z/=100;
        return z;
    }

    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y) {
        float Uxy=var(x)*var(y);//By formula
        Uxy=(float) Math.sqrt(Uxy);//Needed to get root
        Uxy*=100;
        int uxy=(int)Uxy;
        Uxy=(float) uxy;
        Uxy/=100;
        return Uxy;
    }


    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y) {
        float sigmaxMy=(float) Math.sqrt(var(x))*(float) Math.sqrt(var(y));//sigma of x multiply by sigma of y
        float pear=(cov(x, y)) / (sigmaxMy);//By formula
        return  pear;
    }

    // performs a linear regression and returns the line equation
    public static Line linear_reg(Point [] points) {
        float[] x=new float[points.length];//list of x
        float[] y=new float[points.length];//list of y
        for (int i=0;i<x.length;i++){//Put the variable of the points in x and y
            x[i]=points[i].x;
            y[i]=points[i].y;
        }
        float a=cov(x,y)/var(x);//Find Incline
        float b=avg(y)-a*avg(x);//Find b
        return new Line(a,b);//return new line
    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p, Point[] points) {
        Line l=linear_reg(points);
        return dev(p,l);
    }

    // returns the deviation between point p and the line
    public static float dev(Point p, Line l) {
        float y= (float) ;
        return Math.abs((l.a*p.x+l.b)-p.y);
    }*/

}
