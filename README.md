# Selenium Hybrid Automation Testing Framework

* Description
-------------------------------------------------
This is a hybrid framework which can help to get the testing results in excel as well as in html reports.

This way the test data can be extracted from excel and will dynamically run the tests.

Tests are written in Java 8 using TestNG framework.


* Directory Structure
-------------------------------------------------

Selenium_Hybrid_Automation_Testing_Framework
	|
	--src
		|
		--main
			|
			--java
				|
				--businessLogic
					|
					-- * .java
				--main.java
			--resources
				|
				--repository
					|
					--* .properties
				--excelfiles
					|
					--* .xlsx
		--test
			|
			--java
				|
				--* .java
	--pom.xml
	--README

* How To:
-------------------------------------------------


java org.testng.TestNG %projectLocation%\testng.xml

