package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.UserAdapter
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class EditRole : AppCompatActivity() {

    private lateinit var userDAO: UserDAO
    private lateinit var users: MutableList<User>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDAO = UserDAO(this)
        setContentView(R.layout.activity_edit_role)
        setupGridViewProduct()

        val btnSaveChanges = findViewById<Button>(R.id.btn_save_role_changes)

        btnSaveChanges.setOnClickListener {
            val roleId = findViewById<EditText>(R.id.editText_user_role)
            val newRoleId = roleId.text.toString().toInt()
            saveAllChanges(newRoleId) // Lưu tất cả thay đổi vào cơ sở dữ liệu.
        }
    }

    private fun setupGridViewProduct() {
        users = userDAO.getAllUsers()
        val gridView = findViewById<GridView>(R.id.gridViewUser)
        val adapter = UserAdapter(this, users)
        gridView.adapter = adapter
    }

    private fun saveAllChanges(roleId : Int) {
        // Duyệt qua danh sách người dùng và lưu thay đổi vào database
        var updatedCount = 0
        users.forEach { user ->
            // Cập nhật roleId của từng user
            Log.d("EditRole", "User ID: ${user.idUser}, New Role ID: ${user.roleId}")
            val updated = userDAO.saveChangeUserRole(user.idUser, roleId)
            if (updated > 0) {
                updatedCount++
            }
        }

        if (updatedCount == users.size) {
            Toast.makeText(this, "Lưu thay đổi thành công!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Có lỗi khi lưu thay đổi!", Toast.LENGTH_SHORT).show()
        }
    }
}
