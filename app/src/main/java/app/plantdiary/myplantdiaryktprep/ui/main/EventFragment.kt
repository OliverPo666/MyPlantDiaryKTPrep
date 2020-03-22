package app.plantdiary.myplantdiaryktprep.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.plantdiary.myplantdiaryktprep.R
import app.plantdiary.myplantdiaryktprep.dto.Event
import app.plantdiary.myplantdiaryktprep.dto.Specimen
import kotlinx.android.synthetic.main.event_fragment.*

class EventFragment : Fragment() {

    companion object {
        fun newInstance() = EventFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var specimen: Specimen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activity.let {
            viewModel = ViewModelProviders.of(it!!).get(MainViewModel::class.java)
        }
        // TODO: Use the ViewModel
        specimen = viewModel.specimen

        btnSaveEvent.setOnClickListener {
            saveEvent()

        }

    }

    private fun saveEvent() {
        var event = Event()
        var date = edtDate.text.toString()
        event.type = actEventType.toString()
        event.description = edtDescription.text.toString()
        event.quantity = edtQuantity.text.toString().toDoubleOrNull() ?: 0.0
        specimen.events.add(event)
    }

}
