import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		JsonPath js=new JsonPath(PayLoad.coursePrice()); //used to convert the response into json format
		
		int count=js.getInt("courses.size()");  //used to find no. of courses in an array
		System.out.println(count);
		
		//print purchase amount
		int purchaseAmount=js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//Print Title of the first course
		String titleFirstCourse=js.getString("courses[0].title");
		System.out.println(titleFirstCourse);
		
		//Print All course titles and their respective Prices
		for(int i=0;i<count;i++) {
			String courseTitles=js.getString("courses["+i+"].title");
			int coursePrice=js.getInt("courses["+i+"].price");
			System.out.println(courseTitles);
			System.out.println(coursePrice);
		}
		
		//Print no of copies sold by RPA Course
		System.out.println("Print no of copies sold by RPA Course");
		for(int i=0;i<count;i++) 
		{
			String coursesTitles=js.get("courses["+i+"].title");
			if(coursesTitles.equalsIgnoreCase("RPA")) 
			{
				int copies=js.get("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}
		}
		 
	}

}
