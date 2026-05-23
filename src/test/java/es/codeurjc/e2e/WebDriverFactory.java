package es.codeurjc.e2e;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = resolveBrowser();
        boolean headless = useHeadless(browser);
        return switch (browser) {
            case "firefox" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            case "safari" -> createSafari();
            default -> createChrome(headless);
        };
    }

    private static String resolveBrowser() {
        String fromEnv = System.getenv("BROWSER");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim().toLowerCase();
        }
        String fromProperty = System.getProperty("browser");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty.trim().toLowerCase();
        }
        return "chrome";
    }

    private static boolean useHeadless(String browser) {
        if ("safari".equals(browser)) {
            return false;
        }
        String headlessEnv = System.getenv("HEADLESS");
        if (headlessEnv != null && !headlessEnv.isBlank()) {
            return Boolean.parseBoolean(headlessEnv);
        }
        if (System.getenv("GITHUB_ACTIONS") == null) {
            return false;
        }
        String runnerOs = System.getenv("RUNNER_OS");
        return runnerOs != null && !"Linux".equals(runnerOs);
    }

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return new EdgeDriver(options);
    }

    private static WebDriver createSafari() {
        WebDriver driver = new SafariDriver(new SafariOptions());
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        return driver;
    }
}
