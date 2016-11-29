package patchwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Patchwork {

	private double[][] hostImage;
	private double[][] resultImage;
	private int[][] subsets;
	private int delta;
	private int windowSize;
	private double originalSum;
	private double markedSum; 
	
	public Patchwork(double[][] hostImage, int delta, int windowSize){
		this.hostImage 	= 	hostImage;
		this.delta 		= 	delta;
		this.windowSize	=	windowSize;
		this.subsets	= 	new int[2][this.windowSize];
		this.originalSum = 	0;
		this.markedSum 	= 	0;
		
		
	}
	
	
	public Patchwork(double[][] hostImage, int delta, int windowSize, int[][] subsets, double originalSum){
		this.hostImage 	= 	hostImage;
		this.delta 		= 	delta;
		this.windowSize	=	windowSize;
		this.subsets	= 	subsets;
		this.originalSum = 	originalSum;
		this.markedSum 	= 	0;
	}
	
	public double[][] getResultImage(){
		return this.resultImage;
	}
	
	public int[][] getSubSets(){
		return this.subsets;
	}
	public double getOriginalSum(){
		return this.originalSum;
	}
	
	public double getMarkedSum(){
		return this.markedSum;
	}
	private void writeSubSets(){
		
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter("patchowrk_subset_0.txt"));
			for (int i = 0; i < this.subsets[0].length; i++) {				
				output.write(this.subsets[0][i] + "\n");
			}
			output.close();			
			output = new BufferedWriter(new FileWriter("patchowrk_subset_1.txt"));
			for (int i = 0; i < this.subsets[1].length; i++) {
				
				output.write(this.subsets[1][i] + "\n");
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO 
	private void readSubset(int subset){
		try {
			BufferedReader input = new BufferedReader(new FileReader("patchwork_subset_" + subset + ".txt"));
			String currentLine;
			int i = 0;
			this.subsets[subset] = new int[this.windowSize];
			while ((currentLine = input.readLine()) != null) {
				this.subsets[subset][i++] = Integer.parseInt(currentLine);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void mark(){
		//double[][] ret = new double[image.length][image[0].length];
		int max = this.hostImage.length * this.hostImage[0].length;
		//int setSize = 4;
		boolean[] visitedPixels = new boolean[max];
		double[] singleDimensionImage = vetorizeImage(this.hostImage);
		
		int subsetA = 0;
		int subsetB = 1;
		this.subsets[subsetA] = createSubSet(max, visitedPixels, this.windowSize);
		this.subsets[subsetB]= createSubSet(max, visitedPixels, this.windowSize);
		writeSubSets();
		this.originalSum = sumSet(this.subsets[subsetA], this.subsets[subsetB], singleDimensionImage);
		//sumSet(subsetA, subsetB, singleDimensionImage);
		for (int i = 0; i < this.subsets[subsetA].length; i++) {
			
			singleDimensionImage[this.subsets[subsetA][i]] += delta;
			singleDimensionImage[this.subsets[subsetB][i]] -= delta;
		}
		
		this.markedSum = sumSet(this.subsets[subsetA], this.subsets[subsetB], singleDimensionImage);
		this.resultImage = unvetorizeImage(singleDimensionImage, this.hostImage.length, this.hostImage[0]. length);
		
//		System.out.println("result: " + (sum2 - sum1));
//		System.out.println("2 * d * n = " + (2* this.delta * this.windowSize));
	}
	
	private static double sumSet(int[] setA, int[] setB, double[] image){
		double ret = 0;
		
		for (int i = 0; i < setB.length; i++) {
			ret += (int) (image[setA[i]] - image[setB[i]]);
		}
		
//		System.out.println("this print: " + sum);
		return ret;
	}
	
	

	private static double[] vetorizeImage(double[][] image){
		double[] ret = new double[image.length * image[0].length];
		int index = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[index++] = (int)image[i][j];
				
			}
		}
		return ret;
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
	/**
	 * 
	 * Cria um conjuntos do Algoritmo 
	 * 
	 * @param pax - Total de pixels da imagem. Condiçao necessária para definir o range de posição
	 * @param pixels - Array booleano para marcar pixels já visitados. Ou sej aque pertence a um conjunto 
	 * @param setSize - Ttotal de elementos do conjunto
	 * @return
	 */
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
		double sumResult = 0;
		double[] oneDimensionResult = vetorizeImage(this.resultImage);
		for (int i = 0; i < subsets[0].length; i++) {
			sumResult += (oneDimensionResult[subsets[subsetA][i]] - oneDimensionResult[subsets[subsetB][i]]);
		}
		
		this.markedSum = sumResult;
		
		if(this.originalSum == 0){
			double sumOriginal = 0;
			double[] oneDimensionImage = vetorizeImage(this.hostImage);
			for (int i = 0; i < subsets[0].length; i++) {
				sumOriginal += (oneDimensionImage[subsets[subsetA][i]] - oneDimensionImage[subsets[subsetB][i]]);
			}
			
			this.originalSum = sumOriginal;
		}
	}
	
	private void robustness(double sum){
		double rob = sum / (2 * this.delta * this.windowSize);
		System.out.println("Robstuness : " + rob);
	}


	public void setResultImage(double[][] attackedMarked) {
		this.resultImage = attackedMarked;
		
	}
	
	
	
}
