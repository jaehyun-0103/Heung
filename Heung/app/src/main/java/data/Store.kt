package data

import com.google.firebase.firestore.GeoPoint

data class Store(
    val name: String,
    val address: String,
    val location: GeoPoint,
    val district: String,
    val phoneNumber: String,
    val content: String
)