package com.example.pollai.utente;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;

import java.nio.file.Paths;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class UtenteTestSelenium {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void passwordSbagliata() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(782, 831));
    driver.findElement(By.linkText("Choose Your Plan")).click();
    driver.findElement(By.cssSelector(".plan-box:nth-child(1) .subscribe-button")).click();

    String projectPath = System.getProperty("user.dir"); // Ottieni la directory di lavoro corrente
    String pdfPath = Paths.get(projectPath, "RASVSL.pdf").toAbsolutePath().toString(); // Percorso assoluto del file PDF

    driver.findElement(By.id("document")).sendKeys(pdfPath);

    driver.findElement(By.id("nome")).click();
    driver.findElement(By.id("nome")).sendKeys("Giovanna");
    driver.findElement(By.id("cognome")).click();
    driver.findElement(By.id("cognome")).sendKeys("Rossi");
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("+39 324 332 3563");
    driver.findElement(By.id("pIVA")).click();
    driver.findElement(By.id("pIVA")).sendKeys("IT4345678910");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giovannarossi@gmail.com");
    driver.findElement(By.id("registerForm")).click();
    driver.findElement(By.id("registerForm")).click();
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys("1234");
    driver.findElement(By.cssSelector(".submit")).click();
    driver.findElement(By.id("pIVA")).click();
    driver.findElement(By.id("pIVA")).sendKeys("IT14345678910");
    driver.findElement(By.cssSelector(".submit")).click();
    driver.findElement(By.cssSelector(".form")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("passwordError"));
      assert(elements.size() > 0);
    }
    driver.close();
  }
  @Test
  public void passwordLogin() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(782, 831));
    driver.findElement(By.linkText("Click here!")).click();
    driver.findElement(By.id("email")).sendKeys("anna046@gmail.com");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys("diegolove");
    driver.findElement(By.cssSelector(".button-login")).click();
    driver.findElement(By.id("passwordError")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("passwordError"));
      assert(elements.size() > 0);
    }
    driver.close();
  }
  @Test
  public void emailLogin() {
    driver.get("http://localhost:8080/choose-plan");
    driver.manage().window().setSize(new Dimension(788, 824));
    driver.findElement(By.cssSelector("img")).click();
    driver.findElement(By.linkText("Click here!")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("francesca72@gmail");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys("!Giulastarza72");
    driver.findElement(By.cssSelector(".button-login")).click();
    driver.findElement(By.cssSelector("body")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("emailError"));
      assert(elements.size() > 0);
    }
  }

  @Test
  public void emailRegistrazione() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(782, 831));
    driver.findElement(By.linkText("Choose Your Plan")).click();
    driver.findElement(By.cssSelector(".plan-box:nth-child(1) .subscribe-button")).click();

    String projectPath = System.getProperty("user.dir"); // Ottieni la directory di lavoro corrente
    String pdfPath = Paths.get(projectPath, "RASVSL.pdf").toAbsolutePath().toString(); // Percorso assoluto del file PDF

    driver.findElement(By.id("document")).sendKeys(pdfPath);

    driver.findElement(By.id("nome")).click();
    driver.findElement(By.id("nome")).sendKeys("Marco");
    driver.findElement(By.id("cognome")).click();
    driver.findElement(By.id("cognome")).sendKeys("Marconi");
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("+39 324 332 3563");
    driver.findElement(By.id("pIVA")).click();
    driver.findElement(By.id("pIVA")).sendKeys("IT14345678910");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("marcolyno@gmail");
    driver.findElement(By.id("password")).click();
    driver.findElement(By.id("password")).sendKeys("!OdioiQuod96");


    driver.findElement(By.cssSelector(".submit")).click();
    driver.findElement(By.id("emailError")).click();
    {
      List<WebElement> elements = driver.findElements(By.id("emailError"));
      assert(elements.size() > 0);
    }
    driver.close();
  }
}
