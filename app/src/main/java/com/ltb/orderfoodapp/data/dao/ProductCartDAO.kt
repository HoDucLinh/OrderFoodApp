package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart

class ProductCartDAO(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper.getInstance(context).writableDatabase
    //them san pham vao database
    fun insertProduct(product: Product, number:Int): Long {
        val values = ContentValues().apply {
            put("Product_ID", product.idProduct)
            put("Cart_ID", 1)
            put("Quantity",number)
        }
        return db.insert("Product_Cart", null, values)
    }
    fun getAllProductsOfCart(): MutableList<ProductCart> {
        val productCartList = mutableListOf<ProductCart>()
        val query = """
        SELECT 
            pc.ID, pc.Product_ID, pc.Cart_ID, pc.Quantity,
            p.Name, p.Price, p.Rating, GROUP_CONCAT(i.Value) AS ImageSources
        FROM Product_Cart pc
        LEFT JOIN Product p ON pc.Product_ID = p.ID
        LEFT JOIN Image i ON p.ID = i.Product_ID
        GROUP BY pc.ID, pc.Product_ID, pc.Cart_ID, pc.Quantity, p.Name, p.Price, p.Rating
    """
        val cursor = db.rawQuery(query, null)

        cursor?.use {
            while (it.moveToNext()) {
                val productCart = ProductCart(
                    idProductCart = it.getInt(it.getColumnIndexOrThrow("ID")),
                    productId = it.getInt(it.getColumnIndexOrThrow("Product_ID")),
                    cartId = it.getInt(it.getColumnIndexOrThrow("Cart_ID")),
                    quantity = it.getInt(it.getColumnIndexOrThrow("Quantity")),
                    name = it.getString(it.getColumnIndexOrThrow("Name")) ?: "Unknown",
                    price = it.getDouble(it.getColumnIndexOrThrow("Price"))
                )

                productCart.rating = it.getFloat(it.getColumnIndexOrThrow("Rating"))

                // Xử lý danh sách ảnh
                val images = it.getString(it.getColumnIndexOrThrow("ImageSources"))
                if (!images.isNullOrEmpty()) {
                    productCart.images.addAll(images.split(","))
                }

                productCartList.add(productCart)
            }
        }
        return productCartList
    }


}