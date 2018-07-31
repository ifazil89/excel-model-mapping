import com.fazil.mapping.FieldName;

public class Product{
	
	@FieldName(name="Product Code")
	private long productCode;
	@FieldName(name="Product Name")
	private String productName;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param productCode
	 * @param productName
	 */
	public Product(long productCode, String productName) {
		super();
		this.productCode = productCode;
		this.productName = productName;
	}


	public long getProductCode() {
		return productCode;
	}
	public void setProductCode(long productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
