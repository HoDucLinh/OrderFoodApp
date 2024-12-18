package com.ltb.orderfoodapp.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

class ConfirmDialog {
    fun showDeleteConfirmationDialog(
        context: Context,
        onConfirmed: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa ?")
            .setPositiveButton("OK") { _, _ ->
                onConfirmed()
                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}