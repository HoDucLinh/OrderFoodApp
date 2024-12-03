import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

class ProductViewModel(context: Context) {

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

    fun getProductsByRestaurant(restaurant: String): List<Product> {
        return products.filter { it.restaurant == restaurant }.toMutableList()
    }
    // Phương thức đóng cơ sở dữ liệu
    fun close() {
        productDAO.close()
    }
}
