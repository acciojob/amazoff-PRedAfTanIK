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
        if(!partnerDb.containsKey(partnerId)) return;
        if(!orderDb.containsKey(orderId)) return;
        ordersForPartner.putIfAbsent(partnerId,new HashSet<>());
        ordersForPartner.get(partnerId).add(orderId);
        orderBelongsToPartner.put(orderId,partnerId);
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
    }
    public Order getOrder(String orderId){
        return orderDb.getOrDefault(orderId,new Order());
    }
    public DeliveryPartner getPartner(String partnerId){
        return partnerDb.getOrDefault(partnerId,new DeliveryPartner());
    }
    public int getOrderCountForPartner(String partnerId){
        return ordersForPartner.getOrDefault(partnerId,new HashSet<>()).size();
    }
    public List<String> getOrderListForPartner(String partnerId){
        List<String> list = new ArrayList<>();
        if(!ordersForPartner.containsKey(partnerId)) return list;
        list.addAll(ordersForPartner.get(partnerId));
        return list;
    }
    public List<String> getAllOrders(){
        return new ArrayList<>(orderDb.keySet());
    }
    public int getUnassignedOrders(){
        int count = 0;
        for(String order:orderDb.keySet())
            if(!orderBelongsToPartner.containsKey(order))
                count++;

        return count;
    }
    public int getUndeliveredOrdersByPartner(String partnerId,int givenTime){
        if(!partnerDb.containsKey(partnerId)) return 0;
        int count = 0;
        for(String orderId:ordersForPartner.get(partnerId)){
            if(orderDb.get(orderId).getDeliveryTime()<givenTime)
                count++;
        }
        return count;
    }
    public int getLastDeliveryTimeForPartner(String partnerId){
        if(!partnerDb.containsKey(partnerId)) return 0;
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
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(partner.getNumberOfOrders()-1);
    }

}
