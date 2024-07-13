package uk.ac.tees.mad.d3857102

data class user(    val name: String,
                         val phone : String,
                         val image_url: String,
                         val user_id : String,
                         val address : String,
                         val email : String) {
    constructor() : this("", "", "", "", "" , "")
}