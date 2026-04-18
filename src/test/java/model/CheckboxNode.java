package model;

import valueObject.CheckboxLabel;
import valueObject.CheckboxState;

import java.util.ArrayList;
import java.util.List;

public class CheckboxNode {

    CheckboxNode parent;
    CheckboxState state;
    CheckboxLabel labelDomain;
    String label;
    String xpath;
    int level;
    List<CheckboxNode> children;

    public CheckboxNode (CheckboxNode parent, CheckboxState state, CheckboxLabel labelDomain, String label, String xpath, int level, List<CheckboxNode> children) {
        this.parent = parent;
        this.state = state;
        this.labelDomain = labelDomain;
        this.children = children;
        this.label = label;
        this.xpath = xpath;
        this.level = level;
        setSafeParentChildren(this, children); //Este nodo es el padre de la lista de hijos que llega
    }

    public CheckboxNode (CheckboxNode parent, CheckboxState state, CheckboxLabel labelDomain, String label, String xpath,int level) {
        this.parent = parent;
        this.state = state;
        this.labelDomain = labelDomain;
        this.label = label;
        this.xpath = xpath;
        this.level = level;
    }

    //Métodos para consistencia del modelo
    public void setSafeParentChildren(CheckboxNode checkboxParent, List<CheckboxNode> children) {
            //A cada padre le seteo sus hijos
            checkboxParent.setChildren(children);

            //A cada hijo le estoy seteando su padre
            for (CheckboxNode checkboxNodeChildren : children) {
                checkboxNodeChildren.setParent(checkboxParent);

        }
    }

    //Métodos para manejar la transición de estados
    private static void changeStateParentNode(CheckboxNode checkboxNode) {
        //Si tiene padre reviso
        if(checkboxNode.getParent() != null) {

            //Obtengo todos los hijos que tiene el padre
            List<CheckboxNode> children = checkboxNode.getParent().getChildren();

            int countChildren = children.size();
            int countNotSelected=0;
            int countSelected=0;

            //Recorro cada hijo
            for (CheckboxNode child : children) {
                //Regla: Si alguno de los hijos es "INDETERMINATE" ya termino y seteo al padre igual con "INDETERMINATE"
                if (child.getState().equals(CheckboxState.INDETERMINATE)) {
                    checkboxNode.getParent().setState(CheckboxState.INDETERMINATE);
                    break; //Salgo del for
                }
                else {
                    switch (child.getState()) {
                        case NOT_SELECTED:
                            countNotSelected++;
                            break;

                        case SELECTED:
                            countSelected++;
                            break;
                    }
                }
            }

            //Comparo los conteos
            if (countChildren==countNotSelected) {
                //Todos los hijos están en "NOT_SELECTED", el padre debe cambiar a estado "NOT_SELECTED"
                checkboxNode.getParent().setState(CheckboxState.NOT_SELECTED);
            }
            else if (countChildren==countSelected){
                //Todos los hijos están en "SELECTED", el padre debe cambiar a estado "SELECTED"
                checkboxNode.getParent().setState(CheckboxState.SELECTED);
            }
            else  {
                //Los hijos tienen estados mezclados entre "SELECTED" y "NOT_SELECTED"
                checkboxNode.getParent().setState(CheckboxState.INDETERMINATE);
            }

            changeStateParentNode(checkboxNode.getParent()); //Mi referencia el padre de éste nodo
        }
    }

    public static void changeStateNode(CheckboxNode checkboxNode) {
        //En caso tenga hijos reviso
        if(!checkboxNode.getChildren().isEmpty()) {
        //Evalúo los cambios a realizar en los hijos (Propagación descendente)
            switch (checkboxNode.getState()) {
                case NOT_SELECTED: //Si el nodo se cambió a "NOT_SELECTED"
                    //Todos los nodos hijos quedan en "NOT_SELECTED"
                    changeStateChildrenNode(checkboxNode,CheckboxState.NOT_SELECTED);
                    break;

                case SELECTED: //Si el nodo se cambió a "SELECTED"
                    //Todos los nodos hijos quedan en "SELECTED"
                    changeStateChildrenNode(checkboxNode,CheckboxState.SELECTED);
                    break;
            }
        }

        //Evaluó los cambios a realizar en el padre (Propagación ascendente)
        changeStateParentNode(checkboxNode); //Mi referencia éste nodo
    }

    private static void changeStateChildrenNode(CheckboxNode checkboxNode,CheckboxState checkboxState) {
        //Recorro todos los hijos para setear sus estados
        for (CheckboxNode child : checkboxNode.getChildren()) {
            child.setState(checkboxState);

            //Si el hijo actual tiene más hijos
            if (!child.getChildren().isEmpty()) {
                changeStateChildrenNode(child,checkboxState); //Mi referencia el hijo de éste nodo
            }
        }
    }

    //Getters y Setters
    public CheckboxNode getParent() {
        return parent;
    }

    public CheckboxState getState() {
        return state;
    }

    public CheckboxLabel getLabelDomain() {return labelDomain;}

    public String getLabel() {
        return label;
    }

    public String getXpath() {
        return xpath;
    }

    public int getLevel() {
        return level;
    }

    public List<CheckboxNode> getChildren() {
        return children != null ? children : new ArrayList<CheckboxNode>(); // En caso sea nulo, creamos la lista vacía.
    }

    private void setParent(CheckboxNode parent) {
        this.parent = parent;
    }

    public void setState(CheckboxState state) {
        this.state = state;

    }

    public void setLabelDomain(CheckboxLabel labelDomain) {this.labelDomain = labelDomain;}

    public void setLabel(String label) {
        this.label = label;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    private void setChildren(List<CheckboxNode> children) {
        this.children = children;
    }


}
