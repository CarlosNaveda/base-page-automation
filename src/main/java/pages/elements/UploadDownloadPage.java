package pages.elements;

import org.openqa.selenium.WebElement;
import pages.BasePage;

public class UploadDownloadPage extends BasePage {

    public UploadDownloadPage() {
        super();
    }

    //Variables
    String downloadButton = "//a[@id='downloadButton']";
    String selectFile = "//input[@id='uploadFile']";
    String fakePath = "//p[@id='uploadedFilePath']";

    //Métodos privados

    //Métodos públicos
    public WebElement getSelectFileElement(){
        return getWebElement(selectFile);
    }

    public void clickDownloadButton(){
        click(downloadButton);
    }

    public void selectTheFile(WebElement selectFile, String filePath) {
        selectFileToUpload(selectFile, filePath);
    }

    public String getFileName(String filePath){
        return extractFileNameOfPath(filePath);
    }

    public String getFakePath(){
        return getTextWebElement(fakePath);
    }

    public boolean validateFileDownloaded(String filePathDownloaded){
        return isFileDownloaded(filePathDownloaded);
    }

}
