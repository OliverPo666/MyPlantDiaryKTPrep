package app.plantdiary.myplantdiaryktprep.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName="plant")
data class Plant(@PrimaryKey @SerializedName("id") var plantId: Int = 0, var species: String, var genus : String, var cultivar: String, var common:String) {
    override fun toString(): String {
        return "$common $genus $species $cultivar $plantId"
    }
}