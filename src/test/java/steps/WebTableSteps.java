package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Employee;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import pages.elements.WebTablesPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebTableSteps {

    WebTablesPage webTablesPage = new WebTablesPage();
    private Employee employee;


    private List<String> getColumnMatchWithWord(Map<String,String> map, String word){
        List<String> columnList = new ArrayList<>();

        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue().toLowerCase().contains(word.toLowerCase())){
                columnList.add(entry.getKey());
            }
        }

        return columnList;
    }



    @Given("the user click the add button")
    public void theUserClickTheAddButton() {
        webTablesPage.clickAddButton();
    }

    @Given("the user type a {word} in the search box")
    public void theUserTypeWordInTheSearchBox(String searchWord) {
        webTablesPage.typeWordInTheSearchBox(searchWord);
    }

    @When("the user fill all the information requested by the form with:")
    public void theUserFillRegistrationForm(DataTable dataTable) {
        Map<String, String> map = dataTable.asMaps().getFirst();

        employee = new Employee(
                map.get("firstName"),
                map.get("lastName"),
                Integer.parseInt(map.get("age")),
                map.get("email"),
                Integer.parseInt(map.get("salary")),
                map.get("department")
        );

        webTablesPage.fillRegistrationForm(employee);

    }

    @When("the user click the button search")
    public void theUserClickTheButtonSearch() {
        webTablesPage.clickSearchButton();
    }

    @And("the user click the button submit")
    public void theUserClickTheSubmitButton() {
        webTablesPage.clickSubmitButton();
    }

    @Then("the all the information registered should be show in the web table")
    public void validationAllTheInformationWithTheWebTable() {
        List<Map<String,String>> dataTable = webTablesPage.getDataTable();
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


}
