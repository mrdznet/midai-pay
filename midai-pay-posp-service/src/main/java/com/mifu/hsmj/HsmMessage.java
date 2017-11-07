package com.mifu.hsmj;

import java.util.ArrayList;
import java.util.List;

public class HsmMessage {
	private List<byte[]> tempBytes;
	
	public HsmMessage(){
		tempBytes = new ArrayList<byte[]>();
	}
	
	public HsmMessage appendHex(String hex){
		byte[] b = new byte[hex.length()/2];
		HsmApp.Str2Hex(hex.getBytes(), b, hex.length());
		tempBytes.add(b);
		return this;
	}
	
	public HsmMessage appendAsii(String asi){
		tempBytes.add(asi.getBytes());
		return this;
	}
	
	public byte[] makeMessage(){
		int length = 0;
		for(byte[] b:tempBytes){
			length+=b.length;
		}
		
		byte[] message = new byte[length];
		int index = 0;
		for(byte[] b:tempBytes){
			System.arraycopy(b, 0, message, index, b.length);
			index+=b.length;
		}
		return message;
	}
	
}
