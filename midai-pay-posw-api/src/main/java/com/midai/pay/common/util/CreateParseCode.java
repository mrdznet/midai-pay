package com.midai.pay.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成
 * 
 * @author Feng Wong
 * 
 * @version 1.0
 *
 */
public class CreateParseCode {
	
   // 图片设置
	private static final int QR_IMAGE_WIDTH = 1200;	// 生成二维码图片宽度
	private static final int QR_IMAGE_HEIGHT = 1200;	// 生成二维码图片高度
	
    private static final int LOGO_IMAGE_WIDTH = 80;  //logo 图片宽度
    private static final int LOGO_IMAGE_HEIGHT = 80;  // logo 图片高度
    private static final int IMAGE_HALF_WIDTH = LOGO_IMAGE_WIDTH / 2;  
    private static final int FRAME_WIDTH = 2;  

    
	// 二维码写码器  
    private static MultiFormatWriter mutiWriter = new MultiFormatWriter();  
    
	/**
	 *  读取init.properties,获取图片的读写路径
	 */

	
	/**
	 * 二维码的生成(带logo)
	 * @param contents	二维码信息
	 * @param pictureUrl log图片路径
	 * @param desPath 写图片全路径
	 * @param readPath 生成二维码图片后的读图片全路径
	 * @return
	 */
	public static String createQRCodeWithLogo(String contents, String pictureUrl, String desPath, String readPath, String fileName){
		
		
		if(null == contents || contents.equals(""))
			return null;
		
		if(!desPath.isEmpty() && !readPath.isEmpty()){
			
			File filePath = new File(desPath);
			
			// 如没有文件夹，则创建
			if(!filePath.exists())
				filePath.mkdirs();
			
			String imgName = String.valueOf(new Date().getTime()) + ".png";	// 图片名字
			
			String allDesPath = desPath + "/" + imgName;	//
			String allReadPath = readPath + "/" + imgName;	//
			
			encode(contents, QR_IMAGE_WIDTH, QR_IMAGE_HEIGHT, pictureUrl, allDesPath);	//生成二维码
			return allReadPath;
		}
		
		return null;
	}
	
	public static ByteArrayOutputStream createQRCodeWithLogWithStream (String contents, String pictureUrl){
		
		if(null == contents || contents.equals(""))
			return null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encode(contents, QR_IMAGE_WIDTH, QR_IMAGE_HEIGHT, pictureUrl, out);	//生成二维码
		
		return out;
	}

	
	/**
	 * 简单二维码的生成
	 * @param contents 二维码内容
	 * @param desPath 生成二维码地址
	 * @return
	 */
	public static String createCode(String contents, String desPath) {
		
		String imgName = null;	// 图片名字
		
		int width = 500, height = 500;	// 二维码的图片大小
		String format = "png";	// 二维码的图片格式
	
		try {
			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用编码
			
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
			
			File desDir = new File(desPath+"/qrcode");
			
			if(!desDir.exists()){	//创建目录
				desDir.mkdirs();
			}
			
			imgName = String.valueOf(new Date().getTime()) + ".png";
			
			File desFile = new File(desPath+"/qrcode/"+imgName);

			MatrixToImageWriter.writeToFile(bitMatrix, format, desFile);

			return desPath + imgName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;

	}
	
	/**
	 * 二维码的解析
	 * 
	 * @param file
	 */
	public void parseCode(File file) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();

			if (!file.exists()) {
				return;
			}

			BufferedImage image = ImageIO.read(file);

			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("解析结果 = " + result.toString());
			System.out.println("二维码格式类型 = " + result.getBarcodeFormat());
			System.out.println("二维码文本内容 = " + result.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
  
    /**
     * 生成带logo图片的二维码
     * 
     * @param content 二维码内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param srcImagePath logo图片路径
     * @param destImagePath 生成的二维码存放路径
     * 
     */
    public static void encode(String content, int width, int height, String srcImagePath, String destImagePath) {  
        try { 
        	
            ImageIO.write(genBarcode(content, width, height, srcImagePath), "png", new File(destImagePath));
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (WriterException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /**
     * 生成带logo图片的二维码
     * 
     * @param content 二维码内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param out 输出流
     * @param destImagePath 生成的二维码存放路径
     * 
     */
    public static void encode(String content, int width, int height, String srcImagePath, OutputStream out) {  
        try { 
        	
            ImageIO.write(genBarcode(content, width, height, srcImagePath), "png", out);
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (WriterException e) {  
            e.printStackTrace();  
        }  
    }  
    
    private static BufferedImage genBarcode(String content, int width, int height, String srcImagePath) throws WriterException, IOException {  
        // 读取源图像 
    	BufferedImage scaleImage =  null;
    	int[][] srcPixels = new int[LOGO_IMAGE_WIDTH][LOGO_IMAGE_HEIGHT];  
    	if(StringUtils.isNotEmpty(srcImagePath)) {
    		
    		scaleImage = scale(srcImagePath, LOGO_IMAGE_WIDTH, LOGO_IMAGE_HEIGHT, false);  
    		for (int i = 0; i < scaleImage.getWidth(); i++) {  
    			for (int j = 0; j < scaleImage.getHeight(); j++) {  
    				srcPixels[i][j] = scaleImage.getRGB(i, j);  
    			}  
    		}  
    	}
  
//        Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>(); 
        Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
        
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        // 生成二维码  
        BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);  
        
        // 二维矩阵转为一维像素数组  
        int halfW = matrix.getWidth() / 2;  
        int halfH = matrix.getHeight() / 2;  
        int[] pixels = new int[width * height];  
  
        for (int y = 0; y < matrix.getHeight(); y++) {  
            for (int x = 0; x < matrix.getWidth(); x++) {  
                // 读取图片  
                if (x > halfW - IMAGE_HALF_WIDTH  
                        && x < halfW + IMAGE_HALF_WIDTH  
                        && y > halfH - IMAGE_HALF_WIDTH  
                        && y < halfH + IMAGE_HALF_WIDTH) {  
                    pixels[y * width + x] = srcPixels[x - halfW  
                            + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];  
                }   
                // 在图片四周形成边框  
                else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                        && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH  
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                - IMAGE_HALF_WIDTH + FRAME_WIDTH)  
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH  
                                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH  
                                && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH  
                                + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {  
                    pixels[y * width + x] = 0xfffffff;  
                } else {  
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；  
                    pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 : 0xfffffff;  
                }  
            }  
        }  
  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        image.getRaster().setDataElements(0, 0, width, height, pixels);  
  
        return image;  
    }  
  
    /** 
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标 
     *  
     * @param srcImageFile 
     *            源文件地址 
     * @param height 
     *            目标高度 
     * @param width 
     *            目标宽度 
     * @param hasFiller 
     *            比例不对时是否需要补白：true为补白; false为不补白; 
     * @throws IOException 
     */  
    private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller) throws IOException {  
        double ratio = 0.0; // 缩放比例  
        File file = new File(srcImageFile);  
        BufferedImage srcImage = ImageIO.read(file);  
        Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);  
        // 计算比例  
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {  
            if (srcImage.getHeight() > srcImage.getWidth()) {  
                ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();  
            } else {  
                ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();  
            }  
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);  
            destImage = op.filter(srcImage, null);  
        }  
        if (hasFiller) {// 补白  
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics2D graphic = image.createGraphics();  
            graphic.setColor(Color.white);  
            graphic.fillRect(0, 0, width, height);  
            if (width == destImage.getWidth(null))  
                graphic.drawImage(destImage, 0,  
                        (height - destImage.getHeight(null)) / 2,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null);  
            else  
                graphic.drawImage(destImage,  
                        (width - destImage.getWidth(null)) / 2, 0,  
                        destImage.getWidth(null), destImage.getHeight(null),  
                        Color.white, null);  
            graphic.dispose();  
            destImage = image;  
        }  
        return (BufferedImage) destImage;  
    } 
	
	// test
	public static void main(String[] args) throws Exception {
//		CreateParseCode cpCode = new CreateParseCode();
//		// 生成二维码
//		 cpCode.createCode("http://coolshell.cn/articles/10590.html");
		// 解析二维码
		// cpCode.parseCode(new File("D:/二维码生成/bbb.png"));
		
//		CreateParseCode.createQRCodeWithLogo("http://coolshell.cn/articles/10590.html");
		
		// 缩放 logo 图片
//		BufferedImage scaleImage = scale("D:\\var\\www\\files\\logo.png",LOGO_IMAGE_WIDTH, LOGO_IMAGE_HEIGHT, true);
//		ImageIO.write(scaleImage, "png", new File("C:\\Users\\Administrator\\Desktop\\qrLogo.png"));
		
		String logoURI = "C:\\Users\\Niko\\Desktop\\wrt.png";
		String desPath = "C:\\Users\\Niko\\Desktop\\agent" ;
		String readPath = "C:\\Users\\Niko\\Desktop\\agent";
		
		//文件名
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
		String fileName = df.format(new Date()) + "_" + UUID.randomUUID().toString() + "_phone.png";
		
		String url = CreateParseCode.createQRCodeWithLogo("http://www.midai88.com", logoURI, desPath, readPath, fileName);	//二维码图片地址
		System.out.println(url);
		
	}
}
