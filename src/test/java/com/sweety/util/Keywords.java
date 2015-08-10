package com.sweety.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.sweety.util.ErrorUtil;

public class Keywords {

	
	private static final boolean True = false;
	private static List<WebElement> Years = null;
	private static List<WebElement> Levels = null;
	private static List<WebElement> Actions = null;
	public static List<WebElement> radiobuttons = null;
	public static Xls_Reader xls = new Xls_Reader(
			System.getProperty("user.dir")
					+ "\\src\\test\\resources\\Test Suite.xlsx");
	public static Logger APP_LOGS = null;
	private Properties CONFIG = null;
	private Properties OR = null;
	private Properties SQL = null;
	private WebDriver driver = null;
	static Keywords k=null;
	public Connection connection = null;
	public Statement stmt = null;
	@SuppressWarnings("unused")
	private List<String> emails = null;
	private static boolean isBrowserOpened = false;
	private String Email = null;
	@SuppressWarnings("unused")
	private String email;
	DesiredCapabilities cap = null;
	Hashtable<String, String> data = new Hashtable<String, String>();

	private Keywords() {
		System.out.println("Initializing Keywords");
		// initialize properties files
		try {

			APP_LOGS = Logger.getLogger("devpinoyLogger");
			// config
			CONFIG = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\java\\com\\sweety\\config\\config.properties");
			CONFIG.load(fs);
			// OR
			OR = new Properties();
			fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\java\\com\\sweety\\config\\or.properties");
			OR.load(fs);

			SQL = new Properties();
			fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\java\\com\\sweety\\config\\sql.properties");
			SQL.load(fs);
			// xls =new
			// Xls_Reader(System.getProperty("user.dir")+"\\src\\com\\mcpayments\\xls\\Test Suite1.xlsx");

		} catch (Exception e) {
			// Error is found
			e.printStackTrace();
		}
	}

	public void executeKeywords(String testName, Hashtable<String, String> data) {
		System.out.println("Executing - " + testName);
		// find the keywords for the test
		String keyword = null;
		String objectKey = null;
		String dataColVal = null;
		String tcid = null;
		for (int rNum = 2; rNum <= xls.getRowCount("Test Steps"); rNum++) {
			tcid = xls.getCellData("Test Steps", "TCID", rNum);
			if (tcid.equals(testName)) {
				keyword = xls.getCellData("Test Steps", "Keyword", rNum);
				objectKey = xls.getCellData("Test Steps", "Object", rNum);
				dataColVal = xls.getCellData("Test Steps", "Data", rNum);

				String result = null;

				if (keyword.equals("openBrowser")) {
					result = openBrowser(dataColVal);
				} else if (keyword.equals("closeBrowser")) {
					result = closeBrowser();
				} else if (keyword.equals("click")) {
					result = click(objectKey);
				} else if (keyword.equals("clear")) {
					result = clear(objectKey);
				} else if (keyword.equals("input")) {
					result = input(objectKey, data.get(dataColVal));
				} else if (keyword.equals("selectDropdown")) {
					result = selectDropdown(objectKey, data.get(dataColVal));
				}else if (keyword.equals("selectDropdownbyindex")) {
					result = selectDropdownbyindex(objectKey, data.get(dataColVal));
				}else if (keyword.equals("inputfromObjectRepository")) {
					result = inputfromObjectRepository(objectKey, dataColVal);
				} else if (keyword.equals("inputfromConfigurations")) {
					result = inputfromConfigurations(objectKey, dataColVal);
				} else if (keyword.equals("inputconvertfromdecimal")) {
					result = inputconvertfromdecimal(objectKey,
							data.get(dataColVal));
				} else if (keyword.equals("navigate")) {
					result = navigate(dataColVal);
				} else if (keyword.equals("selectRadioButton")) {
					result = selectRadioButton(objectKey, data.get(dataColVal));
				} 
				
				else if (keyword.equals("VerifyReport")) {
					result = VerifyReport(data.get(dataColVal.substring(0, 4)),data.get(dataColVal.substring(4, 9)),data.get(dataColVal.substring(9, 13)),data.get(dataColVal.substring(13, 18)));
				} 
				
				else if (keyword.equals("verifySuccessMessage")) {
					result = verifySuccessMessage(dataColVal);
				} else if (keyword.equals("verifyValidationMessages")) {
					result = verifyValidationMessages(data.get(dataColVal));
				} else if (keyword.equals("assertEquals")) {
					result = assertEquals(objectKey, dataColVal);
				} else if (keyword.equals("assertEquals_Attribute")) {
					result = assertEquals_Attribute(objectKey,
							data.get(dataColVal));
				} else if (keyword.equals("isElementPresence")) {
					result = isElementPresence(objectKey);
				} else if (keyword.equals("isElementSelected")) {
					result = isElementSelected(objectKey);
				} else if (keyword.equals("acceptAlert")) {
					result = acceptAlert();
				} else if (keyword.equals("checkMail")) {
					result = checkMail(data.get(dataColVal));
				} else if (keyword.equals("compareTitle")) {
					result = compareTitle(dataColVal);
				} else if (keyword.equals("updatePasswordthroughDb")) {
					result = updatePasswordthroughDb(dataColVal, Email);
				} else if (keyword.equals("searchEmail")) {
					result = searchEmail(Email);
				} else if (keyword.equals("isElementDisabled")) {
					result = isElementDisabled(objectKey);
				} else if (keyword.equals("isElementReadonly")) {
					result = isElementReadonly(objectKey);
				} else if (keyword.equals("logout")) {
					result = logout();
				} else if (keyword.equals("loginbySuperUser")) {
					result = loginbySuperUser();
				} else if (keyword.equals("datepicker")) {
					result = datepicker(data.get(dataColVal));
				}

				k.log(result);

				if (!result.equals("Pass")) {
					try {
						// screenshot
						String fileName = tcid + "_" + keyword + ".jpg";
						File scrFile = ((TakesScreenshot) driver)
								.getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(scrFile,
								new File(System.getProperty("user.dir")
										+ "//screenshots//" + fileName));
					} catch (IOException e) {
						System.out.println("***ERR***");
						k.log("***ERR***");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String proceed = xls.getCellData("Test Steps",
							"Proceedd_On_Fail", rNum);
					if (proceed.equalsIgnoreCase("y")) {
						try {
							// Fail and Continue the Test
							Assert.fail(result);
						} catch (Throwable t) {
							k.log("***************ERROR*************");
							// listeners
							ErrorUtil.addVerificationFailure(t);
						}
					} else
						// Fail and Stop
						Assert.fail(result);
				}

				System.out.println(tcid + ".........." + keyword + "........."
						+ objectKey + "........." + dataColVal);
				k.log(tcid + ".........." + keyword + "........." + objectKey
						+ "........." + dataColVal);
			}
		}

	}


	
	
	/**
	 * Opens the Browser based upon the browserType
	 * 
	 * @param browserType
	 * @return Pass/Fail.
	 */

	private String openBrowser(String browserType) {
		k.log("Executing openBrowser");
		try {
			if (!isBrowserOpened) {
				if (CONFIG.getProperty("browserType").equals("Mozilla"))
					driver = new FirefoxDriver();
				else if (CONFIG.getProperty("browserType").equals("IE"))
					driver = new InternetExplorerDriver();
				else if (CONFIG.getProperty("browserType").equals("CHROME"))
					driver = new ChromeDriver();
				isBrowserOpened = true;

				driver.manage().window().maximize();
				final String waitTime = CONFIG
						.getProperty("default_implicitWait");
				driver.manage()
						.timeouts()
						.implicitlyWait(Long.parseLong(waitTime),
								TimeUnit.SECONDS);
			}
		} catch (Throwable e) {
			k.log("Unable to open the Browser");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		k.log("Pass");
		isBrowserOpened = true;
		return "Pass";
	}

	/**
	 * 
	 * Closes the Browser
	 * 
	 * @return Pass/Fail
	 */

	private String closeBrowser() {
		k.log("Executing closeBrowser");
		try {
			if (isBrowserOpened) {
				driver.quit();
				isBrowserOpened = false;
			}
		} catch (Throwable e) {
			k.log("Error while closing the Browser");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Clear the Fields
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 */
	private String clear(String identifier) {
		k.log("Executing navigate");
		try {
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.clear();
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).clear();
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier))).clear();
		} catch (Throwable e) {
			k.log("Unable to clear");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * navigate to the given URLKey
	 * 
	 * @param URLKey
	 * @return Pass/Fail
	 */
	public String navigate(String URLKey) {
		k.log("Executing navigate");
		try {
			driver.get(CONFIG.getProperty(URLKey));
		} catch (Throwable e) {
			k.log("Unable to navigate");
			ErrorUtil.addVerificationFailure(e);
			return "Fail - not able to navigate";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Clicks the identifier
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String click(String identifier) throws NoSuchElementException {
		k.log("Executing click");
		try {
			if (identifier.endsWith("_xpath")) {
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.click();
			} else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).click();
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier))).click();
			else if (identifier.endsWith("_linktext"))
				driver.findElement(By.linkText(OR.getProperty(identifier)))
						.click();
		} catch (Throwable e) {
			Assert.fail("Element not found - " + identifier);
			k.log("Element not found");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * input the data in the Fields
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String input(String identifier, String data)
			throws NoSuchElementException {
		k.log("Executing input");
		try {
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.sendKeys(data.trim());

			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).sendKeys(
						data.trim());

			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier)))
						.sendKeys(data.trim());

		} catch (Throwable t) {
			Assert.fail("Element not found - " + identifier);
			k.log("Fail");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Select the drop down based upon given data
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String selectDropdown(String identifier, String data)
			throws NoSuchElementException {
		k.log("Executing selectDropdown");
		try {
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(identifier))));
			select.selectByVisibleText(data);
		} catch (Throwable t) {
			// Assert.fail("Element not found - " + identifier);
			k.log("Element not found - " + identifier);
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}
	
	private String selectDropdownbyindex(String identifier, String data) 
		throws NoSuchElementException {
			k.log("Executing selectDropdown");
			try {
				Select select = new Select(driver.findElement(By.xpath(OR.getProperty(identifier))));
				select.selectByIndex(Integer.parseInt(data)-1);
			} catch (Throwable t) {
				// Assert.fail("Element not found - " + identifier);
				k.log("Element not found - " + identifier);
				ErrorUtil.addVerificationFailure(t);
				return "Fail";
			}
			k.log("Pass");
			return "Pass";
		}


	/**
	 * Superuser login
	 * 
	 * @return Pass/Fail
	 */
	private String loginbySuperUser() {
		k.log("Executing input");
		try {
			driver.findElement(By.xpath(OR.getProperty("loginusername_xpath")))
					.sendKeys(OR.getProperty("adminUsername"));
			driver.findElement(By.xpath(OR.getProperty("loginpassword_xpath")))
					.sendKeys(OR.getProperty("adminPassword"));
			driver.findElement(By.xpath(OR.getProperty("logincaptaha_xpath")))
					.sendKeys(OR.getProperty("Captcha"));
			driver.findElement(By.xpath(OR.getProperty("loginbutton_xpath")))
					.click();
		} catch (Throwable t) {
			k.log("Error Occured while logging in");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * inputs the data from OR.properties
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String inputfromObjectRepository(String identifier, String data)
			throws NoSuchElementException {
		k.log("Executing inputproperties");
		try {
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.sendKeys(OR.getProperty(data));
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).sendKeys(
						OR.getProperty(data));
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier)))
						.sendKeys(OR.getProperty(data));
		} catch (Throwable t) {
			k.log("Element not found - " + identifier);
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * inputs the data from config.properties
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String inputfromConfigurations(String identifier, String data)
			throws NoSuchElementException {
		k.log("Executing inputfromConfigurations");
		try {
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.sendKeys(CONFIG.getProperty(data));
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).sendKeys(
						CONFIG.getProperty(data));
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier)))
						.sendKeys(CONFIG.getProperty(data));
		} catch (Throwable t) {
			k.log("Element not found - " + identifier);
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * converts the exponentials numbers in to the integers
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 * @throws NumberFormatException
	 */
	private String inputconvertfromdecimal(String identifier, String data)
			throws NumberFormatException {
		k.log("Executing input");
		try {
			BigDecimal bd = new BigDecimal(data);
			System.out.println(bd.longValue());
			String converteddata = String.valueOf(bd.longValue());
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(OR.getProperty(identifier)))
						.sendKeys(converteddata);
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(OR.getProperty(identifier))).sendKeys(
						converteddata);
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(OR.getProperty(identifier)))
						.sendKeys(converteddata);
		} catch (Throwable t) {
			k.log("Element not found - " + identifier);
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * @param msg
	 */

	public void log(String msg) {
		APP_LOGS.debug(msg);
	}

	/**
	 * 
	 * @return Pass/Fail
	 */
	private String logout() {
		try {
			driver.findElement(By.cssSelector(OR.getProperty("sudrop")))
					.click();
			driver.findElement(By.linkText(OR.getProperty("logout"))).click();
		} catch (Throwable t) {
			k.log(t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * @return curentTime
	 */
	@SuppressWarnings("unused")
	private String currentTime() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String curentTime = sdf.format(cal.getTime());
		// System.out.println(curentTime);
		return curentTime;
	}

	/************************** Application dependent ****************************/

	/**
	 * 
	 * 
	 * @param mailName
	 * @return "Fail-Mail not found"
	 */
	private String checkMail(String mailName) {
		k.log("Executing checkMail");
		try {
			driver.findElement(By.linkText(mailName)).click();
		} catch (Throwable t) {
			k.log("Fail-Mail not found");
			ErrorUtil.addVerificationFailure(t);
			return "Fail-Mail not found";
		}
		k.log("Pass");
		return "Pass";
	}

	// ******Singleton function*******//
	/**
	 * 
	 * @return singleton Reference
	 */
	public static Keywords getKeywordsInstance() {
		if (k == null) {
			k = new Keywords();
			// System.out.println(xls);
		}

		return k;
	}

	/**
	 * Compares the Page Title with the given Title.
	 * 
	 * @param expectedVal
	 * @return Pass/Fail
	 */
	private String compareTitle(final String expectedVal) {
		k.log("Executing compareTitle Assertion");
		try {
			Assert.assertEquals(driver.getTitle(), OR.getProperty(expectedVal));
		} catch (final Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("Titles do not match");
			k.log("Actual Value is =" + driver.getTitle());
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * selects the Radio Button based on the Value
	 * 
	 * @param radiobuttonname
	 * @param Usrinput
	 * @return Pass/Fail
	 */
	private String selectRadioButton(String radiobuttonname, String Usrinput) {
		k.log("Executing selectRadioButton");
		try {
			getRadioButton(OR.getProperty(radiobuttonname));

			for (int i = 0; i < radiobuttons.size(); i++) {
				if (radiobuttons.get(i).getAttribute("value").equals(Usrinput))
					radiobuttons.get(i).click();
			}
		} catch (final Throwable t) {
			k.log("Radio Button does not found" + Usrinput);
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";

	}

	/**
	 * Load the List of the Radio buttons existing
	 * 
	 * @param radiobuttonname
	 * @return
	 */
	private List<WebElement> getRadioButton(String radiobuttonname) {
		k.log("Loading the Radio Buttons");
		try {
			radiobuttons = driver.findElements(By.name(radiobuttonname));
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("Error occured while loading Radio buttons");
			k.log(t.getMessage());
		}
		k.log("Radio Buttons are loded");
		return radiobuttons;
	}

	/**
	 * Load the List of the EmailID's in the WebTables
	 * 
	 * @param xpathkey
	 * @return list of Email ID's
	 */
	
	private List<WebElement> getYears(String xpathkey) {
		try {
			Years = driver.findElements(By.xpath(xpathkey));
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("Error occured while loading Email id's in Webtable");
			k.log(t.getMessage());
		}
		k.log("Pass");
		// System.out.println(emails);
		return Years;

	}
	
	
	
	/**
	 * Load the List of the Status in the WebTables
	 * 
	 * @param xpathkey
	 * @return return list of Status
	 */
	
	private List<WebElement> getLevels(String xpathkey) {
		try {
			Levels = driver.findElements(By.xpath(xpathkey));
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("Error occured while loading Statuses in Webtable");
			k.log(t.getMessage());
		}
		k.log("Pass");
		return Levels;
	}
    

 private String VerifyReport(String year, String month, String date,String Entry ) {
		try {
			boolean isFound = True;
			
			getYears(OR.getProperty("YearsList"));
			getLevels(OR.getProperty("LevelsList"));
			 for(int i=0;i<Years.size();i++){
				 System.out.println(year +"-"+ month +"-"+ date);
				 System.out.println(Years.get(i).getText());
				 System.out.println(Levels.get(i).getText());
					if(Years.get(i).getText().equalsIgnoreCase(year +"-"+ month +"-"+ date) && Levels.get(i).getText().equalsIgnoreCase(Entry)){
						Assert.assertTrue(2>1);
						break;}
		}
			 
		//System.out.println("Does not found the added date in List page.");
		//Assert.assertFalse(isFound, "Does not found the added date in List page.");
		
		}catch (Throwable t) {
			k.log(t.getMessage());
			k.log("Unable to find the EmailID");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		return "Pass";
		
		
	}
	
	/**
	 * Search the Email ID given in the Search Box.
	 * 
	 * @param email
	 * @return Pass/Fail
	 */
	private String searchEmail(String email) {
		try {
			driver.findElement(By.xpath("//*[@id='emailId']")).sendKeys(email);
			driver.findElement(By.xpath("//*[@id='searchEmailId']")).click();

		} catch (Throwable t) {
			k.log(t.getMessage());
			k.log("Unable to find the EmailID");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		// System.out.println(emails);
		return "Pass";

	}

	/**
	 * Connects with the Database
	 * 
	 * @return Pass/Fail
	 */
	public String dbconnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("MySQL JDBC Driver Registered!");
			connection = DriverManager.getConnection(
					"jdbc:mysql://172.30.65.30:3306/MI_DB", "testing",
					"Tarang01");
			if (connection != null) {
				System.out
						.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (ClassNotFoundException e) {
			// System.out.println("Could not find the database driver "+
			// e.getMessage());
			k.log("Could not find the database driver " + e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		} catch (SQLException e) {
			System.out.println("Could not connect to the database "
					+ e.getMessage());
			k.log("Could not connect to the database " + e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		return "Pass";
	}

	/**
	 * 
	 * @return Pass/Fail
	 */
	public String closeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (connection != null) {
				connection.close();
			}
		} catch (ClassNotFoundException e) {
			// System.out.println("Could not find the database driver "+
			// e.getMessage());
			k.log("Could not find the database driver " + e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		} catch (SQLException e) {
			// System.out.println("Could not connect to the database "+
			// e.getMessage());
			k.log("Could not connect to the database " + e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		return "Pass";
	}

	/**
	 * Update the Password column in the Database
	 * 
	 * @param sql
	 * @param email
	 * @return Pass/Fail
	 */
	public String updatePasswordthroughDb(String sql, String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("MySQL JDBC Driver Registered!");
			dbconnection();
			PreparedStatement ps = connection.prepareStatement(SQL
					.getProperty(sql));
			ps.setString(1, email);
			ps.execute();
			System.out.println("Merchant Admin got created");
			closeConnection();
		} catch (ClassNotFoundException e) {
			System.out.println("Could not updatePassword " + e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		} catch (SQLException e) {
			System.out.println("Could not connect to the database "
					+ e.getMessage());
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		return "Pass";
	}

	/*
	 *  ***************************************Assertion
	 * functions**************************************
	 */

	/**
	 * Verifies the Success Message matches with the given Message
	 * 
	 * @param data
	 * @return Pass/Fail
	 */
	private String verifySuccessMessage(String data) {
		k.log("Executing verifySuccessMessage");
		try {
			// System.out.println(driver.findElement(By.tagName("body")).getText());
			// System.out.println(data);
			Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
					.contains(OR.getProperty(data)));
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("Fail");
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Verifies the Validation Message matches with the given Message
	 * 
	 * @param data
	 * @return Pass/Fail
	 */
	private String verifyValidationMessages(String data) {
		k.log("Executing verifyValidationMessages");
		try {
			// System.out.println(driver.findElement(By.tagName("body")).getText());
			Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
					.contains(data));
		} catch (Throwable t) {
			k.log("Fail");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/*
	 * public String assertTrue(String data){
	 * APP_LOGS.debug("Executing verifyText"); try{
	 * Assert.assertTrue(driver.findElement
	 * (By.xpath(OR.getProperty(data))).getText().contains(s)); }
	 * catch(Exception e){ k.log("Fail"); } k.log("Pass"); return "Pass"; }
	 */

	/**
	 * Returns True if the data matches with the given Text else False
	 * 
	 * @param identifier
	 * @param data
	 * @return Pass/Fail
	 */
	private String assertEquals(String identifier, String data) {
		k.log("Executing assertEquals");
		try {

			if (identifier.endsWith("_xpath"))
				Assert.assertEquals(
						driver.findElement(By.xpath(OR.getProperty(identifier)))
								.getText(), OR.getProperty(data));
			else if (identifier.endsWith("_id"))
				Assert.assertEquals(
						driver.findElement(By.id(OR.getProperty(identifier)))
								.getText(), OR.getProperty(data));
			else if (identifier.endsWith("_name"))
				Assert.assertEquals(
						driver.findElement(By.name(OR.getProperty(identifier)))
								.getText(), OR.getProperty(data));
			else if (identifier.endsWith("_css"))
				Assert.assertEquals(
						driver.findElement(By.cssSelector(OR.getProperty(identifier))).getText(), OR.getProperty(data));
		} catch (Throwable t) {
			k.log("Fail");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Returns True if the Attribute Value matches with the given data else
	 * False
	 * 
	 * @param identifier
	 * @param data
	 * @return
	 */
	private String assertEquals_Attribute(String identifier, String data) {
		k.log("Executing assertEquals_Attribute");
		try {
			if (identifier.endsWith("_xpath"))
				Assert.assertEquals(
						driver.findElement(By.xpath(OR.getProperty(identifier)))
								.getAttribute("value"), data);
			else if (identifier.endsWith("_id"))
				Assert.assertEquals(
						driver.findElement(By.id(OR.getProperty(identifier)))
								.getAttribute("value"), data);
			else if (identifier.endsWith("_name"))
				Assert.assertEquals(
						driver.findElement(By.name(OR.getProperty(identifier)))
								.getAttribute("value"), data);
		} catch (Throwable t) {
			k.log("Fail");
			ErrorUtil.addVerificationFailure(t);
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Verifies whether the Element Present
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 */
	private String isElementPresence(String identifier) {
		k.log("Executing isElementPresence");
		int count = 0;
		try {

			if (identifier.endsWith("_xpath"))
				count = driver.findElements(
						By.xpath(OR.getProperty(identifier))).size();
			else if (identifier.endsWith("_id"))
				count = driver.findElements(By.id(OR.getProperty(identifier)))
						.size();
			else if (identifier.endsWith("_name"))
				count = driver
						.findElements(By.name(OR.getProperty(identifier)))
						.size();
			else if (identifier.endsWith("_css"))
				count = driver.findElements(
						By.cssSelector(OR.getProperty(identifier))).size();

			Assert.assertTrue(count > 0, "No element present");
		} catch (final Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log("No element present");
			return "false";
		}
		return "Pass";
	}

	/**
	 * Verifies whether the Checkbox is selected
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 */
	private String isElementSelected(String identifier) {
		k.log("Executing isElementSelected");

		try {
			System.out.println(OR.getProperty("identifier"));
			if (identifier.endsWith("_xpath"))
				Assert.assertTrue(driver.findElement(
						By.xpath("//*[@id='isMoneyCollectionAgent']"))
						.isSelected());
			else if (identifier.endsWith("_id"))
				Assert.assertTrue(driver.findElement(
						By.id(OR.getProperty("identifier"))).isSelected());
			else if (identifier.endsWith("_name"))
				Assert.assertTrue(driver.findElement(
						By.name(OR.getProperty("identifier"))).isSelected());
			else if (identifier.endsWith("_css"))
				Assert.assertTrue(driver.findElement(
						By.xpath(OR.getProperty("identifier"))).isSelected());
		} catch (StaleElementReferenceException e) {
			// ErrorUtil.addVerificationFailure(t);
			System.out.println(e.getMessage());
			k.log("No element Selected");
			return "false";
		}
		return "Pass";
	}

	/**
	 * Accept the Alerts
	 */
	private String acceptAlert() {
		k.log("Executing acceptAlert");

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (final Throwable t) {
			k.log("Unable to acccept Alert");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		return "Pass";
	}

	/**
	 * Verify whether the Check box is disabled
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String isElementDisabled(String identifier)
			throws NoSuchElementException {
		k.log("Executing isElementDisabled");

		try {
			System.out.println(OR.getProperty("identifier"));
			if (identifier.endsWith("_xpath"))
				Assert.assertFalse(
						driver.findElement(By.xpath(OR.getProperty(identifier)))
								.isEnabled(), "Element is enabled");
			else if (identifier.endsWith("_id"))
				Assert.assertFalse(
						driver.findElement(By.id(OR.getProperty(identifier)))
								.isEnabled(), "Element is enabled");
			else if (identifier.endsWith("_name"))
				Assert.assertFalse(
						driver.findElement(By.name(OR.getProperty(identifier)))
								.isEnabled(), "Element is enabled");
			else if (identifier.endsWith("_css"))
				Assert.assertFalse(
						driver.findElement(
								By.cssSelector(OR.getProperty(identifier)))
								.isEnabled(), "Element is enabled");
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log(identifier + "Element is enabled");
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * Verify whether the check box or Radio Button is readable
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	private String isElementReadonly(String identifier)
			throws NoSuchElementException {
		k.log("Executing isElementReadonly");

		try {
			System.out.println(OR.getProperty("identifier"));
			if (identifier.endsWith("_xpath"))
				Assert.assertNotNull(driver.findElement(
						By.xpath(OR.getProperty(identifier))).getAttribute(
						"readonly"));
			else if (identifier.endsWith("_id"))
				Assert.assertNotNull(driver.findElement(
						By.id(OR.getProperty(identifier))).getAttribute(
						"readonly"));
			else if (identifier.endsWith("_name"))
				Assert.assertNotNull(driver.findElement(
						By.name(OR.getProperty(identifier))).getAttribute(
						"readonly"));
			else if (identifier.endsWith("_css"))
				Assert.assertNotNull(driver.findElement(
						By.xpath(OR.getProperty(identifier))).getAttribute(
						"readonly"));
		} catch (Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			k.log(identifier + "Element is not in Read only mode");
			return "Fail";
		}
		k.log("Pass");
		return "Pass";
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public String datepicker(String dateTime) {
		try {
			driver.findElement(By.cssSelector(".textbox-icon.combo-arrow"))
					.click();
			// button to move next in calendar

			WebElement nextLink = driver.findElement(By
					.xpath("html/body/div[3]/div/div[1]/div/div[1]/div[4]"));
			// button to click in center of calendar header
			// WebElement midLink =
			// driver.findElement(By.xpath("//div[@id='datetimepicker_dateview']//div[@class='k-header']//a[contains(@class,'k-nav-fast')]"));
			// button to move previous month in calendar
			WebElement previousLink = driver.findElement(By
					.xpath("html/body/div[3]/div/div[1]/div/div[1]/div[3]"));
			// Split the date time to get only the date part
			String date_dd_MM_yyyy[] = (dateTime.split(" "));
			// get the year difference between current year and year to set in
			// calander
			int yearDiff = Integer.parseInt(date_dd_MM_yyyy[2])
					- Calendar.getInstance().get(Calendar.YEAR);
			// midLink.click();
			if (yearDiff != 0) {
				// if you have to move next year
				if (yearDiff > 0) {
					for (int i = 0; i < yearDiff; i++) {
						System.out.println("Year Diff->" + i);
						nextLink.click();
					}
				}
				// if you have to move previous year
				else if (yearDiff < 0) {
					for (int i = 0; i < (yearDiff * (-1)); i++) {
						System.out.println("Year Diff->" + i);
						previousLink.click();
					}
				}
			}
			Thread.sleep(1000);
			// Get all months from calendar to select correct one
			driver.findElement(
					By.xpath("html/body/div[3]/div/div[1]/div/div[1]/div[5]/span"))
					.click();
			List<WebElement> list_AllMonthToBook = driver
					.findElements(By
							.xpath("html/body/div[3]/div/div[1]/div/div[2]/div/div[2]/table/tbody/tr/td"));
			list_AllMonthToBook.get(Integer.parseInt(date_dd_MM_yyyy[1]) - 1)
					.click();
			Thread.sleep(1000);

			// get all dates from calendar to select correct one
			List<WebElement> list_AllDateToBook = driver
					.findElements(By
							.xpath("html/body/div[3]/div/div[1]/div/div[2]/table/tbody/tr/td"));
			ArrayList<String> EmailIDs1 = new ArrayList<String>();
			for (WebElement webElement : list_AllDateToBook) {
				EmailIDs1.add(webElement.getText());
			}
			// System.out.println(EmailIDs1);
			int index1 = EmailIDs1.indexOf("1");
			// int index2= EmailIDs1.indexOf("31");
			for (int i = index1; i < EmailIDs1.size(); i++) {
				if (list_AllDateToBook.get(i).getText()
						.equalsIgnoreCase(date_dd_MM_yyyy[0])) {
					// System.out.println(list_AllDateToBook.get(i).getText());
					list_AllDateToBook.get(i).click();
				}
			}
		} catch (StaleElementReferenceException | InterruptedException
				| NumberFormatException t) {
			k.log("Unable to select the Start date-------------");
			ErrorUtil.addVerificationFailure(t);
			return "Fail";
		}
		k.log("Pass");
		return "Pass";

	}

}
