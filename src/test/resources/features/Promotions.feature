Feature: List Promotions
  Scenario: Get All promotions
    Given Intigral API is up and running
    When I hit get promotions url with APIKey 'GDMSTGExy0sVDlZMzNDdUyZ'
    Then API returns the response with status code as 200
    And I should get a list of Promotions
     And response body should include  the following fields
     | promotions.promotionId    |
     | promotions.orderId    | 
     | promotions.promoArea   | 
     | promotions.promoType   | 
    And showPrice and showText should have either true or false 
    And localizedTexts should have ar and en objects
    And promotionId should be any string value 
    And programType should be any of the following values
    | episode |
    | movie |
   	| series |
   	| season |
    
Scenario: Fetch promotions with invalid key
    Given Intigral API is up and running
    When I hit get promotions url with APIKey 'GDMSTGExy0sV'
    Then API returns the response with status code as 403
   	And the response includes the following 
    | error.message    | invalid api key |
  	| error.code| 8001 |
    And requestId is not null 