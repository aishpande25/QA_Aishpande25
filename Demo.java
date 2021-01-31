package aishwarrypande.ExtentReports;



import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

public class Demo {
	
	ExtentReports extent;
	com.relevantcodes.extentreports.ExtentTest logger;
	
	
	@BeforeTest
	public void startReport(){
		
		extent = new ExtentReports (System.getProperty("user.dir") +"/test-output/STMExtentReport.html", true);
		//extent.addSystemInfo("Environment","Environment Name")
		extent.addSystemInfo("Host Name", "SoftwareTesting").addSystemInfo("Environment", "Automation Testing").addSystemInfo("User Name", "Aishwarrya Pande");
                
		extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
	}
	@Test
	public void TestShopDemo() throws Throwable { 
		
	System.setProperty("webdriver.chrome.driver", "D:\\ChromeDriver\\chromedriver.exe");  
	WebDriver driver=new ChromeDriver();
	JavascriptExecutor js = (JavascriptExecutor) driver;  
	  
	driver.get("https://shopee.sg");
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	logger = extent.startTest("passTest");
	Assert.assertTrue(true);
	WebElement sign_in_cross = driver.findElement(By.xpath("//*[@class='shopee-popup__close-btn']"));
	sign_in_cross.click();	
	driver.manage().window().maximize();
	driver.findElement(By.xpath("//*[@class='shopee-searchbar-input__input']")).sendKeys("Toy" +Keys.ENTER);;
	String search_text = driver.findElement(By.xpath("//*[@class='shopee-search-user-brief__header-text-highlight']")).getText();
	String expected_Text= "toy";
	
	if(search_text.equals(expected_Text))
	{
		System.out.println("Search Result is Correct");
	}
	else
	{
		System.out.println("Search Result is Incorrect");
		driver.quit();
	}

	List<WebElement> list_of_price = driver.findElements(By.xpath("//*[@class='_1w9jLI _1DGuEV _7uLl65']"));
	List<WebElement> list_of_product_name= driver.findElements(By.xpath("//*[@class='_1NoI8_ A6gE1J _1co5xN']"));
	
	
	//Use of HashMap to store Products and Their prices(after conversion to Integer)
	String product_price;
	float float_product_price=0.0f;
	String product_name;
	
	HashMap<Float, String> map_final_products = new HashMap<Float,String>();
	System.out.println("length_of_map" +list_of_price.size());
	System.out.println("len_of_mmmap" +list_of_product_name.size());

	
	for(int i=0;i<list_of_price.size();i++) 
	{
		
		System.out.println("lkskjfdsjkf" +i);
		if(list_of_price.get(i).getText().contains("-"))
		{
			String array[] = list_of_price.get(i).getText().split("-");
			String abc = array[1];
			product_price= abc.replace("$", "" ) + "f";
		
			
		}
		else
		{
			product_price=list_of_price.get(i).getText().replace("$", "") +"f";
		}
		
				//replace("$", "");//Iterate and fetch product price
		System.out.println(product_price);
		if(list_of_product_name.get(i) != null)
		{
			product_name = list_of_product_name.get(i).getText();//Iterate and fetch product name
			System.out.println(product_name);
			float_product_price= Float.parseFloat(product_price); 
			map_final_products.put(float_product_price,product_name);//Add product and price in HashMap 
		}
		
		
	}
	
	Reporter.log("Product Name and price fetched from UI and saved in HashMap as:" + map_final_products.toString() + "<br>",true);
	
	Set<Float> allkeys = map_final_products.keySet();
	ArrayList<Float> array_list_values_product_prices = new ArrayList<Float>(allkeys);

	//Sort the Prices in Array List using Collections class
	//this will sort in ascending order lowest at the top and highest at the bottom
	Collections.sort(array_list_values_product_prices);

	//Highest Product is
	Float high_price = array_list_values_product_prices.get(array_list_values_product_prices.size()-1);

	//Low price is
	Float low_price = array_list_values_product_prices.get(0);

	Reporter.log("High Product Price is: " + high_price + " Product name is: " + map_final_products.get(high_price),true);
	Reporter.log("Low Product Price is: " + low_price + " Product name is: " + map_final_products.get(low_price),true);
	
	driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[3]/div[2]/div/div[2]/div[1]/a/div/div/div[2]/div[1]/div[1]")).click();
	
	String toy_name = driver.findElement(By.xpath("//*[@class='_3ZV7fL']")).getText();
	System.out.println("Toy Name:" +toy_name);
	
	String toy_rating_sold_details=driver.findElement(By.xpath("//*[@class='flex _1aTY_T']")).getText();
	System.out.println("Rating,Selling Details of Toy: " +toy_rating_sold_details);
	
	driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/div[2]/div[2]/div[3]/div/div[5]/div/div/button[1]")).click();
	System.out.println(" Dear User, You Will need to Signup To Add item In Your Cart");
	driver.quit();
	logger.log(LogStatus.PASS, "Test Case Passed is passTest");
	}
	
	
	@AfterTest
	public void endReport(){
	extent.flush();
    extent.close();
	
	}

}
