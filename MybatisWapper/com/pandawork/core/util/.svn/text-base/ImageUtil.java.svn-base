package com.pandawork.core.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static void getScaledImg(File F, int height, int width, String fmt) {
		try {
			double Ratio = 0.0; // 缩放比例
			if (!F.isFile()) {
				return;
			}
			BufferedImage Bi = ImageIO.read(F);
			int oheight = Bi.getHeight(), owidth = Bi.getWidth();
			if (height == -1) {
				if (width > 0 && owidth > width) {
					Ratio = Integer.valueOf(width).doubleValue() / owidth;
				} else {
					return;
				}
			} else if (width == -1) {
				if (height > 0 && oheight > height) {
					Ratio = Integer.valueOf(height).doubleValue() / oheight;
				} else {
					return;
				}
			} else if ((oheight > height) || (owidth > width)) {
				if (oheight > owidth) {
					Ratio = Integer.valueOf(height).doubleValue() / oheight;
				} else {
					Ratio = Integer.valueOf(width).doubleValue() / owidth;
				}
			} else {
				return;
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
			BufferedImage Itemp = op.filter(Bi, null);
			ImageIO.write(Itemp, fmt, F);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
