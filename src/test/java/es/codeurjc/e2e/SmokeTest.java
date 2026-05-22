package es.codeurjc.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SmokeTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Configuramos ChromeOptions para que funcione perfectamente tanto en local
        // como en las máquinas virtuales sin pantalla (Headless) de GitHub Actions.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        this.driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    public void verifyAppVersionOnLoginPage() {
        // Capturamos las propiedades que inyecta dinámicamente Maven desde el workflow3.yml
        // Si se ejecuta a mano sin parámetros, usará los valores por defecto.
        String appUrl = System.getProperty("APP_URL", "http://localhost:8080");
        String expectedVersion = System.getProperty("APP_VERSION", "1.0.0");

        // 1. Navegamos directos a la pantalla de Login (donde se exige mostrar la versión por el enunciado)
        driver.get(appUrl + "/login");

        // 2. Localizamos el elemento raíz del cuerpo de la página para escanear su texto
        WebElement body = driver.findElement(By.tagName("body"));
        String bodyText = body.getText();

        // 3. Verificación de humo: Validamos si el string de la versión (SemVer) está pintado en pantalla
        assertTrue(bodyText.contains(expectedVersion), 
            "⚠️ ERROR EN PRUEBA DE HUMO: No se localiza la versión '" + expectedVersion + "' en el login de " + appUrl);
    }
}