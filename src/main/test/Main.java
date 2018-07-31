import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fazil.mapping.ExcelModelMappingService;


public class Main {


	public static void main(String[] args) {
		
		String filePath = "C:\\TEST_1.xlsx";
		try{
			
			List<Mobile> modelList =  new ExcelModelMappingService().getModels(Mobile.class, filePath, 0);
			for(Mobile model : modelList){
				System.out.println(model);
				System.out.println(model.getProductCode());
				System.out.println(model.getProductName());
				System.out.println(model.getScreenSize());
				System.out.println(model.getLaunchedDate());
			}
			System.out.println("finished..");
			

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
