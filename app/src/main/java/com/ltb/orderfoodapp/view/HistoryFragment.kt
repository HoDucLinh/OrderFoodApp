package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class HistoryFragment : Fragment() {

    private lateinit var ordersContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.ltb.orderfoodapp.R.layout.fragment_ongoing, container, false)
        ordersContainer = view.findViewById(com.ltb.orderfoodapp.R.id.ordersContainer)

        for (i in 0 until 10) { // Ví dụ thêm 10 sản phẩm
            // Inflate layout cho từng sản phẩm
            val orderView = LayoutInflater.from(context).inflate(com.ltb.orderfoodapp.R.layout.item_orders_history, ordersContainer, false)

            // Thêm view vào ordersContainer
            ordersContainer.addView(orderView)
        }

        return view
    }
}