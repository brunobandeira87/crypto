package spreadspectrum;


import java.util.Random;
public class SpreadSpectrum {
	private double[][] hostImage;
	private double[][] frequencyImage;
	private double[][] resultImage;	
	private double[][] frequencyResultImage;
	private double[] mark;
	private double markSum;
	private double[] coeficients;
	private double mean;
	private double alpha;
	private int windowSize;
	
	public SpreadSpectrum(double[][] hostImage, double alpha, int windowSize){
		this.hostImage				=		hostImage;
		this.alpha					=		alpha;
		this.windowSize				=		windowSize;
		this.resultImage			=		hostImage;
		this.frequencyImage 		=		new double[this.hostImage.length][this.hostImage[0].length];
		this.frequencyResultImage	=		new double[this.hostImage.length][this.hostImage[0].length];
		this.mark 					=		new double[this.windowSize];
		this.coeficients			=		new double[this.windowSize];
		this.markSum				=		0;
		this.mean 					=		0;
		
	}
	
	public SpreadSpectrum(double[][] hostImage, double alpha, int windowSize, double[] coeficients){
		this.hostImage				=		hostImage;
		this.alpha					=		alpha;
		this.windowSize				=		windowSize;
		this.frequencyImage 		=		new double[this.hostImage.length][this.hostImage[0].length];
		this.resultImage			=		new double[this.hostImage.length][this.hostImage[0].length];
		this.coeficients			=		coeficients;
		this.mark 					=		new double[this.windowSize];
		this.markSum				=		0;
		this.mean 					=		0;
		
	}
	
	private void gaussianDistribution(){
		Random r = new Random();
		double temp; 
		//this.mark =	new double[this.windowSize];
		for (int i = 0; i < this.mark.length; i++) {
			temp = r.nextGaussian();
			this.mark[i] = (temp > 0) ? 1 : 0;
			
		}
	}
	
	public double getMean(){
		if(this.mean == 0)
			setMean();
		return this.mean;
	}
	
	private void setMean(){
		double mean = 0;
		int width = this.resultImage.length;
		int height = this.resultImage[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mean += this.resultImage[i][j];
			}
		}
		this.mean = (double) (mean / (width * height));
	}
	
	public double getMarkSum(){
		if (this.markSum == 0)
			setMarkSum();
		return this.markSum;
		
	}
	
	private void setMarkSum(){
		double sum = 0;
		for (int i = 0; i < this.mark.length; i++) {
			if(!Double.isNaN(this.mark[i]))
				sum += this.mark[i];
		}
		this.markSum = sum;
	}
	
	private static double[][] unvetorizeImage(double[] image, int height, int width){
		double[][] ret = new double[height][width];
		int currentIndex = 0;
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				ret[i][j] = (int)image[currentIndex++];
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
				ret[index++] = (int)image[i][j];
			}
		}
		
		return ret;
	}
	
	
	public double[] getCoeficients(){
//		if(this.coeficients == null)
//			getNthLargeElementsIndexes(linearImage(this.frequencyImage)
		return this.coeficients;
	}
	
	
	private int[] getNthLargeElementsIndexes(double[] linearImage){
		double[] ret = new double[this.windowSize];
		int[] indexes = new int[this.windowSize];
		double[] temp = new double[linearImage.length];
		System.arraycopy( linearImage, 0, temp, 0, linearImage.length);
		double minValue = getMinValue(linearImage);
		int current = 0;
 
		for (int i = 0; i < ret.length; i++) {
			
			current = getMaxValueIndex(temp, minValue);
			//armazena os maiores coeficients em ordem decrescente
			this.coeficients[i] = current;
			temp[current] = minValue;
			indexes[i] = current;
		}
		
		return indexes;
	}
	
	
	private double getMinValue(double[] linearImage){

		double ret = Double.MAX_VALUE;
		for (int i = 0; i < linearImage.length; i++) {
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
		//largura da imagem
		int m = this.frequencyImage.length;
		//altura da imagem
		int n = this.frequencyImage[0].length;

		//total de pixels
		int total = m * n;
		
		//parametros da dct
		double a1 = (double)( 1 / Math.sqrt(m));
		double a2 =  Math.sqrt((double)2/m);
		
		double b1 = (double) 1 / Math.sqrt(n);
		double b2 = Math.sqrt((double)2/n);
		
		for (int i = 0; i < m; i++) {
			//apenas para imprimir progresso na tela
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
				
				this.frequencyImage[i][j] = (int)(sum * a * b);
			}
			
		}
		
	}
	
	
	
	
	public void idct(){
		
		this.resultImage			=		new double[this.hostImage.length][this.hostImage[0].length];
		int m = this.frequencyImage.length;
		int n = this.frequencyImage[0].length;
		int total = n * m;

		double a1 = (double) 1 / Math.sqrt(m);
		double a2 =  Math.sqrt((double)2/m);
		
		double b1 = (double) 1 / Math.sqrt(n);
		double b2 = Math.sqrt((double)2/n);
		
		for (int i = 0; i < m; i++) {
			if(i % 10 == 0)
				System.out.println("current Progress: " + ((double) i * m / total ) * 100 +"%") ;
			for (int j = 0; j < n; j++) {
				
				double sum = 0;
				for (int k = 0; k < m; k++) {
					for (int l = 0; l < n; l++) {
						double a = (k == 0) ? a1 : a2;
						double b = (l == 0) ? b1 : b2;
						double parcela1 = Math.cos(Math.PI * ((double) (2 * i + 1) * k) / (2 * m));
						double parcela2 = Math.cos(Math.PI * ((double) (2 * j + 1) * l) / (2 * n));
						sum += (this.frequencyImage[k][l] * parcela1 * parcela2) * a * b;
					}
					
				}
				this.resultImage[i][j] = (int)sum;

				
			}
		}
		


	
	}
	
	public double[][] getHostImage(){
		return this.hostImage;
	}
	
	public double[][] getResultImage(){
		return this.resultImage;
	}
	public void mark(double[] mark){
		this.coeficients = new double[this.windowSize];
		if(mark != null)
			this.mark = mark;
		gaussianDistribution();
		System.out.println("DCT Begin");
		dct();
		System.out.println("DCT End");

		double[] linearFrequency = linearImage(this.frequencyImage);
		int[] largestIndexes = getNthLargeElementsIndexes(linearFrequency);
		double temp  = 0;
		for (int i = 0; i < largestIndexes.length; i++) {
			temp = linearFrequency[largestIndexes[i]] * (1 + this.alpha * this.mark[i]);

			linearFrequency[largestIndexes[i]] = temp;
//			linearFrequency[largestIndexes[i]] *= (1 + this.alpha * this.mark[i]);
		}
		this.frequencyImage = unvetorizeImage(linearFrequency, this.frequencyImage.length, this.frequencyImage[0].length);
		System.out.println("IDCT Begins");
		idct();
		System.out.println("IDCT Ends");
		
	}
	//TODO
	public void read(double[] originalCoeficients){
		dct();
		double[] linearFrequency = linearImage(this.frequencyImage);
		int[] largestIndexes = getNthLargeElementsIndexes(linearFrequency);
		double[] coeficients = new double[this.windowSize];
		
		double[] supposedMark = new double[this.windowSize];
		
		for (int i = 0; i < supposedMark.length; i++) {
			supposedMark[i] = (double) (this.coeficients[i] - originalCoeficients[i]) / (this.alpha * originalCoeficients[i]);
		}
		this.mark = supposedMark;
		
	}

	
}
