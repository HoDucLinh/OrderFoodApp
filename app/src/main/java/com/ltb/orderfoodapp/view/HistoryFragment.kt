package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel

class HistoryFragment : Fragment() {

    private lateinit var ordersContainer: GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.ltb.orderfoodapp.R.layout.fragment_history, container, false)
        ordersContainer = view.findViewById(com.ltb.orderfoodapp.R.id.ordersContainer)
        // Tạo adapter và set dữ liệu cho GridView
        val productCartViewModel = ProductCartViewModel(requireContext())
        val listCart = productCartViewModel.getProduct()
        val adapterCart = OrderAdapter(requireContext(), listCart)
        ordersContainer.adapter = adapterCart


        return view
    }
}