import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ltb.orderfoodapp.data.model.Product
import com.google.firebase.firestore.DocumentReference
import com.ltb.orderfoodapp.data.model.Restaurant

class RestaurantViewModel {

    private val db = FirebaseFirestore.getInstance()
    private var restaurants: MutableList<Restaurant> = mutableListOf()

    init {
//        val restaurant = listOf(
//        Restaurant("restaurant1", "The Green Garden"),
//        Restaurant("restaurant2", "Sunset Bistro"),
//        Restaurant("restaurant3", "Urban Kitchen"),
//        Restaurant("restaurant4", "Ocean's Delight"),
//        Restaurant("restaurant5", "Mountain Feast")
//        )
//        restaurants.addAll(restaurant)
    }

    // Thêm sản phẩm vào Firebase
    fun addToFirebase() {
        for (restaurant in restaurants ) {
            val restaurantId = restaurant.id_restaurant // ID của sản phẩm
            db.collection("restaurants")
                .document(restaurantId)
                .set(restaurant)
                .addOnSuccessListener {
                    Log.d("TAG", "Product added with ID: $restaurant")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding product", e)
                }
        }
    }

    fun fetchData() {
        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                restaurants.clear()
                for (document in result) {
                    val restaurant = document.toObject(Restaurant::class.java)
                    restaurants.add(restaurant)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ProductViewModel", "Error getting documents.", exception)
            }
    }
    fun getRestaurants(): MutableList<Restaurant> {
        return restaurants
    }
}
