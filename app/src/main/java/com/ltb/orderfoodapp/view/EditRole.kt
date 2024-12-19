package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.UserAdapter
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class EditRole : AppCompatActivity(), AddUserFragment.OnUserAddedListener {

    private lateinit var userDAO: UserDAO
    private lateinit var users: MutableList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var authManager : AuthManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_role)
        enableEdgeToEdge()
        userDAO = UserDAO(this)
        setupGridViewProduct()

        val btnAddNew = findViewById<Button>(R.id.btn_add_new)
        btnAddNew.setOnClickListener {
            val dialogFragment = AddUserFragment()
            dialogFragment.show(supportFragmentManager, "AddUserFragment")
        }
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            val menu  = Intent(this, AdminDashboardHome ::class.java)
            startActivity(menu)
        }
    }

    private fun setupGridViewProduct() {
        users = userDAO.getAllUsers()
        val gridView = findViewById<GridView>(R.id.gridViewUser)
        adapter = UserAdapter(this, users, userDAO)
        gridView.adapter = adapter
    }


    override fun onUserAdded(user: User) {
        val userId = userDAO.addUser(user)
        authManager = AuthManager(this)
        authManager.createAccount(user.getEmail(),user.getPassword())
        if (userId > 0) {
            user.setIdUser(userId)
            users.add(user)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
        }
    }
}
