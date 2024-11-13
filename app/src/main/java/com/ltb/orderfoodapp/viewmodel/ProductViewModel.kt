import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ltb.orderfoodapp.data.model.Product
import com.google.firebase.firestore.DocumentReference

class ProductViewModel {

    private val db = FirebaseFirestore.getInstance()
    private var products: MutableList<Product> = mutableListOf()

    init {
//        val sampleProducts = listOf(
//            Product(
//                id_product = "product1",
//                name = "Bánh Mì Thịt Nướng",
//                restaurant_id = db.collection("restaurants").document("restaurant1"),
//                price = 20000,
//                rating = 4.7f,
//                category_id = db.collection("categories").document("CAT001"), // Category 1: Đồ ăn
//                description = "Bánh mì thịt nướng nóng hổi, giòn tan.",
//                image_id = db.collection("images").document("image1")
//            ),
//            Product(
//                id_product = "product2",
//                name = "Pizza Margherita",
//                restaurant_id = db.collection("restaurants").document("restaurant2"),
//                price = 120000,
//                rating = 4.8f,
//                category_id = db.collection("categories").document("CAT001"), // Category 1: Đồ ăn
//                description = "Pizza với phô mai và cà chua tươi ngon.",
//                image_id = db.collection("images").document("image2")
//            ),
//            Product(
//                id_product = "product3",
//                name = "Mì Ý Bolognese",
//                restaurant_id = db.collection("restaurants").document("restaurant3"),
//                price = 90000,
//                rating = 4.6f,
//                category_id = db.collection("categories").document("CAT001"), // Category 1: Đồ ăn
//                description = "Mì Ý với sốt Bolognese đậm đà.",
//                image_id = db.collection("images").document("image3")
//            ),
//            Product(
//                id_product = "product4",
//                name = "Cà Phê Sữa Đá",
//                restaurant_id = db.collection("restaurants").document("restaurant4"),
//                price = 25000,
//                rating = 4.5f,
//                category_id = db.collection("categories").document("CAT002"), // Category 2: Nước uống
//                description = "Cà phê sữa đá thơm ngon, đậm đà.",
//                image_id = db.collection("images").document("image1")
//            ),
//            Product(
//                id_product = "product5",
//                name = "Trà Sữa Matcha",
//                restaurant_id = db.collection("restaurants").document("restaurant5"),
//                price = 35000,
//                rating = 4.2f,
//                category_id = db.collection("categories").document("CAT002"), // Category 2: Nước uống
//                description = "Trà sữa matcha thơm ngon, mát lạnh.",
//                image_id = db.collection("images").document("image2")
//            )
//        )
//
//
//        products.addAll(sampleProducts)
    }

    // Thêm sản phẩm vào Firebase
    fun addToFirebase() {
        for (product in products) {
            val productId = product.id_product // ID của sản phẩm
            db.collection("products")
                .document(productId)
                .set(product)
                .addOnSuccessListener {
                    Log.d("TAG", "Product added with ID: $productId")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding product", e)
                }
        }
    }


    // Fetch dữ liệu từ Firestore
    fun fetchData() {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                products.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    products.add(product)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ProductViewModel", "Error getting documents.", exception)
            }
    }

    fun getProducts(): MutableList<Product> {
        return products
    }
}
