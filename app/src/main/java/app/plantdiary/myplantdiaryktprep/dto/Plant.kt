package app.plantdiary.myplantdiaryktprep.dto

import com.google.gson.annotations.SerializedName

data class Plant(@SerializedName("id") var plantId: Int = 0, var species: String, var genus : String, var cultivar: String, var common:String) {
    override fun toString(): String {
        return "$common $genus $species $cultivar $plantId"
    }
}