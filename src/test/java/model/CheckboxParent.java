package model;

import java.util.List;

public class CheckboxParent {

    String parentName;
    String parentState;
    CheckboxChildren Children;

    public CheckboxParent(String parentName, String parentState) {
        this.parentName = parentName;
        this.parentState = parentState;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentState() {
        return parentState;
    }

    public void setParentState(String parentState) {
        this.parentState = parentState;
    }

    public CheckboxChildren getChildren() {
        return Children;
    }

    public void setChildren(CheckboxChildren children) {
        Children = children;
    }
}
