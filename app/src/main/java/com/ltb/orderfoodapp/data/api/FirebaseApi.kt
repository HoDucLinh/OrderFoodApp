import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository<T : Any>(private val db: FirebaseFirestore) {

    fun fetchData(collectionName: String, targetList: MutableList<T>, clazz: Class<T>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(clazz)
                    targetList.add(item)
                }
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreRepository", "Error getting documents.", exception)
                onFailure(exception)
            }
    }

    fun addToFirebase(collectionName: String, items: List<T>, onComplete: (Boolean) -> Unit) {
        val collectionRef = db.collection(collectionName)
        var successCount = 0
        for (item in items) {
            collectionRef.add(item)
                .addOnSuccessListener {
                    successCount++
                    if (successCount == items.size) {
                        onComplete(true)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("FirestoreRepository", "Error adding item", exception)
                    onComplete(false)
                }
        }
    }
}
