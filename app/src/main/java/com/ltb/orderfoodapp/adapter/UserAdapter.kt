package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class UserAdapter(
    private val context: Context,
    private val users: MutableList<User>,
    private val userDAO: UserDAO
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
        val btnEdit = view.findViewById<Button>(R.id.btn_save_edit)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete_user)


        val user = users[position]
        username.text = user.fullName
        name_role.setText(user.roleId.toString())

        // Xử lý sự kiện nút Edit
        btnEdit.setOnClickListener {
            val newrole = name_role.text.toString().toInt()

            if (newrole != null) {
                user.roleId = newrole // Cập nhật vai trò trong danh sách
                if (userDAO.updateRole(user.idUser, newrole) != null) { // Lưu vào database
                    Toast.makeText(context, "Updated role for ${user.fullName} to $newrole", Toast.LENGTH_SHORT).show()

                    // Làm mới danh sách để đồng bộ
                    users[position].roleId = newrole
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Failed to update role for ${user.fullName}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Invalid role input for ${user.fullName}", Toast.LENGTH_SHORT).show()
            }
        }


        // Xử lý sự kiện nút Delete
        btnDelete.setOnClickListener {
            userDAO.deleteUser(user.idUser) // Xóa user khỏi database
            users.removeAt(position) // Xóa khỏi danh sách hiển thị
            Toast.makeText(context, "Deleted ${user.fullName}", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged() // Làm mới GridView
        }


        return view
    }


}