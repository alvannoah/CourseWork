import Exceptions.InsufficientStockException;
import Models.PerishableProduct;
import Models.Product;
import service.ShopSale;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ShopSale.loadInventory();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Shop Menu ===");
            System.out.println("1. Add product");
            System.out.println("2. Add perishable product");
            System.out.println("3. Search product");
            System.out.println("4. Sell product");
            System.out.println("5. Restock product");
            System.out.println("6. Generate bill");
            System.out.println("7. Show stock summary");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter code,name,price,qty: ");
                    try {
                        String[] data = sc.nextLine().split(",");
                        Product p = new Product(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]));
                        ShopSale.getProducts().add(p);
                        System.out.println("Product added!");
                    } catch (Exception e) {
                        System.out.println("Error adding product.");
                    }
                    break;

                case 2:
                    System.out.print("Enter code,name,price,qty,expiry date(yyyy-MM-dd): ");
                    try {
                        String[] data = sc.nextLine().split(",");
                        PerishableProduct p = new PerishableProduct(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), LocalDate.parse(data[4]));
                        ShopSale.getProducts().add(p);
                        System.out.println("Product added!");
                    } catch (Exception e) {
                        System.out.println("Error adding product.");
                    }
                    break;

                case 3:
                    System.out.print("Enter product code: ");
                    String code = sc.nextLine();
                    Product found = ShopSale.getProducts().stream().filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
                    System.out.println(found != null ? found : "Not found.");
                    break;

                case 4:
                    System.out.print("Enter code and qty: ");
                    try {
                        String[] sellData = sc.nextLine().split(",");
                        String c = sellData[0];
                        int q = Integer.parseInt(sellData[1]);
                        Product prod = ShopSale.getProducts().stream().filter(p -> p.getCode().equals(c)).findFirst().orElse(null);
                        if (prod != null) {
                            prod.sell(q);
                            System.out.println("Sold " + q + " of " + prod.getName());
                        } else {
                            System.out.println("Product not found.");
                        }
                    } catch (InsufficientStockException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                    }
                    break;

                case 5:
                    System.out.print("Enter code and qty: ");
                    try {
                        String[] restockData = sc.nextLine().split(",");
                        String c = restockData[0];
                        int q = Integer.parseInt(restockData[1]);
                        Product prod = ShopSale.getProducts().stream().filter(p -> p.getCode().equals(c)).findFirst().orElse(null);
                        if (prod != null) {
                            prod.restock(q);
                            System.out.println("Restocked " + q + " of " + prod.getName());
                        } else {
                            System.out.println("Product not found.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                    }
                    break;

                case 6:
                    Map<Product, Integer> cart = new LinkedHashMap<>();
                    while (true) {
                        System.out.print("Enter code and qty (or 'done'): ");
                        String line = sc.nextLine();
                        if (line.equalsIgnoreCase("done")) break;
                        try {
                            String[] cartData = line.split(",");
                            String c = cartData[0];
                            int q = Integer.parseInt(cartData[1]);
                            Product prod = ShopSale.getProducts().stream().filter(p -> p.getCode().equals(c)).findFirst().orElse(null);
                            if (prod != null) {
                                prod.sell(q);
                                cart.put(prod, q);
                            } else {
                                System.out.println("Product not found.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
                        }
                    }
                    String receipt = ShopSale.generateReceipt(cart, 10.0, 18.0);
                    System.out.println(receipt);
                    ShopSale.saveTransaction(receipt);
                    break;

                case 7:
                    ShopSale.printInventoryGrid();
                    break;

                case 8:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}