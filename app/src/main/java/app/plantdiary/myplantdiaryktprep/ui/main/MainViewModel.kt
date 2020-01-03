package app.plantdiary.myplantdiaryktprep.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _names = MutableLiveData<String>()

    val names : LiveData<String>
        get()  {
            _names.setValue("Gloria");
            return _names
        }


    fun updateNames() {
        _names.setValue("Brandan");
    }
}
