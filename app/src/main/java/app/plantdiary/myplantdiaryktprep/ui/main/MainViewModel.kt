package app.plantdiary.myplantdiaryktprep.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.plantdiary.myplantdiaryktprep.RetrofitClientInstance
import app.plantdiary.myplantdiaryktprep.dao.IPlantDAO
import app.plantdiary.myplantdiaryktprep.dto.Plant
import app.plantdiary.myplantdiaryktprep.dto.Specimen
import app.plantdiary.myplantdiaryktprep.service.PlantService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel()  : ViewModel() {
    var  plantService: PlantService = PlantService()

    // livedata goes in here for plants.
    private var _plants = MutableLiveData<ArrayList<Plant>>()
    private var _plantsArray:ArrayList<Plant>? = ArrayList<Plant>()
    private lateinit var firestore: FirebaseFirestore
    private var _specimens = MutableLiveData<ArrayList<Specimen>>()


    // use an initialization (constructor or static) to kick of plant JSON unmarshalling process with Retrofit.
    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        populatePlants()
        readSpecimens()
        listenToSpecimens()
    }

    private fun listenToSpecimens() {
        firestore.collection("specimens").addSnapshotListener{
            snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val allSpecimens = ArrayList<Specimen>()
                val documents = snapshot.documents
                documents.forEach {
                    var id = it.id
                    val specimen = it.toObject(Specimen::class.java)
                    if (specimen != null) {
                        specimen.specimenID = it.id
                        allSpecimens.add(specimen!!)
                    }
                }
                _specimens.value = allSpecimens
            }
        }
    }

    private fun readSpecimens() {
        firestore.collection("specimens").get()
            .addOnSuccessListener {
                document -> try {
                    if (document != null) {
                        val specimens = document.toObjects(Specimen::class.java)
                    } else {
                        Log.e(TAG, "No data returned from Firestore.")
                    }

                } catch (ex: Exception) {
                    Log.e(TAG, "Exception while retreiving Firestore data: $ex")
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Exception while retreiving Firestore data: $it")
            }
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
                _plants.value = body
            }

            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                Log.e("Parse JSON", "Something went wrong")
            }
        })

    }

    fun fetchPlants(plantName:String) {
        plantService.fetchPlants(plantName)
    }

    /**
     * Save to Firebase.
     */
    fun save(specimen: Specimen) {
        // could also update UI with observable.
        val document = firestore.collection("specimens").document()
        val set = document.set(specimen)
        set.addOnSuccessListener {
                val id = document.id
                Log.d("Firebase", "Document saved.")

            }
            .addOnFailureListener {
                Log.d("Firebase", "Something went wrong.")
            }
    }

    var plants:MutableLiveData<ArrayList<Plant>>
        get() { return _plants}
        set(value) {_plants = value}

    var plantsArray:ArrayList<Plant>?
        get() {return _plantsArray}
        set(value) {_plantsArray  = value}

    var specimens:MutableLiveData<ArrayList<Specimen>>
        get() { return _specimens}
        set(value) {_specimens = value}
}
