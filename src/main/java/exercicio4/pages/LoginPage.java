package exercicio4.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object para a página de login/signup
 */
public class LoginPage extends BasePage {

    @FindBy(css = "input[data-qa='signup-name']")
    private WebElement signupNameInput;

    @FindBy(css = "input[data-qa='signup-email']")
    private WebElement signupEmailInput;

    @FindBy(css = "button[data-qa='signup-button']")
    private WebElement signupButton;

    @FindBy(css = "input[data-qa='login-email']")
    private WebElement loginEmailInput;

    @FindBy(css = "input[data-qa='login-password']")
    private WebElement loginPasswordInput;

    @FindBy(css = "button[data-qa='login-button']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[contains(text(), 'incorrect') or contains(text(), 'Your email or password is incorrect')]")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillSignupForm(String name, String email) {
        sendKeys(signupNameInput, name);
        sendKeys(signupEmailInput, email);
    }

    public void clickSignupButton() {
        click(signupButton);
    }

    public void fillLoginForm(String email, String password) {
        sendKeys(loginEmailInput, email);
        sendKeys(loginPasswordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            extendedWait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//p[contains(text(), 'incorrect') or contains(text(), 'Your email or password is incorrect')]")
                ),
                ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//form//p[contains(@style, 'color: red') or contains(@class, 'error')]")
                )
            ));
            return isDisplayed(errorMessage);
        } catch (Exception e) {
            // Tenta verificar diretamente
            return isDisplayed(errorMessage);
        }
    }

    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }

    public WebElement getLoginEmailInput() {
        return loginEmailInput;
    }
}

