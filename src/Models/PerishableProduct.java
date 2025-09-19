package Models;

import Exceptions.InsufficientStockException;

import java.time.LocalDate;

public class PerishableProduct extends Product {
    private LocalDate expiryDate;

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public PerishableProduct(String code, String name, double price, int quantity, LocalDate expiryDate) {
        super(code, name, price, quantity);
        this.expiryDate = expiryDate;
    }

    @Override
    public void sell(int qty) throws InsufficientStockException {
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InsufficientStockException("Cannot sell expired product: " + getName());
        }
        super.sell(qty);
    }

    @Override
    public String toString() {
        return super.toString() + " | Expiry: " + expiryDate;
    }
}