package app.plantdiary.myplantdiaryktprep.dto

data class Specimen(var plantName:String, var latitude: String, var longitude:String, var location: String, var description: String, var specimenID: Int = 0, var plantID: Int = 0) {
    constructor() : this("", "0.0", "0.0", "", "")
}