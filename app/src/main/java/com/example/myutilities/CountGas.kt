package com.example.myutilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityCountGasBinding
import kotlin.math.roundToInt

class CountGas : AppCompatActivity() {
    var tariffModel = 0.0
    var gasCompanyTariff = 0.0
    private lateinit var binding: ActivityCountGasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountGasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pickGasCompany()

        binding.btnCountGas.setOnClickListener{
            if(checkIfBlankGas())
            {
                Toast.makeText(this@CountGas, "Please enter the data!", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (checkIfFirstReadingGasIsHigher()) {
                Toast.makeText(this@CountGas,
                    "First reading cannot be higher than last reading!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            countGas()
        }


    }
    private fun checkIfBlankGas(): Boolean {
        val firstReadingDayGas = binding.txtGasFirstReading.text
        val lastReadingDayGas = binding.txtGasLastReading.text
        if (firstReadingDayGas.isBlank() || lastReadingDayGas.isBlank())
        {
            return true
        }
            return false
    }
    private fun checkIfFirstReadingGasIsHigher(): Boolean {
        val firstReadingDayGas = binding.txtGasFirstReading.text.toString()
        val lastReadingDayGas = binding.txtGasLastReading.text.toString()

        if (firstReadingDayGas.toInt() > lastReadingDayGas.toInt()){
            return true
        }
        return false
    }

    private fun pickGasCompany() {
        val tariffModels = resources.getStringArray(R.array.tariff_models)
        val spinnerTariff = binding.spinnerTariffModels
        val adapterTariff = ArrayAdapter(this, android.R.layout.simple_spinner_item, tariffModels)
        val pickCompany = resources.getStringArray(R.array.gas_company)
        val spinnerCompany = binding.spinnerGasCompany
        val adapterCompany = ArrayAdapter(this, android.R.layout.simple_spinner_item, pickCompany)
        spinnerCompany.adapter = adapterCompany
        spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(pickCompany[p2].toString() == "PPD")
                {
                    spinnerTariff.adapter = adapterTariff
                    spinnerTariff.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
                        {
                            when(tariffModels[p2].toString())
                            {
                                "TM1" -> tariffModel = 0.3298
                                "TM2" -> tariffModel = 0.3298
                                "TM3" -> tariffModel = 0.3279
                                "TM4" -> tariffModel = 0.3234
                                "TM5" -> tariffModel = 0.3179
                                "TM6" -> tariffModel = 0.3143
                                "TM7" -> tariffModel = 0.3096
                                "TM8" -> tariffModel = 0.3050
                                "TM9" -> tariffModel = 0.2995
                                "TM10" -> tariffModel = 0.2914
                                "TM11" -> tariffModel = 0.2821
                                "TM12" -> tariffModel = 0.2730
                            }


                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Toast.makeText(this@CountGas, "You have to select tariff model!", Toast.LENGTH_LONG).show()
                        }
                    }
                    gasCompanyTariff = 9.2607
                }
                if(pickCompany[p2].toString() == "PIS")
                {
                    spinnerTariff.adapter = adapterTariff
                    spinnerTariff.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
                        {
                            when(tariffModels[p2].toString())
                            {
                                "TM1" -> tariffModel = 0.3953
                                "TM2" -> tariffModel = 0.3830
                                "TM3" -> tariffModel = 0.3830
                                "TM4" -> tariffModel = 0.3769
                                "TM5" -> tariffModel = 0.3708
                                "TM6" -> tariffModel = 0.3646
                                "TM7" -> tariffModel = 0.3585
                                "TM8" -> tariffModel = 0.3524
                                "TM9" -> tariffModel = 0.3461
                                "TM10" -> tariffModel = 0.3339
                                "TM11" -> tariffModel = 0.3216
                                "TM12" -> tariffModel = 0.3094

                            }

                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Toast.makeText(this@CountGas, "You have to select tariff model!", Toast.LENGTH_LONG).show()
                        }
                    }
                    gasCompanyTariff = 9.638183
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@CountGas, "You have to select gas company!", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun countGas(){
        val gasDifference = binding.txtGasLastReading.text.toString().toInt() - binding.txtGasFirstReading.text.toString().toInt()
        var gasEnergy = gasDifference * gasCompanyTariff
        gasEnergy *= tariffModel
        val gasPrice = (gasEnergy * 100.0).roundToInt() / 100.0
        binding.txtGasBill.text = ("Your price is $gasPrice kn!")
    }
}





