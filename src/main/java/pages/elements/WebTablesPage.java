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

    String formFirstName = "//input[@id='firstName']";
    String formLastName = "//input[@id='lastName']";
    String formEmail = "//input[@id='userEmail']";
    String formAge = "//input[@id='age']";
    String formSalary = "//input[@id='salary']";
    String formDepartment = "//input[@id='department']";
    String formSubmitButton = "//button[@id='submit']";


    //Métodos privados
    private void showAllData(List<Map<String,String>> alldata) {
        for (Map<String,String> map : alldata) {
            for (String key : map.keySet()) {
                System.out.println(key + " : " + map.get(key));
            }
            System.out.println("\n");
        }
    }


    //Métodos públicos
    public void clickAddButton(){
        click(addButton);
    }

    public void fillRegistrationForm(Employee employee){
        setValueOnWebElement(formFirstName, employee.getFirstName());
        setValueOnWebElement(formLastName, employee.getLastName());
        setValueOnWebElement(formAge,String.valueOf(employee.getAge()));
        setValueOnWebElement(formEmail, employee.getEmail());
        setValueOnWebElement(formSalary,String.valueOf(employee.getSalary()));
        setValueOnWebElement(formDepartment, employee.getDepartment());
    }

    public void clickSubmitButton(){
        click(formSubmitButton);
    }

    public List<Map<String,String>> getDataTable(){

        List<Map<String,String>> alldata = new ArrayList<>();

        //Obtengo las filas existentes con data
        List<WebElement> rowsData = getAllWebElements(tableBodyRow);
        int maxRowsData = rowsData.size();

        //Recorro cada fila y busco las columnas para obtener los campos que son 6
        for (int i=1; i <= maxRowsData; i++) {

            List<WebElement> listData = getAllWebElements(String.format(tableBodyRowData, i)); //Recorro por índice de fila
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

            alldata.add(rows);

        }

     return alldata;
    }


}
