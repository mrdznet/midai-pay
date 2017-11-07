/**
 * Project Name:midai-pay-posp-service
 * File Name:Pay8583Message.java
 * Package Name:com.midai.pay.posp.service.impl
 * Date:2016年9月27日上午9:15:09
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 */

package com.midai.pay.posp.config;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.midai.framework.xml.pay.PayMidaiXmlBean;
import com.midai.framework.xml.pay.PayMidaiXmlFieldBean;
import com.midai.framework.xml.pay.PayMidaiXmlFiledTypeBean;
import com.midai.framework.xml.pay.PayMidaiXmlMessageBean;
import com.mifu.hsmj.HsmMessage;

/**
 * ClassName:Pay8583Message <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月27日 上午9:15:09 <br/>
 *
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class Pay8583Message {

	private static Logger loger = LoggerFactory.getLogger(Pay8583Message.class);

	private final MidaiPayHsmTemplate hsmTemplate;

	private final PayMidaiXmlBean xmlBean;

	private final List<PayMidaiXmlFiledTypeBean> fieldBean;

	private final List<PayMidaiXmlMessageBean> messageBean;

	private String packet_encoding = "GBK";// 报文编码 UTF-8

	public Pay8583Message(MidaiPayHsmTemplate hsmTemplate, PayMidaiXmlBean xmlBean) {
		this.hsmTemplate = hsmTemplate;
		this.xmlBean = xmlBean;
		this.fieldBean = xmlBean.getFiledType();
		this.messageBean = xmlBean.getMessage();
	}
	
	
	

	private TreeMap<Integer, String> validate(TreeMap<Integer, String> filedMap, PayMidaiXmlMessageBean mess) {
		
		
		List<Integer> ingore=new ArrayList<>();
		
		//是否多传		
		for(Integer i:filedMap.keySet()){
			boolean flag=false;
			for (PayMidaiXmlFieldBean field : mess.getField()) {
				if(field.getIndex()==i.intValue()){
					flag=true;
					break;
				}
			}
			if(!flag){
				loger.warn("undefined index :"+i+" in message type:"+mess.getId());
				ingore.add(i);
				//throw new RuntimeException("undefined index :"+i+" in message type:"+mess.getId());
			}
		}
		
		
		
		//是否少传
		TreeMap<Integer, String> returnMap=new TreeMap<>();

		for (PayMidaiXmlFieldBean field : mess.getField()) {
			String value = filedMap.get(field.getIndex());
			if (StringUtils.isEmpty(value)) {
				value = field.getValue();
			}

			if (field.isRequired() && StringUtils.isEmpty(value)) {
				throw new RuntimeException("field:[" + field.getIndex() + "] is required !");
			}
			if(value!=null&&!ingore.contains(field.getIndex())){
				returnMap.put(field.getIndex(), value);
			}
			
		}
		
		

		return returnMap;
	}

	public byte[] pack8583(TreeMap<Integer, String> filedMap, String messType) {
		
		Assert.hasText(messType);
		Assert.notEmpty(filedMap);
		
		PayMidaiXmlMessageBean mess = null;		
		for (PayMidaiXmlMessageBean message : messageBean) {
			if (message.getId().equals(messType)) {
				mess = message;
			}
		}
		if (mess == null) {
			throw new RuntimeException("unsport message type:" + messType);
		}
		filedMap=validate(filedMap, mess);
		
		String bitMap64 = "00000000" + "00000000" + "00000000" + "00000000" + "00000000" + "00000000" + "00000000"
				+ "00000001";

		byte[] content = null;

			Iterator<Integer> it = filedMap.keySet().iterator();
			int count = 8;
			for (; it.hasNext();) {
				
				
				int field = it.next();// 例如1
				// 忽略mac
				if(field==64||field==128){
					continue;
				}
				
				String fieldValue = filedMap.get(field);// 字段值

				try {
					// 将域值编码转换，保证报文编码统一
					fieldValue = new String(fieldValue.getBytes(packet_encoding), packet_encoding);
					// 组二进制位图串
					bitMap64 = change16bitMapFlag(field, bitMap64);

					// 获取域定义信息
					PayMidaiXmlFiledTypeBean pfb = fieldBean.get(field);
					// bcd类型
					byte[] value = null;
					switch (pfb.getDataType()) {
					case "CharASCII":
						switch (pfb.getLengthType().toUpperCase()) {
						case "CONST":
							// 定长处理
							fieldValue = getFixFieldValue(fieldValue, pfb.getLength());
							value = fieldValue.getBytes(packet_encoding);
							break;
						case "VAR2":
						case "VAR3":

							// fieldValue=
							// getVaryLengthValue(fieldValue,pfb.getLength()<100?2:3)

							byte[] t = new byte[pfb.getLength() < 100 ? 1 : 2];
							int len = fieldValue.getBytes(Charset.forName(packet_encoding)).length;

							byte[] vv = null;
							if ("hex".equalsIgnoreCase(pfb.getConvert())) {
								len = len / 2;
								vv = Pay8583Util.ASCII_To_BCD(fieldValue.getBytes(Charset.forName(packet_encoding)),
										fieldValue.length());
							} else {

								vv = fieldValue.getBytes(Charset.forName(packet_encoding));

							}
							String sss = len + "";
							if (sss.length() % 2 > 0) {
								sss = "0" + sss;
							}

							byte[] tt = Pay8583Util.hexStringToBytes(sss);
							System.arraycopy(tt, 0, t, t.length - tt.length, tt.length);

							value = new byte[t.length + vv.length];
							System.arraycopy(t, 0, value, 0, t.length);

							System.arraycopy(vv, 0, value, t.length, vv.length);

							break;

						default:
						}

						break;
					case "ASCBCD":
						switch (pfb.getLengthType().toUpperCase()) {
						case "CONST":
							// 定长 bcd
							value = new byte[pfb.getLength()];
							if (fieldValue.length() < value.length) {
								// 左靠右靠
								StringBuilder str = new StringBuilder();
								for (int i = 0; i < value.length - fieldValue.length(); i++) {
									str.append("0");
								}

								fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + str.toString()
										: str.toString() + fieldValue;
							}

							if (fieldValue.length() % 2 > 0) {
								fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + "0"
										: "0" + fieldValue;
							}

							value = Pay8583Util.hexStringToBytes(fieldValue);
							break;
						case "VAR2":
						case "VAR3":
							// 长度位
							byte[] len = new byte["VAR2".equalsIgnoreCase(pfb.getLengthType()) ? 1 : 2]; //

							String ss = fieldValue.length() + "";
							ss = ss.length() % 2 > 0 ? "0" + ss : ss;

							byte[] t = Pay8583Util.hexStringToBytes(ss);
							System.arraycopy(t, 0, len, len.length - t.length, t.length);
							if (fieldValue.length() % 2 != 0) {
								// 左靠右靠
								StringBuilder str = new StringBuilder();
								str.append("0");
								fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + str.toString()
										: str.toString() + fieldValue;
							}
							// 长度类型
							if (pfb.getVarType().equalsIgnoreCase("bin")) {

								byte[] tmp = Pay8583Util.hexStringToBytes(fieldValue);
								value = new byte[len.length + tmp.length];
								System.arraycopy(len, 0, value, 0, len.length);
								System.arraycopy(tmp, 0, value, len.length, tmp.length);
							}

							break;

						default:
						}

						break;
					case "BIT":
						value = Pay8583Util.hexStringToBytes(fieldValue);

						break;
					default:

					}

					content = arrayApend(content, value);

					loger.info("组装前长度 ==" + count);
					loger.info("组装后报文域 {" + field + "}==" + fieldValue);
					count += value.length;

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		
		// 64域添加  66A7E640A02B75F44C7C7F455FD83137
		String macKey = filedMap.get(64);// mac密钥密文

		byte[] mab = null;
		mab = arrayApend(mab, Pay8583Util.hexStringToBytes(mess.getType()));
		mab = arrayApend(mab, get16BitByteFromStr(bitMap64));
		mab = arrayApend(mab, content);
		
		byte[] macByte = null;
		if(mess.getId().contains("jidian")){ //需解密mac密文 
			try {
				macByte = UnionpayTools.encryptMac(macKey, mab).getBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			macByte = mac(macKey, mab);
		}
		content = arrayApend(content, macByte);

		// 组装
		byte[] package8583 = null;
		package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(mess.getTpdu()));
		package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(mess.getHead()));
		package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(mess.getType()));
		package8583 = arrayApend(package8583, get16BitByteFromStr(bitMap64));
		package8583 = arrayApend(package8583, content);

		int total = package8583.length;
		byte[] result = null;
		result = arrayApend(result, Pay8583Util.hexStringToBytes(String.format("%04x", total)));
		result = arrayApend(result, package8583);
		return result;
	}
	
	public byte[] pack8583_sign(TreeMap<Integer, String> filedMap,String tpdu,String head,String type) {

        String bitMap64 = "00000000" + "00000000" + "00000000" + "00000000" + "00000000" + "00000000" + "00000000"
                + "00000000";

        byte[] content = null;

        if (filedMap != null) {
            Iterator<Integer> it = filedMap.keySet().iterator();
            int count = 8;
            for (; it.hasNext(); ) {
                int field = it.next();// 例如1
                String fieldValue = filedMap.get(field);// 字段值

                try {
                    // 将域值编码转换，保证报文编码统一
                    fieldValue = new String(fieldValue.getBytes(packet_encoding), packet_encoding);
                    // 组二进制位图串
                    bitMap64 = change16bitMapFlag(field, bitMap64);

                    // 获取域定义信息
                    PayMidaiXmlFiledTypeBean pfb = fieldBean.get(field);
                    // bcd类型
                    byte[] value = null;
                    switch (pfb.getDataType()) {
                        case "CharASCII":
                            switch (pfb.getLengthType().toUpperCase()) {
                                case "CONST":
                                    // 定长处理
                                    fieldValue = getFixFieldValue(fieldValue, pfb.getLength());
                                    value = fieldValue.getBytes(packet_encoding);
                                    break;
                                case "VAR2":
                                case "VAR3":

                                    // fieldValue=
                                    // getVaryLengthValue(fieldValue,pfb.getLength()<100?2:3)

                                    byte[] t = new byte[pfb.getLength() < 100 ? 1 : 2];
                                    int len = fieldValue.getBytes(Charset.forName(packet_encoding)).length;

                                    byte[] vv = null;
                                    if ("hex".equalsIgnoreCase(pfb.getConvert())) {
                                        len = len / 2;
                                        vv = Pay8583Util.ASCII_To_BCD(fieldValue.getBytes(Charset.forName(packet_encoding)),
                                                fieldValue.length());
                                    } else {

                                        vv = fieldValue.getBytes(Charset.forName(packet_encoding));

                                    }
                                    String sss = len + "";
                                    if (sss.length() % 2 > 0) {
                                        sss = "0" + sss;
                                    }

                                    byte[] tt = Pay8583Util.hexStringToBytes(sss);
                                    System.arraycopy(tt, 0, t, t.length - tt.length, tt.length);

                                    value = new byte[t.length + vv.length];
                                    System.arraycopy(t, 0, value, 0, t.length);

                                    System.arraycopy(vv, 0, value, t.length, vv.length);

                                    break;

                                default:
                            }

                            break;
                        case "ASCBCD":
                            switch (pfb.getLengthType().toUpperCase()) {
                                case "CONST":
                                    // 定长 bcd
                                    value = new byte[pfb.getLength()];
                                    if (fieldValue.length() < value.length) {
                                        // 左靠右靠
                                        StringBuilder str = new StringBuilder();
                                        for (int i = 0; i < value.length - fieldValue.length(); i++) {
                                            str.append("0");
                                        }

                                        fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + str.toString()
                                                : str.toString() + fieldValue;
                                    }

                                    if (fieldValue.length() % 2 > 0) {
                                        fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + "0" : "0"
                                                + fieldValue;
                                    }

                                    value = Pay8583Util.hexStringToBytes(fieldValue);
                                    break;
                                case "VAR2":
                                case "VAR3":
                                    // 长度位
                                    byte[] len = new byte["VAR2".equalsIgnoreCase(pfb.getLengthType()) ? 1 : 2]; //

                                    String ss = fieldValue.length() + "";
                                    ss = ss.length() % 2 > 0 ? "0" + ss : ss;

                                    byte[] t = Pay8583Util.hexStringToBytes(ss);
                                    System.arraycopy(t, 0, len, len.length - t.length, t.length);
                                    if (fieldValue.length() % 2 != 0) {
                                        // 左靠右靠
                                        StringBuilder str = new StringBuilder();
                                        str.append("0");
                                        fieldValue = "left".equalsIgnoreCase(pfb.getAlignMode()) ? fieldValue + str.toString()
                                                : str.toString() + fieldValue;
                                    }
                                    // 长度类型
                                    if (pfb.getVarType().equalsIgnoreCase("bin")) {

                                        byte[] tmp = Pay8583Util.hexStringToBytes(fieldValue);
                                        value = new byte[len.length + tmp.length];
                                        System.arraycopy(len, 0, value, 0, len.length);
                                        System.arraycopy(tmp, 0, value, len.length, tmp.length);
                                    }


                                    break;

                                default:
                            }

                            break;
                        case "BIT":
                            value = Pay8583Util.hexStringToBytes(fieldValue);

                            break;
                        default:

                    }

                    content = arrayApend(content, value);

                    loger.info("组装前长度 ==" + count);
                    loger.info("组装后报文域 {" + field + "}==" + fieldValue);
                    count += value.length;

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        
        
        // 组装
        byte[] package8583 = null;
        package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(tpdu));//60 00 04 00 00
        package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(head));//60  31 00 00 00 00
        package8583 = arrayApend(package8583, Pay8583Util.hexStringToBytes(type));
        package8583 = arrayApend(package8583, get16BitByteFromStr(bitMap64));
        package8583 = arrayApend(package8583, content);

        int total = package8583.length;
        byte[] result = null;
        result = arrayApend(result, Pay8583Util.hexStringToBytes(String.format("%04x", total)));
        result = arrayApend(result, package8583);
        return result;
    }
	

	public Map<Integer, String> unpack(byte[] data, String messType) {
		
		Assert.hasText(messType);
		
		PayMidaiXmlMessageBean mess = null;		
		for (PayMidaiXmlMessageBean message : messageBean) {
			if (message.getId().equals(messType)) {
				mess = message;
			}
		}
		if (mess == null) {
			throw new RuntimeException("unsport message type:" + messType);
		}

		Map<Integer, String> valueMap = new TreeMap<>();

		int offset = 0;
		// 取长度
		byte[] count = new byte[2];
		System.arraycopy(data, 0, count, 0, count.length);
		offset += count.length;
		int countLength = Integer.parseInt(Pay8583Util.bytesToHexString(count), 16);
		// 取tpdu
		int tpduLen = Pay8583Util.hexStringToBytes(mess.getTpdu()).length;
		offset += tpduLen;
		// 取 head
		int headLen = Pay8583Util.hexStringToBytes(mess.getHead()).length;
		offset += headLen;
		// 取type
		int typeLen = Pay8583Util.hexStringToBytes(mess.getType()).length;
		offset += typeLen;
		// 取位图
		byte[] bitMap8byte = new byte[8];
		System.arraycopy(data, offset, bitMap8byte, 0, bitMap8byte.length);

		// 分析位图
		String bitMap64Str = get8BitMapStr(bitMap8byte);
		offset += bitMap8byte.length;
		try {
			String value = null;  
			for (int i = 1; i < bitMap64Str.length(); i++) {

				if (bitMap64Str.charAt(i) == '1') {
					// 获取域定义信息
					PayMidaiXmlFiledTypeBean pfb = fieldBean.get(i + 1);
					switch (pfb.getLengthType().toUpperCase()) {
					case "CONST":
						switch (pfb.getDataType()) {
						case "CharASCII":
							value = new String(data, offset, pfb.getLength(), packet_encoding);
							offset += pfb.getLength();
							break;
						case "ASCBCD":
							int len = pfb.getLength();
							if (len % 2 != 0) {
								len++;
							}
							byte[] tmp = new byte[len / 2];
							System.arraycopy(data, offset, tmp, 0, tmp.length);
							value = Pay8583Util.bytesToHexString(tmp);
							offset += tmp.length;
							break;
						case "BIT":
							// 暂不处理 mac
							break;
						default:
						}
						break;
					case "VAR2":
					case "VAR3":

						byte[] t = new byte[pfb.getLength() < 100 ? 1 : 2];
						if ("VAR3".equalsIgnoreCase(pfb.getLengthType())) {
							t = new byte[2];
						}
						System.arraycopy(data, offset, t, 0, t.length);
						offset += t.length;
						int len = Integer.parseInt(Pay8583Util.bytesToHexString(t));
						switch (pfb.getDataType()) {
						case "CharASCII":

							if ("hex".equalsIgnoreCase(pfb.getConvert())) {
								// if (len % 2 != 0) {
								// len++;
								// }
								byte[] tmp = new byte[len];
								System.arraycopy(data, offset, tmp, 0, tmp.length);
								offset += tmp.length;
								value = Pay8583Util.bytesToHexString(tmp);
							} else {
								byte[] tmp = new byte[len];
								System.arraycopy(data, offset, tmp, 0, tmp.length);
								offset += tmp.length;
								value = new String(tmp, Charset.forName("GBK"));
							}

							break;
						case "ASCBCD":
							if (len % 2 != 0) {
								len++;
							}
							byte[] tmp1 = new byte[len / 2];
							System.arraycopy(data, offset, tmp1, 0, tmp1.length);
							offset += tmp1.length;
							value = Pay8583Util.bytesToHexString(tmp1);

							break;
						default:
						}

						break;
					default:

					}
//					loger.info("第" + (i + 1) + "域：【" + value.length() + "】【" + value + "】");
					valueMap.put(i, value);
				}

			}

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("解包失败");
		}

		return valueMap;
	}

	/**
	 * 改变64位图中的标志为1
	 *
	 * @param fieldNo
	 * @param res
	 * @return
	 */
	private String change16bitMapFlag(int indexNo, String res) {
		res = res.substring(0, indexNo - 1) + "1" + res.substring(indexNo);
		return res;
	}

	/**
	 * 位图操作
	 *
	 * 把16位图的字节数组转化成128位01字符串
	 * 
	 * @param packet_header_map
	 * @return
	 */
	public String get8BitMapStr(byte[] bitMap16) {
		loger.info(Pay8583Util.bytesToHexString(bitMap16));
		String strsum = "";
		for (int i = 0; i < bitMap16.length; i++) {
			strsum += getEigthBitsStringFormByte(bitMap16[i]);
		}
		loger.info(strsum);
		return strsum;
	}

	public String getEigthBitsStringFormByte(int b) {
		b |= 256;
		String str = Integer.toBinaryString(b);
		int len = str.length();
		return str.substring(len - 8, len);
	}

	/**
	 * 将字段值做定长处理，不足定长则在后面补空格
	 *
	 * @param valueStr
	 * @param defLen
	 * @return
	 */
	private String getFixFieldValue(String valueStr, int defLen) {
		return getFixFieldValue(valueStr, defLen, packet_encoding);
	}

	private String getFixFieldValue(String valueStr, int defLen, String encoding) {
		String fixLen = "";
		try {
			if (valueStr == null) {
				return strCopy(" ", defLen);
			} else {
				byte[] valueStrByte = null;
				// 这里最好指定编码，不使用平台默认编码
				if (encoding == null || encoding.trim().equals("")) {
					valueStrByte = valueStr.getBytes();
				} else {
					valueStrByte = valueStr.getBytes(encoding);
				}
				// 长度的判断使用转化后的字节数组长度，因为中文在不同的编码方式下，长度是不同的，GBK是2，UTF-8是3，按字符创长度算就是1.
				// 解析报文是按照字节来解析的，所以长度以字节长度为准，防止中文带来乱码
				if (valueStrByte.length > defLen) {
					return null;
				} else {
					fixLen = valueStr + strCopy(" ", defLen - valueStrByte.length);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return fixLen;
	}

	/**
	 * 复制字符
	 *
	 * @param str
	 * @param count
	 * @return
	 */
	public String strCopy(String str, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	private byte[] get16BitByteFromStr(String str_64) {
		byte[] bit16 = new byte[8];
		try {
			if (str_64 == null || str_64.length() != 64) {
				return null;
			}
			// 128域位图二进制字符串转16位16进制
			byte[] tmp = str_64.getBytes(packet_encoding);
			int weight;// 权重
			byte[] strout = new byte[64];
			int i, j, w = 0;
			for (i = 0; i < 8; i++) {
				weight = 0x0080;
				for (j = 0; j < 8; j++) {
					strout[i] += ((tmp[w]) - '0') * weight;
					weight /= 2;
					w++;
				}
				bit16[i] = strout[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return bit16;
	}

	private byte[] arrayApend(byte[] a, byte[] b) {
		int a_len = (a == null ? 0 : a.length);
		int b_len = (b == null ? 0 : b.length);
		byte[] c = new byte[a_len + b_len];
		if (a_len == 0 && b_len == 0) {
			return null;
		} else if (a_len == 0) {
			System.arraycopy(b, 0, c, 0, b.length);
		} else if (b_len == 0) {
			System.arraycopy(a, 0, c, 0, a.length);
		} else {
			System.arraycopy(a, 0, c, 0, a.length);
			System.arraycopy(b, 0, c, a.length, b.length);
		}
		return c;
	}

	public byte[] mac(String mackey, byte[] mab) {

		String command = "0410";// mac生成命令
		String macKeyType = "01";// 传输密钥类型，定死01，区域主密钥
		String index = String.format("%04x", 106);// 区索引106，hex化定死
		String macKeyLength =null;// macKey长度而来，hex化
		if(mackey.length()/2==8){
			macKeyLength="08";
		}
		else if(mackey.length()/2==16){
			macKeyLength="10";
		}else if(mackey.length()/2==24){
			macKeyLength="18";
		}else{
			throw new RuntimeException("mac密钥长度错误");
		}

		String macAlo = "01";// mac算法
		String originVector = "0000000000000000";
		// 补全
		if (mab.length % 8 != 0) {
			byte[] b = new byte[8 - mab.length % 8];
			mab = arrayApend(mab, b);
		}
		int k = 0;

		byte[] aaa = new byte[8];
		byte[] bbb = new byte[8]; // 00000000
		while (true) {
			if (k >= mab.length) {
				break;
			}
			System.arraycopy(mab, k, aaa, 0, aaa.length);
			k += aaa.length;
			for (int i = 0; i < aaa.length; i++) {
				bbb[i] = (byte) (aaa[i] ^ bbb[i]);
			}

		}

		String macData = Pay8583Util.bytesToHexString(Pay8583Util.bytesToHexString(bbb).toUpperCase().getBytes());
		String macDataLength = String.format("%04x", macData.length() / 2);

		HsmMessage message = new HsmMessage();
		message.appendHex(command).appendHex(macKeyType).appendHex(index).appendHex(macKeyLength).appendHex(macAlo)
				.appendHex(mackey).appendHex(originVector).appendHex(macDataLength).appendHex(macData);
		// .appendAsii(macData);
		byte[] bSecBufferOut = hsmTemplate.exec(message);

		if (bSecBufferOut[0] == 0x41) {
			byte[] macVal = new byte[8];
			System.arraycopy(bSecBufferOut, 1, macVal, 0, macVal.length);
			loger.info("组装64域：" + Pay8583Util.bytesToHexString(macVal));
			return Pay8583Util.bytesToHexString(macVal).substring(0, 8).getBytes(Charset.forName(packet_encoding));

		} else {
			throw new RuntimeException("mac计算失败");
		}

	}

}