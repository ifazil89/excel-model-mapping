import java.text.SimpleDateFormat;
import java.util.Date;

import com.fazil.mapping.FieldName;

public class Mobile extends Product{
	
	@FieldName(name="Model Number")
	private String modelNumber;
	@FieldName(name="Screen Size")
	private String screenSize;
	@FieldName(name="Launched Date")
	private Date launchedDate;
	
	public Mobile() {
	}
	
	
	/**
	 * @param modelNumber
	 * @param screenSize
	 * @param launchedDate
	 */
	public Mobile(String modelNumber, String screenSize, Date launchedDate) {
		super();
		this.modelNumber = modelNumber;
		this.screenSize = screenSize;
		this.launchedDate = launchedDate;
	}

	public Mobile(long productCode, String productName,String modelNumber, 
			String screenSize, Date launchedDate) {
		super(productCode,productName);
		this.modelNumber = modelNumber;
		this.screenSize = screenSize;
		this.launchedDate = launchedDate;
	}

	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public String getScreenSize() {
		return screenSize;
	}
	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}
	public Date getLaunchedDate() {
		return launchedDate;
	}
	public void setLaunchedDate(Date launchedDate) {
		this.launchedDate = launchedDate;
	}

	@Override
	public String toString() {
		return "Mobile [modelNumber=" + modelNumber + ", screenSize="
				+ screenSize + ", launchedDate=" + new SimpleDateFormat("dd-MM-yyyy").format(launchedDate)
				+ ", getProductCode()=" + getProductCode()
				+ ", getProductName()=" + getProductName() + "]";
	}
	
	
}
