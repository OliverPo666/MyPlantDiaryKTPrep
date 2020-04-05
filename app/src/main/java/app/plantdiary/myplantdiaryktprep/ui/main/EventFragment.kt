package app.plantdiary.myplantdiaryktprep.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.plantdiary.myplantdiaryktprep.MainActivity

import app.plantdiary.myplantdiaryktprep.R
import app.plantdiary.myplantdiaryktprep.dto.Event
import app.plantdiary.myplantdiaryktprep.dto.Photo
import app.plantdiary.myplantdiaryktprep.dto.Specimen
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.event_fragment.btnSaveEvent
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.rowlayout.*
import java.util.*

class EventFragment : DiaryFragment() {

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

        btnBack.setOnClickListener {
            (activity as MainActivity).onSwipeLeft()
        }
        btnSaveEvent.setOnClickListener {
            saveEvent()
        }
        btnTakeEventPhoto.setOnClickListener {
            prepTakePhoto()
        }

        rcyEvents.hasFixedSize()
        rcyEvents.layoutManager = LinearLayoutManager(context)
        rcyEvents.itemAnimator = DefaultItemAnimator()
        rcyEvents.adapter = EventsAdapter(specimen.events, R.layout.rowlayout)
//        rcyEvents.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
//            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//
//                return false
//            }
//        })

    }

    private fun saveEvent() {
        var event = Event()
        var date = edtDate.text.toString()
        event.type = actEventType.toString()
        event.description = edtDescription.text.toString()
        event.quantity = edtQuantity.text.toString().toDoubleOrNull() ?: 0.0
        if (photoURI != null) {
            event.localPhotoUri = photoURI.toString()
        }
        specimen.events.add(event)
        viewModel.save(event)
        clearAll()
        rcyEvents.adapter?.notifyDataSetChanged()
    }

    private fun clearAll() {
        edtDate.setText("")
        actEventType.setText("")
        edtDescription.setText("")
        edtQuantity.setText("")
        photoURI = null
    }

    override fun onSwipeBottom() {
        prepTakePhoto()
    }

}
