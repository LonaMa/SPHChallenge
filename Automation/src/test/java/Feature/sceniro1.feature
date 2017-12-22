Feature: Login and main article link 

Scenario Outline: User can successful login, and Main article link to the correct picture/video
	Given User is on the Login Page
	When User enter "<username>" and "<password>"
	And User click the login button
	Then User should login successfully 
	And main article should has a picture/video
	When User click on the main article
	Then the page has been navigated to the main article
	And the picture/video is present in the article.

Examples: 
| username				| password  | 
| manan.lona@gmail.com	| Test12345 |


	

 
