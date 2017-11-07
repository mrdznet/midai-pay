package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Description: 文件相关工具类
 * @Copyright: Copyright (c)2014-2015 
 * @author: yue.yan
 * @version: $$Rev: 35 $$
 * @date: $$Date: 2014-07-25 16:12:49 +0800 (Fri, 25 Jul 2014) $$
 * @lastUpdate: $$Author$$
 * 
 * Modification History:
 * Date			Author			Version			Description
 * -------------------------------------------------------------
 *
 */
public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static final int BUFFER_SIZE = 1024;

	/**
     * <p>单个文件压缩成zip文件</p>
     * @param in 压缩文件输入流
     * @param out 压缩文件输出流
     * @param fileName 文件名
     * @return true:成功/false:失败
     */
    public static boolean zipFile(InputStream in, OutputStream out, String fileName) {
        try {
		    ZipOutputStream gzout = new ZipOutputStream(out);
		    ZipEntry entry = new ZipEntry(fileName);
		    gzout.putNextEntry( entry );
		    byte[] buf=new byte[BUFFER_SIZE];
		    int num; 
		 
		    while ((num=in.read(buf)) != -1) { 
		        gzout.write(buf,0,num);
		    } 
		    gzout.close();
		    in.close();
        }catch(IOException e) {
        	logger.error(e.getMessage());
		    return false;
        } 
        return true;
    }
}
