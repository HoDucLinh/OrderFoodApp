package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.UserAdapter
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class EditRole : AppCompatActivity(), AddUserFragment.OnUserAddedListener {

    private lateinit var userDAO: UserDAO
    private lateinit var users: MutableList<User>
    private lateinit var adapter: UserAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_role)

        userDAO = UserDAO(this)
        setupGridViewProduct()

        val btnAddNew = findViewById<Button>(R.id.btn_add_new)
        btnAddNew.setOnClickListener {
            val dialogFragment = AddUserFragment()
            dialogFragment.show(supportFragmentManager, "AddUserFragment")
        }
    }

    private fun setupGridViewProduct() {
        users = userDAO.getAllUsers()
        val gridView = findViewById<GridView>(R.id.gridViewUser)
        adapter = UserAdapter(this, users, userDAO)
        gridView.adapter = adapter
    }


    // Hàm xử lý sự kiện khi người dùng thêm mới user
    override fun onUserAdded(user: User) {
        val userId = userDAO.addUser(user)
        if (userId > 0) {
            user.idUser = userId // Cập nhật ID của user
            users.add(user)      // Thêm user mới vào danh sách
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
        }
    }
}
