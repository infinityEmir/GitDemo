import org.testng.Assert;
import org.testng.annotations.Test;

import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	
	@Test
	public void sumOfCourses() {
		
		JsonPath js=new JsonPath(PayLoad.coursePrice());
		int p_amount=js.getInt("dashboard.purchaseAmount");
		System.out.println(p_amount);
		
		int count=js.getInt("courses.size()");
		System.out.println(count);
		
		int sum=0;
		for(int i=0;i<count;i++) {
			
			int price=js.getInt("courses["+i+"].price");
			int copies=js.getInt("courses["+i+"].copies");
			int soldAmount=price*copies;
			sum=sum+soldAmount;
		}
		System.out.println("Sum of all Course prices: "+sum);
		Assert.assertEquals(sum, p_amount);
		
	}

}
