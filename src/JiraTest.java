import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
		
		//session filter => it is used to store the session details
		SessionFilter session = new SessionFilter();
		
		//login scenario
		RestAssured.baseURI = "http://localhost:8080/";
		String response=given().log().all().header("Content-Type","application/json")
		.body("{ \"username\": \"amazingazmath\", \"password\": \"Azmath1234#\" }").filter(session).when()
		.post("rest/auth/1/session").then().log().all().assertThat().statusCode(200).extract()
		.response().asString();
		
		String expectedMessage="Hi, how are you!";
		
		//adding comments to a bug in jira
		//RestAssured.baseURI = "http://localhost:8080/";
		String addCommentResponse=given().log().all().pathParam("key", "RES-1").header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js=new JsonPath(addCommentResponse);
		String commentId=js.getString("id");
		
		//adding attachment
		given().log().all().header("X-Atlassian-Token","no-check")
		.header("Content-Type","multipart/form-data").pathParam("key", "RES-1")
		.filter(session).multiPart("file",new File("jira.txt"))
		.when().post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		//get issue
		String issueDetails=given().log().all().pathParam("key", "RES-1").queryParam("fields", "comment")
		.filter(session).when().get("rest/api/2/issue/{key}").then().log().all()
		.assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(issueDetails);
		
		JsonPath js1=new JsonPath(issueDetails);
		int commentsCount=js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) {
			
			String commentIdIssue=js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				
				String message=js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		
			
		}
				

	}

}
