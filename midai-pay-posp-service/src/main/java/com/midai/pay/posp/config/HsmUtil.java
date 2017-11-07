package com.midai.pay.posp.config; 

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 辅助函数库
 * 
 * @author 余涛
 * 2011/2/23<br>
 * @version 1.00
 * @see 
 * 
 */
public class HsmUtil {

    /**
     * 将字节转化成8ge二进制位并用char数组存储起来，从左到右分别是高bit到低bit位，bit7-bit0
     * @param b
     * @return
     */
    public static char[] byteTo8char(byte b){
        char[] result = new char[8];
        String temp = null;
        if(b < 0){
            temp = Integer.toBinaryString(b).substring(24);
            for(int i=0;i<8;i++){
                result[i] = temp.charAt(i);
            }
        }else{
            temp = Integer.toBinaryString(b);
            int tempIndex = temp.length();
            for(int i=0;i<8-tempIndex;i++){
                result[i] = '0';
            }
            for(int i=0;i<tempIndex;i++){
                result[8-tempIndex+i] = temp.charAt(i);
            }
        }
        return result;
    }
	
	/**
	 * 把8位二进制字符串转换成整形
	 * @param str8位二进制字符串
	 * @return 整形
	 * @throws RuntimeException
	 */
	public static int binaryStr2Byte(String str) throws RuntimeException{
		
		if(str.length() != 8){
			throw new RuntimeException("格式不对，请传入8个二进制bit位");
		}
		int result = 0;
		char[] bitChars = str.toCharArray();
		byte[] bits = new byte[8];
		for(int i=0;i<str.length();i++){
			if(bitChars[i] == '1'){
				bits[i] = 1;
			}else{
				bits[i] = 0;
			}
		}
		result = bits[0]*128+bits[1]*64+
				bits[2]*32+bits[3]*16+
				bits[4]*8+bits[5]*4+
				bits[6]*2+bits[7];
		return result;
	}
	
	/**
	 * 获取当前系统日期yyyyMMdd
	 * @return
	 */
	public static String getCurrentDate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}
	/**
	 * 获取当前系统日期YYMMDD
	 * @return
	 */
	public static String getCurrentDateYY(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		return format.format(date);
	}
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static String getCurrentTime(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(date);
	}
	
	/**
	 * 将一个字节转化成长度为8的二进制字符串
	 * @param b
	 * @return
	 */
	public static String byteToBinaryStr(byte b){
		String sb = new String();
		sb = Integer.toBinaryString(b);
		int length = sb.length();
		switch(length){
		case 1:
			sb = "0000000"+sb;
			break;
		case 2:
			sb = "000000"+sb;
			break;
		case 3:
			sb = "00000"+sb;
			break;
		case 4:
			sb = "0000"+sb;
			break;
		case 5:
			sb = "000"+sb;
			break;
		case 6:
			sb = "00"+sb;
			break;
		case 7:
			sb = "0"+sb;
			break;
		default:
			break;
		}
		return sb.substring(sb.length()-8);
	}
	/*返回b的无符号正数*/
	public static  int UnB(byte b){
		int c=b;
		if(c<0) return 256+c;
		else
			return c;
	}
	
	/*计算校验和*/
	public static byte CalCheckSum(byte[] pak,int len){
		int i;
		int res=0;
		for(i=0;i<len;i++)
			res=res+HsmUtil.UnB(pak[i]);
		byte r=(byte)(res%256);
		r=(byte)(~r);
		r=(byte)(r+(byte)1);
		return r;
	}
	/*将input后补齐8字节整数倍，然后逐个8字节异或后返回*/
	public static byte[] MakeMac8(byte[] input){
		int blockcnt=0;
		if ((input.length)%8 !=0){
			blockcnt=(input.length)/8+1;
		}else
			blockcnt=(input.length)/8;
		byte[] input2=new byte[blockcnt*8];
		
		System.arraycopy(input,0,input2,0,input.length);
//		YTLog.SysLog("mac str:",input2,0,input2.length);
		int i,j;
		//需要做mac的数据
		byte[] t=new byte[8];
		for(i=0;i<blockcnt;i++){
			for(j=0;j<8;j++){
				t[j]=(byte)(t[j]^input2[i*8+j]);
			}
		}
		return t;
	}
	//在s字符串右边补空格，使其长度为len
	public static String FormatStr(String s,int len){
		String kongge="                                       ";
		String res="";
		if(s.length()<len){
			res=s+kongge.substring(0,len-s.length());
			return res;
		}else
			return s;
	}
	/**
	 * 将字符型转换为指定格式日期型
	 * 
	 * @param _date
	 *            需要转换成日期的字符串
	 * @param format
	 *            与需要转换成日期的字符串相匹配的格式
	 * @return
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat df=new SimpleDateFormat(format);
		return df.format(date);
	}
	public static Date stringToDate(String _date, String format) {
		if (null == format || "".equals(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(_date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	// 将烟草订单的金额888.8/888.88/888规整为8888.88标准显示格式化
	public static String FormatYCMoney(String m) {
		try {
			float a = Float.parseFloat(m.trim());
			return String.format("%.2f", a);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 去掉ｍ左边的0字符
	public static String TrimLeftZero(String m) {
		int a = Integer.parseInt(m);
		return Integer.toString(a);

		// if(m.equals("0")) return "0";
		// while(m.length()>0&&m.charAt(0)=='0') m=m.substring(1);
		// return m;
	}

	// 将bcd格式的金额m转换为888.88格式的字符串金额显示
	// 如果左边有0,去掉0
	public static String FormatMoney(String m) {
		String t = m;// Util.BinToHex(m,0,m.length);
		while (t.length() > 0 && t.charAt(0) == '0')
			t = t.substring(1);
		String t1;
		if (t.length() > 2) {
			t1 = t.substring(0, t.length() - 2) + "."
					+ t.substring(t.length() - 2, t.length()) + "元";
		} else if (t.length() == 2) {
			t1 = "RMB 0." + t + " 元";
		} else if (t.length() == 1)
			t1 = "RMB 0.0" + t + " 元";
		else
			t1 = "RBM 0.00 元";

		return t1;
	}

	// 得到当前时间的 yyyy-mm-dd HH:MM:SS格式
	public static String GetNowDateTime() {
		Date da = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df1.format(da);// 日期时间
	}

	// 读取配置文件的字符形变量
	public static String ReadCfgStirng(String cfgfile, String key) {
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			String cfgfile1 = System.getProperty("user.dir") + "/" + cfgfile;
			fis = new FileInputStream(cfgfile1);
			prop.load(fis);
			return prop.getProperty(key);
		} catch (FileNotFoundException e) {
			System.out.println("config file[" + cfgfile + "]not found!");
			// System.exit(0);
			return "";
		} catch (IOException e) {
			System.out.println("load config file[" + cfgfile + "]error!");
			// System.exit(0);
			return "";
		}
	}

	// 读取服务端socket配置
	public static int ReadCfgInt(String cfgfile, String key) {
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			String cfgfile1 = System.getProperty("user.dir") + "/" + cfgfile;
			fis = new FileInputStream(cfgfile1);
			prop.load(fis);
			return Integer.parseInt(prop.getProperty(key).trim());
		} catch (FileNotFoundException e) {
			System.out.println("config file[" + cfgfile + "]not found!");
			// System.exit(0);
			return 0;
		} catch (IOException e) {
			System.out.println("load config file[" + cfgfile + "]error!");
			// System.exit(0);
			return 0;
		}
	}

	// 将字符s右补pad，补齐len长度
	public static String RightPadString(String s, byte pad, int len)
			throws UnsupportedEncodingException {
		byte[] t = new byte[len];
		for (int i = 0; i < len; i++)
			t[i] = pad;
		System.arraycopy(s.getBytes(), 0, t, 0, s.length());
		return new String(t, 0, len, "GBK");
	}

	public static void WriteCfgInt(String cfgfile, String key, int value) {
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			String cfgfile1 = System.getProperty("user.dir") + "/" + cfgfile;
			fis = new FileInputStream(cfgfile1);
			prop.load(fis);
			prop.setProperty(key, Integer.toString(value));
			OutputStream ops = new FileOutputStream(cfgfile1);

			prop.store(ops, key);
		} catch (FileNotFoundException e) {
			System.out.println("config file[" + cfgfile + "]not found!");
			// System.exit(0);
			return;
		} catch (IOException e) {
			System.out.println("load config file[" + cfgfile + "]error!");
			// System.exit(0);
			return;
		}
	}

	// 获得sys.conf的错误信息配置文件
	public static String GetErrMsg(String type, String index) {
		String cfgfile = System.getProperty("user.dir") + "/sys.conf";
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(cfgfile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("config file[" + cfgfile + "]not found!");
			System.exit(0);
			return "";
		} catch (IOException e) {
			System.out.println("load config file[" + cfgfile + "]error!");
			System.exit(0);
			return "";
		}
		String t = prop.getProperty(type + "." + index);
		if (t == null)
			// ver 1.027
			return "终端接收数据超时";
		String t2;
		try {
			t2 = new String(t.getBytes("ISO8859-1"), "GBK");
			return t2;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	// bcd转换字符
	public static String BcdToString(byte[] bcd) {
		if(bcd!=null){
			String t = BinToHex(bcd, 0, bcd.length);
			return t;
		}else
			return "";
	}

	/*带小数点的金额转换为6字节的BCD码数据*/
	public static byte[] StrMoneyToBCD8(String money){
		int yuan,jiaofen;
		yuan=Integer.parseInt(money.substring(0,money.indexOf(".")));
		jiaofen=Integer.parseInt(money.substring(money.indexOf(".")+1,money.length()));
			
		String t;
		t=String.format("%010d%02d",yuan,jiaofen);
			
		return HexToBin(t);
	}
	// byte[]数组转换为整形
	public static int AscIIBytesToInt(byte[] b, int off, int len) {
		return Integer.parseInt(BytesToString(b, off, len));
	}

	// bcd数据转换为整形数据
	// bcdlen-bcd占用字节长度
	public static int BcdToInt(byte[] bcd, int off, int bcdlen) {
		String t = BinToHex(bcd, off, bcdlen);
		return Integer.parseInt(t);
	}

	/* 
	 整形数据转换为bcd byte数组
	 alen-bcd byte 数组长度
	*/ 
	public static byte[] IntToBcd(int ival, int alen) {
		byte[] t = new byte[alen];
		String s = Integer.toString(ival);
		byte[] str = s.getBytes();
		int i, j, cnt;
		cnt = 0;
		for (i = str.length - 1, j = alen - 1; i >= 0; i--) {
			if (cnt == 1) {
				t[j] = (byte) (t[j] | ((str[i] - 48) << 4)& 0xff);
				cnt = 0;
				j--;
			} else {
				t[j] = (byte) ((str[i] - 48) & 0x0f);
				cnt++;
			}
		}
		return t;
	}

	// 整形ival转换到des数组的off位置的2字节
	public static void IntToByte2(int ival, byte[] des, int off) {
		des[off] = (byte) (ival >>> 8);
		des[off + 1] = (byte) (ival & 0x000000ff);
	}

	public static void IntToByte4(int ival, byte[] des, int off) {
		des[off] = (byte) (ival >>> 24);
		des[off + 1] = (byte) (ival >>> 16);
		des[off + 2] = (byte) (ival >>> 8);
		des[off + 3] = (byte) (ival);
	}

	// String数字转换为bcd,s一定是0~9，并且偶数个
	public static byte[] StringToBcd(String s) {
		if (s.length() % 2 != 0)
			s = "0" + s;
		byte[] str = s.getBytes();
		int alen = str.length / 2;
		byte[] t = new byte[str.length / 2];
		int i, j, cnt;
		cnt = 0;
		for (i = str.length - 1, j = alen - 1; i >= 0; i--) {
			if (cnt == 1) {
				t[j] = (byte) (t[j] | ((str[i] - 48) << 4)&0xff);
				cnt = 0;
				j--;
			} else {
				t[j] = (byte) ((str[i] - 48) & 0x0f);
				cnt++;
			}
		}
		return t;
	}

	// 单字节byte转十六进制
	public static String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
				.charAt(b & 0xf));
	}
	
	/**
	 * 将整形数据a转换成16进制字符串（范围：0-65535）2字节
	 * @param a
	 * @return
	 */
	public static String toHexString(int a){
		if(a<16){
			return "0"+Integer.toHexString(a);
		}else if(a >= 16 && a<256){
			return Integer.toHexString(a);
		}else if(a >= 256 && a < 4096){
			return "0"+Integer.toHexString(a);
		}else if(a >= 4096 && a < 65536){
			return Integer.toHexString(a);
		}else {
			return "数据表示超过2字节范围";
		}
		
	}
	
	public static byte[] hexStringToByteArray(String str){
		byte[] data = new byte[str.length()/2];
		for(int i=0;i<data.length;i++){
			//获取一个字节16进制字符串
			String tempStr = str.substring(0,2);
			//截掉用过的的16进制字符串
			str = str.substring(2);
			//16进制字符串转换成10进制整数
			int temp = Integer.parseInt(tempStr, 16);				
			data[i] = (byte)temp;
		}
			return data;
	}
	
	public static byte[] hexStringToByteArray(String str,int length){
        byte[] data=new byte[length];
        byte[] src=hexStringToByteArray(str);
        System.arraycopy(src, 0, data, data.length-src.length, src.length);
        return data;
        
    }

	// 二进制数组转换为十六进制数组
	public static String BinToHex(byte[] bin, int off, int binlen) {
		if(bin==null){
			return "";
		}
		int i;
		String res = "";
		for (i = 0; i < binlen; i++) {
			res = res + toHex(bin[i + off]);
		}
		return res;
	}

	public static byte[] HexToBin(String str) {
		byte[] res;
		if (str == null || str.trim().length() == 0)
			return new byte[0];
		str = str.trim().toLowerCase();
		if (str.matches("[^0-9a-f\\s]"))
			return new byte[0];
		int strLen = str.length();
		res = new byte[strLen / 2];
		for (int i = 0; i < strLen / 2; ++i) {

			int digithigh = str.charAt(2 * i);
			int digitlow = str.charAt(2 * i + 1);
			if (digithigh >= '0' && digithigh <= '9')
				digithigh = digithigh - '0';
			else if (digithigh >= 'a' && digithigh <= 'f')
				digithigh = digithigh - 'a' + 10;

			if (digitlow >= '0' && digitlow <= '9')
				digitlow = digitlow - '0';
			else if (digitlow >= 'a' && digitlow <= 'f')
				digitlow = digitlow - 'a' + 10;

			res[i] = (byte) (digitlow | (digithigh << 4));
		}
		return res;
	}

	// 二进制可见字符串转换为字符串
	public static String BytesToString(byte[] b) {
		// byte[]t =new byte[len];
		try {
			return new String(b, 0, b.length, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String BytesToString(byte[] b, int off, int len) {
		// byte[]t =new byte[len];
		try {
			return new String(b, off, len, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	//将byte[数组]转换为int型
	public static int ByteToInt(byte[] bRefArr){
		int iOutcome = 0;
		byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);

		}

		return iOutcome;
	}

	// 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的低字节位
	private static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);

		}
		return bLocalArr;
	}

	// 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
	private static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < 4; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);

		}

		return iOutcome;
	}

	public static void WriteFile11(String f11) throws IOException {
		FileWriter fw = new FileWriter("field11.txt");
		fw.write(f11);
		fw.close();

	}

	public static String ReadField11() throws IOException {
		FileReader fr = new FileReader("field11.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		br.close();
		fr.close();
		return line;
	}

	
	////add by 白常青 for 数字比对校验
	public static boolean VerifyDataMode1(String str){
		//取当前字符串的长度
		if (null == str){
			return false;
		}
		int len = str.length();
		char[] buff = new char[len];
		str.getChars(0, len, buff, 0);

		if (len<2){
			return false;
		}
		//判断是否全部为数字
		for (int i =0;i<len; i++){
			if ((buff[i]>'9') || (buff[i]<'0'))
				return false;			
		}
		
		int checksum = 0;
	    for (int i=0; i<len-1; i++ ){
	    	int temp =0;
	    	temp = buff[len-2-i]-'0';
	    	if ((i%2)==0)
	    	{
	    		temp=temp*2;
	    	}
	    	checksum += temp%10;
	    	checksum += temp/10;	    	
	    }
		
	    checksum = 10-(checksum%10);
	    
	    if (checksum==(buff[len-1]-'0')){
			return true;
	    }else{
	    	return false;
	    }
	}
	
	////add by 白常青 for 输入比对校验
	public static boolean VerifyDataMode2(String src, String dst){
		
		if(src.equals(dst))
			return true;
		return false;
	}
	
	////add by 白常青 for 先用数字校验，再用输入比对校验
	public static boolean VerifyDataMode3(String src, String dst){
		if (0 == src.length())
			return false;
		
		if (!VerifyDataMode1(src)){
			return false;
		}
		
		return VerifyDataMode2(src, dst);

	}
	

	//add by 白常青 for 加密模式1
	public static boolean EncDataMode1(String str){
		return false;
	}
	
	//add by 白常青 for 加密模式2
	public static boolean EncDataMode2(String str){
		return false;
	}
	
	//add by 白常青 for 加密模式3
	public static boolean EncDataMode3(String str){
		return false;
	}
	
	////add by 白常青 for 将字符串转换为金额格式字符串
	public static String StringToCash(String str){
		String TempCash;
		if ((null == str)||(0 == str.length())){
			//str为空或者长度为0
			TempCash = "￥0.00";
		}else{
			if (1==str.length()){
				TempCash = "￥0.0"+str;				
			}else if(2 == str.length()){
				TempCash = "￥0."+str;
			}else{
				TempCash="￥"+str.substring(0, str.length()-2)+"."+str.substring(str.length()-2, str.length());
			}
		}	
		return TempCash;
		
	}
	
	//add by 白常青 for check 字符串是否为数字
	public static boolean StringIsNum(String str){
		if (null==str){
			return false;
		}
		
		if (0 == str.length()){
			return false;
		}else{
			char []buff = new char[str.length()];
			str.getChars(0, str.length(), buff, 0);
			for (int i=0; i<str.length(); i++){
				if ((buff[i]>'9') || (buff[i]<'0'))
				{
					return false;
				}
			}
		}	
		return true;
	}
	
	////add by 白常青 for 将字符串转换为卡号显示的格式
	public static String StringToCardnumDisp(String src, int num, String connction){
		String dest;
		int len = 0;
		if (null == src){
			dest = "";
			return dest;
		}else{
			dest = src;
			len = src.length();
		}
		
		//判断间隔字符是否为0，或者源数据长度是否<=间隔长度
		if (0 == num){
			return dest;
		}
		
		//判断间隔符号是否为null或者为长度!=1
		if ((null == connction) ||(connction.length()!=1)){
			return dest;
		}
		
		//准备添加分割符
		dest="";
		int start = 0;
		boolean isvalid = true;
		do{
			if (len > num){
				dest +=src.substring(start, start+num)+connction; 
				start += num;
				len -= num;
			}else{
				dest += src.substring(start, start+len);
				isvalid = false;
			}
			
		}while (isvalid);
		
		return dest;
	}
	
	//add by 白常青 for 卡号数据,剔除连接符号
	public static String StringToCardNum(String src, int num, String connction){

		
		//如果是null或者空,返回空
		if (null == src){
			return "";
		}
		
		//如果不需要连接符号,则返回源字符串
		if (0==num){
			return src;
		}
		
		String dest = "";
		for (int i=0; i<src.length();i++){
			if (!src.substring(i, i+1).equals(connction)){
				dest += src.substring(i, i+1);
			}
			
		}
		return dest;	
	}
	
	//add by 白常青 for 判断byte是否为00
	public static boolean ByteIsZero(byte[] b, int start, int len){
		for (int i=start; i<start+len; i++){
			if (b[i] != 0){
				return false;
			}
		}
		return true;
	}
	
	//add by 白常青 for 删除字符串尾部的0
	public static String DelStringZero(String src){
		String  dst="";
		if ((src==null) ||(src.length() == 0)){
			return dst;
		}
		
		dst = src;
		while(dst.substring(dst.length()-1).equals("0")){
			dst=dst.substring(0, dst.length()-1);
		}
		
		return dst;
	}
	
	//add by joe for 转换金额为指定长度的bcd吗
	public static byte[] StringToBcd(String str, int bcdlen){
		String temp;
		int len = bcdlen*2;
		if(null == str){
			temp ="";
		}else if (str.length()>len){
			temp = str.substring(0, len);
		}else{
			temp = str;
		}

		int end=len-temp.length();
		for (int i=0;i<end;i++){
			temp="0"+temp;
		}
		
		return StringToBcd(temp);
	}
	

	
	public static String ByteLowtoHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(b & 0xf));
	}
	
	public static void xor(byte[] t1, byte[] t2, int t2off) {
		int i;
		for (i = 0; i < 8; i++)
			t1[i] = (byte) (t1[i] ^ t2[i + t2off]);
	}
	
	/*将数据先补00到8字节，然后异或
	 */
	public static byte[] xor(byte[] in){
		byte[] input=null;
		if (in.length%8!=0){
			input=new byte[(in.length/8+1)*8];
		}else 
			input=new byte[in.length];
		System.arraycopy(in, 0, input, 0,in.length);
		byte[] t1=new byte[8];
		byte[] t2;
		int i;
		for(i=0;i<input.length/8;i++){
			xor(t1,input,i*8);
		}
		return t1;
	}
//	/**
//	 * 通道终端加密
//	 */
//	public static String Term3Des(String key){
//		try {
//			String inStr="72006A000000000000000001000010"+key;
//			System.out.println(inStr);
//			HsmSession hSession = new HsmSession("../bin/hsm.ini");
//			hSession.ReleaseSession();
//			int iSndLen = 0, nResult = 0,iKeyLen=2;
//			byte[] bSecBufferIn  = new byte[HsmConst.SECBUF_MAX_SIZE];
//			byte[] bSecBufferOut = new byte[HsmConst.SECBUF_MAX_SIZE];
//			byte[] bKeyUndZMK1 = new byte[24];
//			bSecBufferIn=HsmUtil.HexToBin(inStr);			
//			iSndLen=inStr.length()/2;	
//			nResult = hSession.SndAndRcvData(bSecBufferIn,iSndLen,bSecBufferOut);	
//			System.out.println(nResult);
//			System.out.println(HsmUtil.BinToHex(bSecBufferOut, 0, 41));
//			System.arraycopy(bSecBufferOut, 3, bKeyUndZMK1, 0, iKeyLen*8);
//			HsmApp.OutputDataHex("Key under ZMK1", bKeyUndZMK1, iKeyLen * 8);
//			System.out.println("+++++++++"+HsmUtil.BinToHex(bKeyUndZMK1, 0, 16));
//			return HsmUtil.BinToHex(bKeyUndZMK1, 0, 2*8);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}
