# excel-model-mapping
This Repository holds the project for mapping the excel record to pojo model classes

Sample Code:

String filePath = "C:\\TEST_1.xlsx";
#List<Mobile> modelList =  new ExcelModelMappingService().getModels(Mobile.class, filePath);
for(Mobile model : modelList){
	System.out.println(model);
}
