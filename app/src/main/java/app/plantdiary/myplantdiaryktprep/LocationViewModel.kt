package app.plantdiary.myplantdiaryktprep

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class LocationViewModel(context: Context) : ViewModel() {

    private val locationData = LocationLiveData(context)
    fun getLocationData() = locationData;
}