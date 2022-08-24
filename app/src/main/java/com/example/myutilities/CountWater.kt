package com.example.myutilities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityCountWaterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.roundToInt

class CountWater : AppCompatActivity() {
    private lateinit var binding: ActivityCountWaterBinding
    private var waterCompanyPrice = 0.0
    private var waterCompanyPDV = 0.0
    private lateinit var waterCompanyName : String
    private var waterPrice = 0.0
    private lateinit var saveDateYearWater : String
    private lateinit var saveDateMonthWater : String
    private lateinit var saveDateDayWater : String
    private var ifPressedCountWater = false
    private var ifPressedDateWater = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountWaterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pickWaterCompany()

        binding.btnSavedWater.setOnClickListener{
            val intent = Intent(this, SavedWater::class.java)
            startActivity(intent)
        }

        binding.btnCountWater.setOnClickListener {
            if (checkIfBlankWater()) {
                Toast.makeText(this@CountWater, "Please enter the data!", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (checkIfFirstReadingGasIsHigher()) {
                Toast.makeText(
                    this@CountWater,
                    "First reading cannot be higher or same as last reading!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            countWater()
            ifPressedCountWater = true
        }
        binding.btnSaveWater.setOnClickListener {

            if (!ifPressedCountWater)
                Toast.makeText(
                    this@CountWater,
                    "You have to count before saving!",
                    Toast.LENGTH_LONG
                ).show()
            else if (!ifPressedDateWater)
                Toast.makeText(
                    this@CountWater,
                    "You have to select date before saving!",
                    Toast.LENGTH_LONG
                ).show()
            else
                saveWaterToFirebase()
        }
        binding.btnDateWater.setOnClickListener {
            showDateView()
        }
    }


    private fun checkIfBlankWater(): Boolean {
        val firstReadingWater = binding.txtFirstReadingWater.text
        val lastReadingWater = binding.txtLastReadingWater.text
        val billNumber = binding.txtBillNumberWater.text
        if (firstReadingWater.isBlank() || lastReadingWater.isBlank() || billNumber.isBlank()) {
            return true
        }
        return false
    }

    private fun checkIfFirstReadingGasIsHigher(): Boolean {
        val firstReadingWater = binding.txtFirstReadingWater.text.toString()
        val lastReadingWater = binding.txtLastReadingWater.text.toString()

        if (firstReadingWater.toInt() >= lastReadingWater.toInt()) {
            return true
        }
        return false
    }

    private fun pickWaterCompany() {
        val pickCompany = resources.getStringArray(R.array.water_company)
        val spinnerCompany = binding.spinnerWaterCompany
        val adapterCompany = ArrayAdapter(this, android.R.layout.simple_spinner_item, pickCompany)
        spinnerCompany.adapter = adapterCompany
        spinnerCompany.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (pickCompany[p2].toString()) {
                    "Vodovod - Osijek d.o.o" -> {
                        waterCompanyPrice = 28.25
                        waterCompanyPDV = 12.68
                        waterCompanyName = "Vodovod - Osijek d.o.o"
                    }
                    "Vodovod i Kanalizacija d.o.o Rijeka" -> {
                        waterCompanyPrice = 19.20
                        waterCompanyPDV = 12.7157
                        waterCompanyName = "Vodovod i Kanalizacija d.o.o Rijeka"
                    }
                    "Vodoopskrba i odvodnja d.o.o Zagreb" -> {
                        waterCompanyPrice = 18.92
                        waterCompanyPDV = 15.2887
                        waterCompanyName = "Vodoopskrba i odvodnja d.o.o Zagreb"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(
                    this@CountWater,
                    "You have to select water company!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun countWater() {
        var waterDifference = binding.txtLastReadingWater.text.toString().toInt()
            .toDouble() - binding.txtFirstReadingWater.text.toString().toInt().toDouble()
        waterDifference = waterDifference * waterCompanyPDV + waterCompanyPrice
        waterPrice = (waterDifference * 100.0).roundToInt() / 100.0
        binding.txtWaterBill.text = ("Your price is $waterPrice kn!")
    }

    private fun showDateView() {
        val dateView = Calendar.getInstance()
        val year = dateView.get(Calendar.YEAR)
        val month = dateView.get(Calendar.MONTH)
        val day = dateView.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, dateYear, dateMonth, dayOfMonth ->
            saveDateYearWater = dateYear.toString()
            saveDateMonthWater = (dateMonth + 1).toString()
            saveDateDayWater = dayOfMonth.toString()
        }, year, month, day)
        datePicker.show()
        ifPressedDateWater = true
    }

    private fun saveWaterToFirebase() {
        val db = FirebaseFirestore.getInstance()
        val water: MutableMap<String, Any> = HashMap()
        val user = Firebase.auth.currentUser?.email.toString()
        water["Price"] = waterPrice
        water["User"] = user
        water["Date"] = "$saveDateMonthWater/$saveDateYearWater"
        water["Company"] = waterCompanyName
        water["Bill_ID"] = binding.txtBillNumberWater.text.toString()

        db.collection("Water").add(water).addOnCompleteListener {
            Toast.makeText(this@CountWater, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}



