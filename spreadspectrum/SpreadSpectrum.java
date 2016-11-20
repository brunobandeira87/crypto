package spreadspectrum;
import java.util.Arrays;
import java.util.Random;
public class SpreadSpectrum {
	private double[][] hostImage;
	private double[][] frequencyImage;
	private double[][] resultImage;/*
	for (int i = 0; i < this.resultImage.length; i++) {
	for (int j = 0; j < this.resultImage[0].length; j++) {
		System.out.println(this.resultImage[i][j]);
	}
}
*/
	private double[] mark;
	private double[] coeficients;
	private double alpha;
	private int windowSize;
	
	public SpreadSpectrum(double[][] hostImage, double alpha, int windowSize){
		this.hostImage		=		hostImage;
		this.alpha			=		alpha;
		this.windowSize		=		windowSize;
		this.frequencyImage =		new double[this.hostImage.length][this.hostImage[0].length];
		this.resultImage	=		new double[this.hostImage.length][this.hostImage[0].length];
	}
	
	private void gaussianDistribution(){
		Random r = new Random();
		double temp; 
		this.mark =	new double[this.windowSize];
		for (int i = 0; i < this.mark.length; i++) {
			temp	= r.nextGaussian();/*
		for (int i = 0; i < this.resultImage.length; i++) {
			for (int j = 0; j < this.resultImage[0].length; j++) {
				System.out.println(this.resultImage[i][j]);
			}
		}
		*/
			this.mark[i] = (temp > 0) ? 1 : 0;
			
			//System.out.println(mark[i]);
			
		}
	}
	
	private static double[][] unvetorizeImage(double[] image, int height, int width){
		double[][] ret = new double[height][width];
		int currentIndex = 0;
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				ret[i][j] = image[currentIndex++];
//				currentIndex++;
			}
		}
		return ret;
	}
	
	
	private double[] linearImage(double[][] image){
		double[] ret = new double[image.length * image[0].length];
		int index = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[index++] = image[i][j];
			}
		}
		
		return ret;
	}
	
	
	
	private int[] getNthLargeElementsIndexes(double[] linearImage){
		double[] ret = new double[this.windowSize];
		int[] indexes = new int[this.windowSize];
		double[] temp = new double[linearImage.length];
		System.arraycopy( linearImage, 0, temp, 0, linearImage.length);
		double minValue = getMinValue(linearImage);
		int current = 0;
//		int index = 0; 
		for (int i = 0; i < ret.length; i++) {
			
			current = getMaxValueIndex(temp, minValue);
			temp[current] = minValue;
			indexes[i] = current;
		}
		
		return indexes;
	}
	
	
	private double getMinValue(double[] linearImage){
		//double[] temp = new double[linearImage.length];
		////System.arraycopy( linearImage, 0, temp, 0, linearImage.length);
		double ret = Double.MAX_VALUE;
		for (int i = 0; i < linearImage.length; i++) {
//			if()
			if(linearImage[i] < ret)
				ret = linearImage[i];
		}
		return ret;
	}
	
	private int getMaxValueIndex(double[] linearImage, double minValue){
		int ret = 0;
		double max = minValue;
		 for (int i = 0; i < linearImage.length; i++) {
			if(linearImage[i] > max){
				max = linearImage[i];
				ret = i;
			}
		}
		return ret;
	}
	private void dct(){
		double a;
		double b;
		int m = this.frequencyImage.length;
		double a1 = (double)( 1 / Math.sqrt(m));
		double a2 =  Math.sqrt((double)2/m);
		int n = this.frequencyImage[0].length;
		int total = m * n;
		double b1 = (double) 1 / Math.sqrt(n);
		double b2 = Math.sqrt((double)2/n);
		System.out.println("DCT BEGIN!");
		for (int i = 0; i < m; i++) {
			if(i % 10 == 0)
				System.out.println("current Progress: " + ((double) i * m / total ) * 100 +"%") ;
			for (int j = 0; j < n; j++) {
				a = (i == 0) ? a1 : a2;
				b = (j == 0) ? b1 : b2;
				double sum = 0;
				for (int k = 0; k < m; k++) {
					for (int l = 0; l < n; l++) {
						double parcela1 = Math.cos(Math.PI * ((double) (2 * k + 1) * i) / (2 * m));
						double parcela2 = Math.cos(Math.PI * ((double) (2 * l + 1) * j) / (2 * n));
						sum += (this.hostImage[k][l] * parcela1 * parcela2);
					}
					
				}
				this.frequencyImage[i][j] = sum * a * b;
//				double parcela1 = Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m);
//				double parcela2 = Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n);
//				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * parcela1 * parcela2);
//				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * ((Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m)) * (Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n))));
//				System.out.println(i + " " + j +" : " + this.frequencyImage[i][j]);
			}
			
		}
		System.out.println("DCT END!");
	}
	
	
	
	
	public void idct(){
		double a;
		double b;
		int m = this.frequencyImage.length;
		double a1 = (double) 1 / Math.sqrt(m);
		double a2 =  Math.sqrt((double)2/m);
		int n = this.frequencyImage[0].length;
		double b1 = (double) 1 / Math.sqrt(n);
		double b2 = Math.sqrt((double)2/n);
		int total = n * m;
		
		for (int i = 0; i < m; i++) {
			if(i % 10 == 0)
				System.out.println("current Progress: " + ((double) i * m / total ) * 100 +"%") ;
			for (int j = 0; j < n; j++) {
				
				double sum = 0;
				for (int k = 0; k < m; k++) {
					for (int l = 0; l < n; l++) {
						a = (k == 0) ? a1 : a2;
						b = (l == 0) ? b1 : b2;
						double parcela1 = Math.cos(Math.PI * ((double) (2 * i + 1) * k) / (2 * m));
						double parcela2 = Math.cos(Math.PI * ((double) (2 * j + 1) * l) / (2 * n));
						sum += (this.frequencyImage[k][l] * parcela1 * parcela2) * a * b;
					}
					
				}
				this.resultImage[i][j] = sum;
//				this.resultImage[i][j] = sum * a * b;
//				double parcela1 = Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m);
//				double parcela2 = Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n);
//				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * parcela1 * parcela2);
//				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * ((Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m)) * (Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n))));
				
			}
		}
		
		
//		for (int i = 0; i < this.hostImage.length; i++) {
//			for (int j = 0; j < this.hostImage[0].length; j++) {
//				a = (i == 0) ? a1 : a2;
//				b = (j == 0) ? b1 : b2;
//				
//				
//				double parcela1 = Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m);
//				double parcela2 = Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n);
//				this.resultImage[i][j] = a * b * (this.frequencyImage[i][j] * parcela1 * parcela2);
////				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * ((Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m)) * (Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n))));
////				System.out.println(this.resultImage[i][j]);
//			}
//		}
	}
	public double[][] getResultImage(){
		return this.resultImage;
	}
	public void run(){
		gaussianDistribution();
		System.out.println("DCT Begin");
		dct();
		System.out.println("DCT End");
		double[] linearImage = linearImage(this.hostImage);
		double[] linearFrequency = linearImage(this.frequencyImage);
		double[] markedFrequency = new double[this.windowSize];
		int[] largestIndexes = getNthLargeElementsIndexes(linearFrequency);
		double temp  = 0;
		for (int i = 0; i < largestIndexes.length; i++) {
			temp = linearFrequency[largestIndexes[i]] * (1 + this.alpha * this.mark[i]);
			System.out.println(temp);
			linearFrequency[largestIndexes[i]] = temp;
//			linearFrequency[largestIndexes[i]] *= (1 + this.alpha * this.mark[i]);
		}
		this.frequencyImage = unvetorizeImage(linearFrequency, this.frequencyImage.length, this.frequencyImage[0].length);
		System.out.println("IDCT Begins");
		idct();
		System.out.println("IDCT Ends");
		
	}

	
}
