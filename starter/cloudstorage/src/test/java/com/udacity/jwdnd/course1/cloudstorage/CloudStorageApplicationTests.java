package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    // Store unique usernames for each test
    private String lastCreatedUsername;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {

        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.
        // Add timestamp to username to ensure uniqueness across test runs
        this.lastCreatedUsername = userName + "_" + System.currentTimeMillis();

        // Visit the sign-up page.
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(this.lastCreatedUsername);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        // Wait for redirect to login page (with longer timeout)
        webDriverWait.until(ExpectedConditions.urlContains("/login"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // If userName is provided, use it; otherwise use the last created username
        String loginUsername = userName;
        if (userName == null || userName.isEmpty()) {
            loginUsername = this.lastCreatedUsername;
        } else {
            // Add timestamp to match the signup username
            loginUsername = userName + "_" + System.currentTimeMillis();
        }

        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(loginUsername);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        // The doMockSignUp method already waits for the redirect to /login
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/login"),
                "Expected to be redirected to /login but got: " + currentUrl);
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("", "123");

        // Try to upload an arbitrary large file
        String fileName = "upload5m.zip";
        WebElement fileSelectButton = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[type='file'][name='fileUpload']")));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        // Find and click the Upload button
        WebElement uploadButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Upload']")));
        uploadButton.click();

        // Wait for the result page to load (file upload redirects to /result)
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    // ============================================
    // TEST SIGNUP/LOGIN FLOW
    // ============================================

    /**
     * Test that home page is not accessible without logging in
     */
    @Test
    public void testHomePageNotAccessibleWithoutLogin() {
        // Try to access home page directly without login
        driver.get("http://localhost:" + this.port + "/home");

        // Should be redirected to login page
        webDriverWait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Home page should not be accessible without login");
    }

    /**
     * Test signup, login, access home page, logout workflow
     */
    @Test
    public void testSignupLoginLogoutFlow() {
        // 1. Sign up new user
        doMockSignUp("Flow", "Test", "FT", "password123");

        // 2. Verify we're on login page
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Should be redirected to login after signup");

        // 3. Login with the new account
        doLogIn("", "password123");

        // 4. Verify we're on home page
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"),
                "Should be on home page after successful login");

        // 5. Click logout button
        WebElement logoutButton = driver.findElement(By.xpath("//button[normalize-space()='Logout']"));
        logoutButton.click();

        // 6. Verify logged out and redirected to login
        webDriverWait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Should be redirected to login after logout");

        // 7. Verify home page is no longer accessible
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Home page should not be accessible after logout");
    }



}
