import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.concurrent.TimeUnit;


import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class UiTestClient {
    private String pathToChromeDriver = "/Users/anton/Downloads/chromedriver";

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://hms.anzuev.ru:8888/";

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", this.pathToChromeDriver);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/Users/anton/Library/Application Support/Google/Chrome/Default");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, 10);
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));
    }

    @After
    public void stop() throws Exception {
        driver.quit();
        driver = null;
    }


    @Test
    public void checkLeftMenuVisibility() throws Exception {

        // check that menu is hidden
        if(driver.findElement(By.id("unAuthMenu")).isDisplayed()){
            throw new Exception("Menu should be hidden in the very beginning");
        }

        // click the button
        driver.findElement(By.className("navbar-toggler")).click();

        wait.until(presenceOfElementLocated(By.id("unAuthMenu")));

        if(!driver.findElement(By.id("unAuthMenu")).isDisplayed()){
            throw new Exception("Menu should be displayed after button click");
        }

        driver.findElement(By.className("navbar-toggler")).click();
        Thread.sleep(1000);
        if(driver.findElement(By.id("unAuthMenu")).isDisplayed()){
            throw new Exception("Menu should be hidden after closing it");
        }
    }


    @Test
    public void checkPromotionPageVisibility() throws Exception {
        if(!driver.findElement(By.className("promotionPageDescription")).isDisplayed()){
            throw new Exception("PromotionPageDescription should be displayed in the very beginning");
        }
    }

    @Test
    public void clickLetsStartButton() throws Exception{
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));
        wait.until(presenceOfElementLocated(By.cssSelector(".promotionPageInitSearchButton button")));
        Thread.sleep(4000);

        driver.findElement(By.cssSelector(".promotionPageInitSearchButton button")).click();
        if(driver.findElement(By.className("promotionPageDescription")).isDisplayed()){
            throw new Exception("PromotionPageDescription should " +
                    "be hidden after 'let's' start button has been clicked");
        }
        if(!driver.findElement(By.className("RoomSearchPage")).isDisplayed()){
            throw new Exception("RoomSearchPage should " +
                    "be shown after 'let's' start button has been clicked");
        }
    }

    @Test
    public void signIn() throws Exception {
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        driver.findElement(By.className("navbar-toggler")).click();
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        for (WebElement a : l){
            if(a.getText().equals("Sign in")){
                a.click();
                break;
            }
        }
        driver.findElement(By.id("login-form__mail")).sendKeys("user1@web.com");
        driver.findElement(By.id("login-form__password")).sendKeys("adminadmin");
        driver.findElement(By.cssSelector(".loginForm button")).click();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        if(!driver.findElement(By.id("profileBlock__ordersBlock")).isDisplayed()){
            throw new Exception("profileBlock__ordersBlock should be displayed after signing in");
        }
    }

    @Test
    public void signUp() throws Exception {
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        driver.findElement(By.className("navbar-toggler")).click();
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        for (WebElement a : l){
            if(a.getText().equals("Sign up")){
                a.click();
                break;
            }
        }
        driver.findElement(By.id("signUpForm__firstName")).sendKeys("Selenium");
        driver.findElement(By.id("signUpForm__lastName")).sendKeys("The");
        driver.findElement(By.id("signUpForm__fatherName")).sendKeys("Best");
        driver.findElement(By.id("signUpForm__phoneNumber")).sendKeys("89991234567");
        driver.findElement(By.id("signUpForm__mail"))
                .sendKeys("userSome" + (Math.random() * 1000 + 1) + "@web.com");

        driver.findElement(By.id("signUpForm__password")).sendKeys("adminadmin");

        driver.findElement(By.cssSelector(".signUpForm button")).click();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        if(!driver.findElement(By.id("profileBlock__ordersBlock")).isDisplayed()){
            throw new Exception("profileBlock__ordersBlock should be displayed after signing in");
        }
        this.logout();
    }


    @Test
    public void logout() throws Exception{
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        this.signIn();
        driver.findElement(By.className("navbar-toggler")).click();
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        for (WebElement a : l){
            if(a.getText().equals("Logout")){
                a.click();
                break;
            }
        }
        wait.until(presenceOfElementLocated(By.className("promotionPageDescription")));
    }

    @Test
    public void editProfile() throws Exception{
        this.signIn();
        for (WebElement a: driver.findElements(By.cssSelector(".profileBlock__tabControl p")) ){
            if(a.getText().equals("Profile")){
                a.click();
                break;
            }
        }

        driver.findElement(By.cssSelector("#profileBlock button")).click();
        String firstName = driver.findElement(By.id("profileBlock__firstNameInput")).getText();
        String lastName = driver.findElement(By.id("profileBlock__lastNameInput")).getText();
        String fatherName = driver.findElement(By.id("profileBlock__fatherNameInput")).getText();
        String password = "adminadmin";

        driver.findElement(By.id("profileBlock__firstNameInput")).sendKeys("1");
        driver.findElement(By.id("profileBlock__lastNameInput")).sendKeys("1");
        driver.findElement(By.id("profileBlock__fatherNameInput")).sendKeys("1");
        driver.findElement(By.id("profileBlock__passwordInput")).sendKeys(password);

        driver.findElement(By.cssSelector("#profileBlock button")).click();

        wait.until(textToBe(By.cssSelector("#profileBlock button"), "Edit"));


        List<WebElement> l = driver.findElements(By.cssSelector(".profileBlock__line"));

        if(l.get(0).findElements(By.tagName("span")).get(1).getText().equals(firstName + '1')){
            throw new Exception("firstName should be updated");
        }
        if(l.get(1).findElements(By.tagName("span")).get(1).getText().equals(lastName + '1')){
            throw new Exception("lastName should be updated");
        }
        if(l.get(2).findElements(By.tagName("span")).get(1).getText().equals(fatherName + '1')){
            throw new Exception("fatherName should be updated");
        }


    }


    @Test
    public void createOrder() throws Exception{
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        this.signIn();

        Thread.sleep(2000);
        driver.findElement(By.className("navbar-toggler")).click();
        Thread.sleep(3000);
        for (WebElement a : driver.findElements(By.cssSelector("#unAuthMenu a"))){
            if(a.getText().equals("Hotels")){
                a.click();
                break;
            }
        }
        Thread.sleep(2000);

        wait.until(presenceOfElementLocated(By.className("HotelItem")));

        Thread.sleep(1000);

        driver.findElement(By.cssSelector(".HotelItem button")).click();

        wait.until(presenceOfElementLocated(By.className("RoomPage")));

        driver.findElement(By.cssSelector(".RoomPage button")).click();

        wait.until(presenceOfElementLocated(By.className("RoomSearchForm")));

        Thread.sleep(1000);

        driver.findElement(By.cssSelector(".RoomSearchForm button")).click();


        wait.until(presenceOfElementLocated(By.className("RoomSearchResultBlock")));

        driver.findElement(By.cssSelector(".RoomSearchBlockItem button")).click();

        wait.until(presenceOfElementLocated(By.id("profileBlock__ordersBlock")));
    }


    public void createTwoOrders() throws Exception{
        this.createOrder();
        this.createOrder();
    }


    @Test
    public void cancelOrder() throws Exception{
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        this.signIn();
        List<WebElement> orders = driver.findElements(By.cssSelector(".profileBlock__ordersBlock__orderItem button"));
        orders.get(0).click();

    }


    @Test
    public void getListOfHotels() throws Exception{
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        wait.until(titleIs("Booking system"));

        this.signIn();

        Thread.sleep(2000);
        driver.findElement(By.className("navbar-toggler")).click();
        Thread.sleep(1000);
        for (WebElement a : driver.findElements(By.cssSelector("#unAuthMenu a"))){
            if(a.getText().equals("Hotels")){
                a.click();
                break;
            }
        }
        Thread.sleep(1000);
        wait.until(presenceOfElementLocated(By.className("HotelItem")));
    }


    @Test
    public void checkBackToHotelPageButton() throws Exception{
        this.getListOfHotels();

        driver.findElement(By.cssSelector(".HotelItem button")).click();

        wait.until(presenceOfElementLocated(By.className("RoomPage")));
        driver.findElement(By.className("RoomPage__backToHotelPageButton")).click();
        Thread.sleep(2000);
        wait.until(presenceOfElementLocated(By.className("HotelItem")));
    }
}