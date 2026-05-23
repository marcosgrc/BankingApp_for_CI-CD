package es.codeurjc.e2e;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public final class BrowserTestHelper {

    private BrowserTestHelper() {
    }

    public static boolean isSafari() {
        String browser = System.getenv("BROWSER");
        if (browser == null || browser.isBlank()) {
            browser = System.getProperty("browser", "chrome");
        }
        return "safari".equalsIgnoreCase(browser.trim());
    }

    public static void fill(WebDriver driver, WebElement element, String value) {
        if (isSafari()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].focus();"
                            + "arguments[0].value=arguments[1];"
                            + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                            + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element, value);
        } else {
            element.clear();
            element.sendKeys(value);
        }
    }

    public static void click(WebDriver driver, WebElement element) {
        if (isSafari()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } else {
            element.click();
        }
    }

    public static void selectOptionByValue(WebElement selectElement, String value) {
        new Select(selectElement).selectByValue(value);
    }
}
