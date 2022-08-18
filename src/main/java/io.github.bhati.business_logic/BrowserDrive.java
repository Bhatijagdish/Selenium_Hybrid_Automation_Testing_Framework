package io.github.bhati.business_logic;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrowserDrive {

    private static WebDriver driver;

    public enum Locators {
        ID, XPATH, CSSSELECTOR, CLASSNAME, LINKTEXT, NAME, PARTIALLINKTEXT, TAGNAME
    }

    public static void loadBrowser(String Browser, String url, String DriverLocation) {
        if (Browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", DriverLocation);
            driver = new ChromeDriver();
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.get(url);
        } else if (Browser.equalsIgnoreCase("edge")) {

        } else if (Browser.equalsIgnoreCase("firefox")) {

        } else if (Browser.equalsIgnoreCase("safari")) {

        }
    }

    public static void closeExtraWindows() {
        String mainWindow = driver.getWindowHandle();
        ArrayList<String> windowTabs = new ArrayList<>(driver.getWindowHandles());
        for (String tab : windowTabs) {
            if (!tab.equalsIgnoreCase(mainWindow)) {
                driver.switchTo().window(tab);
                driver.close();
            }

        }
    }

    public static void loginSuccess(String username, String password) {

    }

    public static By getElement(String utilObject) {
        String[] arrObj = utilObject.split(":", 2);
        String identifier = arrObj[0].toUpperCase();
        String locatorVal = arrObj[1];

        switch (Locators.valueOf(identifier)) {
            case ID:
                return By.id(locatorVal);
            case NAME:
                return By.name(locatorVal);
            case XPATH:
                return By.xpath(locatorVal);
            case TAGNAME:
                return By.tagName(locatorVal);
            case LINKTEXT:
                return By.linkText(locatorVal);
            case CLASSNAME:
                return By.className(locatorVal);
            case CSSSELECTOR:
                return By.cssSelector(locatorVal);
            case PARTIALLINKTEXT:
                return By.partialLinkText(locatorVal);
        }
        return null;
    }

    public static WebElement webElement(String utilObject) {
        try {
            return driver.findElement(getElement(utilObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<WebElement> webElements(String utilsObject) {
        try {
            return driver.findElements(getElement(utilsObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void selectLastDropdownValue(String utilsObject) {
        try {
            Select dropDown = new Select(driver.findElement(getElement(utilsObject)));
            dropDown.selectByIndex(dropDown.getOptions().size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean elementClickable(String utilsObject, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(getElement(utilsObject)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean elementVisible(String utilsObject, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOf(webElement(utilsObject)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyElementFluentWait(String utilsObject, int timeoutInSeconds, int pollingTimeInSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofSeconds(pollingTimeInSeconds))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.presenceOfElementLocated(getElement(utilsObject)));
        return true;
    }

    public static List<String> getAllOptions(String utilsObject) {
        List<String> opts = new ArrayList<>();
        for (WebElement option : new Select(Objects.requireNonNull(webElement(utilsObject))).getOptions()) {
            if (!option.getAttribute("value").isEmpty()
                    || !option.getAttribute("value").equalsIgnoreCase("select"))
                opts.add(option.getText());
        }
        return opts;
    }

    public static void selectDropdownByText(String utilsObject, String option_value) {
        new Select(Objects.requireNonNull(webElement(utilsObject))).selectByVisibleText(option_value);
    }

    public static void scrollTillBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight");
    }

    public static boolean clickElementByIndex(String utilsObject, int Index) {
        boolean result = false;
        List<WebElement> elems = webElements(utilsObject);
        int numOfIndexes = elems.size() - 1;
        if (numOfIndexes >= Index) {
            elems.get(Index).click();
            result = true;
        }
        return result;
    }

    public static boolean isElemPresent(String utilsObject) {
        try {
            if (webElements(utilsObject).size() > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<WebElement> iframes() {
        return driver.findElements(By.tagName("iframe"));
    }

    public static void switchToLastFrame() {
        try {
            List<WebElement> frames = iframes();
            if (frames.size() > 0)
                driver.switchTo().frame(frames.get(frames.size() - 1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scrollActionKeys(String scrollKey) {
        String keys = scrollKey.toLowerCase();
        Actions act = new Actions(driver);
        switch (keys) {
            case ("page up"):
                act.sendKeys(Keys.PAGE_UP);
                act.build().perform();
                break;
            case ("page down"):
                act.sendKeys(Keys.PAGE_DOWN);
                act.build().perform();
                break;
            case ("right"):
                act.sendKeys(Keys.ARROW_RIGHT);
                act.build().perform();
                break;
            case ("left"):
                act.sendKeys(Keys.ARROW_LEFT);
                act.build().perform();
                break;
            case ("up"):
                act.sendKeys(Keys.ARROW_UP);
                act.build().perform();
                break;
            case ("down"):
                act.sendKeys(Keys.ARROW_DOWN);
                act.build().perform();
                break;
            case ("enter"):
                act.sendKeys(Keys.ENTER);
                act.build().perform();
                break;
            case ("tab"):
                act.sendKeys(Keys.TAB);
                act.build().perform();
                break;
            case ("space"):
                act.sendKeys(Keys.SPACE);
                act.build().perform();
                break;
            default:
                break;
        }
    }

}

