import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.PayLoad;
import files.ReUsableMethods;

public class DynamicJson {
	//add book
	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		String response=given().log().all().header("Content-Type","application/json")
		.when().body(PayLoad.addBook(isbn,aisle)).post("Library/Addbook.php").then()
		.log().all().assertThat().statusCode(200).extract().response().asString();
		JsonPath js=ReUsableMethods.rawToJson(response);
		String id=js.getString("ID");
		System.out.println(id);	
	}
	
	
	@DataProvider(name="BooksData")
	public Object[][] booksData() {
		
		return new Object[][] {{"akbjkb","1234"},{"adgy","8723"},{"adyu","87473"}};
	}
	
	
	//delete book
	@Test(dataProvider = "BooksData")
	public void deleteBook(String isbn, String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		String response=given().log().all().header("Content-Type","application/json")
		.when().body(PayLoad.deleteBook(isbn,aisle)).post("Library/DeleteBook.php").then()
		.log().all().assertThat().statusCode(200).extract().response().asString();
	}
	
	

}
