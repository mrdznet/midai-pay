package com.midai.pay.posp.config;

import com.mifu.hsmj.ByteUtil;
import com.mifu.hsmj.CodingUtil;
import com.mifu.hsmj.DesUtil;

public class CdTool {

  public static void main(String[] args) {
    // System.out.println(getPin("123456789012345678=11dassa", "123456"));
    byte[] blockData = /* getPin("123456789012345678=11dassa", "123456") */ CodingUtil
        .hexStringToByteArray("061253DFFEDCBA98");
    // 异或数据
    String blockString = "";
    for (int i = 0; i < blockData.length; i++) {
      String str = Integer.toHexString((int) (blockData[i] & 0xff));
      if (str.length() == 1) {
        str = "0" + str;
      }
      blockString = blockString + str;
    }
    Long hexdata = Long.parseLong(blockString, 16);
    blockString = String.valueOf(hexdata);
    // 卡号数据
    String ePan = "0000678901234567";
    long encryptedPin = Long.parseLong(blockString);
    long encryptedPan = Long.parseLong(ePan, 16);

    long outData = encryptedPin ^ encryptedPan;
    String pinData = Long.toHexString(outData);
    System.out.println(pinData);
    String pin = pinData.substring(1, 7);
    pinData = "06" + pin + "FFFFFFFF";
    System.out.println(pinData);

  }

  /**
   * 计算两个数组的异或值
   * 
   * @param src1
   * @param src2
   * @return
   */
  public static byte[] bytesXOR(byte[] src1, byte[] src2) {
    int length = src1.length;
    if (length != src2.length) {
      return null;
    }
    byte[] result = new byte[length];
    for (int i = 0; i < length; i++) {
      result[i] = (byte) ((src1[i] & 0xFF) ^ (src2[i] & 0xFF));

    }
    return result;
  }

  /**
   * pin加密
   * 
   * @param track2Data
   * @param pin
   * @return
   */
  public static String getPin(String pan, String pinKey, String pin) {
    String panA = "0000" + pan.substring(pan.length() - 13, pan.length() - 1);
    byte[] pinByte = ByteUtil.hexStringToByteArray(pin);
    byte[] panTmp = ByteUtil.hexStringToByteArray((panA));
    byte[] pinBlock = new byte[8];
    for (int i = 0; i < pinByte.length; i++) {
      pinBlock[i] = (byte) (panTmp[i] ^ pinByte[i]);
    }
    System.out.println(ByteUtil.convertByteArrayToHexStr(pinBlock));
    String openPin = ByteUtil
        .convertByteArrayToHexStr(DesUtil.encode2(pinBlock, ByteUtil.hexStringToByteArray(pinKey)));
    return openPin;
  }

  /**
   * pin加密
   * 
   * @param track2Data
   * @param pin
   * @return
   */
  public static String getPin2(String track2Data, String pinKey, String pin) {
    String card = track2Data.split("=")[0];
    String panA = "0000" + card.substring(card.length() - 13, card.length() - 1);
    byte[] pinByte = ByteUtil.hexStringToByteArray("06" + pin + "FFFFFFFF");
    System.out.println(ByteUtil.convertByteArrayToHexStr(pinByte));
    byte[] panTmp = ByteUtil.hexStringToByteArray((panA));
    System.out.println(ByteUtil.convertByteArrayToHexStr(panTmp));
    byte[] pinBlock = new byte[8];
    System.out.println(pinByte.length);
    for (int i = 0; i < pinByte.length; i++) {
      pinBlock[i] = (byte) (panTmp[i] ^ pinByte[i]);
    }
    // String openPin = ByteUtil.convertByteArrayToHexStr((pinBlock));
    System.out.println("pin========"+ByteUtil.convertByteArrayToHexStr((pinBlock)));
    String openPin = ByteUtil.convertByteArrayToHexStr(
        DesUtil.compute3DES(pinBlock, ByteUtil.hexStringToByteArray(pinKey)));
    System.out.println(openPin);
    return openPin;
  }
  /**
   * 磁道软加密
   * @Description: 
   * @Auther: muzi
   * @Date: 2017年2月22日 上午11:22:07
   */
  public static String getTrans(String track2Data, String transKey) {
    String result = "";
    track2Data = track2Data.replaceAll("=", "D");
    System.out.println(track2Data.length());
    byte[] input = CodingUtil.hexStringToByteArray(track2Data.length() + track2Data);
    int length = input.length;
    int x = length % 8;
    int addLength = 0;
    byte[] src = null;
    if (x != 0) {
      addLength = 8 - x;
      src = new byte[length + addLength];
      System.arraycopy(input, 0, src, 0, length);
    } else {
      src = input;
    }
    byte[] data = new byte[8];
    int temp = 0;
    System.out.println(src.length / 8);
    for (int i = 0; i < src.length / 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (temp < 8 && temp != 8) {
          data[j] = src[j];
        } else {
          data[j] = src[temp];
        }
        temp = temp + 1;
      }
      System.out.println(CodingUtil.byteArrayToHexString(data));
      System.out.println(CodingUtil
          .byteArrayToHexString(DesUtil.encode2(data, CodingUtil.hexStringToByteArray(transKey))));
      result += CodingUtil
          .byteArrayToHexString(DesUtil.encode2(data, CodingUtil.hexStringToByteArray(transKey)));
    }
    return result;
  }

}
