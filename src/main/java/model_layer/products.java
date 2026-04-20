package model_layer;

public class products {
    private String productID;
    private String shopID;
    private String catgID;
    private String name;
    private double unitPrice;
    private int unitInStock;
    private String quantityPerUnit;
    private boolean discontinued;
    private String imagePath;

    public products() {
    }

    public products(String productID, String shopID, String catgID, String name,
                   double unitPrice, int unitInStock, String quantityPerUnit,
                   boolean discontinued, String imagePath) {
        this.productID = productID;
        this.shopID = shopID;
        this.catgID = catgID;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitInStock = unitInStock;
        this.quantityPerUnit = quantityPerUnit;
        this.discontinued = discontinued;
        this.imagePath = imagePath;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String string) {
        this.productID = string;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String string) {
        this.shopID = string;
    }

    public String getCatgID() {
        return catgID;
    }

    public void setCatgID(String string) {
        this.catgID = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    // can xem lai
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}