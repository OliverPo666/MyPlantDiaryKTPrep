package app.plantdiary.myplantdiaryktprep.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import app.plantdiary.myplantdiaryktprep.R
import app.plantdiary.myplantdiaryktprep.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // not working well in fragment... may need to move to activity.
        val binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this.activity;




        return binding.root;
        // return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.names.observe(this,
            Observer {
                name -> Toast.makeText(context, name, Toast.LENGTH_LONG).show();
                txtDescription.setText(name)
            })

        btnSave.setOnClickListener {
            Toast.makeText(context, "Save", Toast.LENGTH_LONG).show()
        }


    }

    public fun addName(v:View?) {
        viewModel.updateNames();
    }


}
