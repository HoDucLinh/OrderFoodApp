package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Status
import com.ltb.orderfoodapp.databinding.ActivityAddNewItemsBinding

class OrderStatus : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewItemsBinding
    private lateinit var selectedStatus: Status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
    }

    private fun setupSpinner() {
        // Lấy danh sách các trạng thái từ enum
        val statuses = Status.values()

        // Tạo ArrayAdapter cho Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses.map { it.description })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Gán adapter cho Spinner
        binding.categorySpinner.adapter = adapter

        // Thiết lập listener cho Spinner
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStatus = statuses[position]
                // Xử lý khi một trạng thái được chọn
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý khi không có trạng thái nào được chọn
            }
        }
    }
}
