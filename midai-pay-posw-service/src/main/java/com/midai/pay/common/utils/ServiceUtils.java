package com.midai.pay.common.utils;

import java.util.ArrayList;
import java.util.List;

public final class ServiceUtils {

    /** 根据机身号始和数量，返回指定数量的机身号列表 */
    public static List<String> buildDeviceNo(String str,String addStr) {
        String result;
        String str1;

        int strLenght = str.length();
        int addLength = addStr.length();
        //String str2 = str.substring(strLenght-addLength, strLenght);
        String str2=str.substring(9, strLenght);
        int lastNum = Integer.parseInt(str2);// 101
        int addNum = Integer.parseInt(addStr);
        List<String> list=new ArrayList<String>();
        for (int i = 0; i < addNum; i++) {
            int sumNum = lastNum + i;
            String sumStr = sumNum + "";
            int n=Math.abs(sumStr.length() - addLength);
            if (sumStr.length() > addLength) {
                str1 = str.substring(0, strLenght - addLength - n);
                str2 = str.substring(strLenght - addLength - n);
                sumNum = Integer.parseInt(str2) + i;
                sumStr = sumNum + "";
            }
            else if(sumStr.length() < addLength){
                str1 = str.substring(0, strLenght - addLength + n);
            }
            else {
                str1 = str.substring(0, strLenght - addLength);
            }
            result = str1+sumStr  ;
            list.add(result);
            //System.out.println("str********"+result);
        }
        return list;
    }

    /** 通过机身号始-和数量 获取机身号尾 */
    public static String getDeviceNoEnd(String deviceNoStart,String addNum) {
        String str1;
        int strLenght = deviceNoStart.length();
        int addLength = addNum.length();
        String str2 = null;
        if(deviceNoStart.length()>=15){
            str2=deviceNoStart.substring(13, strLenght);
        }else{
            str2=deviceNoStart.substring(9, strLenght);
        }
        int i=Integer.parseInt(addNum)-1;
        int sumNum = i+Integer.parseInt(str2);
        String sumStr = sumNum + "";
        int n=Math.abs(sumStr.length() - addLength);
        if (sumStr.length() > addLength) {
            str1 = deviceNoStart.substring(0, strLenght - addLength - n);
            str2 = deviceNoStart.substring(strLenght - addLength - n);
            sumNum = Integer.parseInt(str2) + i;
            sumStr = sumNum + "";
        }else if(sumStr.length() < addLength){
            str1 = deviceNoStart.substring(0, strLenght - addLength + n);
        }else {
            str1 = deviceNoStart.substring(0, strLenght - addLength);
        }
        return str1+sumStr;
    }
}
