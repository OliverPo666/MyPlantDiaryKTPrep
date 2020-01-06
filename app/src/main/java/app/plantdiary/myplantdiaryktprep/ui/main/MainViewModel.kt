package app.plantdiary.myplantdiaryktprep.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.plantdiary.myplantdiaryktprep.dto.Plant

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    // livedata goes in here for plants.
    var plants = MutableLiveData<ArrayList<Plant>>()
}
