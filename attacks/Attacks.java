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
}
