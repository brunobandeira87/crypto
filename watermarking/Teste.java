package watermarking;

import java.util.Random;

import attacks.Attacks;
import patchwork.Patchwork;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/home/bandeira/Documents/university/2016.2/criptografia/source/watermarking/src/watermarking/";
		String filename = "lena.jpg";
		double[][] image = PDI.lerImagem(path + filename);
		//PDI.salvaImagem("/home/bandeira/Documents/university/2016.2/criptografia/source/watermarking/src/watermarking/lena.jpg", PDI.retornaImagemCinza(PDI.lerImagemColorida(path)));
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
