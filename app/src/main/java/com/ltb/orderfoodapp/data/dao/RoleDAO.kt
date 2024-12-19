package com.ltb.orderfoodapp.data.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper

class RoleDAO(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun getRoleNameByRoleId(roleId: Int): String? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = "SELECT RoleName FROM Role WHERE ID = ?"
        val cursor = db.rawQuery(query, arrayOf(roleId.toString()))

        var roleName: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("RoleName")
            if (columnIndex != -1) {
                roleName = cursor.getString(columnIndex)
            }
        }
        cursor.close()
        db.close()
        return roleName
    }
}
