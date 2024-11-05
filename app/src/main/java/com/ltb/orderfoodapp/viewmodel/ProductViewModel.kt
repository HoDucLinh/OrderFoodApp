
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product

class ProductViewModel {
    private val products: MutableList<Product> = mutableListOf()

    init {
        products.add(Product("Áo phông", "Cửa hàng thời trang", 200000, R.drawable.fruit,4))
        products.add(Product("Quần jeans", "Cửa hàng thời trang", 300000, R.drawable.fruit,5))
        products.add(Product("Giày thể thao", "Cửa hàng thể thao", 500000, R.drawable.fruit,2))
        products.add(Product("Điện thoại", "Cửa hàng điện tử", 10000000, R.drawable.fruit,1))
        products.add(Product("Laptop", "Cửa hàng điện tử", 20000000, R.drawable.fruit,2))
        products.add(Product("Bàn phím", "Cửa hàng điện tử", 500000, R.drawable.fruit,4))
        products.add(Product("Chuột máy tính", "Cửa hàng điện tử", 200000, R.drawable.fruit,3))
        products.add(Product("Tai nghe", "Cửa hàng điện tử", 300000, R.drawable.fruit,5))
        products.add(Product("Loa di động", "Cửa hàng điện tử", 500000, R.drawable.fruit,2))
        products.add(Product("Máy ảnh", "Cửa hàng điện tử", 10000000, R.drawable.fruit,1))
    }


    fun getProducts(): List<Product> {
        return products
    }
}
