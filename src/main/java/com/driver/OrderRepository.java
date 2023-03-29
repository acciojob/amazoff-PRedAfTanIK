package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderDb = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String,Set<String>> ordersForPartner = new HashMap<>();
    HashMap<String,String> orderBelongsToPartner = new HashMap<>();

    public void addOrder(Order order){
        orderDb.put(order.getId(),order);
    }
    public void addPartner(DeliveryPartner partner){
        partnerDb.put(partner.getId(),partner);
    }
    public void addOrderToPartner(String orderId,String partnerId){
        ordersForPartner.putIfAbsent(partnerId,new HashSet<>());
        ordersForPartner.get(partnerId).add(orderId);
        orderBelongsToPartner.put(orderId,partnerId);
    }
    public Order getOrder(String orderId){
        return orderDb.get(orderId);
    }
    public DeliveryPartner getPartner(String partnerId){
        return partnerDb.get(partnerId);
    }
    public int getOrderCountForPartner(String partnerId){
        return ordersForPartner.getOrDefault(partnerId,new HashSet<>()).size();
    }
    public List<String> getOrderListForPartner(String partnerId){
        List<String> list = new ArrayList<>();
        if(!ordersForPartner.containsKey(partnerId)) return list;

        for(String order:ordersForPartner.get(partnerId))
            list.add(order);

        return list;
    }
    public List<String> getAllOrders(){
        List<String> list = new ArrayList<>();
        for(String order:orderDb.keySet())
            list.add(order);
        return list;
    }
    public int getUnassignedOrders(){
        int count = 0;
        for(String order:orderDb.keySet())
            if(!orderBelongsToPartner.containsKey(order))
                count++;

        return count;
    }
    public int getUndeliveredOrdersByPartner(String partnerId,int givenTime){
        int count = 0;
        for(String orderId:ordersForPartner.get(partnerId)){
            if(orderDb.get(orderId).getDeliveryTime()<givenTime)
                count++;
        }
        return count;
    }
    public int getLastDeliveryTimeForPartner(String partnerId){
        int lastTime = 0;
        for(String orderId:ordersForPartner.get(partnerId)){
            int currentTime = orderDb.get(orderId).getDeliveryTime();
            lastTime = Math.max(lastTime,currentTime);
        }

        return lastTime;
    }
    public void deletePartner(String partnerId){
        if(!partnerDb.containsKey(partnerId)) return;
        partnerDb.remove(partnerId);
        for(String orderId:ordersForPartner.get(partnerId)){
            orderBelongsToPartner.remove(orderId);
        }
        ordersForPartner.remove(partnerId);
    }
    public void deleteOrder(String orderId){
        if(!orderDb.containsKey(orderId)) return;
        orderDb.remove(orderId);
        String partnerId = orderBelongsToPartner.getOrDefault(orderId,"");
        if(partnerId.length() == 0) return;
        orderBelongsToPartner.remove(orderId);
        ordersForPartner.get(partnerId).remove(orderId);
    }

}
