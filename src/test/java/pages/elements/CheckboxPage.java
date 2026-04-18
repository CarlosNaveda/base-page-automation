package pages.elements;

import model.CheckboxNode;
import model.ResolverCheckboxLabel;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.BasePage;
import valueObject.CheckboxLabel;
import valueObject.CheckboxState;

import java.awt.*;
import java.util.*;
import java.util.List;

import static model.CheckboxNode.changeStateNode;

public class CheckboxPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CheckboxPage.class);

    public CheckboxPage() {
        super();
    }

    //Variables Globales
    Stack<String> stackLabelsNodes = new Stack<>();
    List<CheckboxNode> listGlobal = new ArrayList<>();

    private String noIndent = "//div[@role='treeitem']";
    private String indent ="//span[contains(@class,'rc-tree-indent-unit')]%s/ancestor::span[@class='rc-tree-indent']/ancestor::div[@role='treeitem']";
    private String checkboxItemIn = ".//span[contains(@class,'rc-tree-checkbox')]";
    private String closedItemIn = "//span[@aria-label='%s']/preceding-sibling::span[contains(@class,'rc-tree-switcher_close')]";
    private String xpathCheckbox= "//span[@aria-label='%s']";


    //Métodos privados
    private String getLocatorIndent(int level) {

        if (level == 0) {
            return noIndent;
        }//En caso sea otro número tenemos que modificar el indent aumentándole cada nivel el siguiente texto:
        String plusLevel = "%s/preceding-sibling::span[contains(@class,'rc-tree-indent-unit')]";
        String customizeIndent = indent;
        int aux = level - 1;

        while (aux > 0) {
            customizeIndent = String.format(customizeIndent, plusLevel);
            aux--;
        }

        return String.format(customizeIndent, ""); //Para quitar el %s

    }




    private List<WebElement> getWebElementsByLevel(String locatorTree,String locatorCheckbox, Stack<String> stackLabelsNodes) {
        List<WebElement> elementsToReturn = new ArrayList<>();
        List<WebElement> elements = getAllWebElements(locatorTree); //Tengo los elementos Tree con el locator

        for (WebElement element : elements) {
            //Busco el elemento checkbox dentro del elemento tree
            WebElement checkboxDomElement = getWebElementInside(element, locatorCheckbox); //Dentro de cada item del árbol, tengo que ubicar el elemento del checkbox
            String checkboxLabel = checkboxDomElement.getAttribute("aria-label"); //Consigo el label

            if (!stackLabelsNodes.contains(checkboxLabel)) {
                elementsToReturn.add(element);
            }
        }

        return elementsToReturn;
    }



    private List<CheckboxNode> createCheckboxNodes(CheckboxNode parentCheckbox,int level,List<CheckboxNode> listGlobal) {
        List<CheckboxNode> listcheckboxNodes = new ArrayList<>();

        String locatorIndent = getLocatorIndent(level); //Nivel Actual
        List<WebElement> elements = getWebElementsByLevel(locatorIndent,checkboxItemIn,stackLabelsNodes); //Obtengo los elementos del Nivel actual, pero verificando que aún no existan en mis checkboxNodes

        //Recorremos los checkbox del Nivel actual y los vamos abriendo
        for (WebElement element : elements) {
            CheckboxNode checkboxNode;
            WebElement checkboxDomElement = getWebElementInside(element, checkboxItemIn); //Dentro de cada item del árbol, tengo que ubicar el elemento del checkbox
            CheckboxState checkboxState = getStateOfCheckbox(checkboxDomElement); //Obtengo el estado
            String checkboxLabel = checkboxDomElement.getAttribute("aria-label"); //Consigo el label
            CheckboxLabel labelDomain = ResolverCheckboxLabel.getMapLabel(checkboxLabel);
            String checkboxXpath = String.format(xpathCheckbox, checkboxLabel); //Armo el xpath
            checkboxNode = new CheckboxNode(parentCheckbox,checkboxState,labelDomain,checkboxLabel,checkboxXpath, level); //Creo mi nodo
            stackLabelsNodes.push(checkboxLabel); //Actualizo mi stack de labels
            //Actualizo listas
            listcheckboxNodes.add(checkboxNode); //Esta es la lista local para manejar la asignación de nodos a sus padres
            listGlobal.add(checkboxNode); //Esta es la lista global para ver resultados totales


            boolean isOpen = openCheckboxNode(checkboxLabel); //Reviso el checkbox actual pudo abrir +

            //¿Se pudo abrir?
            if (isOpen) {
                int nextLevel= level + 1;
                //Recursión - Todos los nodos que obtenga de adentro serán hijos del padre actual
                List<CheckboxNode> checkboxNodesIn = createCheckboxNodes(checkboxNode, nextLevel,listGlobal);
                //Completo la relación Padre con Hijos
                checkboxNode.setSafeParentChildren(checkboxNode, checkboxNodesIn);
            }
        }
        return listcheckboxNodes;
    }

    private CheckboxState getStateOfCheckbox (WebElement checkboxWebElement) {
        String state = checkboxWebElement.getAttribute("aria-checked");
        CheckboxState checkboxState = null;
        switch (state) {
            case "true":
                checkboxState = CheckboxState.SELECTED;
                break;
            case "false":
                checkboxState = CheckboxState.NOT_SELECTED;
                break;
            case "mixed":
                checkboxState = CheckboxState.INDETERMINATE;
                break;
            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        return checkboxState;
    }

    private void getListGlobal(){
        for (CheckboxNode checkboxNode : listGlobal) {
            int auxLevel = checkboxNode.getLevel();
            StringBuilder tab = new StringBuilder("\t");
            while (auxLevel > 0) {
                tab.append(tab);
                auxLevel--;
            }

            System.out.println("\n"+tab+"Datos del nodo actual: " + checkboxNode);
            System.out.println(tab+"Padre: " + checkboxNode.getParent());
            System.out.println(tab+"Estado: " + checkboxNode.getState());
            System.out.println(tab+"Label Domain: " + checkboxNode.getLabelDomain());
            System.out.println(tab+"Label: " + checkboxNode.getLabel());
            System.out.println(tab+"Xpath: " + checkboxNode.getXpath());
            System.out.println(tab+"Nivel: " + checkboxNode.getLevel());
            System.out.println(tab+"Hijos: " + checkboxNode.getChildren());
        }
    }

    private CheckboxNode findNode(String label){
        CheckboxNode checkboxNode = null;
        CheckboxLabel labelDomain = CheckboxLabel.valueOf(label);

        return listGlobal.stream()
                .filter( node -> node.getLabelDomain()
                .equals(labelDomain))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el label:  "+labelDomain));
    }

    private String getTransitionStates(CheckboxNode checkboxNode, String State){
        //Concatenamos el estado actual con lo esperado
        CheckboxState toState = CheckboxState.valueOf(State);
        return checkboxNode.getState() + "-" + toState;
    }

    private void clickAndUpdateStates(CheckboxNode checkboxNode, CheckboxState state){
        click(checkboxNode.getXpath()); //1 click al propio checkbox
        checkboxNode.setState(state); //Actualizo su estado
        changeStateNode(checkboxNode); //Actualizo mis demás nodos en base al nodo actualizado.
    }

    private void checkboxChangeState(String label,String state) {

        CheckboxNode checkboxNode = findNode(label);
        String transitionStates = getTransitionStates(checkboxNode,state);

        //Realizo la transición de estado
        switch (transitionStates) {
            case "NOT_SELECTED-SELECTED": // Checkbox No Seleccionado --> Checkbox Seleccionado
                //1 click al propio checkbox
                clickAndUpdateStates(checkboxNode,CheckboxState.SELECTED);
                break;

            case "NOT_SELECTED-INDETERMINATE": // Checkbox No Seleccionado --> Checkbox Indeterminado
                //pasar a true al menos a alguno de sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están en false
                int randomNumber = generateRandomNumber(checkboxNode.getChildren().size());
                int counter = 1;

                //Recorremos los hijos del padre que llega
                for(CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en mixed
                    if (!checkboxNode.getState().equals(CheckboxState.INDETERMINATE) && counter==randomNumber)  {
                        clickAndUpdateStates(children,CheckboxState.SELECTED);
                    }
                    counter++;
                }
                break;

            case "SELECTED-NOT_SELECTED": // Checkbox Seleccionado --> Checkbox No Seleccionado
                //1 click al propio checkbox
                clickAndUpdateStates(checkboxNode,CheckboxState.NOT_SELECTED);
                break;

            case "SELECTED-INDETERMINATE": // Checkbox Seleccionado --> Checkbox Indeterminado
                //pasar a false al menos a uno de sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están en true
                int randomNumber2 = generateRandomNumber(checkboxNode.getChildren().size());
                int counter2 = 1;

                //Recorremos los hijos del padre que llega
                for(CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en mixed
                    if (!checkboxNode.getState().equals(CheckboxState.INDETERMINATE) && counter2==randomNumber2) {
                        clickAndUpdateStates(children,CheckboxState.NOT_SELECTED);
                    }

                    counter2++;
                }
                break;

            case "INDETERMINATE-NOT_SELECTED": // Checkbox Indeterminado --> Checkbox No Seleccionado
                //pasar a false a todos sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están entre true y false mezclados
                //Recorremos los hijos del padre que llega
                for(CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en false
                    if (!checkboxNode.getState().equals(CheckboxState.NOT_SELECTED)) {
                        //Buscamos los hijos que estén en true
                        if (children.getState().equals(CheckboxState.SELECTED)) {
                            clickAndUpdateStates(children,CheckboxState.NOT_SELECTED);
                        }
                    }
                }

                break;

            case "INDETERMINATE-SELECTED": // Checkbox Indeterminado --> Checkbox Seleccionado
                //pasar a true a todos sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están entre true y false mezclados
                //Recorremos los hijos del padre que llega
                for(CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en true
                    if (!checkboxNode.getState().equals(CheckboxState.NOT_SELECTED)) {
                    //Buscamos los hijos que estén en false
                        if (children.getState().equals(CheckboxState.NOT_SELECTED)) {
                            clickAndUpdateStates(children,CheckboxState.SELECTED);
                        }
                    }
                }
                break;
        }
//        getListGlobal();
    }


    private Boolean openCheckboxNode(String checkboxLabel){
        boolean isOpen=false;

        //Armo el xpath
        String switcherXpath = String.format(closedItemIn,checkboxLabel);
        WebElement itemClose = getWebElement(switcherXpath); //Dentro de cada item del árbol, busco si existe elemento +

        if (itemClose!=null) {
            click(switcherXpath);
            isOpen=true;
        }

        return isOpen;
    }

    //Métodos públicos
    public void setCheckboxState(String label, String state){
        createCheckboxNodes(null,0,listGlobal); //Mapeo del árbol dinámicamente
        checkboxChangeState(label,state);
    }

    public void validationFinalState(String label, String finalState){
        CheckboxNode checkboxNode = findNode(label);
        CheckboxState checkboxState = CheckboxState.valueOf(finalState);
        Assert.assertEquals(checkboxNode.getState(),checkboxState,"Los estados no coinciden");
    }






}
