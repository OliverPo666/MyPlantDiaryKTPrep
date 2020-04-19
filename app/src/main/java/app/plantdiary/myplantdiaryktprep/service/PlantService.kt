package app.plantdiary.myplantdiaryktprep.service

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import app.plantdiary.myplantdiaryktprep.RetrofitClientInstance
import app.plantdiary.myplantdiaryktprep.dao.IPlantDAO
import app.plantdiary.myplantdiaryktprep.dao.PlantDatabase
import app.plantdiary.myplantdiaryktprep.dto.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantService {

    suspend fun fetchPlants(application: Application, plantName:String)  {
        withContext(Dispatchers.IO) {
            // TODO move this to a specialized class, perhaps in service or DAO layer.
            var _plants = MutableLiveData<ArrayList<Plant>>();
            val service = RetrofitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
            val call = service?.getPlants(plantName)
            call?.enqueue(object : Callback<ArrayList<Plant>> {
                override fun onResponse(
                    call: Call<ArrayList<Plant>>,
                    response: Response<ArrayList<Plant>>
                ) {
                    val body = response?.body()
                    val size = body?.size;
                    _plants.value = body
                    // _plants.postValue(body)
                    updateLocalPlants(application, body!!)
                    var i = 1 + 1

                }

                override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                    val i = 1 + 1;
                    val j = 2 + 2;
                }
            })
            delay(60000)
        }


    }

    fun updateLocalPlants(application: Application, plants: ArrayList<Plant>) {

            try {
                val db =
                    Room.databaseBuilder(application, PlantDatabase::class.java, "diary").build()

                val localPlantDAO = db.localPlantDAO()
                localPlantDAO.insertAll(plants)
            } catch (e:  Exception ) {
                var foo = e.message
            }
            var i = 1+ 1


    }

}