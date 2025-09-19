package service;

import Models.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ShopSale {
    private static List<Product> products = new ArrayList<>();
    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String INVENTORY_FILE = "inventory.txt";

    private static Product[][] inventoryGrid = new Product[3][3]; // 3 categories × 3 shelves

    public static List<Product> getProducts() {
        return products;
    }

    public static void setProducts(List<Product> products) {
        ShopSale.products = products;
    }

    public static Product[][] getInventoryGrid() {
        return inventoryGrid;
    }

    public static void setInventoryGrid(Product[][] inventoryGrid) {
        ShopSale.inventoryGrid = inventoryGrid;
    }

    public static double calculateSubtotal(Map<Product, Integer> cart) {
        double subtotal = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
        return subtotal;
    }

    public static double calculateSubtotal(Product[] products, int[] quantities) {
        double subtotal = 0;
        for (int i = 0; i < products.length; i++) {
            subtotal += products[i].getPrice() * quantities[i];
        }
        return subtotal;
    }

    public static double applyDiscount(double subtotal, double percent) {
        return subtotal - (subtotal * percent / 100.0);
    }

    public static double calculateTax(double amount, double taxPercent) {
        return amount * taxPercent / 100.0;
    }

    // (d) Generate receipt
    public static String generateReceipt(Map<Product, Integer> cart, double discountPercent, double taxPercent) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECEIPT =====\n");

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            sb.append(p.getName())
                    .append(" x ").append(qty)
                    .append(" = ").append(p.getPrice() * qty).append("\n");
        }

        double subtotal = calculateSubtotal(cart);
        double afterDiscount = applyDiscount(subtotal, discountPercent);
        double tax = calculateTax(afterDiscount, taxPercent);
        double total = afterDiscount + tax;

        sb.append("Subtotal: ").append(subtotal).append("\n");
        sb.append("Discount: ").append(discountPercent).append("%\n");
        sb.append("Tax: ").append(taxPercent).append("%\n");
        sb.append("TOTAL: ").append(total).append("\n");

        String receipt = sb.toString();
        System.out.println("Receipt length: " + receipt.length());
        System.out.println("Does receipt contain 'TOTAL'? " + receipt.contains("TOTAL"));

        return receipt;
    }

    public static void saveTransaction(String receipt) {
        try (FileWriter fw = new FileWriter(TRANSACTION_FILE, true)) {
            fw.write(receipt + "\n--------------------\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadInventory() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) return;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                try {
                    String code = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int qty = Integer.parseInt(parts[3]);
                    products.add(new Product(code, name, price, qty));
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + Arrays.toString(parts));
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void printInventoryGrid() {
        System.out.println("Inventory Grid (categories × shelves):");
        for (Product[] value : inventoryGrid) {
            for (Product product : value) {
                System.out.print((product != null ? product.getName() : "Empty") + "\t");
            }
            System.out.println();
        }
    }
}
