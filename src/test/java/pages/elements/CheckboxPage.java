package pages.elements;

import model.CheckboxNode;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.BasePage;
import valueObject.CheckboxState;

import java.awt.*;
import java.util.*;
import java.util.List;

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
            String checkboxXpath = String.format(xpathCheckbox, checkboxLabel); //Armo el xpath
            checkboxNode = new CheckboxNode(parentCheckbox,checkboxState,checkboxLabel,checkboxXpath, level); //Creo mi nodo
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

    //Métodos públicos
    public void setCheckboxState(String parent, String initialState){
        createCheckboxNodes(null,0,listGlobal); //La primera vez, el locator enviado busca cualquier item del árbol que exista en el DOM


    }






}
