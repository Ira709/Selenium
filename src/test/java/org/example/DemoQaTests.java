package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class DemoQaTests {
    private WebDriver driver;

    @BeforeMethod
    public void configureDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/trojan4ik/Downloads/chromedriver-mac-x64/chromedriver");

        this.driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        driver.manage().window().maximize();
    }

    @Test
    public void loginUser() {
        final String userName = "v3";
        final String password = "P@ssW0rd";

        driver.get("https://demoqa.com/login");

        WebElement userNameInput = driver.findElement(By.id("userName"));
        userNameInput.sendKeys(userName);

        WebElement passwordInput = driver.findElement(By.cssSelector("#password"));
        passwordInput.sendKeys(password);

        scrollToBottomUsing(driver);

        WebElement loginButton = driver.findElement(By.xpath("//*[@id='login']"));
        loginButton.click();

        WebElement userNameValue = driver.findElement(By.id("userName-value"));
        Assert.assertEquals(userNameValue.getText(), userName, "Некорректное значение UserName обнаружено на странице после логина");

        List<WebElement> possibleLogOutButtons = driver.findElements(By.xpath("//*[text()='Log out']"));
        Assert.assertFalse(possibleLogOutButtons.isEmpty(), "Log Out button was not found on the page");
    }

    @Test
    public void loginUser_useNonExistentUserName() {

        final String userName34 = UUID.randomUUID().toString();
        final String password1 = "docker";

        driver.get("https://demoqa.com/login");

        WebElement userNameInput = driver.findElement(By.id("UUID.randomUUID().toString();"));
        userNameInput.sendKeys(UUID.randomUUID().toString());

        WebElement passwordInput = driver.findElement(By.cssSelector("#password1"));
        passwordInput.sendKeys(password1);

        scrollToBottomUsing(driver);

        WebElement loginButton = driver.findElement(By.xpath("//*[@id='login']"));
        loginButton.click();

        WebElement userNameValue = driver.findElement(By.id("userName-value"));
        Assert.assertEquals(userNameValue.getText(), userName34 , "Invalid username or password!");
    }

    @Test
    public void loginUser_incorrectPassword() {

        final String userName = "v3";
        final String password4 = "docker45";

        driver.get("https://demoqa.com/login");

        WebElement userNameInput = driver.findElement(By.id("userName"));
        userNameInput.sendKeys(userName);

        WebElement passwordInput = driver.findElement(By.cssSelector("#password1"));
        passwordInput.sendKeys(password4);

        scrollToBottomUsing(driver);

        WebElement loginButton = driver.findElement(By.xpath("//*[@id='login']"));
        loginButton.click();

        WebElement userNameValue = driver.findElement(By.id("userName-value"));
        Assert.assertEquals(userNameValue.getText(), password4, "Invalid username or password!");
    }

    @Test
    public void createNewUser() {
        final String firstName = UUID.randomUUID().toString();
        final String lastName = UUID.randomUUID().toString();
        final String userName = UUID.randomUUID().toString();
        final String password = "P@ssw0rd";

        driver.get("https://demoqa.com/register");

        WebElement registrationHeader = driver.findElement(By.tagName("h4"));
        Assert.assertEquals(registrationHeader.getText(), "Register to Book Store", "Couldn't access register page");

        WebElement firstNameInput = driver.findElement(By.id("firstname"));
        firstNameInput.sendKeys(firstName);

        WebElement lastNameInput = driver.findElement(By.cssSelector("#lastname"));
        lastNameInput.sendKeys(lastName);

        WebElement userNameInput = driver.findElement(By.xpath("//input[@id='userName']"));
        userNameInput.sendKeys(userName);

        WebElement passwordInput = driver.findElements(By.cssSelector(".mr-sm-2.form-control")).get(3);
        passwordInput.sendKeys(password);

        scrollToBottomUsing(driver);

        WebElement recaptchaIframe = driver.findElement(By.cssSelector("#g-recaptcha iframe"));
        driver.switchTo().frame(recaptchaIframe);

        WebElement captchaCheckbox = driver.findElement(By.id("recaptcha-anchor"));
        captchaCheckbox.click();

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.attributeToBe(captchaCheckbox, "aria-checked", "true"));

        driver = driver.switchTo().defaultContent();

        WebElement registerButton = driver.findElement(By.id("register"));
        registerButton.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        Assert.assertEquals(alert.getText(), "User Register Successfully.", "Алерт содержит некорректное сообщение об успешной регистрации");

        alert.accept();
    }

    @AfterMethod
    public void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static void scrollToBottomUsing(WebDriver webDriver) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}
