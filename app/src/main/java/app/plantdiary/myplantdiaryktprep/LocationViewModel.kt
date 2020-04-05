package app.plantdiary.myplantdiaryktprep

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import app.plantdiary.myplantdiaryktprep.dao.IPlantDAO
import app.plantdiary.myplantdiaryktprep.dao.PlantDatabase
import app.plantdiary.myplantdiaryktprep.dto.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)
    fun getLocationData() = locationData;

    init {
        populatePlants(application)
    }

    private fun populatePlants(application: Application) {
        val service = RetrofitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
        val call = service?.getAllPlants()
        call?.enqueue(object: Callback<ArrayList<Plant>> {
            override fun onResponse(
                call: Call<ArrayList<Plant>>,
                response: Response<ArrayList<Plant>>
            ) {
                val body = response?.body()
                val size = body?.size;
                viewModelScope.launch {
                    updateLocalPlants(application, body!!)
                }
                var i = 1 + 1
            }

            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                Log.e("Parse JSON", "Something went wrong")
            }
        })

    }

    suspend fun updateLocalPlants(application: Application, plants: ArrayList<Plant>) {
        withContext(Dispatchers.IO) {
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
}