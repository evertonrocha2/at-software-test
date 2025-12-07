package exercicio4.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object para a página inicial
 */
public class HomePage extends BasePage {

    @FindBy(css = "a[href='/login']")
    private WebElement signupLoginLink;

    @FindBy(css = "a[href='/logout']")
    private WebElement logoutLink;

    @FindBy(xpath = "//li[contains(text(), 'Logged in as')]")
    private WebElement loggedInText;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void clickSignupLogin() {
        click(signupLoginLink);
    }

    public boolean isUserLoggedIn() {
        try {
            // Aguarda um pouco e tenta várias formas de verificar
            WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            extendedWait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//li[contains(text(), 'Logged in as')]")
                ),
                ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//a[contains(@href, '/logout')]")
                ),
                ExpectedConditions.urlContains("account")
            ));
            
            // Verifica se o elemento está visível
            return isDisplayed(loggedInText) || 
                   driver.findElement(org.openqa.selenium.By.xpath("//a[contains(@href, '/logout')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        if (isDisplayed(logoutLink)) {
            click(logoutLink);
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}

