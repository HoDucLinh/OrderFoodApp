package com.ltb.orderfoodapp.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.User

class HistoryFragment : Fragment(){

    private lateinit var ordersContainer: GridView
    private lateinit var productsList :  List<Pair<Product, Int>>
    private var userId : Int = -1
    private var cartId : Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.ltb.orderfoodapp.R.layout.fragment_history, container, false)
        ordersContainer = view.findViewById(com.ltb.orderfoodapp.R.id.ordersContainer)

        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(user, User::class.java)
        cartId = userObject.getCartId()
        userId = userObject.getIdUser()
        val orderDAO = OrderDAO(requireContext())
        productsList = orderDAO.getAllProducts(mutableListOf(3),userId)
        val adapterCart = OrderAdapter(requireContext(),productsList, this, cartId)
        ordersContainer.adapter = adapterCart


        return view
    }

    override fun onStart() {
        super.onStart()
        val orderDAO = OrderDAO(requireContext())
        productsList = orderDAO.getAllProducts(mutableListOf(3),userId)
        val adapterCart = OrderAdapter(requireContext(),productsList, this, cartId)
        ordersContainer.adapter = adapterCart

    }
}