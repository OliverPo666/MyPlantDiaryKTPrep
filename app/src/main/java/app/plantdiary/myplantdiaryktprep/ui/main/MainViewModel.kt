package app.plantdiary.myplantdiaryktprep.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.plantdiary.myplantdiaryktprep.RetrofitClientInstance
import app.plantdiary.myplantdiaryktprep.dao.IPlantDAO
import app.plantdiary.myplantdiaryktprep.dto.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    // livedata goes in here for plants.
    private var _plants = MutableLiveData<ArrayList<Plant>>()

    // use an initialization (constructor or static) to kick of plant JSON unmarshalling process with Retrofit.
    init {
        populatePlants()
    }

    private fun populatePlants() {
        // TODO move this to a specialized class, perhaps in service or DAO layer.
        val service = RetrofitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
        val call = service?.getAllPlants()
        call?.enqueue(object: Callback<ArrayList<Plant>> {
            override fun onResponse(
                call: Call<ArrayList<Plant>>,
                response: Response<ArrayList<Plant>>
            ) {
                val body = response?.body()
                val size = body?.size;
            }

            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                val i = 1 + 1;
                val j = 2 + 2;
            }
        })

    }

    var plants:MutableLiveData<ArrayList<Plant>>
        get() { return _plants}
        set(value) {_plants = value}
}
