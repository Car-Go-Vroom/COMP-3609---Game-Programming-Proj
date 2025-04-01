package vandi;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ImageManager {

	public ImageManager() {

	}

	public static Image loadImage(String fileName) {
		return new ImageIcon(fileName).getImage();
	}

	public static BufferedImage loadBufferedImage(String filename) {
		BufferedImage bi = null;

		File file = new File(filename);
		try {
			bi = ImageIO.read(file);
		} catch (IOException ioe) {
			System.out.println("Error opening file " + filename + ":" + ioe);
		}
		return bi;
	}


	public static BufferedImage copyImage(BufferedImage src) {
		if (src == null)
			return null;

		int imWidth = src.getWidth();
		int imHeight = src.getHeight();

		BufferedImage copy = new BufferedImage(imWidth, imHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = copy.createGraphics();

		g2d.drawImage(src, 0, 0, null);
		g2d.dispose();

		return copy;
	}

	public static BufferedImage hFlipImage(BufferedImage src) {

		int imWidth = src.getWidth();
		int imHeight = src.getHeight();

		BufferedImage dest = new BufferedImage(imWidth, imHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dest.createGraphics();


		g2d.drawImage(src, imWidth, 0, 0, imHeight,
				0, 0, imWidth, imHeight, null);

		return dest;
	}

	public static BufferedImage vFlipImage(BufferedImage src) {

		int imWidth = src.getWidth();
		int imHeight = src.getHeight();

		BufferedImage dest = new BufferedImage(imWidth, imHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dest.createGraphics();


		g2d.drawImage(src, 0, imHeight, imWidth, 0,
				0, 0, imWidth, imHeight, null);

		return dest;
	}

}
