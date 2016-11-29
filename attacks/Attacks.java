package attacks;

public class Attacks {

	
	public static double[][] shiftRow(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		ret[0] = image[image.length - 1];		
		for (int i = 1; i < ret.length; i++) {
			ret[i] = image[i-1];
		}
		
		return ret;
	}
	
	public static double[][] saltAndPepper(double[][] image){
		double[][] ret = new double[image.length][image[0].length];
		
		for(int i = 0 ; i < image.length; i++){
			for(int j = 0; j < image[0].length; j++){
				double random = Math.random(); 
				if(random < 0.1){
					ret[i][j] = 0;
				}
				else if(random < 0.15){
					ret[i][j] = 255;
				}
				else{
					ret[i][j] = image[i][j];
				}
			}
		}
		
		
		return ret;
	}
	
	
	
	
	
	private static int[] getHistogram(double[][] image){
		int[] ret = new int[(int)(getHighestPixel(image) + 1)];
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[(int)image[i][j]]++; 			
			}
		}		
		
		return ret;
	}
	
	/**
	 * Equalização do Histograma.
	 * @param image
	 * @return double[][] imagem
	 */
	
	public static double[][] histogramEqualization(double[][] image){
		//System.out.println(variancia(image));
		int[] histogram = getHistogram(image);
//		double[] probabilidade = new double[histogram.length];

		int size = image.length * image[0].length;
		double[] probabilidade = probabilidade(histogram, size);
		
		
		double[][] ret = new double[image.length][image[0].length];
		
		// calculando a probabilidade para cada valor de pixel aparecer na imagem
		
		
		// calculando a probabilidade acumulativa
		int[] cumulativeProbabilidade = cumulativeDistribution(probabilidade, size);	
		
		// mapear os valores de níveis de cinza para os novos valores
		//printHistogram(cumulativeProbabilidade);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int pixelValue = (int) image[i][j];
				ret[i][j] = cumulativeProbabilidade[pixelValue];
			}
		}
		//System.out.println(getLowestPixel(image));
		//System.out.println(getHighestPixel(image));
		return ret;
	}
	
	/**
	 * Calcular a probabilidade de cada intensidade de cinza da imagem aparece na imagem
	 * @param histogram
	 * @param pixels
	 * @return
	 */
	private static double[] probabilidade(int[] histogram, int pixels){
		double[] ret = new double[histogram.length];		
		for (int i = 0; i < histogram.length; i++) {
			ret[i] = (double) histogram[i] / pixels;
		}
		
		return ret;
	}
	
	/*
	 * transformar as probabilidades, somando a atual com a anterior. depois multiplicar
	 * pela quantidade de tons de cinza e arrendondar esse valor.
	 */
	
	private static int[] cumulativeDistribution(double[] probabilidade, int pixels){
		int[] ret = new int[probabilidade.length];
		double cumulative = 0;
		for (int i = 0; i < probabilidade.length; i++) {
			cumulative += probabilidade[i] / pixels;
			ret[i] = (int) Math.round(cumulative * (probabilidade.length - 1 ));
		}
		
		
		return ret;
	}
	
	private static double getHighestPixel(double[][] image){
		double ret = Double.MIN_VALUE;		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(ret < image[i][j]){
					ret = image[i][j];
				}
			}			
		}
		
		return ret;
	}
	
	private static double getLowestPixel(double[][] image){
		double ret = Double.MAX_VALUE;
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(image[i][j] < ret){
					ret = image[i][j];
				}
			}
		}
		
		return ret;
	}
	
	
	
}
