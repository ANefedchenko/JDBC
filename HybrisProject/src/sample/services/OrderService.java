package sample.services;

import sample.data_access.OrdersDataAccess;
import sample.entities.OrderEntity;
import sample.entities.OrderItemEntity;
import sample.models.OrderCreateModel;
import sample.models.OrderInformationModel;
import sample.models.OrderModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private OrdersDataAccess ordersDataAccess;

    public OrderService(){
        try {
            ordersDataAccess = new OrdersDataAccess();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createOrder(OrderCreateModel createOrderModel){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.user_id = createOrderModel.user_id;
        orderEntity.create_at = createOrderModel.date;

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.product_id = createOrderModel.product_id;
        orderItemEntity.quantity = createOrderModel.quantity;

        ordersDataAccess.createOrder(orderEntity, orderItemEntity);
    }

    public ArrayList<OrderInformationModel> getAllOrderInformation(){
        ArrayList<OrderInformationModel> result = new ArrayList<OrderInformationModel>();
        ArrayList<OrderInformationModel> allProducts = ordersDataAccess.getAllOrderInformation();
        for(OrderInformationModel item : allProducts){
            OrderInformationModel model = new OrderInformationModel();
            model.id = item.id;
            model.sum = item.sum;
            model.name = item.name;
            model.quantity = item.quantity;
            model.date = item.date;
            result.add(model);
        }
        return result;
    }

    public OrderInformationModel getOrderInformationById(int id){
        ArrayList<OrderInformationModel> orders = getAllOrderInformation();
        for (OrderInformationModel item : orders) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }

    public List<OrderModel> getAll(){
        List<OrderModel> result = new ArrayList<OrderModel>();
        ArrayList<OrderEntity> allItems = ordersDataAccess.getAllOrders();
        for (OrderEntity order : allItems){
            OrderModel model = new OrderModel();
            model.id = order.id;
            model.user_id = order.user_id;
            model.status = order.status;
            model.create_at = order.create_at;
            result.add(model);
        }
        return result;
    }
}
