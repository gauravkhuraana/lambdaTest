package org.lambdaTest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class testScenarios {

    private RemoteWebDriver driver;
    private String Status = "failed";
    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "YOURUSERIDHERE" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "YOURPASSWORDHERE" : System.getenv("LT_ACCESS_KEY");
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        // Configure your capabilities here
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("version", "92.0");
        caps.setCapability("resolution", "1920x1080");
        caps.setCapability("build", "Lambda Test Contest with RTP");
        caps.setCapability("name", m.getName() + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");

        String[] Tags = new String[] { "Feature", "Magicleap", "Severe" };
        caps.setCapability("tags", Tags);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);
    }

    String URL = "https://www.lambdatest.com/selenium-playground";

    @Test
    public void testScenario1ForEnteringTextAndVerify()
    {
        System.out.println("Starting the scenario for entering text");

        driver.get(URL);
        driver.findElement(By.xpath("//a[normalize-space()='Simple Form Demo']")).click();
        assert driver.getCurrentUrl().contains("simple-form-demo")==true;

        String text="Welcome to LambdaTest";
        driver.findElement(By.xpath("//input[@id='user-message']")).sendKeys(text);
        driver.findElement(By.xpath("//button[@id='showInput']")).click();

        assert driver.findElement(By.xpath("//p[@id='message']")).getText().contains(text);
        System.out.println("Able to enter the text "+ text+ " succesfully. Also validated the text in URL");
        Status = "passed";
    }

    @Test
    public void testScenario2ForDragAndDrop() throws InterruptedException {

        System.out.println("Starting the scenario for moving the slider");
        driver.get(URL);
        driver.findElement(By.xpath("//h2[contains(text(),'Progress Bars & Sliders')]/following-sibling::ul//a[normalize-space()='Drag & Drop Sliders']")).click();
        WebElement slider = driver.findElement(By.xpath("//h4[normalize-space()='Default value 15']/following-sibling::div"));
        // Move the slider
        new Actions(driver).dragAndDropBy(slider, 100, 0).perform();

        assert driver.findElement(By.xpath("//output[@id='rangeSuccess']")).getText().equals("95");
        System.out.println("Able to move the slider succesfully.  Also checked the value got updated accordingly ");

        Status = "passed";
    }


    @Test
    public void testScenario3InputFormSubmit() throws InterruptedException {

        System.out.println("Starting the scenario to fill the form");
        driver.get(URL);
        driver.findElement(By.xpath("//h2[normalize-space()='Input Forms']//following::a[normalize-space()='Input Form Submit']")).click();

        WebElement submit = driver.findElement(By.xpath("//div[@class='container']//button[@type='submit']"));
        submit.click();
        String text = driver.findElement(By.xpath("//input[@id='name']")).getAttribute("validationMessage");
        assert text.equals("Please fill out this field.");
        System.out.println("Validation message checked successfully for name field");

        driver.findElement(By.xpath("//input[@id='name']")).sendKeys("Gaurav");
        driver.findElement(By.xpath("//input[@id='inputEmail4']")).sendKeys("gaurav.test@gmail.com");
        driver.findElement(By.xpath("//input[@id='inputPassword4']")).sendKeys("test#@##$#");
        driver.findElement(By.xpath("//input[@id='company']")).sendKeys("Microsoft");
        driver.findElement(By.xpath("//input[@id='websitename']")).sendKeys("https://udzial.com");
        driver.findElement(By.xpath("//input[@id='name']")).sendKeys("Gaurav");

        Select country = new Select(driver.findElement(By.xpath("//select[@name='country']")));
        driver.findElement(By.xpath("//select[@name='country']")).click();
        country.selectByValue("US");

        driver.findElement(By.xpath("//input[@id='inputCity']")).sendKeys("Kingston");
        driver.findElement(By.xpath("//input[@id='inputAddress1']")).sendKeys("132");
        driver.findElement(By.xpath("//input[@id='inputAddress2']")).sendKeys("My Street");
        driver.findElement(By.xpath("//input[@id='inputState']")).sendKeys("New York");
        driver.findElement(By.xpath("//input[@id='inputZip']")).sendKeys("12401");

        submit.click();

        assert driver.findElement(By.xpath("//p[@class='success-msg hidden']")).getText()
                .equals("Thanks for contacting us, we will get back to you shortly.");
        System.out.println("Able to fill the form successfully and validate the success message with all inputs variety which included the country dropdown");
        Status = "passed";
    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }
}
