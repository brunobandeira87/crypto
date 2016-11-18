package watermarking;

import java.util.Random;

import attacks.Attacks;
import patchwork.Patchwork;
import spreadspectrum.SpreadSpectrum;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "watermarking/";
		String filename = "lena.jpg";
		double[][] image = PDI.lerImagem(path + filename);
		//PDI.salvaImagem("/home/bandeira/Documents/university/2016.2/criptografia/source/watermarking/src/watermarking/lena.jpg", PDI.retornaImagemCinza(PDI.lerImagemColorida(path)));
//		patchwork(image, path);
		spreadspectrum(image, path);
		

	}
	
	public static void spreadspectrum(double[][] image, String path){
		int windowSize = 1000;
		double alpha = 0.1;
		
		SpreadSpectrum spreadSpectrum = new SpreadSpectrum(image, alpha, windowSize);
		spreadSpectrum.run();
		PDI.salvaImagem(path + "lena_dct.jpg", spreadSpectrum.getResultImage());
	}
	
	public static void patchwork(double[][] image, String path){
		int delta = 4; 
		int size = 20000;
		
		Patchwork patchwork = new Patchwork(image, delta, size);
		patchwork.run(); 
		//int[][]subsets = PDI.patchwork(image, delta, size);
		PDI.salvaImagem(path + "lena_2.jpg", patchwork.getResultImage());
		double[][] attacked =  Attacks.shiftRow(patchwork.getResultImage());
		PDI.salvaImagem(path + "attacked.jpg" , attacked);
		
		Patchwork av = new Patchwork(attacked, delta, size);
		int[][] t = patchwork.getSubSets();
		av.read(t);
	}

}
