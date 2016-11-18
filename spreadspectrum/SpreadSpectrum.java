package spreadspectrum;
import java.util.Random;
public class SpreadSpectrum {
	private double[][] hostImage;
	private double[][] frequencyImage;
	private double[][] resultImage;
	private double[] mark;
	private double[] coeficients;
	private double alpha;
	private int windowSize;
	
	public SpreadSpectrum(double[][] hostImage, double alpha, int windowSize){
		this.hostImage		=		hostImage;
		this.alpha			=		alpha;
		this.windowSize		=		windowSize;
		this.frequencyImage =		new double[this.hostImage.length][this.hostImage[0].length];
	}
	
	private void gaussianDistribution(){
		Random r = new Random();
		double temp; 
		this.mark =	new double[this.windowSize];
		for (int i = 0; i < this.mark.length; i++) {
			temp	= r.nextGaussian();
			this.mark[i] = (temp > 0) ? 1 : 0;
			
			//System.out.println(mark[i]);
			
		}
	}
	
	private void dct(){
		double a;
		double b;
		int m = this.frequencyImage.length;
		double a1 = (double) 1 / Math.sqrt(m);
		double a2 =  Math.sqrt((double)2/m);
		int n = this.frequencyImage[0].length;
		double b1 = (double) 1 / Math.sqrt(n);
		double b2 = Math.sqrt((double)2/n);
		for (int i = 0; i < this.hostImage.length; i++) {
			for (int j = 0; j < this.hostImage[0].length; j++) {
				a = (i == 0) ? a1 : a2;
				b = (j == 0) ? b1 : b2;
				double parcela1 = Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m);
				double parcela2 = Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n);
				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * parcela1 * parcela2);
//				this.frequencyImage[i][j] = a * b * (this.hostImage[i][j] * ((Math.cos((Math.PI * (double) (2 * i + 1) * i) / 2 * m)) * (Math.cos((Math.PI * (double) (2 * j + 1) * j) / 2 * n))));
				System.out.println(this.frequencyImage[i][j]);
			}
		}
	}
	
	public void run(){
		gaussianDistribution();
		dct();
		
	}
	
}
