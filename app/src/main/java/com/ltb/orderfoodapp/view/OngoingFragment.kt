package com.ltb.orderfoodapp.view

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.User
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel

class OngoingFragment : Fragment() {

    private lateinit var ordersContainer: GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ongoing, container, false)
        ordersContainer = view.findViewById(R.id.ordersContainer)

        val orderDAO = OrderDAO(requireContext())
        val productsList = orderDAO.getAllProducts(1)

        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(user, User::class.java)
        val cartId = userObject.getCartId()

        val adapterCart = OrderAdapter(requireContext(), productsList, this, cartId )
        ordersContainer.adapter = adapterCart

        return view
    }
}