package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductCartDAO(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper.getInstance(context).writableDatabase
    private val dt: SQLiteDatabase = DatabaseHelper.getInstance(context).readableDatabase
    //them san pham vao database
    fun insertProduct(product: Product, number: Int, cartId: Int): Long {
        val values = ContentValues().apply {
            put("Product_ID", product.idProduct)
            put("Cart_ID", cartId)
            put("Quantity", number)
        }
        return db.insert("Product_Cart", null, values)
    }
    //lấy ds sp Product
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
                    price = it.getInt(it.getColumnIndexOrThrow("Price"))
                )

                productCart.setRating(it.getFloat(it.getColumnIndexOrThrow("Rating")))

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
    //xoa Product
    fun deleteProduct(productId: Int) {
        db.delete("Product_Cart", "Product_ID = ?", arrayOf(productId.toString()))
        db.close()
    }
    //hàm kiểm tra sản phẩm đã tồn tại hay chưa
    fun isProductInCart(productId: Int, cartId: Int): Boolean {
        val query = "SELECT COUNT(*) FROM Product_Cart WHERE Product_ID = ? AND Cart_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(productId.toString(), cartId.toString()))

        var exists = false
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0
        }

        cursor.close()
        return exists
    }
    suspend fun updateQuantity(productId: Int, newQuantity: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put("Quantity", newQuantity)
            }
            val rowsUpdated = db.update("Product_Cart", values, "Product_ID = ?", arrayOf(productId.toString()))
            rowsUpdated > 0
        }
    }





}