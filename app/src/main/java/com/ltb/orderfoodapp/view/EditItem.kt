package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

class EditItem : AppCompatActivity() {
    private var productId: Int = -1
    private var rating: Float = 0f
    private lateinit var productDAO: ProductDAO
    private lateinit var editTextName: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var description: EditText

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        // Nhận product_id từ Intent
        productId = intent.getIntExtra("product_id", -1)
        rating = intent.getFloatExtra("rating" , 0f)
        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Khởi tạo các thành phần UI
        editTextName = findViewById(R.id.editTextName)
        editTextPrice = findViewById(R.id.editTextPrice)
        description = findViewById(R.id.description)

        // Lấy sản phẩm từ cơ sở dữ liệu và hiển thị
        productDAO = ProductDAO(this)
        val product = productDAO.getProductById(productId)
        editTextName.setText(product.name)
        editTextPrice.setText(product.price.toString())
        description.setText(product.description)

        // Xử lý sự kiện nút lưu
        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            saveProductChanges(product)
        }
        val btnClose = findViewById<ImageButton>(R.id.close)
        btnClose.setOnClickListener{
            val close = Intent(this, MyFood::class.java)
            startActivity(close)
        }
        val btnDelete = findViewById<Button>(R.id.btndelete)
        btnDelete.setOnClickListener{
            deleteProduct()
        }

    }

    private fun saveProductChanges(product: Product) {
        // Lưu lại các thay đổi vào cơ sở dữ liệu
        val updatedProduct = Product(product.idProduct, editTextName.text.toString(), editTextPrice.text.toString().toInt(),rating, description.text.toString())
        productDAO.updateProduct(updatedProduct)
        Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun deleteProduct() {
        // Kiểm tra xem sản phẩm có tồn tại không
        if (productId > 0) {
            // Xóa hình ảnh (nếu có) liên quan đến sản phẩm
            val imageDeleted = productDAO.deleteProductImages(productId)
            if (imageDeleted) {
                Log.d("DeleteItem", "Images deleted successfully")
            }

            // Xóa sản phẩm khỏi cơ sở dữ liệu
            val deleted = productDAO.deleteProductById(productId)

            if (deleted) {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()

                // Cập nhật lại cơ sở dữ liệu (đã xóa thành công)
                // Trở lại màn hình trước đó
                val intent = Intent(this, MyFood::class.java) // Hoặc bất kỳ Activity nào bạn muốn quay lại
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()  // Kết thúc Activity hiện tại
            } else {
                Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show()
        }
    }

}
