package uk.ac.tees.mad.d3857102

import com.google.firebase.firestore.auth.User

data class Wearables(
    val name: String,
    val price: Int,
    val image_url: String,
    val description : String,
    val latitude: Double,
    val longitude: Double,
) {
    constructor() : this("", 0, "","",0.0,0.0)
}
