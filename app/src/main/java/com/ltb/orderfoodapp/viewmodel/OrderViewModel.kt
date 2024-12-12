package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Order
import com.ltb.orderfoodapp.data.model.Product

class OrderViewModel(context: Context) {
    private val orderDAO = OrderDAO(context)
    private var orders: MutableList<Order> = mutableListOf()
    private var runningorders: MutableList<Order> = mutableListOf()

    init {
        fetchOrders()
    }
    private fun fetchOrders() {
        orders = orderDAO.getAllOrders()
    }


    fun getTotalOrders(): Int {
        return orders.size
    }
    fun getRevenue(): Float {
        return orderDAO.getTotalRevenue()
    }
    fun getRunningOrdersCount():Int{
        return runningorders.size
    }

    fun getOrders(): MutableList<Order> {
        runningorders = orders.filter { it.getStatus() == 1 }.toMutableList()
        return runningorders
    }




}