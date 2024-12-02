import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val productDAO = ProductDAO(context)
    private var products: MutableList<Product> = mutableListOf()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        products = productDAO.getAllProducts()
    }

    fun getProducts(): MutableList<Product> {
        return products
    }

    fun getProductsFilter(kw: String): MutableList<Product> {
        return products.filter { it.name.contains(kw, ignoreCase = true) }.toMutableList()
    }

    fun getProductsByRestaurant(restaurantId: Int): List<Product> {
        val db = DatabaseHelper(context).readableDatabase
        val query = "SELECT * FROM Product WHERE Restaurant_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(restaurantId.toString()))
        val products = mutableListOf<Product>()

        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    idProduct = cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    price = cursor.getInt(cursor.getColumnIndexOrThrow("Price")),
                    rating = cursor.getFloat(cursor.getColumnIndexOrThrow("Rating")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    restaurant = restaurantId.toString()
                )
                products.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return products
    }
    // Phương thức đóng cơ sở dữ liệu
    fun close() {
        productDAO.close()
    }
}
