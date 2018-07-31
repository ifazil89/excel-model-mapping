import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fazil.mapping.ExcelModelMappingService;


public class ExcelModelTest {

	@Test
	public void testExcelModelList() throws Exception{
		String filePath = "TEST_1.xlsx";
		List<Mobile> modelList =  new ExcelModelMappingService().getModels(Mobile.class, filePath, 0);
		Assert.assertEquals("List size not matching.",3, modelList.size());
		Assert.assertNotNull(modelList.get(0));
		Assert.assertEquals(modelList.get(0).getProductCode(),101);
	}
	

}
