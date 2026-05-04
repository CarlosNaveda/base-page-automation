package steps;

import data.EmployeeFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Employee;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import pages.elements.WebTablesPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static data.EmployeeFactory.generateEmployeeByRecords;

public class WebTableSteps {

    WebTablesPage webTablesPage = new WebTablesPage();
    private Employee employee;
    private int countRegisters;


    private SoftAssertions validateInformationIsInWebTable(List<Map<String,String>> dataTable){
        SoftAssertions softAssertions = new SoftAssertions();

        for (Map<String,String> map : dataTable) {

            boolean isEmailExists = employee.getEmail().equals(map.get("Email")); //Buscamos por correo

            if (isEmailExists) {
                int index = 1;

                for(Map.Entry<String,String> entry : map.entrySet()) {

                    switch (index) {
                        case 1:
                            softAssertions.assertThat(entry.getValue()).isEqualTo(employee.getFirstName());
                            break;

                        case 2:
                            softAssertions.assertThat(entry.getValue()).isEqualTo(employee.getLastName());
                            break;

                        case 3:
                            int valueAge = Integer.parseInt(entry.getValue());
                            softAssertions.assertThat(valueAge).isEqualTo(employee.getAge());
                            break;

                        case 4:
                            softAssertions.assertThat(entry.getValue()).isEqualTo(employee.getEmail());
                            break;

                        case 5:
                            int valueSalary = Integer.parseInt(entry.getValue());
                            softAssertions.assertThat(valueSalary).isEqualTo(employee.getSalary());
                            break;

                        case 6:
                            softAssertions.assertThat(entry.getValue()).isEqualTo(employee.getDepartment());
                            break;

                        default:
                            break;
                    }
                    index++;
                }

            }
        }

      return softAssertions;

    }

    private void setEmployeeWithRowOfDataTable(List<Map<String,String>> dataTable, int numberOfRow){

        String firstName = "",lastName= "",email= "",department = "";
        int valueAge=0,valueSalary = 0;
        int count = 1; //Contador de los maps

        for (Map<String,String> map : dataTable) {
            if (count == numberOfRow) { //Estoy en el registro solicitado

                int index=1; //Contador de los entries

                for(Map.Entry<String,String> entry : map.entrySet()) {

                    switch (index) {
                        case 1:
                            firstName = entry.getValue();
                            break;

                        case 2:
                            lastName = entry.getValue();
                            break;

                        case 3:
                            valueAge = Integer.parseInt(entry.getValue());
                            break;

                        case 4:
                            email = entry.getValue();
                            break;

                        case 5:
                            valueSalary = Integer.parseInt(entry.getValue());
                            break;

                        case 6:
                            department = entry.getValue();
                            break;

                        default:
                            break;
                    }

                    index++;
                }

                employee = new Employee(firstName,lastName,valueAge,email,valueSalary,department);
            }

          count++;
        }
    }

    private SoftAssertions validateInformationIsNotInWebTable(List<Map<String,String>> dataTable){
        SoftAssertions softAssertions = new SoftAssertions();

        for (Map<String,String> map : dataTable) {
            softAssertions.assertThat(employee.getEmail()).isNotEqualTo(map.get("Email"));
        }

        return softAssertions;

    }


    private List<String> getColumnMatchWithWord(Map<String,String> map, String word){
        List<String> columnList = new ArrayList<>();

        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue().toLowerCase().contains(word.toLowerCase())){
                columnList.add(entry.getKey());
            }
        }

        return columnList;
    }

    private Employee generateEmployeeByTable (Map<String, String> row){
        employee = new Employee(
                row.get("firstName"),
                row.get("lastName"),
                Integer.parseInt(row.get("age")),
                row.get("email"),
                Integer.parseInt(row.get("salary")),
                row.get("department")
        );
        return employee;
    }



    @Given("the user click the add button")
    public void theUserClickTheAddButton() {
        webTablesPage.clickAddButton();
    }

    @Given("the user type a {word} in the search box")
    public void theUserTypeWordInTheSearchBox(String searchWord) {
        webTablesPage.typeWordInTheSearchBox(searchWord);
    }

    @Given("The user add {int} of information to registers a employee")
    public void theUserAddALotOfRegisters(int numberRecords) {
        List<Employee> employeeList = generateEmployeeByRecords(numberRecords);
        employeeList.forEach(employee -> {
            webTablesPage.clickAddButton();
            webTablesPage.fillRegistrationForm(employee);
            webTablesPage.clickSubmitButton();
        });

        countRegisters = webTablesPage.getSizeDataTable(); //Guardamos la cantidad de registros totales
    }

    @Given("the user click the edit button in one of the record displayed")
    public void theUserClickTheEditButton() {
        int recordToEdit = webTablesPage.randomNumber(webTablesPage.getSizeDataTable());
        webTablesPage.clickEditButtonInRecord(recordToEdit);
    }

    @Given("the user is on the first page")
    public void theUserIsOnTheFirstPage() {
        webTablesPage.setOnFirstPage();
    }

    @When("the user fill all the information requested by the form with:")
    public void theUserFillRegistrationForm(DataTable dataTable) {
        Map<String, String> map = dataTable.asMaps().getFirst();
        Employee employee = generateEmployeeByTable(map);
        webTablesPage.fillRegistrationForm(employee);
    }

    @When("the user click the button search")
    public void theUserClickTheButtonSearch() {
        webTablesPage.clickSearchButton();
    }

    @When("the user change to {string}")
    public void theUserChangeTheControlOfPagination(String controlPagination) {
        webTablesPage.changeControlPagination(controlPagination);
    }

    @When("The user edit the record with:")
    public void theUserEditTheRecordWithTheInformation(DataTable dataTable) {
        Map<String, String> map = dataTable.asMaps().getFirst();
        Employee employee = generateEmployeeByTable(map);
        webTablesPage.fillRegistrationForm(employee);
    }

    @When("the user click the delete button in one of the record displayed")
    public void theUserClickTheDeleteButton() {
        List<Map<String,String>> dataTable = webTablesPage.getDataTable();
        int recordToDelete = webTablesPage.randomNumber(dataTable.size());
        setEmployeeWithRowOfDataTable(dataTable, recordToDelete);
        webTablesPage.clickDeleteButtonInRecord(recordToDelete);
    }

    @And("the user click the button submit")
    public void theUserClickTheSubmitButton() {
        webTablesPage.clickSubmitButton();
    }

    @Then("the all the information registered should be show in the web table")
    @Then("the edit information should be show in the web table")
    public void validationAllTheInformationWithTheWebTable() {
        List<Map<String,String>> dataTable = webTablesPage.getDataTable();
        SoftAssertions softAssertions = validateInformationIsInWebTable(dataTable);
        softAssertions.assertAll();
    }

    @Then("the coincidence with the {word} show in the web table")
    public void validationWordWithWebTable(String word) {
        List<Map<String,String>> dataTable = webTablesPage.getDataTable();
        SoftAssertions softAssertions = new SoftAssertions();
        int index = 1;

        for (Map<String,String> map : dataTable) {
            List<String> columnList = getColumnMatchWithWord(map,word);
            softAssertions.assertThat(columnList.size()).isGreaterThan(0); //Valido que al menos deba haber una coincidencia en la fila mostrada.
            System.out.println("\nFila: " + index);
            System.out.println("Columnas que coinciden: " + columnList);
            index++;
        }

        softAssertions.assertAll();
    }

    @Then("The text pagination updates correctly by {string}")
    public void validateTextPaginationUpdateCorrectly(String controlPagination) {
        String textPaginationToComplete = "1 of %s";
        String[] controlPaginationSplit = controlPagination.split(" ");
        int lastPage = (int) Math.ceil((double) countRegisters / Integer.parseInt(controlPaginationSplit[1]));
        String textPaginationExpected = String.format(textPaginationToComplete, lastPage);
        String textPaginationActual = webTablesPage.getTextPagination();
        Assert.assertEquals(textPaginationActual, textPaginationExpected);
    }

    @Then("the delete information should not be in the web table")
    public void theDeleteInformationShouldNotBeInTheWebTable() {
        List<Map<String,String>> dataTable = webTablesPage.getDataTable();
        SoftAssertions softAssertions = validateInformationIsNotInWebTable(dataTable);
        softAssertions.assertAll();
    }



}
