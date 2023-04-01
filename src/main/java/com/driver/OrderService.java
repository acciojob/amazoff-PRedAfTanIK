package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    OrderRepository repository = new OrderRepository();

    public void addOrder(Order order){
        repository.addOrder(order);
    }
    public void addPartner(DeliveryPartner partner){
        repository.addPartner(partner);
    }
    public void assignOrderToPartner(String orderId,String partnerId){
        repository.addOrderToPartner(orderId,partnerId);
    }
    public Order getOrder(String orderId){
        return repository.getOrder(orderId);
    }
    public DeliveryPartner getPartner(String partnerId){
        return repository.getPartner(partnerId);
    }
    public int getOrderCountForPartner(String partnerId){
        return repository.getOrderCountForPartner(partnerId);
    }
    public List<String> getOrderListForPartner(String partnerId){
        return repository.getOrderListForPartner(partnerId);
    }
    public List<String> getAllOrders(){
        return repository.getAllOrders();
    }
    public int getUnassignedOrderCount(){
        return repository.getUnassignedOrders();
    }
    public int getOrdersUndeliveredByPartnerAfterTime(String partnerId,String time){
        int hours = Integer.parseInt(time.substring(0,2));
        int mins = Integer.parseInt(time.substring(3));
        int timeInInt = hours*60 + mins;
        return repository.getUndeliveredOrdersByPartner(partnerId,timeInInt);
    }
    public String getLastDeliveryTime(String partnerId){
        int time = repository.getLastDeliveryTimeForPartner(partnerId);
        int hours = time/60,mins = time%60;
        String hoursString = ""+hours;
        if(hoursString.length() == 1) hoursString = "0"+hoursString;
        String minString = ""+mins;
        if(minString.length() == 1) minString = "0"+minString;
        return hoursString+":"+minString;
    }
    public void deletePartner(String partnerId){
        repository.deletePartner(partnerId);
    }
    public void deleteOrder(String orderId){
        repository.deleteOrder(orderId);
    }
}
