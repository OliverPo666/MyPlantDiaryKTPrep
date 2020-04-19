package app.plantdiary.myplantdiaryktprep.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.plantdiary.myplantdiaryktprep.LocationViewModel
import app.plantdiary.myplantdiaryktprep.MainActivity
import app.plantdiary.myplantdiaryktprep.R
import app.plantdiary.myplantdiaryktprep.dto.Event
import app.plantdiary.myplantdiaryktprep.dto.Photo
import app.plantdiary.myplantdiaryktprep.dto.Plant
import app.plantdiary.myplantdiaryktprep.dto.Specimen
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.event_fragment.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.btnSaveEvent
import java.util.*

class MainFragment : DiaryFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    private val LOCATION_REQUEST_CODE = 1997
    private lateinit var _plants : List<Plant>
    private var plantId = 0;
    private var specimen = Specimen()
    private var _events : ArrayList<Event> = ArrayList<Event>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activity.let {
            viewModel = ViewModelProviders.of(it!!).get(MainViewModel::class.java)
        }
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        var localPlantDAO  = locationViewModel.getLocalPlantDAO()
        localPlantDAO.getAllPlants().observe( this, Observer {
            // do something here to wire up the objects, from the feed of JSON data, to be the autocomplete's data source.
            plants ->  actPlants.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, plants))
            _plants = plants
            prepRequestLocationUpdates()
        })
        viewModel.events.observe(this, Observer {
            // update event collection and inform adapter.
            events ->
            _events.removeAll(_events)
            _events.addAll(events)
            rcyEventsForSpecimens.adapter!!.notifyDataSetChanged()
        })

        actPlants.setOnItemClickListener { parent, view, position, id ->
            var selectedPlant = parent.getItemAtPosition(position) as Plant
             if (selectedPlant != null) {
                 plantId = selectedPlant.plantId
             }
         }
        spnSpecimens.onItemSelectedListener = object :OnItemSelectedListener {
            /**
             * Callback method to be invoked when the selection disappears from this
             * view. The selection can disappear for instance when touch is activated
             * or when the adapter becomes empty.
             *
             * @param parent The AdapterView that now contains no selected item.
             */
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            /**
             *
             * Callback method to be invoked when an item in this view has been
             * selected. This callback is invoked only when the newly selected
             * position is different from the previously selected position or if
             * there was no selected item.
             *
             * Implementers can call getItemAtPosition(position) if they need to access the
             * data associated with the selected item.
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var specimen = parent?.getItemAtPosition(position) as Specimen
                actPlants.setText(specimen.plantName)
                txtDescription.setText(specimen.description)
                viewModel.specimen = specimen
                viewModel.fetchEvents()

            }

        }

        viewModel.specimens.observe(this, Observer {
            specimens -> spnSpecimens.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, specimens))
        })



        btnTakePhoto.setOnClickListener{
            prepTakePhoto()
        }

        btnLogon.setOnClickListener {
            logon()
            // prepOpenGallery()
        }

        btnSaveEvent.setOnClickListener {
            saveSpecimen()
        }
        btnRightArrow.setOnClickListener {
            (activity as MainActivity).onSwipeRight()
        }
        rcyEventsForSpecimens.hasFixedSize()
        rcyEventsForSpecimens.layoutManager = LinearLayoutManager(context)
        rcyEventsForSpecimens.itemAnimator = DefaultItemAnimator()
        rcyEventsForSpecimens.adapter = EventsAdapter(_events, R.layout.rowlayout)

    }

    private fun logon() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), AUTH_REQUEST_CODE
        )
    }

    internal fun saveSpecimen() {
        if (user == null) {
            logon()
        }
        // user ?: return
        storeSpecimen()
        viewModel.save(specimen, photos, user!!)

        // new specimen for the next go.
        specimen = Specimen()
        photos = ArrayList<Photo>()

    }

    internal fun storeSpecimen() {
        specimen.plantName = actPlants.text.toString()
        specimen.description = txtDescription.text.toString()
        specimen.latitude = lblLatitudeValue.text.toString()
        specimen.longitude = lblLongitudeValue.text.toString()
        specimen.plantID = plantId
        specimen.specimenID = "1"
        viewModel.specimen = specimen
    }

    private fun prepOpenGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
        }
    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_REQUEST_CODE)
        }
    }

    private fun requestLocationUpdates() {

        locationViewModel.getLocationData().observe(this, Observer {
            lblLatitudeValue.text = it.latitude
            lblLongitudeValue.text = it.longitude
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_IMAGE_REQUEST_CODE) {
                Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
                var myPhoto = Photo(localUri = photoURI.toString(), dateTaken = Date())
                photos.add(myPhoto)
                photoURI = null
            } else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
            } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                val selectedImage = data!!.data
                val source = ImageDecoder.createSource(activity!!.contentResolver, selectedImage!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
            } else if (requestCode == AUTH_REQUEST_CODE) {
                user = FirebaseAuth.getInstance().currentUser
                var i = 1 + 1
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            if (requestCode == AUTH_REQUEST_CODE) {
                Toast.makeText(context!!, "Kan't logon", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_REQUEST_CODE  -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(context, "Unable to show GPS without location permission", Toast.LENGTH_LONG).show()
                }

            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
         }

    }

}
