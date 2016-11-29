package leastsignifcantbit;

public class LSB {
	private double[][] image;
	private double[][] markedImage;
	
	
	
	public LSB(double[][] image){
		this.image = image;
		this.markedImage = new double[image.length][image[0].length];
		
	}
	
	public LSB(double[][] image, double[][] markedImage){
		this.image = image;
		this.markedImage = markedImage;
	}
	
	public void mark(String mark){
		String watermark = "";
//		int markSize = mark.length() * 8;
		String asciiBit = "";
		
		for (int i = 0; i < mark.length(); i++) {
			asciiBit += Integer.toString(mark.charAt(i),2);
		}
		
		System.out.println("ascii bit: ");
		System.out.println(asciiBit);
		
	}
	
}
