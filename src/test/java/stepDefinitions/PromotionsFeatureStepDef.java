package stepDefinitions;

import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.*;

public class PromotionsFeatureStepDef {

	private static RequestSpecification _REQUEST_SPEC;
	private static Response _RESP;
	private final String BASE_URL = "http://api.intigral-ott.net";
	int size = 0;

	@Given("Intigral API is up and running")
	public void intigral_API_is_up_and_running() {
		_REQUEST_SPEC = given().baseUri(BASE_URL);
	}

	@When("I hit get promotions url with APIKey {string}")
	public void i_hit_get_promotions_url_with_APIKey(String apiKey) {
		_RESP = _REQUEST_SPEC.when().request().param("apikey", apiKey).get("/popcorn-api-rs-7.9.10/v1/promotions");
	}

	@Then("API returns the response with status code as {int}")
	public void api_returns_the_response_with_status_code_as(Integer statusCode) {
		_RESP.then().assertThat().statusCode(statusCode);
	}

	@Then("I should get a list of Promotions")
	public void i_should_get_a_list_of_Promotions() {
		_RESP.then().assertThat().body("$", hasKey("promotions"));
	}

	@Then("response body should include  the following fields")
	public void response_body_should_include_the_following_fields(List<String> responseFields) {
		size = _RESP.jsonPath().getList("promotions").size();
		for (String element : responseFields) {
			_RESP.then().assertThat().body(element, hasSize(size));
		}

	}

	@Then("showPrice and showText should have either true or false")
	public void showprice_and_showText_should_have_either_true_or_false() {
		for (int i = 0; i < size; i++) {
			_RESP.then().assertThat().body("promotions[" + i + "].showPrice", anyOf(is(true), is(false))).and()
					.body("promotions[" + i + "].showText", anyOf(is(true), is(false)));
		}
	}

	@Then("localizedTexts should have ar and en objects")
	public void localizedtexts_should_have_ar_and_en_objects() {
		for (int i = 0; i < size; i++) {
			_RESP.then().assertThat().body("promotions[" + i + "].localizedTexts", hasKey("en")).and()
					.body("promotions[" + i + "].localizedTexts", hasKey("ar"));
		}
	}

	@Then("promotionId should be any string value")
	public void promotionid_should_be_any_string_value() {

		for (int i = 0; i < size; i++) {
			_RESP.then().assertThat().body("promotions[" + i + "].promotionId", isA(String.class));
		}
	}

	@Then("programType should be any of the following values")
	public void programtype_should_be_any_of_the_following_values(List<String> programTypes) {
		for (int i = 0; i < size; i++) {
			_RESP.then().assertThat().body("promotions[" + i + "].properties[0].programType", isIn(programTypes));

		}
	}

	@Then("the response includes the following")
	public void the_response_includes_the_following(Map<String, String> responseFields) {
		for (Map.Entry<String, String> field : responseFields.entrySet()) {
			_RESP.then().assertThat().body(field.getKey(), equalTo(field.getValue()));
		}
	}

	@Then("requestId is not null")
	public void requestid_is_not_null() {
		_RESP.then().assertThat().body("error.requestId", notNullValue());
	}
}
