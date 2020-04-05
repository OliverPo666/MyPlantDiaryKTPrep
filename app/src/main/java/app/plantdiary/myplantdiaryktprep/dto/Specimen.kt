package app.plantdiary.myplantdiaryktprep.dto

import com.google.firebase.firestore.Exclude

data class Specimen(var plantName:String, var latitude: String, var longitude:String, var location: String, var description: String, var specimenID: String = "", var plantID: Int = 0) {
    constructor() : this("", "0.0", "0.0", "", "")

    private var _events: ArrayList<Event> = ArrayList<Event>()


    var events : ArrayList<Event>
        @Exclude get() { return _events}
        set(value) {_events = value}
}