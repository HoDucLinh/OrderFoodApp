package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.ltb.orderfoodapp.R

class OngoingFragment : Fragment() {

    private lateinit var ordersContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ongoing, container, false)
        ordersContainer = view.findViewById(R.id.ordersContainer)

        for (i in 0 until 3) { // Ví dụ thêm 10 sản phẩm
            // Inflate layout cho từng sản phẩm
            val orderView = LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, ordersContainer, false)

            // Thêm view vào ordersContainer
            ordersContainer.addView(orderView)
        }

        return view
    }
}