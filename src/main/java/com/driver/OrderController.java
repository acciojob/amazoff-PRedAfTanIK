package com.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderService service = new OrderService();

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order){
        service.addOrder(order);
        return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable("partnerId")String partnerId){
        service.addPartner(new DeliveryPartner(partnerId));
        return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam("order id")String orderId,
                                                      @RequestParam("partner id")String partnerId){
        service.assignOrderToPartner(orderId,partnerId);
        //This is basically assigning that order to that partnerId
        return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId")String orderId){
        Order order = service.getOrder(orderId);
        //order should be returned with an orderId.
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable("partnerId")String partnerId){
        DeliveryPartner deliveryPartner = service.getPartner(partnerId);
        //deliveryPartner should contain the value given by partnerId
        return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable("partnerId")String partnerId){
        Integer orderCount = service.getOrderCountForPartner(partnerId);
        //orderCount should denote the orders given by a partner-id
        return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable("partnerId")String partnerId){
        List<String> orders = service.getOrderListForPartner(partnerId);
        //orders should contain a list of orders by PartnerId
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders(){
        List<String> orders = service.getAllOrders();
        //Get all orders
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders(){
        Integer countOfOrders = service.getUnassignedOrderCount();
        //Count of orders that have not been assigned to any DeliveryPartner
        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{time}/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable("time")String time,
                                                                          @PathVariable("partnerId")String partnerId){
        Integer countOfOrders = service.getOrdersUndeliveredByPartnerAfterTime(partnerId,time);
        //countOfOrders that are left after a particular time of a DeliveryPartner
        return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable("partnerId")String partnerId){
        String time = service.getLastDeliveryTime(partnerId);
        //Return the time when that partnerId will deliver his last delivery order.
        return new ResponseEntity<>(time, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable("partnerId")String partnerId){
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        service.deletePartner(partnerId);
        return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable("orderId")String orderId){
        //Delete an order and also
        // remove it from the assigned order of that partnerId
        service.deleteOrder(orderId);
        return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.CREATED);
    }
}
