package app.plantdiary.myplantdiaryktprep.dto

import java.util.*

data class Event(var id : String = "", var type: String = "", var date : Date = Date(), var quantity: Double? = 0.0, var description: String = "", var localPhotoUri: String?  = null) {
    override fun toString(): String {
        return "$type $quantity $description"
    }
}