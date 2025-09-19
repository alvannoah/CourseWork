package Models;

import Exceptions.InsufficientStockException;

public class Product {
    private String code;
    private String name;
    private double price;
    private int quantity;

    public Product(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void restock(int qty) {
        this.quantity += qty;
    }

    public void sell(int qty) throws InsufficientStockException {
        if (qty > quantity) {
            throw new InsufficientStockException("Not enough stock for product " + name);
        }
        this.quantity -= qty;
    }

    @Override
    public String toString() {
        return code + " | " + name + " | Price: " + price + " | Stock: " + quantity;
    }
}
