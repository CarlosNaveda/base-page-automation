package pages.elements;

import model.CheckboxNode;
import model.ResolverCheckboxLabel;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import static org.assertj.core.api.Assertions.assertThat;
import pages.BasePage;
import valueObject.CheckboxLabel;
import valueObject.CheckboxState;

import java.util.*;
import java.util.List;

import static model.CheckboxNode.changeStateNode;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

public class CheckboxPage extends BasePage {


    public CheckboxPage() {
        super();
    }


    //Variables
    private String treeItemNoIndent = "//div[@role='treeitem']";
    private String treeItemIndent = "//span[contains(@class,'rc-tree-indent-unit')]%s/ancestor::span[@class='rc-tree-indent']/ancestor::div[@role='treeitem']";
    private String switcherNoIndent = "//span[contains(@class,'rc-tree-switcher_open')]";
    private String switcherIndent = "//span[contains(@class,'rc-tree-indent-unit')]%s/ancestor::span[@class='rc-tree-indent']/following-sibling::span[contains(@class,'rc-tree-switcher_open')]";
    private String checkboxItemIn = ".//span[contains(@class,'rc-tree-checkbox')]";
    private String closedItemIn = "//span[@aria-label='%s']/preceding-sibling::span[contains(@class,'rc-tree-switcher_close')]";
    private String xpathCheckbox = "//span[@aria-label='%s']";
    private String rootTree = "//div[@class='rc-tree']";
    private String spanItem = "//span[@class='text-success']";
    private Map<CheckboxLabel, String> labelOutputs = Map.ofEntries(
            Map.entry(CheckboxLabel.HOME, "home"),
            Map.entry(CheckboxLabel.DESKTOP, "desktop"),
            Map.entry(CheckboxLabel.NOTES, "notes"),
            Map.entry(CheckboxLabel.COMMANDS, "commands"),
            Map.entry(CheckboxLabel.DOCUMENTS, "documents"),
            Map.entry(CheckboxLabel.WORKSPACE, "workspace"),
            Map.entry(CheckboxLabel.REACT, "react"),
            Map.entry(CheckboxLabel.ANGULAR, "angular"),
            Map.entry(CheckboxLabel.VEU, "veu"),
            Map.entry(CheckboxLabel.OFFICE, "office"),
            Map.entry(CheckboxLabel.PUBLIC, "public"),
            Map.entry(CheckboxLabel.PRIVATE, "private"),
            Map.entry(CheckboxLabel.CLASSIFIED, "classified"),
            Map.entry(CheckboxLabel.GENERAL, "general"),
            Map.entry(CheckboxLabel.DOWNLOADS, "downloads"),
            Map.entry(CheckboxLabel.WORD_FILE, "wordFile"),
            Map.entry(CheckboxLabel.EXCEL_FILE, "excelFile")
    );


    //Métodos privados
    private String getLocatorTreeItemIndent(int level, String noIndent, String indent) {

        String customizeIndent = "";
        int aux = level - 1;

        if (level == 0) {
            customizeIndent = noIndent;
        } else {
            String levelIndent = "%s/preceding-sibling::span[contains(@class,'rc-tree-indent-unit')]";
            customizeIndent = indent;

            while (aux > 0) {
                customizeIndent = String.format(customizeIndent, levelIndent);
                aux--;
            }
        }

        return String.format(customizeIndent, ""); //Para quitar el %s
    }

    //ARREGLAR
    private String getLocatorSwitcherIndent(int level, String noIndent, String indent) {

        String customizeIndent = "";
        int aux = level - 1;


        switch (aux) {
            case 0:
                customizeIndent = noIndent;
                break;

            case 1:
                customizeIndent = indent;
                break;

            default:
                String levelIndent = "%s/preceding-sibling::span[contains(@class,'rc-tree-indent-unit')]";
                customizeIndent = indent;


                while (aux > 1) {
                    customizeIndent = String.format(customizeIndent, levelIndent);
                    aux--;
                }
                break;

        }

        return String.format(customizeIndent, ""); //Para quitar el %s

    }


    private List<WebElement> getWebElementsByLevel(String locatorTree, String locatorCheckbox, Stack<String> stackLabelsNodes) {
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


    private void closeCheckboxNode(List<CheckboxNode> nodes) {
        //Hay que obtener el nivel más alto del árbol
        int maxLevel = nodes.stream()
                .mapToInt(CheckboxNode::getLevel)
                .max()
                .orElse(0);


        //Generamos el indent para el switcher open
        while (maxLevel > 0) {
            String switcherLocatorIndent = getLocatorSwitcherIndent(maxLevel, switcherNoIndent, switcherIndent); //Nivel Actual
            clickAll(switcherLocatorIndent);
            maxLevel--;
        }

    }

    private List<CheckboxNode> createCheckboxNodes(CheckboxNode parentCheckbox, int level, List<CheckboxNode> listGlobal, Stack<String> stackLabelsNodes) {

        List<CheckboxNode> listcheckboxNodes = new ArrayList<>();

        String treeItemLocatorIndent = getLocatorTreeItemIndent(level, treeItemNoIndent, treeItemIndent); //Nivel Actual
        List<WebElement> elements = getWebElementsByLevel(treeItemLocatorIndent, checkboxItemIn, stackLabelsNodes); //Obtengo los elementos del Nivel actual, pero verificando que aún no existan en mi stack de labels

        //Recorremos los checkbox del Nivel actual y los vamos abriendo
        for (WebElement element : elements) {
            CheckboxNode checkboxNode;
            WebElement checkboxDomElement = getWebElementInside(element, checkboxItemIn); //Dentro de cada item del árbol, tengo que ubicar el elemento del checkbox
            CheckboxState checkboxState = getStateOfCheckbox(checkboxDomElement); //Obtengo el estado
            String checkboxLabel = checkboxDomElement.getAttribute("aria-label"); //Consigo el label
            CheckboxLabel labelDomain = ResolverCheckboxLabel.getMapLabel(checkboxLabel);
            String checkboxXpath = String.format(xpathCheckbox, checkboxLabel); //Armo el xpath
            checkboxNode = new CheckboxNode(parentCheckbox, checkboxState, labelDomain, checkboxLabel, checkboxXpath, level); //Creo mi nodo
            stackLabelsNodes.push(checkboxLabel); //Actualizo mi stack de labels
            //Actualizo listas
            listcheckboxNodes.add(checkboxNode); //Esta es la lista local para manejar la asignación de nodos a sus padres
            listGlobal.add(checkboxNode); //Esta es la lista global para ver resultados totales


            boolean isOpen = openCheckboxNode(checkboxLabel); //Reviso el checkbox actual pudo abrir +

            //¿Se pudo abrir?
            if (isOpen) {
                int nextLevel = level + 1;
                //Recursión - Todos los nodos que obtenga de adentro serán hijos del padre actual
                List<CheckboxNode> checkboxNodesIn = createCheckboxNodes(checkboxNode, nextLevel, listGlobal, stackLabelsNodes);
                //Completo la relación Padre con Hijos
                checkboxNode.setSafeParentChildren(checkboxNode, checkboxNodesIn);
            }


        }
        return listcheckboxNodes;
    }

    private CheckboxState getStateOfCheckbox(WebElement checkboxWebElement) {
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

    private void getListGlobal(List<CheckboxNode> nodes) {
        for (CheckboxNode checkboxNode : nodes) {
            int auxLevel = checkboxNode.getLevel();
            StringBuilder tab = new StringBuilder("\t");
            while (auxLevel > 0) {
                tab.append(tab);
                auxLevel--;
            }

            System.out.println("\n" + tab + "Datos del nodo actual: " + checkboxNode);
            System.out.println(tab + "Padre: " + checkboxNode.getParent());
            System.out.println(tab + "Estado: " + checkboxNode.getState());
            System.out.println(tab + "Label Domain: " + checkboxNode.getLabelDomain());
            System.out.println(tab + "Label: " + checkboxNode.getLabel());
            System.out.println(tab + "Xpath: " + checkboxNode.getXpath());
            System.out.println(tab + "Nivel: " + checkboxNode.getLevel());
            System.out.println(tab + "Hijos: " + checkboxNode.getChildren());
        }
    }

    private CheckboxNode findNode(String label, List<CheckboxNode> nodes) {
        CheckboxLabel labelDomain = CheckboxLabel.valueOf(label);

        return nodes.stream()
                .filter(node -> node.getLabelDomain().equals(labelDomain))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el label:  " + labelDomain));
    }

    private void clickAndUpdateStates(CheckboxNode checkboxNode, CheckboxState state) {
        click(checkboxNode.getXpath()); //1 click al propio checkbox
        checkboxNode.setState(state); //Actualizo su estado
        changeStateNode(checkboxNode); //Actualizo mis demás nodos en base al nodo actualizado.
    }

    private void checkboxChangeState(String label, CheckboxState toState, List<CheckboxNode> nodes) {
        CheckboxNode checkboxNode = findNode(label, nodes);
        String transitionStates = checkboxNode.getState() + "-" + toState;

        //Realizo la transición de estado
        switch (transitionStates) {
            case "NOT_SELECTED-SELECTED": // Checkbox No Seleccionado --> Checkbox Seleccionado
                //1 click al propio checkbox
                clickAndUpdateStates(checkboxNode, CheckboxState.SELECTED);
                break;

            case "NOT_SELECTED-INDETERMINATE": // Checkbox No Seleccionado --> Checkbox Indeterminado
                //pasar a true al menos a alguno de sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están en false
                int randomNumber = generateRandomNumber(checkboxNode.getChildren().size());
                int counter = 1;

                //Recorremos los hijos del padre que llega
                for (CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en mixed
                    if (!checkboxNode.getState().equals(CheckboxState.INDETERMINATE) && counter == randomNumber) {
                        clickAndUpdateStates(children, CheckboxState.SELECTED);
                    }
                    counter++;
                }
                break;

            case "SELECTED-NOT_SELECTED": // Checkbox Seleccionado --> Checkbox No Seleccionado
                //1 click al propio checkbox
                clickAndUpdateStates(checkboxNode, CheckboxState.NOT_SELECTED);
                break;

            case "SELECTED-INDETERMINATE": // Checkbox Seleccionado --> Checkbox Indeterminado
                //pasar a false al menos a uno de sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están en true
                int randomNumber2 = generateRandomNumber(checkboxNode.getChildren().size());
                int counter2 = 1;

                //Recorremos los hijos del padre que llega
                for (CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en mixed
                    if (!checkboxNode.getState().equals(CheckboxState.INDETERMINATE) && counter2 == randomNumber2) {
                        clickAndUpdateStates(children, CheckboxState.NOT_SELECTED);
                    }

                    counter2++;
                }
                break;

            case "INDETERMINATE-NOT_SELECTED": // Checkbox Indeterminado --> Checkbox No Seleccionado
                //pasar a false a todos sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están entre true y false mezclados
                //Recorremos los hijos del padre que llega
                for (CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en false
                    if (!checkboxNode.getState().equals(CheckboxState.NOT_SELECTED)) {
                        //Buscamos los hijos que estén en true
                        if (children.getState().equals(CheckboxState.SELECTED)) {
                            clickAndUpdateStates(children, CheckboxState.NOT_SELECTED);
                        }
                    }
                }

                break;

            case "INDETERMINATE-SELECTED": // Checkbox Indeterminado --> Checkbox Seleccionado
                //pasar a true a todos sus checkbox hijos
                //Regla: Si entra aquí es porque sus hijos están entre true y false mezclados
                //Recorremos los hijos del padre que llega
                for (CheckboxNode children : checkboxNode.getChildren()) {
                    //Si el padre todavía no está en true
                    if (!checkboxNode.getState().equals(CheckboxState.NOT_SELECTED)) {
                        //Buscamos los hijos que estén en false
                        if (children.getState().equals(CheckboxState.NOT_SELECTED)) {
                            clickAndUpdateStates(children, CheckboxState.SELECTED);
                        }
                    }
                }
                break;
        }

    }


    private Boolean openCheckboxNode(String checkboxLabel) {
        boolean isOpen = false;

        //Armo el xpath
        String switcherXpath = String.format(closedItemIn, checkboxLabel);
        WebElement itemClose = getWebElement(switcherXpath); //Dentro de cada item del árbol, busco si existe elemento +

        if (itemClose != null) {
            click(switcherXpath);
            isOpen = true;
        }

        return isOpen;
    }


    private void waitForCheckboxTreeToLoad() {
        getWebElementPresent(rootTree);
    }

    private List<CheckboxNode> buildTree() {
        waitForCheckboxTreeToLoad();
        Stack<String> stackLabelsNodes = new Stack<>();
        List<CheckboxNode> nodes = new ArrayList<>();
        createCheckboxNodes(null, 0, nodes, stackLabelsNodes); //Mapeo del árbol dinámicamente
        return nodes;
    }

    private List<String> getResultsLabels(String locator) {
        List<WebElement> spanWebElements = getAllWebElements(locator);
        List<String> resultLabels = new ArrayList<>();
        for (WebElement spanWebElement : spanWebElements) {
            resultLabels.add(spanWebElement.getText());
        }
        return resultLabels;
    }


    //Métodos públicos

    public void setCheckboxState(String label, String state) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxState toState = CheckboxState.valueOf(state);
        checkboxChangeState(label, toState, nodes);
        closeCheckboxNode(nodes);
    }

    public void setCheckboxesState(String label, String state) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxState toState = CheckboxState.valueOf(state);
        CheckboxNode parentNode = findNode(label, nodes);
        List<CheckboxNode> children = parentNode.getChildren();
        for (CheckboxNode child : children) {
            checkboxChangeState(child.getLabelDomain().toString(), toState, nodes);
        }
        closeCheckboxNode(nodes);
    }

    public void setChildrenAllSelectedExceptOne(String label) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode parentNode = findNode(label, nodes);
        List<CheckboxNode> children = parentNode.getChildren();

        int randomNumber = generateRandomNumber(children.size());
        int counter = 1;

        //Recorremos los hijos del padre que llega
        for(CheckboxNode child : children) {
            //Solo uno al azar lo dejaremos sin seleccionar
            if (counter==randomNumber)  {
                checkboxChangeState(child.getLabelDomain().toString(),CheckboxState.NOT_SELECTED,nodes);
            }
            else{
                checkboxChangeState(child.getLabelDomain().toString(),CheckboxState.SELECTED,nodes);
            }
            counter++;
        }

        closeCheckboxNode(nodes);

    }

    public void setChildrenAllSelected(String label) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode parentNode = findNode(label, nodes);
        List<CheckboxNode> children = parentNode.getChildren();

        //Recorremos los hijos del padre que llega
        for(CheckboxNode child : children) {
            checkboxChangeState(child.getLabelDomain().toString(),CheckboxState.SELECTED,nodes);
        }

        closeCheckboxNode(nodes);

    }


    public void selectionContextIsInitialState(String Context, String parent,String state) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(parent,nodes);
        CheckboxState checkboxState = CheckboxState.valueOf(state);
        List<CheckboxNode> children = checkboxNode.getChildren();

        switch (Context) {
            case "a single child":
                int randomNumber = generateRandomNumber(checkboxNode.getChildren().size());
                int counter = 1;

                //Recorremos los hijos del padre que llega
                for(CheckboxNode child : children) {
                    if (counter==randomNumber)  {
                        checkboxChangeState(child.getLabelDomain().toString(),checkboxState,nodes);
                    }
                    counter++;
                }
                break;

            case "all children":
                //Recorremos los hijos del padre que llega
                for(CheckboxNode child : children) {
                    checkboxChangeState(child.getLabelDomain().toString(),checkboxState,nodes);
                }
                break;

            case "the last child":
                checkboxChangeState(children.getLast().getLabelDomain().toString(),checkboxState,nodes);
                break;
        }

        closeCheckboxNode(nodes);

    }

    public void validationFinalState(String label, String finalState){
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(label,nodes);
        CheckboxState checkboxState = CheckboxState.valueOf(finalState);
        Assert.assertEquals(checkboxNode.getState(),checkboxState,"Los estados no coinciden");
    }

    public void validationChildrenFinalState(String label, String finalState) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(label,nodes);
        List<CheckboxNode> children = checkboxNode.getChildren();
        CheckboxState checkboxState = CheckboxState.valueOf(finalState);
        SoftAssert softAssert = new SoftAssert();

        for (CheckboxNode child : children) {
            softAssert.assertEquals(child.getState(),checkboxState,"Los estados no coinciden");
        }

        softAssert.assertAll();
    }

    public void validationElementOnTheTextOutput(String element, String expectedBehavior) {
        List<CheckboxNode> nodes = buildTree();
        List<String> resultLabels = getResultsLabels(spanItem);
        CheckboxNode checkboxNode = findNode(element,nodes);
        String labelExpected = labelOutputs.get(checkboxNode.getLabelDomain());

        if (expectedBehavior.equals("should be")) {
            assertThat(resultLabels).as("El output debería contener el label seleccionado").contains(labelExpected);
        }
        else if (expectedBehavior.equals("should not be")) {
            assertThat(resultLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
        }

    }

    public void validationParentChildrenOnTheTextOutput(String parent, String expectedBehavior) {
        List<CheckboxNode> nodes = buildTree();
        List<String> resultLabels = getResultsLabels(spanItem);
        CheckboxNode parentNode = findNode(parent,nodes);
        List<CheckboxNode> children = parentNode.getChildren();
        List<String> labelsExpected = new ArrayList<>();
        labelsExpected.add(labelOutputs.get(parentNode.getLabelDomain())); // El padre

        for (CheckboxNode child : children) {
            labelsExpected.add(labelOutputs.get(child.getLabelDomain())); // Los Hijos
        }

        SoftAssertions softAssert = new SoftAssertions ();

        if (expectedBehavior.equals("should be")) {
            for (String labelExpected : labelsExpected) {
                softAssert.assertThat(resultLabels).as("El output debería contener el label seleccionado").contains(labelExpected);
            }
            softAssert.assertAll();
        }
        else if (expectedBehavior.equals("should not be")) {

            for (String labelExpected : labelsExpected) {
                softAssert.assertThat(resultLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
            }
            softAssert.assertAll();
        }

    }

    public void validationParentChildOnTheTextOutput(String parent, String expectedBehavior) {
        List<CheckboxNode> nodes = buildTree();
        List<String> resultLabels = getResultsLabels(spanItem);
        CheckboxNode parentNode = findNode(parent,nodes);
        List<CheckboxNode> children = parentNode.getChildren();
        List<String> labelsExpected = new ArrayList<>();
        labelsExpected.add(labelOutputs.get(parentNode.getLabelDomain())); // El padre

        for (CheckboxNode child : children) {
            if (child.getState().equals(CheckboxState.NOT_SELECTED)) { //El hijo no seleccionado
                labelsExpected.add(labelOutputs.get(child.getLabelDomain()));
                break; // Solo debe ser uno
            }
        }

        SoftAssertions softAssert = new SoftAssertions ();
        for (String labelExpected : labelsExpected) {
            softAssert.assertThat(resultLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
        }
        softAssert.assertAll();
    }



    public void theUserActionTheElement(String action, String label) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(label,nodes);
        CheckboxState checkboxState = CheckboxState.valueOf(action);
        clickAndUpdateStates(checkboxNode,checkboxState);
        closeCheckboxNode(nodes);
    }



    public void theUserActionContext(String action, String contextOfSelection, String parent) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(parent,nodes);
        List<CheckboxNode> children = checkboxNode.getChildren();
        CheckboxState checkboxState = CheckboxState.valueOf(action);


        switch (contextOfSelection) {
            case "a single child":
                int randomNumber = generateRandomNumber(checkboxNode.getChildren().size());
                int counter = 1;

                //Recorremos los hijos del padre que llega
                for(CheckboxNode child : children) {
                    if (counter==randomNumber)  {
                        checkboxChangeState(child.getLabelDomain().toString(),checkboxState,nodes);
                    }
                    counter++;
                }

                break;

            case "all children":
                //Recorremos los hijos del padre que llega
                for(CheckboxNode child : children) {
                    checkboxChangeState(child.getLabelDomain().toString(),checkboxState,nodes);
                }

                break;

            case "the last child":
                checkboxChangeState(children.getLast().getLabelDomain().toString(),checkboxState,nodes);
                break;
        }

        closeCheckboxNode(nodes);

    }

    public void theUserActionOnChildren(String action, String parent) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(parent,nodes);
        List<CheckboxNode> children = checkboxNode.getChildren();
        CheckboxState toState = CheckboxState.valueOf(action);

        for (CheckboxNode child : children) {
            checkboxChangeState(child.getLabelDomain().toString(),toState,nodes);
        }

        closeCheckboxNode(nodes);

    }

    public void theUserSelectTheLastChildNotSelected(String parent) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(parent,nodes);
        List<CheckboxNode> children = checkboxNode.getChildren();

        for (CheckboxNode child : children) {
            if(child.getState().equals(CheckboxState.NOT_SELECTED)) { //Solo cambio el que no está seleccionado
                checkboxChangeState(child.getLabelDomain().toString(),CheckboxState.SELECTED,nodes);
                break; // Debe ser solo uno.
            }
        }

        closeCheckboxNode(nodes);

    }

    public void theUserDeselectOneOfChild(String parent) {
        List<CheckboxNode> nodes = buildTree();
        CheckboxNode checkboxNode = findNode(parent,nodes);
        List<CheckboxNode> children = checkboxNode.getChildren();

        int randomNumber = generateRandomNumber(children.size());
        int counter = 1;

        //Recorremos los hijos del padre que llega
        for(CheckboxNode child : children) {
            //Solo uno al azar lo dejaremos sin seleccionar
            if (counter==randomNumber)  {
                checkboxChangeState(child.getLabelDomain().toString(),CheckboxState.NOT_SELECTED,nodes);
                break;
            }
            counter++;
        }

        closeCheckboxNode(nodes);

    }



}
