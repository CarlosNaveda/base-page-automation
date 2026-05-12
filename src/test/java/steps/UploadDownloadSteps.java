package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.elements.UploadDownloadPage;


public class UploadDownloadSteps {

    UploadDownloadPage uploadDownloadPage = new UploadDownloadPage();
    WebElement selectFile;
    String fakePath = "C:\\fakepath\\%s";
    String downloadPath = "C:\\Users\\Carlos\\Downloads\\%s";


    @When("the user click the select file button")
    public void theUserClickTheSelectFileButton() {
        selectFile = uploadDownloadPage.getSelectFileElement();
    }

    @When("the user click the download button")
    public void theUserClickTheDownloadButton() {
        uploadDownloadPage.clickDownloadButton();
    }

    @And("select {string}")
    public void selectFile(String filePath) {
        uploadDownloadPage.selectTheFile(selectFile, filePath);
    }

    @Then("The path shows the fake path with name of {string}")
    public void validateTheFakePathWithNameOfFile(String filePath) {
        String fileName = uploadDownloadPage.getFileName(filePath);
        String ActualFakePath = uploadDownloadPage.getFakePath();
        String ExpectedFakePath = String.format(fakePath, fileName);
        Assert.assertEquals(ActualFakePath, ExpectedFakePath);
    }

    @Then("the {string} is downloaded")
    public void validateTheFileIsDownloaded(String fileName) {
        String expectedFileDownloaded = String.format(downloadPath, fileName);
        Assert.assertTrue(uploadDownloadPage.validateFileDownloaded(expectedFileDownloaded));

    }

}
