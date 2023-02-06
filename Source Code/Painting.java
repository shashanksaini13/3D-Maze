import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/*
 * Image Citations:
 * Apple: https://www.newgrounds.com/art/view/thexxxreaper/pixel-apple
 * Cherry: http://pixelartmaker.com/art/3546fa4607cc7a7
 * Dancing Banana: https://www.pixilart.com/art/dancing-banana-5c91295bb26c62d
 * Charmander: https://www.pixilart.com/art/charmander-pixel-art-dd047f6882301fa
 * Pikachu: http://pixelartmaker.com/art/b6d428941bb2adf
 * Mario: https://www.hiclipart.com/free-transparent-background-png-clipart-dxhui
 * Sonic: https://www.kindpng.com/imgv/TbJhwih_sonic-mania-resprite-sonic-the-hedgehog-2-pixel/
 */

public class Painting {
	private static ArrayList<BufferedImage> bufferedImages;
	public static void loadImages () {
		bufferedImages = new ArrayList<BufferedImage>();
		try {
			for (File file : new File("Paintings").listFiles()) {
				bufferedImages.add(ImageIO.read(file));
			}
		} catch (Exception e) {}
	}
	
	private ArrayList<Triangle> triangles;
	public ArrayList<Triangle> getTriangles () {
		return triangles;
	}
	
	public Painting () {
		triangles = new ArrayList<Triangle>();
	}
	
	public Painting specifyPainting (int direction, Vector offset, boolean random) {
		Vector aHat, bHat = new Vector(0, 0, 1), directionOffset;
		if (direction == 0) { //north
			aHat = new Vector(1, 0, 0);
			directionOffset = new Vector(30, 98, 30);
		} else if (direction == 2) { //south
			aHat = new Vector(-1, 0, 0);
			directionOffset = new Vector(70, 2, 30);
		} else if (direction == 3) { //west
			aHat = new Vector(0, 1, 0);
			directionOffset = new Vector(2, 30, 30);
		} else { //east
			aHat = new Vector(0, -1, 0);
			directionOffset = new Vector(98, 70, 30);
		}
		
//		ArrayList<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
//		try {
//			for (File file : new File("Paintings").listFiles()) {
//				bufferedImages.add(ImageIO.read(file));
//			}
//		} catch (Exception e) {}
		BufferedImage bufferedImage = null;
		if (random)
			bufferedImage = bufferedImages.get(new Random().nextInt(bufferedImages.size()));
		else
			try {
				bufferedImage = ImageIO.read(new File("OtherImages/ExitImage.png"));
			} catch (Exception e) {}
		int A = bufferedImage.getWidth(), B = bufferedImage.getHeight(), W = 40, H = 40;
		offset = offset.clone().plus(directionOffset);
		for (int i = 0; i < A; i++)
			for (int j = 0; j < B; j++) {
				  int colorValue =  bufferedImage.getRGB(i,B-1-j); 
				  int  red   = (colorValue & 0x00ff0000) >> 16;
				  int  green = (colorValue & 0x0000ff00) >> 8;
				  int  blue  =  colorValue & 0x000000ff;
				  Color color = new Color(red, green, blue);
				triangles.add(new Triangle(
						offset.clone().plus(aHat.clone().scale((double) W*i/A)).plus(bHat.clone().scale((double) H*j/B)), 
						offset.clone().plus(aHat.clone().scale((double) W*(i+1)/A)).plus(bHat.clone().scale((double) H*j/B)), 
						offset.clone().plus(aHat.clone().scale((double) W*i/A)).plus(bHat.clone().scale((double) H*(j+1)/B)),
						color));
				triangles.add(new Triangle(
						offset.clone().plus(aHat.clone().scale((double) W*(i+1)/A)).plus(bHat.clone().scale((double) H*(j+1)/B)), 
						offset.clone().plus(aHat.clone().scale((double) W*(i+1)/A)).plus(bHat.clone().scale((double) H*j/B)), 
						offset.clone().plus(aHat.clone().scale((double) W*i/A)).plus(bHat.clone().scale((double) H*(j+1)/B)),
						color));
			}
		
//		triangles.add(new Triangle(
//				offset.plus(new Vector(1,1,1)),
//				offset.plus(new Vector(1,99,1)),
//				offset.plus(new Vector(1,99,99)), Color.WHITE));
		
//		if (direction == 2) {
//			triangles.add(new Triangle(new Vector(40, 2, 40), new Vector(60, 2, 40), new Vector(40, 2, 60), Color.WHITE));
//			triangles.add(new Triangle(new Vector(60, 2, 60), new Vector(60, 2, 40), new Vector(40, 2, 60), Color.WHITE));
//		} else if (direction == 0) {
//			triangles.add(new Triangle(new Vector(40, 98, 40), new Vector(60, 98, 40), new Vector(40, 98, 60), Color.WHITE));
//			triangles.add(new Triangle(new Vector(60, 98, 60), new Vector(60, 98, 40), new Vector(40, 98, 60), Color.WHITE));
//		} else if (direction == 3) {
//			triangles.add(new Triangle(new Vector(2, 40, 40), new Vector(2, 60, 40), new Vector(2, 40, 60), Color.WHITE));
//			triangles.add(new Triangle(new Vector(2, 60, 60), new Vector(2, 60, 40), new Vector(2, 40, 60), Color.WHITE));
//		} else {
//			triangles.add(new Triangle(new Vector(98, 40, 40), new Vector(98, 60, 40), new Vector(98, 40, 60), Color.WHITE));
//			triangles.add(new Triangle(new Vector(98, 60, 60), new Vector(98, 60, 40), new Vector(98, 40, 60), Color.WHITE));
//		}
		
//		for (Triangle triangle : triangles)
//			triangle.offset(offset.clone());
		
		return this;
	}
}
