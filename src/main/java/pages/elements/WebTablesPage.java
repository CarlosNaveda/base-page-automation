package pages.elements;

import model.Employee;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.*;

public class WebTablesPage extends BasePage {

    public WebTablesPage() {
        super();
    }

    //Variables
    String tableHeader = "//thead";
    String tagColumns = "/th";
    String tableBody = "//tbody";
    String tagRow = "/tr";
    String tagData = "/td";
    String tableBodyRow = "//tbody/tr";
    String tableBodyRowData = "//tbody/tr[%s]/td";
    String addButton = "//button[@id='addNewRecordButton']";
    String editButtonRecord = "//div[@class='action-buttons']//span[@id='edit-record-%s']";
    String deleteButtonRecord = "//div[@class='action-buttons']//span[@id='delete-record-%s']";
    String formFirstName = "//input[@id='firstName']";
    String formLastName = "//input[@id='lastName']";
    String formEmail = "//input[@id='userEmail']";
    String formAge = "//input[@id='age']";
    String formSalary = "//input[@id='salary']";
    String formDepartment = "//input[@id='department']";
    String formSubmitButton = "//button[@id='submit']";
    String searchBox = "//input[@id='searchBox']";
    String searchButton = "//button[@id='basic-addon2']";
    String comboPagination = "//select[@class='form-control']";
    String maxControlPagination = "Show 50";
    String containerPagination = "//div[@role='group']";
    String firstPage = "//div[contains(@class,'pagination')]//button[.='First']";
    String previousPage = "//div[contains(@class,'pagination')]//button[.='Previous']";
    String nextPage = "//div[contains(@class,'pagination')]//button[.='Next']";
    String lastPage = "//div[contains(@class,'pagination')]//button[.='Last']";
    String textPagination = "//strong";

    //Métodos privados
    private void showAllData(List<Map<String,String>> alldata) {
        for (Map<String,String> map : alldata) {
            for (String key : map.keySet()) {
                System.out.println(key + " : " + map.get(key));
            }
            System.out.println("\n");
        }
    }

    private Map<String,String> setRows(List<WebElement> listData) {

        Map<String,String> rows = new LinkedHashMap<>();

        int count=1;
        for (WebElement data : listData) {

            switch (count) {
                case 1:
                    rows.put("FirstName",data.getText());
                    break;

                case 2:
                    rows.put("LastName",data.getText());
                    break;

                case 3:
                    rows.put("Age",data.getText());
                    break;

                case 4:
                    rows.put("Email",data.getText());
                    break;

                case 5:
                    rows.put("Salary",data.getText());
                    break;

                case 6:
                    rows.put("Department",data.getText());
                    break;

                default:
                    break;
            }
            count++;
        }

        return rows;

    }

    private void addRows(List<Map<String,String>> dataTable) {
        //Obtengo las filas existentes con data
        List<WebElement> rowsData = getAllWebElements(tableBodyRow);
        int maxRowsData = rowsData.size();

        //Recorro cada fila y busco las columnas para obtener los campos que son 6
        for (int i=1; i <= maxRowsData; i++) {
            List<WebElement> listData = getAllWebElements(String.format(tableBodyRowData, i)); //Recorro por índice de fila
            Map<String,String> rows = setRows(listData);
            dataTable.add(rows);
        }
    }

    //Métodos públicos
    public void clickAddButton(){
        click(addButton);
    }

    public void typeWordInTheSearchBox(String searchWord) {
        setValueOnWebElement(searchBox, searchWord);
    }

    public void fillRegistrationForm(Employee employee){
        setValueOnWebElement(formFirstName, employee.getFirstName());
        setValueOnWebElement(formLastName, employee.getLastName());
        setValueOnWebElement(formAge,String.valueOf(employee.getAge()));
        setValueOnWebElement(formEmail, employee.getEmail());
        setValueOnWebElement(formSalary,String.valueOf(employee.getSalary()));
        setValueOnWebElement(formDepartment, employee.getDepartment());
    }

    public int randomNumber(int max){
        return generateRandomNumber(max);
    }

    public void clickEditButtonInRecord(int record){
        click(String.format(editButtonRecord, record));
    }

    public void clickDeleteButtonInRecord(int record){
        click(String.format(deleteButtonRecord, record));
    }

    public void clickSubmitButton(){
        click(formSubmitButton);
    }

    public void clickSearchButton(){
        click(searchButton);
    }

    public void setOnFirstPage(){
        if (isLocatorEnabled(firstPage)) {
            click(firstPage);
        }

        System.out.println("Ya estamos en la primera página");
    }

    public void changeControlPagination(String controlPagination){
        safeClick(firstPage); //Regresamos a la página inicial
        selectOption(comboPagination, controlPagination);
    }

    public List<Map<String,String>> getDataTable(){
        //Elegimos la cantidad máxima para mostrar registros
        selectOption(comboPagination, maxControlPagination);
        List<Map<String,String>> dataTable = new ArrayList<>();

        while(isLocatorEnabled(nextPage)){
            addRows(dataTable);
            String currentPage = getTextWebElement(textPagination); //Antes de pagina, obtengo el texto de la página actual
            safeClick(nextPage); //Paginamos
            waitUntilPageChange(textPagination,currentPage); //Me aseguro que cambié de página
        }

        addRows(dataTable);

        return dataTable;
    }

    public int getSizeDataTable(){
        return getDataTable().size();

    }

    public String getTextPagination(){
        return getTextWebElement(textPagination);
    }


}
