import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.PayLoad;
//import files.ReUsableMethods;
import files.ReUsableMethods;

public class Basic {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// validate if Add Place API is working as expected 
		//Add place-> Update Place with New Address -> Get Place to validate if New address is present in response
		
		//given - all input details 
		//when - Submit the API -resource,http method
		//Then - validate the response
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(PayLoad.addPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String placeId=js.getString("place_id");
		System.out.println(placeId);
		
		//Update Place
		String newAddress = "70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//Get Place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json").then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js1=ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress=js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
		
		
		
	}
}