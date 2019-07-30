package com.enterprise.util.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/26 上午11:09
 */
public class ImageUtil {

    /**
     * 添加图片水印
     *
     * @param srcImg   目标图片路径，如：C:\\kutuku.jpg
     * @param waterImg 水印图片路径，如：C:\\kutuku.png
     * @param x        水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y        水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha    透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @throws IOException
     */
    public final static void addWaterMark(String srcImg, String waterImg, int x, int y, float alpha) throws IOException {
        try {
            // 加载目标图片
            File file = new File(srcImg);
            String ext = srcImg.substring(srcImg.lastIndexOf(".") + 1);
            Image image = null;
            image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            // 加载水印图片。
            Image waterImage = ImageIO.read(new File(waterImg));
            int width_1 = waterImage.getWidth(null);
            int height_1 = waterImage.getHeight(null);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 设置水印图片的位置。
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if (x < 0) {
                x = widthDiff / 2;
            } else if (x > widthDiff) {
                x = widthDiff;
            }
            if (y < 0) {
                y = heightDiff / 2;
            } else if (y > heightDiff) {
                y = heightDiff;
            }
            // 将水印图片“画”在原有的图片的制定位置。
            g.drawImage(waterImage, x, y, width_1, height_1, null);
            // 关闭画笔。
            g.dispose();
            // 保存目标图片。
            ImageIO.write(bufferedImage, ext, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "/home/www/tomcat_online/pic_dir_neixun/1543202298376logo.png";
//        String filePath = "https://neixun.forwe.store/static/neixun/1543202298376logo.png";
        Image waterImage = ImageIO.read(new File(filePath));
        int width_1 = waterImage.getWidth(null);
        int height_1 = waterImage.getHeight(null);

    }
}
