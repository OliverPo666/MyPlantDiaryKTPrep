package app.plantdiary.myplantdiaryktprep.dao

import app.plantdiary.myplantdiaryktprep.dto.Plant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IPlantDAO {

    // May need to change generic that goes to Call.
    @GET("/perl/mobile/viewplantsjsonarray.pl?Combined_Name=e")
    fun getAllPlants() : Call<ArrayList<Plant>>

    @GET("/perl/mobile/viewplantsjsonarray.pl")
    fun getPlants(@Query("Combined_Name") plantName:String ) : Call<ArrayList<Plant>>
}