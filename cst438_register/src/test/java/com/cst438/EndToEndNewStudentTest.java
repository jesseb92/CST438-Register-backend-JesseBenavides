package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndNewStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/jbena/Downloads/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000/admin";

	public static final String TEST_USER_EMAIL = "johnDoe@csumb.edu";

	public static final String TEST_USER_NAME = "John Doe";

	public static final int SLEEP_DURATION = 3000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	StudentRepository studentRepository;

	@Autowired
	CourseRepository courseRepository;

	/*
	 * New student TEST_USER_NAME to new student.
	 */
	
	@Test
	public void addNewStudentTest() throws Exception {

		/*
		 * if student is already added, then delete the enrollment.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			
			WebElement we;

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			
			// Locate and click "Add Student" button which is the first and only button on the page.
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);
			
            // Enter Student Name  
            // find the input tag with name="name"
            we = driver.findElement(By.name("name"));
            we.sendKeys(TEST_USER_NAME);
            
            // Enter Student Email 
            // find the input tag with name="email"
            we = driver.findElement(By.name("email"));
            we.sendKeys(TEST_USER_EMAIL);
            
            // find and click the add button
            we = driver.findElement(By.id("Add"));
            we.click();
            Thread.sleep(SLEEP_DURATION);
            
            // verify the correct message
            we = driver.findElement(By.id("message"));
            String message = we.getText();
            assertEquals("Student " + TEST_USER_NAME + " was added.", message);
            
			
			// verify that enrollment row has been inserted to database.
			
			Student e = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(e, "Student not found in database.");

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			
			Student e = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (e != null)
				studentRepository.delete(e);

			driver.quit();
		}

	}
}
