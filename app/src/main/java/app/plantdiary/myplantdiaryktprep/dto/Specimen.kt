package app.plantdiary.myplantdiaryktprep.dto

data class Specimen(var plantName:String, var latitude: String, var longitude:String, var location: String, var description: String, var specimenID: String = "", var plantID: Int = 0, var photos : ArrayList<Photo> = ArrayList<Photo>()) {
    constructor() : this("", "0.0", "0.0", "", "")
}