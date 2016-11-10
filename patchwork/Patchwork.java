package patchwork;

import java.util.Random;

public class Patchwork {

	private double[][] hostImage;
	private double[][] resultImage;
	private int[][] subsets;
	private int delta;
	private int windowSize;
	
	public Patchwork(double[][] hostImage, int delta, int windowSize){
		this.hostImage 	= 	hostImage;
		this.delta 		= 	delta;
		this.windowSize	=	windowSize;
		this.subsets	= new int[2][this.windowSize];
	}
	
	public double[][] getResultImage(){
		return this.resultImage;
	}
	
	public int[][] getSubSets(){
		return this.subsets;
	}
	
	public void run(){
		//double[][] ret = new double[image.length][image[0].length];
		int max = this.hostImage.length * this.hostImage[0].length;
		//int setSize = 4;
		boolean[] visitedPixels = new boolean[max];
		double[] singleDimensionImage = vetorizeImage(this.hostImage);
		
		int subsetA = 0;
		int subsetB = 1;
		this.subsets[subsetA] = createSubSet(max, visitedPixels, this.windowSize);
		this.subsets[subsetB]= createSubSet(max, visitedPixels, this.windowSize);
		
		double sum1 = sumSet(this.subsets[subsetA], this.subsets[subsetB], singleDimensionImage);
		//sumSet(subsetA, subsetB, singleDimensionImage);
		for (int i = 0; i < this.subsets[subsetA].length; i++) {
			singleDimensionImage[this.subsets[subsetA][i]] += delta;
			singleDimensionImage[this.subsets[subsetB][i]] -= delta;
		}
		
		double sum2 = sumSet(this.subsets[subsetA], this.subsets[subsetB], singleDimensionImage);
		this.resultImage = unvetorizeImage(singleDimensionImage, this.hostImage.length, this.hostImage[0]. length);
		
		System.out.println("result: " + (sum2 - sum1));
		System.out.println("2 * d * n = " + (2* this.delta * this.windowSize));
	}
	
	private static double sumSet(int[] setA, int[] setB, double[] image){
		double ret = 0;
		
		for (int i = 0; i < setB.length; i++) {
			ret += image[setA[i]] - image[setB[i]];
		}
		
//		System.out.println("this print: " + sum);
		return ret;
	}
	
	

	private static double[] vetorizeImage(double[][] image){
		double[] ret = new double[image.length * image[0].length];
		int index = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[index++] = image[i][j];
				
			}
		}
		return ret;
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
	
	private static int[] createSubSet(int max, boolean[] pixels, int setSize){
		Random r = new Random();
		int currentRandomValue = 0;
		int count = 0;
		int[] ret = new int[setSize];
		while(count < setSize){
			currentRandomValue = r.nextInt(max);
			if(!pixels[currentRandomValue]){
				pixels[currentRandomValue] = true;
				ret[count++] = currentRandomValue;

			}
		}
		
		return ret;
		
	}
	
	public void read(int[][] subsets){
		
		int subsetA = 0;
		int subsetB = 1; 
		double sum = 0;
		double[] oneDimesionImage = vetorizeImage(this.hostImage);
		for (int i = 0; i < subsets[0].length; i++) {
			sum += (oneDimesionImage[subsets[subsetA][i]] - oneDimesionImage[subsets[subsetB][i]]);
		}
		robustness(sum);
	}
	
	private void robustness(double sum){
		double rob = sum / (2 * this.delta * this.windowSize);
		System.out.println("Robstuness : " + rob);
	}
	
	
	
}
