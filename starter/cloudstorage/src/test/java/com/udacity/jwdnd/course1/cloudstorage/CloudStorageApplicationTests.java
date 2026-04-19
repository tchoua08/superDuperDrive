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
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");

        // Wait for the page to load
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Check that we're NOT seeing the Whitelabel Error Page
        String pageSource = driver.getPageSource();
        Assertions.assertFalse(pageSource.contains("Whitelabel Error Page"),
                "Custom error handling not working - Whitelabel Error Page is displayed");
    }


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

    // ============================================
    // TEST NOTE OPERATIONS
    // ============================================

	/**
	 * Test creating a new note and verifying it appears in the list
	 */
	@Test
	public void testCreateNote() {
		// 1. Sign up and login
		doMockSignUp("Note","Test","NT","123");
		doLogIn("", "123");
		
		// Wait for page to load completely
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 2. Click on Notes tab using XPath
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]")));
		WebElement notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		// 3. Click "Add Note" button to open modal
		WebElement addNoteButton = driver.findElement(By.xpath("//button[normalize-space()='+ Add Note']"));
		addNoteButton.click();
		
		// 4. Wait for modal and fill in note details
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		
		// Clear any existing content first
		noteTitle.clear();
		noteDescription.clear();
		
		// Fill in new content
		noteTitle.sendKeys("Test Note Title");
		noteDescription.sendKeys("This is a test note description");
		
		// 5. Save the note - Find button in modal context
		WebElement saveButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//div[@id='noteModal']//button[@class='btn btn-primary']")));
		saveButton.click();
		
		// 6. Wait for modal to close
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));
		
		// 7. Wait longer for page to reload/process the note creation
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 8. Refresh the notes tab to ensure we see the created note
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]/")));
		notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]/"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		// 9. Wait for and verify note appears in the list
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//tbody//tr")));
		
		// Additional wait to ensure the note is fully rendered
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		Assertions.assertTrue(
			driver.getPageSource().contains("Test Note Title"),
			"Note title should be visible in notes list");
		Assertions.assertTrue(
			driver.getPageSource().contains("This is a test note description"),
			"Note description should be visible in notes list");
	}

	/**
	 * Test editing an existing note
	 */
	@Test
	public void testEditNote() {
		// 1. Create a note first
		testCreateNote();
		
		// 2. Wait a moment for page stability
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 3. Navigate to notes tab - ensure button exists first
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]")));
		WebElement notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		// 4. Find and click the Edit button for the note we just created
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//button[contains(text(), 'Edit')]")));
		WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//button[contains(text(), 'Edit')]")));
		editButton.click();
		
		// 5. Wait for modal and update note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		
		// Clear existing content
		noteTitle.clear();
		noteTitle.sendKeys("Updated Note Title");
		noteDescription.clear();
		noteDescription.sendKeys("This is an updated note description");
		
		// 6. Save changes - Find button in modal context
		WebElement saveButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//div[@id='noteModal']//button[@class='btn btn-primary']")));
		saveButton.click();
		
		// 7. Wait for modal to close
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));
		
		// 8. Wait longer for page to process the update
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 9. Refresh notes tab to see updated content
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]")));
		notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		Assertions.assertTrue(
			driver.getPageSource().contains("Updated Note Title"),
			"Updated note title should be visible");
		Assertions.assertTrue(
			driver.getPageSource().contains("This is an updated note description"),
			"Updated note description should be visible");
	}

	/**
	 * Test deleting a note
	 */
	@Test
	public void testDeleteNote() {
		// 1. Create and edit a note first to ensure we're deleting the right one
		testEditNote();
		
		// 2. Wait a moment for page stability
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 3. Navigate to notes tab - ensure button exists first
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]")));
		WebElement notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 4. Find and click the Delete button
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//a[contains(text(), 'Delete')]")));
		WebElement deleteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//a[contains(text(), 'Delete')]")));
		deleteButton.click();
		
		// 5. Wait for page reload
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 6. Navigate back to notes tab to verify deletion
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'notes-tab')]")));
		notesTab = driver.findElement(By.xpath("//button[contains(@id, 'notes-tab')]"));
		notesTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 7. Verify the note is no longer in the list
		Assertions.assertFalse(
			driver.getPageSource().contains("Updated Note Title"),
			"Deleted note should not appear in the list");
	}

	// ============================================
	// TEST CREDENTIAL OPERATIONS
	// ============================================

	/**
	 * Test creating a new credential and verifying it appears in the list
	 */
	@Test
	public void testCreateCredential() {
		// 1. Sign up and login
		doMockSignUp("Credential","Test","CT","123");
		doLogIn("", "123");
		
		// Wait for page to load completely
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 2. Click on Credentials tab
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		WebElement credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		// 3. Click "Add a New Credential" button
		WebElement addCredentialButton = driver.findElement(By.xpath("//button[contains(text(), 'Add a New Credential')]"));
		addCredentialButton.click();
		
		// 4. Wait for modal and fill in credential details
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		
		// Clear any existing content
		credentialUrl.clear();
		credentialUsername.clear();
		credentialPassword.clear();
		
		// Fill in new content
		credentialUrl.sendKeys("https://example.com");
		credentialUsername.sendKeys("testuser");
		credentialPassword.sendKeys("testpassword123");
		
		// 5. Save the credential
		WebElement saveButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//div[@id='credentialModal']//button[normalize-space()='Save changes']")));
		saveButton.click();
		
		// 6. Wait for modal to close
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));
		
		// 7. Wait longer for page to process
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 8. Refresh credentials tab
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		Assertions.assertTrue(
			driver.getPageSource().contains("https://example.com"),
			"Credential URL should be visible in credentials list");
		Assertions.assertTrue(
			driver.getPageSource().contains("testuser"),
			"Credential username should be visible in credentials list");
	}

	/**
	 * Test editing an existing credential
	 */
	@Test
	public void testEditCredential() {
		// 1. Create a credential first
		testCreateCredential();
		
		// 2. Wait a moment for page stability
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 3. Navigate to credentials tab - ensure button exists first
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		WebElement credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 4. Find and click the Edit button for the credential
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//button[contains(text(), 'Edit')]")));
		WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//button[contains(text(), 'Edit')]")));
		editButton.click();
		
		// 5. Wait for modal and update credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		
		// Clear and update
		credentialUrl.clear();
		credentialUrl.sendKeys("https://updated.com");
		credentialUsername.clear();
		credentialUsername.sendKeys("updateduser");
		
		// 6. Save changes
		WebElement saveButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//div[@id='credentialModal']//button[normalize-space()='Save changes']")));
		saveButton.click();
		
		// 7. Wait for modal to close
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("credentialModal")));
		
		// 8. Wait longer for page to process
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 9. Refresh credentials tab
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		Assertions.assertTrue(
			driver.getPageSource().contains("https://updated.com"),
			"Updated credential URL should be visible");
		Assertions.assertTrue(
			driver.getPageSource().contains("updateduser"),
			"Updated credential username should be visible");
	}

	/**
	 * Test deleting a credential
	 */
	@Test
	public void testDeleteCredential() {
		// 1. Create and edit a credential first
		testEditCredential();
		
		// 2. Wait a moment for page stability
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 3. Navigate to credentials tab - ensure button exists first
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		WebElement credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 4. Find and click the Delete button
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//a[contains(text(), 'Delete')]")));
		WebElement deleteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(
			By.xpath("//a[contains(text(), 'Delete')]")));
		deleteButton.click();
		
		// 5. Wait for page reload
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		// 6. Navigate back to credentials tab to verify deletion
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@id, 'credentials-tab')]/")));
		credentialsTab = driver.findElement(By.xpath("//button[contains(@id, 'credentials-tab')]/"));
		credentialsTab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials")));
		
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		
		// 7. Verify the credential is no longer in the list
		Assertions.assertFalse(
			driver.getPageSource().contains("https://updated.com"),
			"Deleted credential should not appear in the list");
	}

}
