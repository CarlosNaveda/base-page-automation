package model;

import java.util.List;

public class CheckboxChildren {
    String ChildrenName;
    String ChildrenState;

    public CheckboxChildren(String ChildrenName, String ChildrenState) {
        this.ChildrenName = ChildrenName;
        this.ChildrenState = ChildrenState;
    }

    public String getChildrenName() {
        return ChildrenName;
    }

    public void setChildrenName(String childrenName) {
        ChildrenName = childrenName;
    }

    public String getChildrenState() {
        return ChildrenState;
    }

    public void setChildrenState(String childrenState) {
        ChildrenState = childrenState;
    }
}
