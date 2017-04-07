/*
 * ExcelModelMappingUtil.java
 * version 1.0 20-12-2016
 */
package com.fazil.mapping;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * It provides the specified model list from the excel. 
 * It will provide the generic type given. 
 * @author Fazil.ibrahim
 *
 * @param <T>
 */
public class ExcelModelMappingUtil<T>  {

	/**
	 * 
	 * @param inputModelClass
	 * @param filePath file location with file name, file type is xlsx and file contains first row as column header row
	 * @return
	 * @throws Exception
	 */
	public List<T> getModels(Class<T> inputModelClass,String filePath,int sheetIndex) throws Exception{
		
		Map<String,Method> fieldSetterMethodMap = new HashMap<>(); 
		T dynamicObject = inputModelClass.newInstance();
		for(Field field : inputModelClass.getDeclaredFields()){
			FieldName annotation = field.getDeclaredAnnotation(FieldName.class);
			String fileHeaderName;
			if(annotation != null){
					fileHeaderName = annotation.name();
			}else{
				fileHeaderName = field.getName();
			}
			try {
				fieldSetterMethodMap.put(fileHeaderName,inputModelClass.getMethod(makeSetter(field.getName()), field.getType()));
			} catch (NoSuchMethodException | SecurityException e) {
				throw e;
			}
		}
		
		//fieldSetterMethodMap.forEach((k,v)-> {System.out.println(k+" - "+v);});
		//String fileName = "C:\\Users\\fazil.ibrahim\\Desktop\\ProjectFiles\\11-01-2017\\Email to RSA.xlsx";
		List<T> modelList = new ArrayList<>();
		try{
			XSSFWorkbook workBook = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(filePath)));
			XSSFSheet sheet = workBook.getSheetAt(sheetIndex);
			Iterator<Row> rowIterator =sheet.rowIterator();
			Map<String,Integer> headerNameIndexMap = new HashMap<>();
			DataFormatter formatter = new DataFormatter();
			if(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator(); 
				while(cellIterator.hasNext()){
					Cell cell = cellIterator.next();
					headerNameIndexMap.put(formatter.formatCellValue(cell), cell.getColumnIndex());
				}
			}
			//headerNameIndexMap.forEach((k,v) -> System.out.println(k+"-"+v));
			while(rowIterator.hasNext()){
				T model = inputModelClass.newInstance();
				Row row = rowIterator.next();
				fieldSetterMethodMap.forEach((k,v)-> {
					try {
						//System.out.println(k);
					String value = formatter.formatCellValue(row.getCell(headerNameIndexMap.get(k)));
					v.invoke(model, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				modelList.add(model);
			}
		}catch(IOException e){
			throw e;
		}
		return modelList;
	}
	
	public static String makeSetter(String fieldName){
		return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}
}
