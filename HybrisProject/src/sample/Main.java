package sample;

import sample.enums.ProductsStatus;
import sample.models.*;
import sample.services.OrderService;
import sample.services.ProductService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    static ProductService productService = new ProductService();
    static OrderService orderService = new OrderService();

    public static void createOrder(OrderService orderService){
        OrderCreateModel orderCreateModel = new OrderCreateModel();

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        int randNumber = (int) (1000 + Math.random() * 9000);
        orderCreateModel.user_id = randNumber;
        orderCreateModel.date = date;

        ArrayList<ProductModel> productModels = productService.getAll();
        for (ProductModel item : productModels){
            System.out.printf("| ID: %-2s | name: %-10s | price: %-7d | status: %-12s |\n", item.id, item.name, item.price, item.status);
        }
        System.out.print(" \nYour order number is " + randNumber + "\n" +
                "Choose product to order (input an ID and press ENTER): ");

        Scanner scanner = new Scanner(System.in);
        orderCreateModel.product_id = scanner.nextInt();
        System.out.print("\nHow many products you want? -> ");
        scanner = new Scanner(System.in);
        orderCreateModel.quantity = scanner.nextInt();

        orderService.createOrder(orderCreateModel);
    }

    public static void createProduct(ProductService productService){
        Scanner scanner = new Scanner(System.in);

        ProductCreateModel productCreateModel = new ProductCreateModel();

        System.out.print("Input name of product: ");
        productCreateModel.name = scanner.nextLine();
        System.out.print("Input price: ");
        productCreateModel.price = scanner.nextInt();
        System.out.print("Choose status ==> 1 - out_of_stock, 2 - in_stock, 3 - running_low\n" +
                "Input a number and press ENTER: ");
        productCreateModel.status = ProductsStatus.values()[scanner.nextInt()];

        productService.createProduct(productCreateModel);
        System.out.printf("| name: %s | price: %d | status: %s | ==> add to stock \n",
                productCreateModel.name, productCreateModel.price, productCreateModel.status);
    }

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        String pass;

        System.out.print("********************************************************MENU********************************************************\n" +
                "1 Create Product\n" +
                "2 Create Order\n" +
                "3 Update Order Entries quantities\n" +
                "Show following views:\n" +
                "4 | Product Name | Product Price | Product Status | => for all products\n" +
                "5 List all products, which have been ordered at least once, with total ordered" +
                "quantity sorted descending by the quantity\n" +
                "6 | Order ID | Products total Price | Product Name | Products Quantity in orderEntry " +
                "| Order Created Date [YYYY-MM-DD HH:MM ] | => by order Id\n" +
                "7 List all orders using previous view\n" +
                "8 Remove product by ID\n" +
                "9 Remove all products\n\n" +
                "Choose an action (input a number and press ENTER): ");

        Scanner scanner = new Scanner(System.in);
        int choose = scanner.nextInt();
        while (choose != 0) {
            switch (choose){
                case 1:
                    createProduct(productService);
                    break;
                case 2:
                    createOrder(orderService);
                    break;
                case 3:
                    System.out.println("3!!!");
                    break;
                case 4:
                    ArrayList<ProductModel> productModels = productService.getAll();
                    for (ProductModel item : productModels){
                        System.out.printf("| name: %-10s | price: %-7d | status: %-12s |\n", item.name, item.price, item.status);
                    }
                    break;
                case 5:
                    ArrayList<QuantityModel> quantityModels = productService.getSumQuantities();
                    for (QuantityModel item : quantityModels) {
                        System.out.printf("| id: %-2d | name: %-10s | sum: %-7s |\n", item.id, item.name, item.sum);
                    }
                    break;
                case 6:
                    System.out.print("\nChoose ID of order (input a number and press ENTER): ");
                    scanner = new Scanner(System.in);
                    int id1 = scanner.nextInt();
                    OrderInformationModel orderInformationModel = orderService.getOrderInformationById(id1);
                    if (orderInformationModel != null){
                    System.out.printf("| id: %-2d | sum: %-7s | name: %-10s | quantity: %-3d | date: %-10s |\n",
                            orderInformationModel.id, orderInformationModel.sum, orderInformationModel.name,
                            orderInformationModel.quantity, orderInformationModel.date);}
                    else{
                        System.out.println("\nThere isn't such order!");
                    }
                    break;
                case 7:
                    ArrayList<OrderInformationModel> products2 = orderService.getAllOrderInformation();
                    for (OrderInformationModel item : products2) {
                        System.out.printf("| id: %-2d | sum: %-7s | name: %-10s | quantity: %-3d | date: %-10s |\n",
                                item.id, item.sum, item.name,  item.quantity, item.date);
                    }
                    break;
                case 8:
                    System.out.print("\nInput an ID of product (input a number and press ENTER): ");
                    scanner = new Scanner(System.in);
                    int id2 = scanner.nextInt();
                    System.out.print("\nInput the password: ");
                    scanner = new Scanner(System.in);
                    pass = scanner.nextLine();
                    ProductModel product = productService.deleteProduct(id2, pass);
                    if(product != null) {
                        System.out.printf("\nYou delete this product\n" +
                                "| id: %-2d | name: %-10s | price: %-7d | status: %-12s | date: %-10s |\n",
                                product.id, product.name, product.price, product.status, product.created_at);
                    } else{
                        System.out.println("\nThere isn't such product!");
                    }
                    break;
                case 9:
                    System.out.print("\nInput the password: ");
                    scanner = new Scanner(System.in);
                    pass = scanner.nextLine();
                    productService.deleteAll(pass);
                    break;
                default:
                    System.out.println("Wrong number!!!");
            }
            System.out.print("\nChoose an action (input a number and press ENTER): ");
            scanner = new Scanner(System.in);
            choose = scanner.nextInt();
        }
    }
}
