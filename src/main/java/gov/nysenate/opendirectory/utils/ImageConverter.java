package gov.nysenate.opendirectory.utils;

import java.io.IOException;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.ProcessStarter;

/**
 * This class will not work unless ImageMagick is present on the system
 * 
 * www.imagemagick.org
 * 
 * 
 */
public class ImageConverter {
	
	public static void resizeImage(String oldPath, String newPath, String name, String type, int width, int height) throws IOException, InterruptedException, IM4JavaException {
		//set path to image magick tools
		ProcessStarter.setGlobalSearchPath("/usr/bin/");
		
		//convert williams.JPG -resize 165x213\> -size 165x213 xc:transparent +swap -gravity center -composite williams.png
		IMOperation frame = new IMOperation();
		frame.addImage(oldPath + name + type);
		//maintains image proportions by width
		frame.resize(width, height);
		//resizes canvas
		frame.size(width, height);
		//unfilled space (at top and bottom) made transparent
		frame.addRawArgs("xc:transparent");
		frame.p_swap();
		//centeres image on canvas
		frame.gravity("center");
		frame.composite();
		frame.addImage(newPath + name + ".png");
		
		ImageCommand resize = new ImageCommand();
		resize.setCommand("convert");
		resize.run(frame);
	}
	
	public static String writeProfileImages(String path, String name, String type) throws IOException, InterruptedException, IM4JavaException {
		resizeImage(path, path + "profile/", name, type, 165, 213);
		resizeImage(path, path + "thumb/", name, type, 55, 71);
		
		return name + ".png";
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, IM4JavaException {
		
	}
}
