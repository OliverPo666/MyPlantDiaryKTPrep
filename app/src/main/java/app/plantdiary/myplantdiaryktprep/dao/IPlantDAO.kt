package app.plantdiary.myplantdiaryktprep.dao

import app.plantdiary.myplantdiaryktprep.dto.Plant
import retrofit2.Call
import retrofit2.http.GET

interface IPlantDAO {

    // May need to change generic that goes to Call.
    @GET("/perl/mobile/viewplantsjsonarray.pl?Combined_Name=e")
    fun getAllPlants() : Call<ArrayList<Plant>>
}