package model_layer;

public class category {
    private int catgID;
    private String name;

    public category() {
    }

    public category(int catgID, String name) {
        this.catgID = catgID;
        this.name = name;
    }

    public int getCatgID() {
        return catgID;
    }

    public void setCatgID(int catgID) {
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
        return "Category{" +
                "catgID=" + catgID +
                ", name='" + name + '\'' +
                '}';
    }
}