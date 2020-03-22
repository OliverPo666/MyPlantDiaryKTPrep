package app.plantdiary.myplantdiaryktprep.dto

import com.google.firebase.firestore.Exclude

data class Specimen(var plantName:String, var latitude: String, var longitude:String, var location: String, var description: String, var specimenID: String = "", var plantID: Int = 0, var events: ArrayList<Event> = ArrayList<Event>()) {
    constructor() : this("", "0.0", "0.0", "", "")

}