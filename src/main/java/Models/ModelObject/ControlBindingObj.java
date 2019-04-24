package main.java.Models.ModelObject;

//CtrlBdgObj
public class ControlBindingObj {
    private String name;
    private String id;

    public ControlBindingObj(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}