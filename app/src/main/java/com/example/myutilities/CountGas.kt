package com.example.myutilities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityCountGasBinding

class CountGas : AppCompatActivity() {
    private lateinit var binding: ActivityCountGasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountGasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tariffModels = resources.getStringArray(R.array.planets_array)
        val spinner = binding.planetsSpinner


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, tariffModels
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               binding.textView.text = tariffModels[p2]

            }



            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}




