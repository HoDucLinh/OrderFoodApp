package com.ltb.orderfoodapp.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel

class OngoingFragment : Fragment() {

    private lateinit var ordersContainer: GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ongoing, container, false)
        ordersContainer = view.findViewById(R.id.ordersContainer)

        // Them adapter
//        val productCartViewModel = ProductCartViewModel(requireContext())
//        val listCart = productCartViewModel.getProduct()
        val orderDAO = OrderDAO(requireContext())
        val productsList = orderDAO.getAllProducts(1)
        val adapterCart = OrderAdapter(requireContext(), productsList, this)
        ordersContainer.adapter = adapterCart

        return view
    }
}