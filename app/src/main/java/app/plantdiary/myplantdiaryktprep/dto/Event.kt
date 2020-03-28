package app.plantdiary.myplantdiaryktprep.dto

import java.util.*

data class Event(var type: String = "", var date : Date = Date(), var quantity: Double? = 0.0, var description: String = "", var localPhotoUri: String?  = null) {
}