package com.example.myutilities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityCountGasBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.roundToInt

class CountGas : AppCompatActivity() {
    private lateinit var gasCompany : String
    private var tariffModelPrice = 0.0
    private var gasCompanyTariff = 0.0
    private var savePriceGas = 0.0
    private lateinit var saveDateYearGas : String
    private lateinit var saveDateMonthGas : String
    private lateinit var saveDateDayGas : String
    private var ifPressedCountGas = false
    private var ifPressedDateGas = false
    private lateinit var tariffModelGas : String
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
                    "First reading cannot be higher or same as last reading!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            countGas()
            ifPressedCountGas = true
        }
        binding.btnSaveGas.setOnClickListener{

            if(!ifPressedCountGas)
                Toast.makeText(this@CountGas, "You have to count before saving!", Toast.LENGTH_LONG).show()
            else if (!ifPressedDateGas)
                Toast.makeText(this@CountGas, "You have to select date before saving!", Toast.LENGTH_LONG).show()
            else
                saveGasToFirebase()

        }
        binding.btnShowDateGas.setOnClickListener {
            showDateView()
        }

    }
    private fun checkIfBlankGas(): Boolean {
        val firstReadingGas = binding.txtGasFirstReading.text
        val lastReadingGas = binding.txtGasLastReading.text
        if (firstReadingGas.isBlank() || lastReadingGas.isBlank())
        {
            return true
        }
            return false
    }
    private fun checkIfFirstReadingGasIsHigher(): Boolean {
        val firstReadingGas = binding.txtGasFirstReading.text.toString()
        val lastReadingGas = binding.txtGasLastReading.text.toString()

        if (firstReadingGas.toInt() >= lastReadingGas.toInt()){
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
                    gasCompany = pickCompany[p2].toString()
                    spinnerTariff.adapter = adapterTariff
                    spinnerTariff.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
                        {
                            when(tariffModels[p2].toString())
                            {
                                "TM1" -> tariffModelPrice = 0.3298
                                "TM2" -> tariffModelPrice = 0.3298
                                "TM3" -> tariffModelPrice = 0.3279
                                "TM4" -> tariffModelPrice = 0.3234
                                "TM5" -> tariffModelPrice = 0.3179
                                "TM6" -> tariffModelPrice = 0.3143
                                "TM7" -> tariffModelPrice = 0.3096
                                "TM8" -> tariffModelPrice = 0.3050
                                "TM9" -> tariffModelPrice = 0.2995
                                "TM10" -> tariffModelPrice = 0.2914
                                "TM11" -> tariffModelPrice = 0.2821
                                "TM12" -> tariffModelPrice = 0.2730
                            }
                            tariffModelGas = tariffModels[p2].toString()
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Toast.makeText(this@CountGas, "You have to select tariff model!", Toast.LENGTH_LONG).show()
                        }
                    }
                    gasCompanyTariff = 9.2607
                }
                if(pickCompany[p2].toString() == "PIS")
                {
                    gasCompany = pickCompany[p2].toString()
                    spinnerTariff.adapter = adapterTariff
                    spinnerTariff.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
                        {
                            when(tariffModels[p2].toString())
                            {
                                "TM1" -> tariffModelPrice = 0.3953
                                "TM2" -> tariffModelPrice = 0.3830
                                "TM3" -> tariffModelPrice = 0.3830
                                "TM4" -> tariffModelPrice = 0.3769
                                "TM5" -> tariffModelPrice = 0.3708
                                "TM6" -> tariffModelPrice = 0.3646
                                "TM7" -> tariffModelPrice = 0.3585
                                "TM8" -> tariffModelPrice = 0.3524
                                "TM9" -> tariffModelPrice = 0.3461
                                "TM10" -> tariffModelPrice = 0.3339
                                "TM11" -> tariffModelPrice = 0.3216
                                "TM12" -> tariffModelPrice = 0.3094

                            }
                            tariffModelGas = tariffModels[p2].toString()
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
        gasEnergy *= tariffModelPrice
        val gasPrice = (gasEnergy * 100.0).roundToInt() / 100.0
        savePriceGas = gasPrice
        binding.txtGasBill.text = ("Your price is $gasPrice kn!")
    }


    private fun showDateView(){
        val dateView = Calendar.getInstance()
        val year = dateView.get(Calendar.YEAR)
        val month = dateView.get(Calendar.MONTH)
        val day = dateView.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, dateYear, dateMonth, dayOfMonth ->
            saveDateYearGas = dateYear.toString()
            saveDateMonthGas = (dateMonth + 1).toString()
            saveDateDayGas = dayOfMonth.toString()
                                                },year,month,day)
        datePicker.show()
        ifPressedDateGas = true
    }
    private fun saveGasToFirebase()
    {
        val db = FirebaseFirestore.getInstance()
        val gas: MutableMap<String, Any> = HashMap()
        val user = Firebase.auth.currentUser?.email.toString()
        gas["Company"] = gasCompany
        gas["Price"] = savePriceGas
        gas["User"] = user
        gas["TM"] = tariffModelGas
        gas["Date"]= "$saveDateMonthGas/$saveDateYearGas"


        db.collection("Gas").add(gas).addOnCompleteListener {
            Toast.makeText(this@CountGas, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}






