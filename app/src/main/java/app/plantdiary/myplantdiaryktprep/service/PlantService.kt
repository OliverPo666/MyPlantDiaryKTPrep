package app.plantdiary.myplantdiaryktprep.service

import androidx.lifecycle.MutableLiveData
import app.plantdiary.myplantdiaryktprep.RetrofitClientInstance
import app.plantdiary.myplantdiaryktprep.dao.IPlantDAO
import app.plantdiary.myplantdiaryktprep.dto.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantService {

    fun fetchPlants(plantName:String) : MutableLiveData<ArrayList<Plant>> {
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
                var i = 1 + 1

            }

            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                val i = 1 + 1;
                val j = 2 + 2;
            }
        })
        return _plants
    }

}