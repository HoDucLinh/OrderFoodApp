package com.ltb.orderfoodapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.dao.RatingDAO
import com.ltb.orderfoodapp.data.model.User


class RateProductDialogFragment : DialogFragment() {

    private lateinit var productDAO: ProductDAO

    companion object {
        private const val ARG_PRODUCT_ID = "productId"

        fun newInstance(productId: Int): RateProductDialogFragment {
            val fragment = RateProductDialogFragment()
            val args = Bundle()
            args.putInt(ARG_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog)

        productDAO = ProductDAO(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_product_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ratingDAO = RatingDAO(requireContext())

        // Lấy productId từ arguments
        val productId = arguments?.getInt(ARG_PRODUCT_ID) ?: 0
        if (productId == 0) {
            Toast.makeText(requireContext(), "Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show()
            dismiss()
            return
        }

        val commentEditText = view.findViewById<EditText>(R.id.editText_comment)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        // Lấy userID
        // Lấy thông tin từ SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val userJson = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(userJson, User::class.java)
        val userId = userObject?.getIdUser() ?: -1

        if (userId == -1) {
            Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show()
            dismiss()
            return
        }


        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            val comment = commentEditText.text.toString().trim()
            ratingDAO.addRating(rating, comment, productId, userId) // Lưu đánh giá vào database
            productDAO.syncProductRatings() // đồng bộ hoá rating khi start

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

