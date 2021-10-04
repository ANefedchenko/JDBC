package sample.services;

import sample.data_access.OrdersDataAccess;
import sample.entities.ProductEntity;
import sample.models.ProductCreateModel;
import sample.models.ProductModel;
import sample.models.QuantityModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {

    private OrdersDataAccess ordersDataAccess;

    public ProductService(){
        try {
            ordersDataAccess = new OrdersDataAccess();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createProduct(ProductCreateModel productCreateModel){
        ProductEntity productEntity = new ProductEntity();
        productEntity.name = productCreateModel.name;
        productEntity.price = productCreateModel.price;
        productEntity.status = productCreateModel.status;

        ordersDataAccess.createProduct(productEntity);

    }

    public ProductModel deleteProduct(int id, String dbPass){
        ProductModel product = getProductById(id);
        ordersDataAccess.deleteProduct(id, dbPass);
        return product;
    }

    public void deleteAll(String dbPass){
        ordersDataAccess.deleteAll(dbPass);
    }

    public ProductModel getProductById(int id){
        ArrayList<ProductEntity> products = ordersDataAccess.getAllProducts();
        for (ProductEntity item : products) {
            if (item.id == id){
                ProductModel product = new ProductModel();
                product.id = item.id;
                product.name = item.name;
                product.price = item.price;
                product.status = item.status;
                product.created_at = item.created_at;
                return product;
            }
        }
        return null;
    }

    public ArrayList<ProductModel> getAll(){
        ArrayList<ProductModel> result = new ArrayList<ProductModel>();
        ArrayList<ProductEntity> allItems = ordersDataAccess.getAllProducts();
        for(ProductEntity item : allItems){
            ProductModel model = new ProductModel();
            model.id = item.id;
            model.name = item.name;
            model.price = item.price;
            model.status = item.status;
            result.add(model);
        }
        return result;
    }

    public ArrayList<QuantityModel> getSumQuantities(){
        ArrayList<QuantityModel> result = new ArrayList<QuantityModel>();
        ArrayList<QuantityModel> allProducts = ordersDataAccess.getSumQuantities();
        for(QuantityModel item : allProducts){
            QuantityModel model = new QuantityModel();
            model.id = item.id;
            model.name = item.name;
            model.sum = item.sum;
            result.add(model);
        }
        return result;
    }
}
