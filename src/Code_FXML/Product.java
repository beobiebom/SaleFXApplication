package Code_FXML;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Product {
    private int stt;
    private String name;
    private IntegerProperty quantity=new SimpleIntegerProperty(1);
    private int cost;
    private int costsum;


    public Product(int stt, String name, int cost, int costsum) {
        this.stt = stt;
        this.name = name;
        this.cost = cost;
        this.costsum=costsum;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCostsum() {
        return costsum;
    }

    public void setCostsum(int costsum) {
        this.costsum = costsum;
    }
}
