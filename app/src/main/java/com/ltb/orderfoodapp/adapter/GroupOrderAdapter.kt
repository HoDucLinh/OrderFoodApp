package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.Status
import com.ltb.orderfoodapp.data.dao.OrderDAO
class GroupOrderAdapter(
    private val context: Context,
    private val orderDao: OrderDAO
) : ArrayAdapter<GroupOrderAdapter.OrderWithProducts>(context, 0) {

    // Get the list of orders with their products
    private val orderDetails = orderDao.getAllOrdersWithProducts()

    override fun getCount(): Int {
        return orderDetails.size
    }

    override fun getItem(position: Int): OrderWithProducts? {
        return orderDetails.getOrNull(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.order_group, parent, false)

        val orderDetail = getItem(position)!!
        val orderId = orderDetail.orderId
        val products = orderDetail.products
        val userName = orderDetail.userEmail

        val txtOrderId = view.findViewById<TextView>(R.id.titleOrder)
        val txtUserName = view.findViewById<TextView>(R.id.userName)

        // Display order ID and user name
        txtOrderId.text = "Order ID: $orderId"
        txtUserName.text = userName

        // Set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.Product)
        val productAdapter = StatusAdapter(context, products)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = productAdapter

        // Set up the spinner for order status
        val statusAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            Status.values().map { it.description }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val spinner = view.findViewById<Spinner>(R.id.statusOrder)
        spinner.adapter = statusAdapter

        // Set the current status of the order in the spinner
        val currentStatusIndex = Status.values().indexOf(orderDetail.products.first().status)
        spinner.setSelection(currentStatusIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedStatus = Status.values()[pos]
                orderDetail.products.forEach { it.status = selectedStatus }
                orderDao.updateOrderStatus(orderId, selectedStatus.id)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if no selection
            }
        }

        return view
    }

    data class OrderWithProducts(
        val orderId: Int,
        val products: List<ProductWithStatus>,
        val userEmail: String
    )

    data class ProductWithStatus(
        val product: Product,
        var status: Status
    )
}
