package com.ltb.orderfoodapp.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.User


class AddUserFragment : DialogFragment() {

    interface OnUserAddedListener {
        fun onUserAdded(user: User)
    }

    private var listener: OnUserAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserAddedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnUserAddedListener")
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameEditText = view.findViewById<EditText>(R.id.et_user_name)
        val userRoleEditText = view.findViewById<EditText>(R.id.et_user_role)
        val userEmail = view.findViewById<EditText>(R.id.et_email)
        val userPassword = view.findViewById<EditText>(R.id.et_password)
        val saveButton = view.findViewById<Button>(R.id.btn_save_user)

        // Xử lý sự kiện khi người dùng nhấn nút "Lưu"
        saveButton.setOnClickListener {
            val name = userNameEditText.text.toString()
            val role = userRoleEditText.text.toString().toIntOrNull()
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && role != null) {
                val newUser = User(
                    fullName = name,
                    email = email,
                    password = password,
                    roleId = role
                )
                listener?.onUserAdded(newUser)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}