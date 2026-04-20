package model_layer;

public class category {
    private String catgID;
    private String name;

    public category() {
    }

    public category(String catgID, String name) {
        this.catgID = catgID;
        this.name = name;
    }

    public String getCatgID() {
        return catgID;
    }

    public void setCatgID(String catgID) {
        this.catgID = catgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}