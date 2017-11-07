package com.midai.framework.common;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 
 * @author 陈勋
 *
 */
public class ExcelUtil {
	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
	 *            如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
	 *            fieldMap.put("college.collegeName","学院名称")
	 * @param sheetName
	 *            工作表的名称
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, OutputStream out,LinkedHashMap<String, String> newFieldMap) throws ExcelException {

		if (list.size() == 0 || list == null) {
			throw new ExcelException("数据源中没有任何数据");
		}

		if (sheetSize > 65535 || sheetSize < 1) {
			sheetSize = 65535;
		}

		// 创建工作簿并发送到OutputStream指定的地方
		WritableWorkbook wwb;
		try {
			wwb = Workbook.createWorkbook(out);

			// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			double sheetNum = Math.ceil(list.size()
					/ new Integer(sheetSize).doubleValue());

			// 2.创建相应的工作表，并向其中填充数据
			for (int i = 0; i < sheetNum; i++) {
				// 如果只有一个工作表的情况
				if (1 == sheetNum) {
					WritableSheet sheet = wwb.createSheet(sheetName, i);
					fillSheet(sheet, list, fieldMap, 0, list.size() - 1, newFieldMap);

					// 有多个工作表的情况
				} else {
					WritableSheet sheet = wwb.createSheet(sheetName + (i + 1),
							i);

					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list
							.size() - 1 : (i + 1) * sheetSize - 1;
					// 填充工作表
					fillSheet(sheet, list, fieldMap, firstIndex, lastIndex, newFieldMap);
				}
			}

			wwb.write();
			wwb.close();
			//关闭数据流，以便删除操作
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，工作表大小为2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			OutputStream out,LinkedHashMap<String, String> newFieldMap) throws ExcelException {

		listToExcel(list, fieldMap, sheetName, 65535, out,newFieldMap);

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, HttpServletResponse response,LinkedHashMap<String, String> newFieldMap) throws ExcelException {

		// 设置默认文件名为当前时间：年月日时分秒
		String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(
				new Date()).toString();

		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + ".xls");

		// 创建工作簿并发送到浏览器
		try {

			OutputStream out = response.getOutputStream();
			listToExcel(list, fieldMap, sheetName, sheetSize, out , newFieldMap);

		} catch (Exception e) {
			e.printStackTrace();

			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}
	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list,
			LinkedHashMap<String, String> fieldMap, String sheetName,
			HttpServletResponse response,LinkedHashMap<String, String> newFieldMap) throws ExcelException {

		listToExcel(list, fieldMap, sheetName, 65535, response, newFieldMap);
	}

	/**
	 * @MethodName : excelToList
	 * @Description : 将Excel转化为List
	 * @param in
	 *            ：承载着Excel的输入流
	 * @param sheetIndex
	 *            ：要导入的工作表序号
	 * @param entityClass
	 *            ：List中对象的类型（Excel中的每一行都要转化为该类型的对象）
	 * @param fieldMap
	 *            ：Excel中的中文列头和类的英文属性的对应关系Map
	 * @param uniqueFields
	 *            ：指定业务主键组合（即复合主键），这些列的组合不能重复
	 * @return ：List
	 * @throws ExcelException
	 */
	public static <T> List<T> excelToList(InputStream in, String sheetName,
			Class<T> entityClass, LinkedHashMap<String, String> fieldMap,
			String[] uniqueFields) throws ExcelException {

		// 定义要返回的list
		List<T> resultList = new ArrayList<T>();

		try {

			// 根据Excel数据源创建WorkBook
			Workbook wb = Workbook.getWorkbook(in);
			// 获取工作表
			Sheet sheet = wb.getSheet(sheetName);

			// 获取工作表的有效行数
			int realRows = 0;
			for (int i = 0; i < sheet.getRows(); i++) {

				int nullCols = 0;
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell currentCell = sheet.getCell(j, i);
					if (currentCell == null
							|| "".equals(currentCell.getContents().toString())) {
						nullCols++;
					}
				}

				if (nullCols == sheet.getColumns()) {
					break;
				} else {
					realRows++;
				}
			}

			// 如果Excel中没有数据则提示错误
			if (realRows <= 1) {
				throw new ExcelException("Excel文件中没有任何数据");
			}

			Cell[] firstRow = sheet.getRow(0);

			String[] excelFieldNames = new String[firstRow.length];

			// 获取Excel中的列名
			for (int i = 0; i < firstRow.length; i++) {
				excelFieldNames[i] = firstRow[i].getContents().toString()
						.trim();
			}

			// 判断需要的字段在Excel中是否都存在
			boolean isExist = true;
			List<String> excelFieldList = Arrays.asList(excelFieldNames);
			for (String cnName : fieldMap.keySet()) {
				if (!excelFieldList.contains(fieldMap.get(cnName))) {// update:2015年12月21日11:55:24
																		// by XM
					isExist = false;
					break;
				}
			}

			// 如果有列名不存在，则抛出异常，提示错误
			if (!isExist) {
				throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
			}

			// 将列名和列号放入Map中,这样通过列名就可以拿到列号
			LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < excelFieldNames.length; i++) {
				colMap.put(excelFieldNames[i], firstRow[i].getColumn());
			}

		if(uniqueFields!=null){
			// 判断是否有重复行
			// 1.获取uniqueFields指定的列
			Cell[][] uniqueCells = new Cell[uniqueFields.length][];
			for (int i = 0; i < uniqueFields.length; i++) {
				int col = colMap.get(uniqueFields[i]);
				uniqueCells[i] = sheet.getColumn(col);
			}

			// 2.从指定列中寻找重复行
			for (int i = 1; i < realRows; i++) {
				int nullCols = 0;
				for (int j = 0; j < uniqueFields.length; j++) {
					String currentContent = uniqueCells[j][i].getContents();
					Cell sameCell = sheet.findCell(currentContent,
							uniqueCells[j][i].getColumn(),
							uniqueCells[j][i].getRow() + 1,
							uniqueCells[j][i].getColumn(),
							uniqueCells[j][realRows - 1].getRow(), true);
					if (sameCell != null) {
						nullCols++;
					}
				}

				if (nullCols == uniqueFields.length) {
					throw new ExcelException("Excel中有重复行，请检查");
				}
			}
		 }

			// 将sheet转换为list
			for (int i = 1; i < realRows; i++) {
				// 新建要转换的对象
				T entity = entityClass.newInstance();

				// 给对象中的字段赋值
				for (Entry<String, String> entry : fieldMap.entrySet()) {
					// 获取中文字段名
					String cnNormalName = entry.getValue();// update:2015年12月21日11:55:24
															// by XM
					// 获取英文字段名
					String enNormalName = entry.getKey(); // update:2015年12月21日11:55:24
															// by XM
					// 根据中文字段名获取列号
					int col = colMap.get(cnNormalName);

					// 获取当前单元格中的内容
					String content = sheet.getCell(col, i).getContents()
							.toString().trim();

					// 给对象赋值
					setFieldValueByName(enNormalName, content, entity);
				}

				resultList.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				e.printStackTrace();
				throw new ExcelException("导入Excel失败");
			}
		}
		return resultList;
	}

	/*
	 * <-------------------------辅助的私有方法------------------------------------------
	 * ----->
	 */
	/**
	 * @MethodName : getFieldValueByName
	 * @Description : 根据字段名获取字段值
	 * @param fieldName
	 *            字段名
	 * @param o
	 *            对象
	 * @return 字段值
	 */
	private static Object getFieldValueByName(String fieldName, Object o)
			throws Exception {

		Object value = null;
		Field field = getFieldByName(fieldName, o.getClass());

		if (field != null) {
			field.setAccessible(true);
			value = field.get(o);
		}/* else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 "
					+ fieldName);
		}*/

		return value;
	}

	/**
	 * @MethodName : getFieldByName
	 * @Description : 根据字段名获取字段
	 * @param fieldName
	 *            字段名
	 * @param clazz
	 *            包含该字段的类
	 * @return 字段
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * @MethodName : getFieldValueByNameSequence
	 * @Description : 根据带路径或不带路径的属性名获取属性值
	 *              即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
	 * 
	 * @param fieldNameSequence
	 *            带路径的属性名或简单属性名
	 * @param o
	 *            对象
	 * @return 属性值
	 * @throws Exception
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence,
			Object o) throws Exception {

		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, o);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], o);
			String subFieldNameSequence = fieldNameSequence
					.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;

	}

	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值
	 * @param o
	 *            对象
	 */
	private static void setFieldValueByName(String fieldName,
			Object fieldValue, Object o) throws Exception {

		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();

			// 根据字段类型给字段赋值
			if (String.class == fieldType) {
				field.set(o, String.valueOf(fieldValue));
			} else if ((Integer.TYPE == fieldType)
					|| (Integer.class == fieldType)) {
				field.set(o, Integer.parseInt(fieldValue.toString()));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(o, Long.valueOf(fieldValue.toString()));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				field.set(o, Float.valueOf(fieldValue.toString()));
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(o, Short.valueOf(fieldValue.toString()));
			} else if ((Double.TYPE == fieldType)
					|| (Double.class == fieldType)) {
				field.set(o, Double.valueOf(fieldValue.toString()));
			} else if (Character.TYPE == fieldType) {
				if ((fieldValue != null)
						&& (fieldValue.toString().length() > 0)) {
					field.set(o,
							Character.valueOf(fieldValue.toString().charAt(0)));
				}
			} else if (Date.class == fieldType) {
				field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(fieldValue.toString()));
			} else {
				field.set(o, fieldValue);
			}
		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 "
					+ fieldName);
		}
	}

	/**
	 * @MethodName : setColumnAutoSize
	 * @Description : 设置工作表自动列宽和首行加粗
	 * @param ws
	 */
	private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
		// 获取本列的最宽单元格的宽度
		for (int i = 0; i < ws.getColumns(); i++) {
			int colWith = 0;
			for (int j = 0; j < ws.getRows(); j++) {
				String content = ws.getCell(i, j).getContents().toString();
				int cellWith = content.length();
				if (colWith < cellWith) {
					colWith = cellWith;
				}
			}
			// 设置单元格的宽度为最宽宽度+额外宽度
			ws.setColumnView(i, colWith + extraWith);
		}

	}

	/**
	 * @MethodName : fillSheet
	 * @Description : 向工作表中填充数据
	 * @param sheet
	 *            工作表
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            中英文字段对应关系的Map
	 * @param firstIndex
	 *            开始索引
	 * @param lastIndex
	 *            结束索引
	 */
	private static <T> void fillSheet(WritableSheet sheet, List<T> list,
			LinkedHashMap<String, String> fieldMap, int firstIndex,
			int lastIndex,LinkedHashMap<String, String> newFieldMap) throws Exception {

		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];

		// 填充数组
		int count = 0;
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}
		// 填充表头
		int i = 0;
		for (; i < cnFields.length; i++) {
			Label label = new Label(i, 0, cnFields[i]);
			sheet.addCell(label);
		}

		//处理新增字段
		String[] enFieldsn = null;
		String[] cnFieldsn = null;
		if(newFieldMap!=null){
			//表头
			cnFieldsn = new String[newFieldMap.size()];
			//数据
			enFieldsn = new String[newFieldMap.size()];
		}
		if(newFieldMap!=null){
			// 定义存放英文字段名和中文字段名的数组
			int count1 = 0;
			for (Entry<String, String> entry : newFieldMap.entrySet()) {
				cnFieldsn[count1] = entry.getKey();
				enFieldsn[count1] = entry.getValue();
				count1++;
			}
			int tempi = i;
			for (int i1=0; i < cnFieldsn.length+tempi; i++,i1++) {
				Label label = new Label(i, 0, cnFieldsn[i1]);
				sheet.addCell(label);
			}
		}
		// 填充内容    
		int rowNo = 1;
		int j=0;
		for (int index = firstIndex; index <= lastIndex; index++) {
			j=0;
			// 获取单个对象
			T item = list.get(index);
			if(item!=null){
				for (; j < enFields.length; j++) {
					Object objValue = getFieldValueByNameSequence(enFields[j], item);
						//格式化数据库中日期字段数据
						if(objValue instanceof Date) {
							objValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objValue);
						}
						String fieldValue = objValue == null ? "" : objValue.toString();
						Label label = new Label(j, rowNo, fieldValue);
						sheet.addCell(label);
				}
			}

			rowNo++;
		}
		

		rowNo--;
		//处理新增字段
		if(cnFieldsn!=null){
			// 定义存放英文字段名和中文字段名的数组
			int tempj = j;
			for (int j1=0; j < enFieldsn.length+tempj; j++,j1++) {
				Label label = new Label(j, rowNo, enFieldsn[j1]);
				sheet.addCell(label);
			}
			rowNo++;
		}
		
		
		
		
		// 设置自动列宽
		setColumnAutoSize(sheet, 5);
	}
	
	/**
	 * 
	 * listToExcelByLocalExcel:(根据本地Excel模板文件追加内容). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param file
	 * @param list
	 * @param sheetSize
	 * @param out
	 * @throws ExcelException 
	 * @since JDK 1.7
	 */
	public static <T> void listToExcelByLocalExcel(File file,List<T> list,String sheetName, LinkedHashMap<String, String> fieldMap,LinkedHashMap<String, String> newFieldMap, HttpServletResponse response,Map<String, String> dataMap) throws ExcelException{
		listToExcelByLocalExcel2(file, list,sheetName, fieldMap, newFieldMap, response,dataMap);
	}
	
	public static <T> void listToExcelByLocalExcel2(File file,List<T> list,String sheetName,LinkedHashMap<String, String> fieldMap,LinkedHashMap<String, String> newFieldMap, HttpServletResponse response,Map<String, String> dataMap) throws ExcelException {

		// 设置默认文件名为当前时间：年月日时分秒
		String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(
				new Date()).toString();

		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + ".xls");

		// 创建工作簿并发送到浏览器
		try {

			OutputStream out = response.getOutputStream();
			excelAppendContent(file, list,sheetName, fieldMap, 65535, newFieldMap, out,dataMap);

		} catch (Exception e) {
			e.printStackTrace();

			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}
	}
	
	
	public static <T> void excelAppendContent(File file,List<T> list,String sheetName,LinkedHashMap<String, String> fieldMap,int sheetSize,LinkedHashMap<String, String> newFieldMap, OutputStream out,Map<String, String> dataMap) throws ExcelException {

		if (list.size() == 0 || list == null) {
			throw new ExcelException("数据源中没有任何数据");
		}

		if (sheetSize > 65535 || sheetSize < 1) {
			sheetSize = 65535;
		}

		// 根据当前指定的excel文件生产工作簿
	    WritableWorkbook wwb;
	    Workbook book;
		try {
			book = Workbook.getWorkbook(file); 
			
			    
			    WorkbookSettings settings = new WorkbookSettings ();  
			    settings.setWriteAccess(null); 
			     wwb = book.createWorkbook(out, book, settings);
			
			   // WritableSheet wws = wwb.getSheet(1); 

			// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			double sheetNum = Math.ceil(list.size()
					/ new Integer(sheetSize).doubleValue());

			// 2.创建相应的工作表，并向其中填充数据
			for (int i = 0; i < sheetNum; i++) {
				
				

				
				
				// 如果只有一个工作表的情况
				if (1 == sheetNum) {
					
					WritableSheet sheet = wwb.getSheet(0);
					if("还款数据".equals(sheetName)){
						sheet.getSettings().setVerticalFreeze(1);
						sheet.getSettings().setVerticalFreeze(2);
					}else{
						sheet.getSettings().setVerticalFreeze(1);
					}

					sheet.setName(sheetName);
					
					fillSheet2(sheetName,sheet, list, fieldMap, 0, list.size() - 1, newFieldMap,dataMap);
					
					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list
							.size() - 1 : (i + 1) * sheetSize - 1;
							if("业务数据统计".equals(sheetName)){
						        //求和公式  
								sumFormula("N",13,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("T",19,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("V",21,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("W",22,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("Z",25,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
							}
			       

					// 有多个工作表的情况
				} else {
					WritableSheet sheet = wwb.getSheet(i+1);
					sheet.setName(sheetName+i);
					if("还款数据".equals(sheetName)){
						sheet.getSettings().setVerticalFreeze(1);
						sheet.getSettings().setVerticalFreeze(2);
					}else{
						sheet.getSettings().setVerticalFreeze(1);
					}

					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list
							.size() - 1 : (i + 1) * sheetSize - 1;
					// 填充工作表
							fillSheet2(sheetName,sheet, list, fieldMap, firstIndex, lastIndex, newFieldMap,dataMap);
							
							
						   if("业务数据统计".equals(sheetName)){
						        //求和公式  
								sumFormula("N",13,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("T",19,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("V",21,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("W",22,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
								sumFormula("Z",25,firstIndex, lastIndex, 2, sheet, CellType.NUMBER_FORMULA);
							}

				}
			}
			wwb.write();
			wwb.close();
			book.close();
			
			
//			wwb.setOutputFile(targetFile);  
			
			//关闭数据流，以便删除操作
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}

	}
	
	@SuppressWarnings("unused")
	private static <T> void fillSheet2(String sheetName,WritableSheet sheet, List<T> list,
			LinkedHashMap<String, String> fieldMap, int firstIndex,
			int lastIndex,LinkedHashMap<String, String> newFieldMap,Map<String, String> dataMap) throws Exception {
		
		
		  sheet.setRowView(0, 581, false); //设置行高

		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];
		
		// 填充数组
		int count = 0;
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}
		
		// 填充表头
		int i = 0;
		
		
		if("还款数据".equals(sheetName)){
			//设置样式        还款数据表头
			Label labelHeader;
			WritableCellFormat wCellFormat  = new WritableCellFormat();
			wCellFormat.setAlignment(Alignment.CENTRE);  //设置水平居中
			wCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中
			//wCellFormat.setBackground(jxl.format.Colour.RED);  //设置背景颜色
			wCellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);  //设置边框
			for (; i < 8; i++) {
				if(i == 0){
					labelHeader = new Label(0, 0, "当月收款额",wCellFormat);
					sheet.addCell(labelHeader);
				}else if(i == 1){
					labelHeader = new Label(1, 0, dataMap.get("principalInterest"),wCellFormat);
					sheet.addCell(labelHeader);
				}else if(i == 3){
					labelHeader = new Label(3, 0, "逾期率",wCellFormat);
					sheet.addCell(labelHeader);
				}else if(i == 4){
					labelHeader = new Label(4, 0, dataMap.get("overdueRate"),wCellFormat);
					sheet.addCell(labelHeader);
				}else if(i == 6){
					labelHeader = new Label(6, 0, "实收账款/应收账款",wCellFormat);
					sheet.addCell(labelHeader);
				}else if(i == 7){
					labelHeader = new Label(7, 0, dataMap.get("avgArrivalAmount"),wCellFormat);
					sheet.addCell(labelHeader);
				}
			}
		}
		
		

		//处理新增字段
		String[] enFieldsn = null;
		String[] cnFieldsn = null;
		if(newFieldMap!=null){
			//表头
			cnFieldsn = new String[newFieldMap.size()];
			//数据
			enFieldsn = new String[newFieldMap.size()];
		}
		if(newFieldMap!=null){
			// 定义存放英文字段名和中文字段名的数组
			int count1 = 0;
			for (Entry<String, String> entry : newFieldMap.entrySet()) {
				cnFieldsn[count1] = entry.getKey();
				enFieldsn[count1] = entry.getValue();
				count1++;
			}
			int tempi = i;
			for (int i1=0; i < cnFieldsn.length+tempi; i++,i1++) {
				Label label = new Label(i, 0, cnFieldsn[i1]);
				sheet.addCell(label);
			}
		}
		// 填充内容    
		int rowNo = 1;
		
		if("还款数据".equals(sheetName)){
			rowNo = 2;
		}
		
		int j=0;
		
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);  //设置水平居中
		wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中
		wcf.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);  //设置边框
		
	    NumberFormat nf = new NumberFormat("#.00");     
	    WritableCellFormat wnf = new WritableCellFormat(nf);
	    wnf.setAlignment(Alignment.CENTRE);  //设置水平居中
	    wnf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中
	    wnf.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);  //设置边框
		
		
		for (int index = firstIndex; index <= lastIndex; index++) {
			j=0;
			// 获取单个对象
			T item = list.get(index);
			
			sheet.setRowView(index, 581, false); //设置行高
			
			if(item!=null){
				for (; j < enFields.length; j++) {
					Object objValue = getFieldValueByNameSequence(enFields[j], item);
					
					if(index ==61 && j == 6){
						System.out.println(enFields[j].toString()
								);
					}
						//格式化数据库中日期字段数据
					
					
						if(objValue instanceof Date) {
							objValue = new SimpleDateFormat("yyyy-MM-dd").format(objValue);
							String fieldValue = objValue == null ? "" : objValue.toString();
//							wcf = new WritableCellFormat();
							Label label = new Label(j, rowNo, fieldValue,wcf);
							sheet.addCell(label);
						}else if(objValue instanceof Double){
							     
							if(objValue != null && Double.valueOf(objValue.toString()) > 0){
					                Number number = new Number(j, rowNo,(double)objValue,wnf);
					                sheet.addCell(number);
							}else{
								String fieldValue = objValue == null ? "" : objValue.toString();
								Label label = new Label(j, rowNo, "",wcf);
								sheet.addCell(label);
							}
							       
						}else{
							String fieldValue = objValue == null ? "" : objValue.toString();
							Label label = new Label(j, rowNo, fieldValue,wcf);
							sheet.addCell(label);
						}
				}
			}

			rowNo++;
		}
		
		

		rowNo--;
		//处理新增字段
		if(cnFieldsn!=null){
			// 定义存放英文字段名和中文字段名的数组
			int tempj = j;
			for (int j1=0; j < enFieldsn.length+tempj; j++,j1++) {
				Label label = new Label(j, rowNo, enFieldsn[j1]);
				sheet.addCell(label);
			}
			rowNo++;
		}
		//sheet.getSettings().setPrintHeaders(true);  
		
		
		//设置行高
		if("还款数据".equals(sheetName)){
			sheet.setRowView(lastIndex+1, 581, false); 
			sheet.setRowView(lastIndex+2, 581, false);
		}else{
			sheet.setRowView(lastIndex+1, 581, false);
		}
		// 设置自动列宽
		setColumnAutoSize(sheet, 10);
		

		
	}
	
    /**   
     * 得到数据格式   
     * @return   
     */    
    public static WritableCellFormat getDataCellFormat(CellType type, Colour colour){     
        WritableCellFormat wcf = null;     
        try {  
            //字体样式     
            if(type == CellType.NUMBER || type == CellType.NUMBER_FORMULA){//数字     
               NumberFormat nf = new NumberFormat("#.00");     
               wcf = new WritableCellFormat(nf);      
            }else if(type == CellType.DATE || type == CellType.DATE_FORMULA){//日期     
                jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm:ss");      
                wcf = new jxl.write.WritableCellFormat(df);      
            }else{     
                WritableFont wf =   
                    new WritableFont(WritableFont.TIMES,10, WritableFont.NO_BOLD,false);  
                wcf = new WritableCellFormat(wf);     
            }  
			wcf.setAlignment(Alignment.CENTRE);  //设置水平居中
			wcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //设置垂直居中
			//wCellFormat.setBackground(jxl.format.Colour.RED);  //设置背景颜色
			wcf.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);  //设置边框
            
            if(colour == null){
            	colour = Colour.WHITE;
            }
            
            wcf.setBackground(colour);     
                 
            wcf.setWrap(true);//自动换行     
                 
        } catch (WriteException e) {     
         e.printStackTrace();     
        }     
        return wcf;     
    }  
    
    /**
     * 
     * createFormula:(求和公式). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author 屈志刚
     * @param firstIndex
     * @param lastIndex
     * @param increment
     * @param sheet
     * @param CellType
     * @throws RowsExceededException
     * @throws WriteException
     * @since JDK 1.7
     */
	public static Object  sumFormula(String positioanStr, int column, int firstIndex, int lastIndex, int increment, WritableSheet sheet, CellType CellType) throws RowsExceededException, WriteException{
		
		Colour colour = Colour.WHITE;
        Formula formula =   new Formula(column, (lastIndex+increment+1), "SUM("+positioanStr+""+(firstIndex+increment)+":"+positioanStr+""+(lastIndex+increment)+")",getDataCellFormat(CellType.NUMBER_FORMULA,colour)); 
        sheet.setRowView((lastIndex+increment+1), 581, false); //设置行高
        sheet.addCell(formula); 
        
        return formula.getCellFeatures();
	}
	
	
}
