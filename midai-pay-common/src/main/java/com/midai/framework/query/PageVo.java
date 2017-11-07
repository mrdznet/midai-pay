package com.midai.framework.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PageVo<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 872312534407910399L;

	private int total;
	
	private List<T> rows =new ArrayList<>();
	
}
