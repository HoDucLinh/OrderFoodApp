
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.data.model.Drink
import com.ltb.orderfoodapp.data.model.Food
import com.ltb.orderfoodapp.data.model.Product

class ProductViewModel {
    private val db = Firebase.firestore
    private var products: MutableList<Product> = mutableListOf()

    init{
        // Tạo các đối tượng Product mẫu bằng tiếng Việt
        val product1 = Product(
            name = "Bánh Mì",
            storeName = "Bánh Mì Huỳnh Hoa",
            price = 30_000,
            imageResource = "https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbanhmi.webp?alt=media&token=7e9fe5a6-2aba-44ab-8e19-a5e66eb47f77",
            rating = 4.6f,
            category = "1", // Đồ ăn
            description = "Bánh mì với thịt nguội, chả lụa, rau thơm và gia vị đặc biệt"
        )

        val product2 = Product(
            name = "Phở Bò",
            storeName = "Phở Hòa",
            price = 40_000,
            imageResource = "https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fphobo.webp?alt=media&token=90d20361-3831-4364-9581-65825da3cfab",
            rating = 4.8f,
            category = "1", // Đồ ăn
            description = "Phở bò với nước lèo trong, thịt bò tươi và bánh phở mềm"
        )

        val product3 = Product(
            name = "Cơm Tấm",
            storeName = "Cơm Tấm Cali",
            price = 35_000,
            imageResource = "https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fcomtam.webp?alt=media&token=95a13171-c41d-4bee-b08f-a38117bb89f1",
            rating = 4.5f,
            category = "1", // Đồ ăn
            description = "Cơm tấm với sườn nướng, bì, chả và trứng ốp la"
        )

        val product4 = Product(
            name = "Bánh Xèo",
            storeName = "Bánh Xèo Dì Lan",
            price = 25_000,
            imageResource = "banhxeo_image_url",
            rating = 4.4f,
            category = "1", // Đồ ăn
            description = "Bánh xèo giòn, nhân tôm thịt, rau sống và nước mắm chua ngọt"
        )

        val product5 = Product(
            name = "Gỏi Cuốn",
            storeName = "Gỏi Cuốn 123",
            price = 15_000,
            imageResource = "goicuon_image_url",
            rating = 4.7f,
            category = "1", // Đồ ăn
            description = "Gỏi cuốn tươi với tôm, thịt và rau sống cuốn trong bánh tráng"
        )

        val product6 = Product(
            name = "Bánh Canh Cua",
            storeName = "Bánh Canh Bà Lúa",
            price = 50_000,
            imageResource = "banhcanh_image_url",
            rating = 4.3f,
            category = "1", // Đồ ăn
            description = "Bánh canh cua với nước lèo ngọt, thịt cua tươi ngon và bánh canh mềm"
        )

        val product7 = Product(
            name = "Hủ Tiếu",
            storeName = "Hủ Tiếu Mỹ Tho",
            price = 35_000,
            imageResource = "hutieu_image_url",
            rating = 4.6f,
            category = "1", // Đồ ăn
            description = "Hủ tiếu với thịt xá xíu, tôm, mực và nước lèo đậm đà"
        )

        val product8 = Product(
            name = "Cháo Lòng",
            storeName = "Cháo Lòng Ngọc Bích",
            price = 20_000,
            imageResource = "chaolong_image_url",
            rating = 4.2f,
            category = "1", // Đồ ăn
            description = "Cháo lòng nóng hổi, thịt lòng mềm, ăn kèm rau thơm và gia vị"
        )

        val product9 = Product(
            name = "Bún Riêu",
            storeName = "Bún Riêu Cô Lan",
            price = 30_000,
            imageResource = "bunrieu_image_url",
            rating = 4.5f,
            category = "1", // Đồ ăn
            description = "Bún riêu cua với nước lèo chua ngọt, riêu cua tươi và rau sống"
        )

        val product10 = Product(
            name = "Trà Sữa",
            storeName = "Trà Sữa Phúc Long",
            price = 40_000,
            imageResource = "trasua_image_url",
            rating = 4.8f,
            category = "2", // Nước uống
            description = "Trà sữa với hương vị trà đậm, kết hợp với trân châu dai ngon"
        )


        products.addAll(listOf(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10))
    }

    fun fetchData() {
        db.collection("product")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    products.add(product)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ProductViewModel", "Error getting documents.", exception)
            }
    }
    public fun addtoFirebase(){
        for (product in products) {
            db.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "Product added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding product", e)
                }
        }
    }
    fun getProducts(): MutableList<Product> {
        return products
    }
}
