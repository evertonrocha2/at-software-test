package exercicio4.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object para a página de cadastro
 */
public class SignupPage extends BasePage {

    @FindBy(id = "id_gender1")
    private WebElement titleMrRadio;

    @FindBy(id = "id_gender2")
    private WebElement titleMrsRadio;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "days")
    private WebElement daysSelect;

    @FindBy(id = "months")
    private WebElement monthsSelect;

    @FindBy(id = "years")
    private WebElement yearsSelect;

    @FindBy(id = "newsletter")
    private WebElement newsletterCheckbox;

    @FindBy(id = "optin")
    private WebElement offersCheckbox;

    @FindBy(id = "first_name")
    private WebElement firstNameInput;

    @FindBy(id = "last_name")
    private WebElement lastNameInput;

    @FindBy(id = "company")
    private WebElement companyInput;

    @FindBy(id = "address1")
    private WebElement address1Input;

    @FindBy(id = "address2")
    private WebElement address2Input;

    @FindBy(id = "country")
    private WebElement countrySelect;

    @FindBy(id = "state")
    private WebElement stateInput;

    @FindBy(id = "city")
    private WebElement cityInput;

    @FindBy(id = "zipcode")
    private WebElement zipcodeInput;

    @FindBy(id = "mobile_number")
    private WebElement mobileNumberInput;

    @FindBy(css = "button[data-qa='create-account']")
    private WebElement createAccountButton;

    @FindBy(xpath = "//h2[contains(text(), 'Account Created!')] | //b[contains(text(), 'Account Created!')]")
    private WebElement accountCreatedMessage;

    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    public SignupPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Aguarda a página de cadastro carregar
    public void waitForPageLoad() {
        try {
            WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            // Aguarda URL conter signup ou account ou information
            extendedWait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("signup"),
                ExpectedConditions.urlContains("account"),
                ExpectedConditions.urlContains("information")
            ));
            // Aguarda elementos principais estarem presentes e visíveis
            extendedWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.id("id_gender1")),
                ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.id("password")),
                ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.id("first_name")),
                ExpectedConditions.presenceOfElementLocated(org.openqa.selenium.By.cssSelector("input[type='radio'][name='title']"))
            ));
        } catch (Exception e) {
            // Continua mesmo se houver erro no wait
        }
    }

    public void selectTitle(boolean isMr) {
        try {
            WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            if (isMr) {
                extendedWait.until(ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.id("id_gender1")));
                WebElement element = driver.findElement(org.openqa.selenium.By.id("id_gender1"));
                element.click();
            } else {
                extendedWait.until(ExpectedConditions.elementToBeClickable(
                    org.openqa.selenium.By.id("id_gender2")));
                WebElement element = driver.findElement(org.openqa.selenium.By.id("id_gender2"));
                element.click();
            }
        } catch (Exception e) {
            // Tenta usar o PageFactory se o método direto falhar
            waitForPageLoad();
            if (isMr) {
                wait.until(ExpectedConditions.elementToBeClickable(titleMrRadio));
                titleMrRadio.click();
            } else {
                wait.until(ExpectedConditions.elementToBeClickable(titleMrsRadio));
                titleMrsRadio.click();
            }
        }
    }

    public void fillPassword(String password) {
        sendKeys(passwordInput, password);
    }

    public void selectDateOfBirth(String day, String month, String year) {
        daysSelect.sendKeys(day);
        monthsSelect.sendKeys(month);
        yearsSelect.sendKeys(year);
    }

    public void fillAddress(String firstName, String lastName, String company, 
                           String address1, String address2, String country,
                           String state, String city, String zipcode) {
        sendKeys(firstNameInput, firstName);
        sendKeys(lastNameInput, lastName);
        sendKeys(companyInput, company);
        sendKeys(address1Input, address1);
        sendKeys(address2Input, address2);
        countrySelect.sendKeys(country);
        sendKeys(stateInput, state);
        sendKeys(cityInput, city);
        sendKeys(zipcodeInput, zipcode);
    }

    public void fillMobileNumber(String mobile) {
        sendKeys(mobileNumberInput, mobile);
    }

    public void clickCreateAccount() {
        click(createAccountButton);
    }

    public boolean isAccountCreated() {
        try {
            // Aguarda mensagem de sucesso ou mudança de URL
            WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            extendedWait.until(ExpectedConditions.or(
                ExpectedConditions.textToBePresentInElementLocated(
                    org.openqa.selenium.By.xpath("//h2[contains(text(), 'Account Created!')] | //b[contains(text(), 'Account Created!')]"), 
                    "Account Created!"
                ),
                ExpectedConditions.urlContains("account_created"),
                ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//h2[contains(text(), 'Account Created!')] | //b[contains(text(), 'Account Created!')]")
                )
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        click(continueButton);
    }
}

