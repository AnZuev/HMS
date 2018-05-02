import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class UiTestManager {
    private String pathToChromeDriver = "/Users/anton/Downloads/chromedriver";

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://hms.anzuev.ru:8888/Employee/";



    @Before
    public void start() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", this.pathToChromeDriver);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/Users/anton/Library/Application Support/Google/Chrome/Default");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, 10);
        driver.get(baseUrl);
        Thread.sleep(1000);
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        driver.get(baseUrl);
        Thread.sleep(1000);

    }

    @After
    public void stop() throws Exception {
        driver.quit();
        driver = null;
    }


    @Step
    public void openLeftMenu() throws Exception {
        // click the button
        driver.findElement(By.className("navbar-toggler")).click();

        wait.until(presenceOfElementLocated(By.id("unAuthMenu")));
        Thread.sleep(1000);
    }

    @Step
    public void closeLeftMenu() throws Exception{
        driver.findElement(By.className("navbar-toggler")).click();
        Thread.sleep(1000);
    }

    @Step
    public void clickOnMyHotelMenuItem(){
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        l.get(1).click();
    }

    @Step
    public void clickOnOrdersMenuItem(){
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        l.get(2).click();
    }

    @Step
    public void clickOnRoomsMenuItem(){
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        l.get(3).click();
    }

    @Step
    public void clickOnRoomTypesMenuItem(){
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        l.get(4).click();
    }

    @Step
    public void clickOnLogoutMenuItem() throws InterruptedException {
        List<WebElement> l = driver.findElements(By.cssSelector("#unAuthMenu a"));
        l.get(5).click();
        Thread.sleep(2000);
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        driver.get(baseUrl);
    }

    @Step
    public void checkHotelItemVisibility(){
        wait.until(presenceOfElementLocated(By.className("HotelItem")));
    }

    @Step
    public void checkOrderBlockVisibility(){
        wait.until(presenceOfElementLocated(By.className("OrderBlock")));
    }


    @Step
    public void searchForOrders() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(By.cssSelector(".ManagerPart__Orders__searchForm button")).click();
    }

    @Step
    public void checkOrdersResultItemsExistence(){
        wait.until(presenceOfElementLocated(By.className("OrderBlockItem")));
    }

    @Step
    public void checkRoomTypesPageExistence(){
        wait.until(presenceOfElementLocated(By.id("ManagerPart__RoomTypePage")));
    }
    @Step
    public void signIn() throws Exception {
        driver.findElement(By.id("login-form__mail")).sendKeys("manager123@hotel.ru");
        driver.findElement(By.id("login-form__password")).sendKeys("adminadmin");
        driver.findElement(By.cssSelector(".loginForm button")).click();
        Thread.sleep(1000);
    }



    @Step
    // TODO: not implemented
    public void setRoomStatusToOk(){
        driver.findElement(By.cssSelector(".RoomItem button")).click();
        driver.findElement(By.id("RoomItem__statusInput")).click();
    }

    @Step
    public void approveOrder(WebElement el) throws Exception{
        el.findElement(By.cssSelector("button.btn-success")).click();
        Thread.sleep(100);
    }

    @Step
    public void cancelOrder(WebElement el) throws Exception{
        el.findElement(By.cssSelector("button.btn-danger")).click();
        Thread.sleep(100);
    }

    @Step
    public void editRoomType(WebElement  el) throws Exception {
        el.findElement(By.cssSelector("button.btn-primary")).click();
        Thread.sleep(200);
    }

    @Step
    public void deleteRoomType(WebElement  el) throws Exception {
        el.findElement(By.cssSelector("button.btn-danger")).click();
        Thread.sleep(200);
    }

    @Step
    public void setTitleForRoomType(WebElement el) throws Exception {
        el.findElement(By.id("RoomItem__titleInput")).sendKeys("1");
        Thread.sleep(200);
    }

    @Step
    public void saveEditedRoomType(WebElement el) throws Exception{
        el.findElement(By.cssSelector("button.btn-success")).click();
        Thread.sleep(200);
    }



    @Step
    public void clickAddRoomType() throws Exception{
        driver.findElement(By.id("ManagerPart__RoomTypePage"))
                .findElement(By.cssSelector("button.btn-success"))
                .click();
    }

    @Step
    public void fillNewRoomTypeInfo() throws Exception{
        driver
                .findElement(By.id("RoomTypePart__addRoomTypeBlock__titleInput"))
                .sendKeys("NewTestRoomType");
        driver
                .findElement(By.id("RoomTypePart__addRoomTypeBlock__descriptionInput"))
                .sendKeys("New test room type test description");
        driver
                .findElement(By.id("RoomTypePart__addRoomTypeBlock__costInput"))
                .sendKeys("150");
        driver
                .findElement(By.id("RoomTypePart__addRoomTypeBlock__photoPathInput"))
                .sendKeys("https://i.pinimg.com/originals/01/d7/44/01d7440eff4830436df18e099e680aac.jpg");
    }

    @Step
    public void saveNewRoomType() throws Exception{
        driver
                .findElement(By.cssSelector(".RoomTypePart__addRoomTypeBlock button"))
                .click();

        Thread.sleep(200);
    }

    @Step
    public void goBackToRoomTypesPage() throws Exception{
        driver
                .findElement(By.cssSelector(".RoomTypePage__goToShowRoomTypesButton"))
                .click();
        Thread.sleep(100);
    }


    @Test
    public void signInTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(1000);
        this.closeLeftMenu();
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }

    @Test
    public void searchForOrdersTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnOrdersMenuItem();
        this.searchForOrders();
        this.checkOrdersResultItemsExistence();
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }

    @Test
    public void showRoomTypeTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnRoomTypesMenuItem();
        Thread.sleep(500);
        this.checkRoomTypesPageExistence();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnLogoutMenuItem();
    }

    @Test
    public void editRoomTypeTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnRoomTypesMenuItem();
        Thread.sleep(500);
        this.checkRoomTypesPageExistence();

        List<WebElement> elements = driver.findElements(By.className("RoomItem"));

        this.editRoomType(elements.get(0));
        this.setTitleForRoomType(elements.get(0));
        this.saveEditedRoomType(elements.get(0));
        this.checkRoomTypesPageExistence();

        Thread.sleep(500);
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnLogoutMenuItem();
    }

    @Test
    @Step
    public void addNewRoomTypeTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnRoomTypesMenuItem();
        Thread.sleep(500);
        this.checkRoomTypesPageExistence();
        this.clickAddRoomType();
        Thread.sleep(500);
        this.fillNewRoomTypeInfo();
        this.saveNewRoomType();
        this.checkRoomTypesPageExistence();
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }


    @Test
    public void cancelNewRoomTypeAddingTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnRoomTypesMenuItem();
        Thread.sleep(500);
        this.checkRoomTypesPageExistence();
        this.clickAddRoomType();
        this.goBackToRoomTypesPage();
        this.checkRoomTypesPageExistence();
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }

    @Test
    public void deleteRoomTypeTest() throws Exception{
        this.addNewRoomTypeTest();
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnRoomTypesMenuItem();
        Thread.sleep(500);
        this.checkRoomTypesPageExistence();
        List<WebElement> elements = driver.findElements(By.cssSelector(".RoomItem"));
        this.deleteRoomType(elements.get(0));
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }


    @Test
    public void approveOrderTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnOrdersMenuItem();
        Thread.sleep(500);
        this.searchForOrders();
        this.checkOrdersResultItemsExistence();
        List<WebElement> elements = driver.findElements(By.cssSelector(".OrderBlockItem button.btn-success"));
        if(elements.size() > 0){
            elements.get(0).click();
        }
        Thread.sleep(100);
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }

    @Test
    public void cancelOrderTest() throws Exception{
        this.signIn();
        this.checkHotelItemVisibility();
        this.openLeftMenu();
        Thread.sleep(500);
        this.clickOnOrdersMenuItem();
        Thread.sleep(500);
        this.searchForOrders();
        this.checkOrdersResultItemsExistence();
        List<WebElement> elements = driver.findElements(By.cssSelector(".OrderBlockItem button.btn-danger"));
        if(elements.size() > 0){
            elements.get(0).click();
        }
        Thread.sleep(200);
        this.openLeftMenu();
        this.clickOnLogoutMenuItem();
    }



}