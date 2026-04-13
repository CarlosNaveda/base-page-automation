package model;

import valueObject.CheckboxState;
import java.util.List;

public class CheckboxNode {

    CheckboxNode parent;
    CheckboxState state;
    List<CheckboxNode> children;

    public CheckboxNode (CheckboxNode parent, CheckboxState state, List<CheckboxNode> children) {
        this.parent = parent;
        this.state = state;
        this.children = children;
        setSafeChildren(this, children); //Este nodo es el padre de la lista de hijos que llega
    }

    //Métodos para consistencia del modelo
    private void setSafeChildren(CheckboxNode checkboxNode, List<CheckboxNode> children) {
        //Si el nodo actual tiene hijos
        if(children != null) {
            for (CheckboxNode checkboxNodeChildren : children) {
                checkboxNodeChildren.setParent(checkboxNode);
            }
        }
    }

    //Métodos para manejar la transición de estados
    private void changeStateParentNode(CheckboxNode checkboxNode) {

        //Si tiene padre reviso
        if(checkboxNode.getParent() != null) {

            //Obtengo todos los hijos que tiene el padre
            List<CheckboxNode> children = checkboxNode.getParent().getChildren();

            int countChildren = children.size();
            int countNotSelected=0;
            int countSelected=0;

            //Recorro cada hijo
            for (CheckboxNode child : children) {

                //Si alguno de los hijos es "INDETERMINATE" ya termino y seteo al padre igual con "INDETERMINATE"
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

            //Solo comparo los conteos en el padre del nodo no esté ya seteado en "INDETERMINATE"
            if (!checkboxNode.getParent().getState().equals(CheckboxState.INDETERMINATE)) {
                if (countChildren==countNotSelected) {
                    //Todos los hijos están en "NOT_SELECTED", el padre debe cambiar a estado "NOT_SELECTED"
                    checkboxNode.getParent().setState(CheckboxState.NOT_SELECTED);
                }
                else if (countChildren==countSelected){
                    //Todos los hijos están en "SELECTED", el padre debe cambiar a estado "SELECTED"
                    checkboxNode.getParent().setState(CheckboxState.SELECTED);
                }
            }

            changeStateParentNode(checkboxNode.getParent()); //Mi referencia el padre de éste nodo

        }
    }

    private void changeStateNode(CheckboxNode checkboxNode) {

        //Si tiene hijos reviso
        if(checkboxNode.getChildren() != null) {
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

            //Evaluó los cambios a realizar en el padre (Propagación ascendente)
            changeStateParentNode(checkboxNode); //Mi referencia éste nodo
        }
    }

    private void changeStateChildrenNode(CheckboxNode checkboxNode,CheckboxState checkboxState) {

        //Recorro todos los hijos para setear sus estados
        for (CheckboxNode checkboxNodeChildren : checkboxNode.getChildren()) {
            checkboxNodeChildren.setState(checkboxState);

            //Si el hijo actual tiene más hijos
            if (checkboxNodeChildren.getChildren()!=null) {
                changeStateChildrenNode(checkboxNodeChildren,checkboxState); //Mi referencia el hijo de éste nodo
            }
        }
    }

    //Getters y Setters
    public CheckboxNode getParent() {
        return parent;
    }

    private void setParent(CheckboxNode parent) {
        this.parent = parent;
    }

    public CheckboxState getState() {
        return state;
    }

    public void setState(CheckboxState state) {
        this.state = state;
    }

    public List<CheckboxNode> getChildren() {
        return children;
    }

}
