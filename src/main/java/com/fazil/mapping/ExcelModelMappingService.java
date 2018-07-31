package com.fazil.mapping;

import java.io.BufferedInputStream;
import java.io.File;
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
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fazil.ArgumetTypeNotMatchException;
import com.fazil.HeaderNotMatchException;
import com.fazil.MethodNotFoundException;


public class ExcelModelMappingService {
	
	/**
	 * get the excel value into the list of model class. It will call with default sheet index 0
	 * @param inputClass
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getModels(Class<T> inputClass,String filePath) throws Exception {
		return getModels(inputClass, filePath, 0);
	}

	/**
	 * get the excel value into the list of model class.
	 * @param inputClass
	 * @param filePath
	 * @param sheetIndex
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getModels(Class<T> inputClass,String filePath,int sheetIndex) throws Exception {
		
		//parse the model class and seperate the setter fields 
		Map<String,Method> modelFieldNameAndSetterMap = new HashMap<String, Method>();
		for(Field field : getAllMembers(inputClass)){
			FieldName annotatedFieldName = field.getDeclaredAnnotation(FieldName.class);
			String fieldName = annotatedFieldName != null ? annotatedFieldName.name() : field.getName();
			
			try {
				Method setterMethod = inputClass.getMethod(makeSetter(field.getName()), field.getType());
				modelFieldNameAndSetterMap.put(fieldName,setterMethod);
			} catch (NoSuchMethodException e) {
				throw new MethodNotFoundException(fieldName+" does not have corresponding setter method.",e);
			}catch(SecurityException e){
				throw e;
			}
			
		}
		
		//fetch the data from the excel
		List<T> modelList = new ArrayList<>();
		try{
			
			Workbook workBook = WorkbookFactory.create(new BufferedInputStream(new FileInputStream(new File(filePath))));
			Sheet sheet = workBook.getSheetAt(sheetIndex);
			
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
				T model = inputClass.newInstance();
				Row row = rowIterator.next();
				for(String key : modelFieldNameAndSetterMap.keySet()){
					Method declaredSetterMethod = null;
					Object cellValue = null;
					try {
						//String cellValue = formatter.formatCellValue(row.getCell(headerNameIndexMap.get(key)));
						Integer cellIndex = headerNameIndexMap.get(key);
						if(cellIndex == null){
							throw new HeaderNotMatchException(key+" not available in the file header."
									+ "Provide header name or give annotation header for the field in the model.");
						}
						Cell cell = row.getCell(cellIndex);
						cellValue = getCellValue(cell);
						declaredSetterMethod = modelFieldNameAndSetterMap.get(key);
					if(declaredSetterMethod != null) 
						declaredSetterMethod.invoke(model, getValueOfArgType(declaredSetterMethod,cellValue));
					}catch (IllegalArgumentException e) {
						throw new ArgumetTypeNotMatchException(declaredSetterMethod.getName()+" expect input argument expected type is "
									+declaredSetterMethod.getParameters()[0]+". cell value is : "+cellValue,e);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
				modelList.add(model);
			}
		}catch(IOException e){
			throw e;
		}catch(Exception e){
			throw e;
		}
		
		return modelList;
	}
	
	//make the filed name into crrespondinf setter format
	private static String makeSetter(String fieldName){
		return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}
	
	//get all fields from the class and the super classes
	private static List<Field> getAllMembers(Class clazz){
		return getAllMembers(clazz,new ArrayList<Field>());
	}
	public static List<Field> getAllMembers(Class clazz,List<Field> members){
		
		if(clazz == Object.class || clazz == null){
			return members;
		}else{
			getAllMembers(clazz.getSuperclass(),members);
			Field[] fields = clazz.getDeclaredFields();
			for(Field field :fields){
				members.add(field);
			}
		}
		
		return members;
	}
	
	/**
	 * get the exact cell value for the correspnding type from the given cell
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell){
		Object cellValue = null;
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			if(DateUtil.isCellDateFormatted(cell)){
				cellValue = cell.getDateCellValue();
			}else{
				cellValue = cell.getNumericCellValue();
			}
		}else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			cellValue = cell.getStringCellValue();
		}
		return cellValue;
	}
	
	/**
	 * get the argument value corresponding to the file type
	 * @param method
	 * @param value
	 * @return
	 */
	private Object getValueOfArgType(Method method,Object value){
		
		Class typeClass =  method.getParameterTypes()[0];
		if(typeClass == java.lang.String.class){
			return String.valueOf(value);
		}else if(typeClass == java.lang.Long.class || typeClass == long.class){
			return Double.valueOf(String.valueOf(value)).longValue();
		}
		else if(typeClass == java.lang.Double.class || typeClass == double.class){
			return Double.valueOf(String.valueOf(value));
		}else if(typeClass == java.util.Date.class){
			return value;
		}
		return value;
	}
	
}

