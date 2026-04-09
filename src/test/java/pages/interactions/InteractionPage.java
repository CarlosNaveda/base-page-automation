package pages.interactions;

import pages.BasePage;

public class InteractionPage extends BasePage {

    public InteractionPage() {
        super();
    }


    //Variables de opciones para Interactions
    String sortableOption = "//span[normalize-space()='Sortable']";
    String selectableOption = "//span[normalize-space()='Selectable']";
    String resizableOption = "//span[normalize-space()='Resizable']";
    String droppableOption = "//span[normalize-space()='Droppable']";
    String dragabbleOption = "//span[normalize-space()='Dragabble']";


    //Métodos públicos
    public SortablePage clickToSortableOption()
    {
        click(sortableOption);
        return new SortablePage();
    }

    public SelectablePage clickToSelectableOption()
    {
        click(selectableOption);
        return new SelectablePage();
    }

    public ResizablePage clickToResizableOption()
    {
        click(resizableOption);
        return new ResizablePage();
    }

    public DroppablePage clickToDroppableOption()
    {
        click(droppableOption);
        return new DroppablePage();
    }

    public DragabblePage clickToDragabbleOption()
    {
        click(dragabbleOption);
        return new DragabblePage();
    }

}
