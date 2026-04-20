package model_layer;

public class CartItem {

    private products product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // ===== GETTER / SETTER =====

    public products getProduct() {
        return product;
    }

    public void setProduct(products product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            quantity = 1;
        }
        this.quantity = quantity;
    }

    // ===== LOGIC =====

    // tổng tiền của dòng này
    public double getLineTotal() {
        if (product == null) return 0;
        return product.getUnitPrice() * quantity;
    }

    // tăng số lượng
    public void increaseQuantity() {
        this.quantity++;
    }

    // giảm số lượng
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    // set trực tiếp
    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) newQuantity = 1;
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}