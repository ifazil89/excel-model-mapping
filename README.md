# Excel-model-mapping
 This the project for mapping the excel files record to pojo model classes directly by using reflection.
 
  **Note**: Reflection will cause little delay in performance.

 ## Sample Code Snippet:
Below code snippet will get the list of Models from the Excel

     String filePath = "C:\\TEST_1.xlsx";
     
     List<Mobile> modelList =  new ExcelModelMappingService().getModels(Mobile.class, filePath);	
     
     for(Mobile model : modelList){
     
     System.out.println(model);	
     
     }
