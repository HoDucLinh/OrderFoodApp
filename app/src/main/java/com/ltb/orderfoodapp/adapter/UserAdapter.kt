package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.User

class UserAdapter(
    private val context: Context,
    private val users: MutableList<User>,
    ) : BaseAdapter (){
    override fun getCount(): Int {
      return users.size
    }

    override fun getItem(p0: Int): Any {
        return users[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_edit_role, parent, false)

        val username = view.findViewById<TextView>(R.id.username)
        val name_role = view.findViewById<EditText>(R.id.editText_user_role)

        val user = users[position]
        username.text = user.fullName
        name_role.setText(user.roleId.toString())

//        name_role.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                val newRoleId = name_role.text.toString().toIntOrNull()
//                if (newRoleId != null) {
//                    user.roleId = newRoleId // Cập nhật roleId trong danh sách users
//                } else {
//                    name_role.error = "Vui lòng nhập số hợp lệ"
//                }
//            }
//        }

        return view
    }


}