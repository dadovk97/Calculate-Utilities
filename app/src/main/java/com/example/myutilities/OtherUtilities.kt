package com.example.myutilities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myutilities.databinding.ActivityOtherUtilitiesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityOtherUtilitiesBinding
private lateinit var pickedUtility : String
private lateinit var saveDateYearGas: String
private lateinit var saveDateMonthGas: String
private lateinit var saveDateDayGas: String
private var price = 0.0
private var ifPressedDateOther = false


class OtherUtilities : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherUtilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pickUtility()

        binding.btnSavedOther.setOnClickListener{
            val intent = Intent(this, SavedOtherUtilities::class.java)
            startActivity(intent)
        }

        binding.btnSaveOther.setOnClickListener{
            if(checkIfBlankOther()){
                Toast.makeText(this@OtherUtilities, "Please enter the data!", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            else if (!ifPressedDateOther)
                Toast.makeText(
                    this@OtherUtilities, "You have to select date before saving!", Toast.LENGTH_LONG).show()

            else
                saveOtherToFirebase()
        }

        binding.btnDateOther.setOnClickListener{
            showDateView()
        }
    }

    private fun pickUtility() {
        val pickUtility = resources.getStringArray(R.array.utility)
        val spinnerUtility = binding.spnPickUtility
        val adapterUtility = ArrayAdapter(this, android.R.layout.simple_spinner_item, pickUtility)
        spinnerUtility.adapter = adapterUtility
        spinnerUtility.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               when(pickUtility[p2].toString()){
                   "Mobilna pretplata" -> pickedUtility = "Mobilna pretplata"
                   "Komunalne usluge" -> pickedUtility = "Komunalne usluge"
                   "Fiksna mreza/Internet" -> pickedUtility = "Fiksna mreza/Internet"
               }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@OtherUtilities, "You have to select utility", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun checkIfBlankOther(): Boolean {
        val companyName = binding.txtCompanyName.text
        val otherPrice = binding.txtPrice.text
        val billNumberOther = binding.txtBillNumberOther.text
        if (companyName.isBlank() || otherPrice.isBlank() || billNumberOther.isBlank()) {
            return true
        }
        return false
    }

    private fun saveOtherToFirebase() {
        price = binding.txtPrice.text.toString().toDouble()
        val db = FirebaseFirestore.getInstance()
        val otherUtilities: MutableMap<String, Any> = HashMap()
        val user = Firebase.auth.currentUser?.email.toString()
        otherUtilities["Utility"] = pickedUtility
        otherUtilities["Company"] = binding.txtCompanyName.text.toString()
        otherUtilities["Price"] = price
        otherUtilities["User"] = user
        otherUtilities["Date"]= "$saveDateMonthGas/$saveDateYearGas"
        otherUtilities["Bill_ID"] = binding.txtBillNumberOther.text.toString()
        db.collection("Other").add(otherUtilities).addOnCompleteListener {

            Toast.makeText(this@OtherUtilities, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }


    }
    private fun showDateView() {
        val dateView = Calendar.getInstance()
        val year = dateView.get(Calendar.YEAR)
        val month = dateView.get(Calendar.MONTH)
        val day = dateView.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, dateYear, dateMonth, dayOfMonth ->
            saveDateYearGas = dateYear.toString()
            saveDateMonthGas = (dateMonth + 1).toString()
            saveDateDayGas = dayOfMonth.toString()
        }, year, month, day)
        datePicker.show()
        ifPressedDateOther = true
    }
}