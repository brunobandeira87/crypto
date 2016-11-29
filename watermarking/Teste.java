package watermarking;

import java.util.Random;

import attacks.Attacks;
import leastsignifcantbit.LSB;
import patchwork.Patchwork;
import spreadspectrum.SpreadSpectrum;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "watermarking/";
		String filename = "lena128";
		String ext = ".bmp";
		
		
		int delta = 4; 
		int windowSize = 2000;
		PatchworkAnalyzes(path, filename, ext, windowSize, delta);
		
		//LSB n = new LSB(null, null);
		//n.mark("POLI");
		
		
		
		
		
		
		
		
		
		
		
		
		
//		int windowSize = 1000;
//		double alpha = 0.1;
//		double[][] image = PDI.lerImagem(path + filename);
//		SpreadSpectrum img = spreadspectrumMark(image, path);
//		///spreadspectrumRead(path, alpha, windowSize);
//		SpreadSpectrum obj = new SpreadSpectrum(PDI.lerImagem(path + "lena128_dct.bmp"), alpha, windowSize);
//		obj.read(img.getCoeficients());
//		System.out.println("Similaridade: " + similaridade(img, obj));
//		double cor = correlation(img, obj);
//		
//		System.out.println("Correlation: " + cor);
		
//		RIDRICH; GOLJAN; BALDOZA ,
//		2000; WONG , 1998; YEUNG; MINTZER , 1997)
		
	}
	
	
	public static void PatchworkAnalyzes(String path, String filename, String ext, int windowSize, int delta){
		double[][] original = PDI.lerImagem(path + filename + ext);
		String filetype = "";
		Patchwork pw = new Patchwork(original, delta, windowSize);
		pw.mark();
		
		// IMAGEM MARCADA SEM RUIDO
		double[][] marked = pw.getResultImage();
		PDI.salvaImagem(path + filename + "_pw_marked"+ ext, marked, filetype);
		

		
		System.out.println(pw.getMarkedSum() - pw.getOriginalSum());

		
		
		// ATAQUE DE SHIFTROW E ANALISE DA IMAGEM
		
		double[][] attackedMarked = Attacks.shiftRow(marked);

		Patchwork read = new Patchwork(marked, delta, windowSize, pw.getSubSets(), pw.getMarkedSum());
		
		read.setResultImage(attackedMarked);
		read.read(pw.getSubSets());
		


		System.out.println("Shift Row Attack: ");
		System.out.println(read.getMarkedSum() - read.getOriginalSum());
		
		//ATAQUE DE SALT AND PEPPER 
		double[][] spMarked = Attacks.saltAndPepper(marked);
		
		Patchwork pp = new Patchwork(marked, delta, windowSize, pw.getSubSets(), pw.getMarkedSum());
		
		pp.setResultImage(spMarked);		
		pp.read(pw.getSubSets());
		
		System.out.println("Salt & Pepper Attack: ");
//		System.out.println(pp.getMarkedSum());// - pp.getOriginalSum());
//		System.out.println(pp.getOriginalSum());
		System.out.println(pp.getMarkedSum() - pp.getOriginalSum());
		
		
		//EQUALIZAÇÃO DE HISTOGRAMA
		
		double[][] eqMarked = Attacks.histogramEqualization(marked);
		
		Patchwork eq = new Patchwork(marked, delta, windowSize, pw.getSubSets(), pw.getMarkedSum());
		
		eq.setResultImage(eqMarked);
		eq.read(pw.getSubSets());
		
		System.out.println("Histogram Attack: ");
		System.out.println(eq.getMarkedSum() - eq.getOriginalSum());
		
		
		
				
		
		
		// 
		
		//Patchwork pwm = new Patchwork(marked, delta, windowSize, pw.getSubSets(), pw.getMarkedSum());
		
		
		
		
		
		
		
	}
	// TODO 
	
	public static void spreadSpectrumAnalyze(String path, String filename, String ext, int windowSize, double alpha){
		double[][] original = PDI.lerImagem(path + filename + ext);
		String filetype = "";
		SpreadSpectrum spec = new SpreadSpectrum(original, alpha, windowSize);
	}
	
	public static SpreadSpectrum spreadspectrumMark(double[][] image, String path){
		int windowSize = 1000;
		double alpha = 0.1;
		String filetype = "";
		SpreadSpectrum ret = new SpreadSpectrum(image, alpha, windowSize);
		double[] mark = null;
		ret.mark(mark);
		PDI.salvaImagem(path + "lena128_dct.bmp", ret.getResultImage(), filetype);
		return ret;
		
	}
	
	public static void spreadspectrumRead(String path, double alpha, int windowSize){
		
		String imgFilename = "lena128.bmp";		
		String objFilename = "lena128_dct.bmp";
		
		SpreadSpectrum image = new SpreadSpectrum(PDI.lerImagem(path + imgFilename), alpha, windowSize);
		SpreadSpectrum obj = new SpreadSpectrum(PDI.lerImagem(path + objFilename), alpha, windowSize);
		
		double sim = similaridade(image, obj);
		
		System.out.println("Similaridade : " + sim);
		
		
	}
	/*
	public static void patchwork(double[][] image, String path){
		int delta = 4; 
		int size = 20000;
		String filetype = "";
		Patchwork patchwork = new Patchwork(image, delta, size);
		patchwork.mark(); 
		PDI.salvaImagem(path + "lena_2.jpg", patchwork.getResultImage(), filetype);
		double[][] attacked =  Attacks.shiftRow(patchwork.getResultImage());
		PDI.salvaImagem(path + "attacked.jpg" , attacked, filetype);		
		Patchwork av = new Patchwork(attacked, delta, size);
		int[][] t = patchwork.getSubSets();
		av.read(t);
	}
	*/
	public static double similaridade(SpreadSpectrum image, SpreadSpectrum obj){
		double ret = 0;
		double imageWaterMarkSum = image.getMarkSum();
		double objWaterMarkSum = obj.getMarkSum();
		
		ret = (imageWaterMarkSum * objWaterMarkSum) / Math.sqrt(imageWaterMarkSum * objWaterMarkSum);
		return ret;
	}
	
	//TODO
	public static double correlation(SpreadSpectrum image, SpreadSpectrum obj){
		double ret = 0;
		
		double[][] img = image.getResultImage();
		double[][] objImg = obj.getResultImage();
		
		double imgMean = image.getMean();
		double objMean = obj.getMean();
		
		for (int i = 0; i < objImg.length; i++) {
			for (int j = 0; j < objImg[0].length; j++) {
				double imgDesv = (img[i][j] - imgMean);
				double objDesv = (objImg[i][j] - objMean);
				double num = imgDesv * objDesv;
				double den = Math.sqrt(Math.pow(imgDesv, 2) * Math.pow(objDesv, 2));
				double corr = (double) num / den;
				if(!Double.isNaN(corr));
					ret += corr;
			}
		}
		
		return ret;
		
	}
}
