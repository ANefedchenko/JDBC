package sample.data_access;

import sample.Const;
import sample.entities.OrderEntity;
import sample.entities.OrderItemEntity;
import sample.entities.ProductEntity;
import sample.enums.ProductsStatus;
import sample.models.OrderInformationModel;
import sample.models.QuantityModel;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class OrdersDataAccess {
    private Connection dbConnection;

    public OrdersDataAccess()
            throws ClassNotFoundException, SQLException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("./config/local.properties"))){
            props.load(in);
        } catch (Exception ex) {
            System.out.println("Connection to the file is failed...");
            System.out.println(ex);
        }
        String connectionString = props.getProperty("url");
        String dbUser = props.getProperty("dbUser");
        String dbPass = props.getProperty("dbPass");

        try{
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createProduct(ProductEntity productEntity) {
        String sql = "INSERT INTO " + Const.PRODUCTS + "(" + Const.NAME + "," +
                Const.PRICE + "," + Const.STATUS + ")" + "VALUES(?,?,?)";

        try {
            PreparedStatement prSt = dbConnection.prepareStatement(sql);
            prSt.setString(1, productEntity.name);
            prSt.setInt(2, productEntity.price);
            prSt.setInt(3, productEntity.status.ordinal());
            prSt.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
        }
    }

    public ArrayList<ProductEntity> getAllProducts() {
        ArrayList<ProductEntity> products = new ArrayList<ProductEntity>();
        String sql = "SELECT " + Const.ID + ", " + Const.NAME + ", " + Const.PRICE + ", " + Const.STATUS + ", " + Const.CREATED_AT +
                " FROM " + Const.PRODUCTS;

        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ProductEntity productItem = new ProductEntity();
                productItem.id = resultSet.getInt(1);
                productItem.name = resultSet.getString(2);
                productItem.price = resultSet.getInt(3);
                productItem.status = ProductsStatus.valueOf(resultSet.getString(4));
                productItem.created_at = resultSet.getString(5);
                products.add(productItem);
            }
            return products;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ArrayList<ProductEntity>();
        }
    }

    public void deleteProduct(int value, String dbPass){
        String sql = "DELETE FROM " + Const.PRODUCTS + " WHERE " + Const.ID + " = ?";
        try{
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", dbPass);
            PreparedStatement prSt = dbConnection.prepareStatement(sql);
            prSt.setInt(1, value);
            prSt.execute();
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
        }
    }

    public void deleteAll(String dbPass){
        String sql = "DELETE FROM " + Const.PRODUCTS;

        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", dbPass);
            Statement statement = dbConnection.createStatement();
            statement.execute(sql);
        } catch (SQLException throwables) {
            System.out.println("Warning password!!!");
        }
    }

    public void createOrder(OrderEntity orderEntity, OrderItemEntity orderItemEntity) {
        String sql1 = "INSERT INTO " + Const.ORDERS + "(" + Const.USER_ID + ", " + Const.CREATED_AT + ")" + "VALUES(?,?)";
        String sql2 = "INSERT INTO " + Const.ORDER_ITEMS + "(" + Const.ORDER_ID + ", " + Const.PRODUCT_ID + ", " + Const.QUANTITY + ")" + "VALUES(last_insert_id(),?,?)";

        try {
            PreparedStatement prSt = dbConnection.prepareStatement(sql1);
            prSt.setInt(1, orderEntity.user_id);
            prSt.setString(2, orderEntity.create_at);
            prSt.execute();

            prSt = dbConnection.prepareStatement(sql2);
            prSt.setInt(1, orderItemEntity.product_id);
            prSt.setInt(2, orderItemEntity.quantity);
            prSt.execute();
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
        }
    }

    public ArrayList<OrderEntity> getAllOrders() {
        ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
        String sql = "SELECT * FROM " + Const.ORDERS;

        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                OrderEntity orderItem = new OrderEntity();
                orderItem.id = resultSet.getInt(1);
                orderItem.user_id = resultSet.getInt(2);
                orderItem.status = resultSet.getString(3);
                orderItem.create_at = resultSet.getString(4);
                orders.add(orderItem);
            }
            return orders;
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
            return new ArrayList<OrderEntity>();
        }
    }

    public ArrayList<QuantityModel> getSumQuantities() {
        ArrayList<QuantityModel> orders = new ArrayList<QuantityModel>();
        String sql = "SELECT order_items.product_id, products.name, SUM(order_items.quantity) AS sum " +
                "FROM order_items " +
                "INNER JOIN products " +
                "ON order_items.product_id = products.id " +
                "GROUP BY products.name " +
                "ORDER BY sum DESC;";

        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                QuantityModel orderProductUnique = new QuantityModel();
                orderProductUnique.id = resultSet.getInt(1);
                orderProductUnique.name = resultSet.getString(2);
                orderProductUnique.sum = resultSet.getInt(3);
                orders.add(orderProductUnique);
            }
            return orders;
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
            return new ArrayList<QuantityModel>();
        }
    }

    public ArrayList<OrderInformationModel> getAllOrderInformation(){
        ArrayList<OrderInformationModel> orders = new ArrayList<OrderInformationModel>();
        String sql = "SELECT orders.id, products.price * order_items.quantity AS sum, products.name, order_items.quantity, orders.created_at " +
                "FROM orders " +
                "INNER JOIN order_items " +
                "ON order_items.order_id = orders.id " +
                "INNER JOIN products " +
                "ON order_items.product_id = products.id " +
                "ORDER BY orders.id;";

        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                OrderInformationModel orderInformationModel = new OrderInformationModel();
                orderInformationModel.id = resultSet.getInt(1);
                orderInformationModel.sum = resultSet.getInt(2);
                orderInformationModel.name = resultSet.getString(3);
                orderInformationModel.quantity = resultSet.getInt(4);
                orderInformationModel.date = resultSet.getString(5);
                orders.add(orderInformationModel);
            }
            return orders;
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            throwables.printStackTrace();
            return new ArrayList<OrderInformationModel>();
        }
    }
}
