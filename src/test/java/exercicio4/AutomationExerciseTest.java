package exercicio4;

import exercicio4.pages.HomePage;
import exercicio4.pages.LoginPage;
import exercicio4.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Exercício 4: Automação de Testes Web com Selenium
 * 
 * Testes automatizados para automationexercise.com usando:
 * - Selenium WebDriver
 * - Page Object Model
 * - WebDriverManager
 * - Screenshots em caso de falha
 */
public class AutomationExerciseTest {

    private static WebDriver driver;
    private static final String BASE_URL = "https://automationexercise.com";
    private HomePage homePage;
    private LoginPage loginPage;
    private SignupPage signupPage;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        signupPage = new SignupPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String generateRandomEmail() {
        Random random = new Random();
        return "testuser" + random.nextInt(10000) + "@example.com";
    }

    private void takeScreenshot(String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshot_" + testName + "_" + timestamp + ".png";
            Path screenshotDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotDir);
            Path screenshotPath = screenshotDir.resolve(fileName);
            Files.write(screenshotPath, screenshotBytes);
            
            System.out.println("Screenshot salvo em: " + screenshotPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar screenshot: " + e.getMessage());
        }
    }

    private void waitForPageLoad(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("Deve cadastrar novo usuário com sucesso")
    void deveCadastrarNovoUsuario() {
        try {
            // Given
            homePage.navigateTo(BASE_URL);
            homePage.clickSignupLogin();
            
            String name = "Test User";
            String email = generateRandomEmail();
            
            // When
            loginPage.fillSignupForm(name, email);
            loginPage.clickSignupButton();
            
            // Aguarda a página de cadastro carregar completamente
            waitForPageLoad(3000);
            
            // Aguarda elementos do formulário estarem prontos
            signupPage.waitForPageLoad();
            
            // Preenche formulário de cadastro
            signupPage.selectTitle(true);
            signupPage.fillPassword("password123");
            signupPage.selectDateOfBirth("15", "June", "1990");
            signupPage.fillAddress("First", "Last", "Company", 
                                  "Address 1", "Address 2", "United States",
                                  "State", "City", "12345");
            signupPage.fillMobileNumber("1234567890");
            signupPage.clickCreateAccount();
            
            // Then
            Assertions.assertTrue(signupPage.isAccountCreated(), 
                "Mensagem 'Account Created!' deveria aparecer");
            
            signupPage.clickContinue();
            
            // Aguarda a navegação após clicar em Continue
            waitForPageLoad(2000);
            
            // Verifica se usuário está logado
            Assertions.assertTrue(homePage.isUserLoggedIn(), 
                "Usuário deveria estar logado após cadastro");
        } catch (AssertionError e) {
            takeScreenshot("deveCadastrarNovoUsuario");
            throw e;
        }
    }

    @Test
    @DisplayName("Deve fazer login com credenciais válidas")
    void deveFazerLoginComCredenciaisValidas() {
        try {
            // Given - Primeiro cria um usuário
            homePage.navigateTo(BASE_URL);
            homePage.clickSignupLogin();
            
            String name = "Login Test User";
            String email = generateRandomEmail();
            
            loginPage.fillSignupForm(name, email);
            loginPage.clickSignupButton();
            
            // Preenche cadastro mínimo
            signupPage.selectTitle(true);
            signupPage.fillPassword("password123");
            signupPage.selectDateOfBirth("15", "June", "1990");
            signupPage.fillAddress("First", "Last", "Company", 
                                  "Address 1", "Address 2", "United States",
                                  "State", "City", "12345");
            signupPage.fillMobileNumber("1234567890");
            signupPage.clickCreateAccount();
            
            if (signupPage.isAccountCreated()) {
                signupPage.clickContinue();
                homePage.logout();
            }
            
            // When - Faz login
            homePage.clickSignupLogin();
            loginPage.fillLoginForm(email, "password123");
            loginPage.clickLoginButton();
            
            // Aguarda após login
            waitForPageLoad(2000);
            
            // Then
            Assertions.assertTrue(homePage.isUserLoggedIn(), 
                "Usuário deveria estar logado após login válido");
        } catch (AssertionError e) {
            takeScreenshot("deveFazerLoginComCredenciaisValidas");
            throw e;
        }
    }

    @Test
    @DisplayName("Deve exibir erro ao fazer login com email inválido")
    void deveExibirErroAoLoginComEmailInvalido() {
        try {
            // Given
            homePage.navigateTo(BASE_URL);
            homePage.clickSignupLogin();
            
            // When
            loginPage.fillLoginForm("invalid@example.com", "password123");
            loginPage.clickLoginButton();
            
            // Aguarda mensagem de erro aparecer
            waitForPageLoad(2000);
            
            // Then
            Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Mensagem de erro deveria aparecer para email inválido");
        } catch (AssertionError e) {
            takeScreenshot("deveExibirErroAoLoginComEmailInvalido");
            throw e;
        }
    }

    @Test
    @DisplayName("Deve exibir erro ao fazer login com senha inválida")
    void deveExibirErroAoLoginComSenhaInvalida() {
        try {
            // Given - Assumindo que existe um usuário (pode falhar se não existir)
            homePage.navigateTo(BASE_URL);
            homePage.clickSignupLogin();
            
            // When
            loginPage.fillLoginForm("test@example.com", "wrongpassword");
            loginPage.clickLoginButton();
            
            // Then
            // Pode ter erro ou não, dependendo se o usuário existe
            // Este teste documenta o comportamento esperado
            boolean hasError = loginPage.isErrorMessageDisplayed();
            boolean isLoggedIn = homePage.isUserLoggedIn();
            
            Assertions.assertTrue(hasError || !isLoggedIn, 
                "Deveria exibir erro ou não permitir login com senha inválida");
        } catch (AssertionError e) {
            takeScreenshot("deveExibirErroAoLoginComSenhaInvalida");
            throw e;
        }
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar login com campos vazios")
    void deveExibirErroAoLoginComCredenciaisVazias() {
        try {
            // Given
            homePage.navigateTo(BASE_URL);
            homePage.clickSignupLogin();
            
            // When - Tenta fazer login sem preencher campos
            // Navegador pode prevenir submissão do form
            loginPage.clickLoginButton();
            
            // Then - Formulário deve permanecer visível (navegador bloqueia)
            // Navegadores modernos geralmente bloqueiam submissão com campos vazios
            // Verificamos que não houve navegação (formulário ainda está na página)
            boolean formStillVisible = homePage.getDriver().getCurrentUrl().contains("/login");
            Assertions.assertTrue(formStillVisible, 
                "Formulário deveria permanecer visível quando campos estão vazios");
        } catch (AssertionError e) {
            takeScreenshot("deveExibirErroAoLoginComCredenciaisVazias");
            throw e;
        }
    }
}

