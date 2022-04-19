package com.example.myutilities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityCountElectricityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class CountElectricity : AppCompatActivity() {
    private lateinit var binding: ActivityCountElectricityBinding
    private var savePrice = 0.0
    private var saveDateYear = 0
    private var saveDateMonth = 0
    private var saveDateDay = 0
    private var ifPressedCount = false
    private var ifPressedDate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountElectricityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCountElectricity.setOnClickListener {

            if (checkIfBlank()) {
                Toast.makeText(this@CountElectricity, "Please enter the data!", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (checkIfFirstReadingIsHigher()) {
                Toast.makeText(this@CountElectricity,
                    "First reading cannot be higher than last reading!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            countElectricity()
            ifPressedCount = true
        }
        binding.btnSaveElectricity.setOnClickListener{

            if(!ifPressedCount)
                Toast.makeText(this@CountElectricity, "You have to count before saving!", Toast.LENGTH_LONG).show()
            else if (!ifPressedDate)
                Toast.makeText(this@CountElectricity, "You have to select date before saving!", Toast.LENGTH_LONG).show()
            else
                saveToFirebase()

        }
        binding.btnShowDate.setOnClickListener {
            showDateView()
        }
    }

    private fun checkIfBlank(): Boolean {

        val firstReadingDay = binding.txtFirstReadingDay.text.toString()
        val lastReadingDay = binding.txtLastReadingDay.text.toString()
        val firstReadingNight = binding.txtFirstReadingNight.text.toString()
        val lastReadingNight = binding.txtLastReadingNight.text.toString()

        if (firstReadingDay.isBlank() || lastReadingDay.isBlank() || firstReadingNight.isBlank() || lastReadingNight.isBlank()) {
            return true
        }
        return false
    }

    private fun checkIfFirstReadingIsHigher(): Boolean {
        val firstReadingDay = binding.txtFirstReadingDay.text.toString()
        val lastReadingDay = binding.txtLastReadingDay.text.toString()
        val firstReadingNight = binding.txtFirstReadingNight.text.toString()
        val lastReadingNight = binding.txtLastReadingNight.text.toString()

        if (firstReadingDay.toInt() > lastReadingDay.toInt() || firstReadingNight.toInt() > lastReadingNight.toInt()) {
            return true
        }

        return false
    }
    @SuppressLint("SetTextI18n")
    private fun countElectricity() {

        val differenceDay = binding.txtLastReadingDay.text.toString().toInt() - binding.txtFirstReadingDay.text.toString().toInt()
        val differenceNight = binding.txtLastReadingNight.text.toString().toInt() - binding.txtFirstReadingNight.text.toString().toInt()
        val firstFee = differenceDay.toDouble() * 0.11 + differenceNight.toDouble() * 0.05
        val secondFee = differenceDay.toDouble() * 0.24 + differenceNight.toDouble() * 0.12 + 10
        val thirdFee = (differenceDay.toDouble() + differenceNight.toDouble()) * 0.1050
        val x1 = differenceDay.toDouble() * 0.49
        val x2 = differenceNight.toDouble() * 0.25
        val pdv = ((x1 + x2 + 7.40 + firstFee + secondFee + thirdFee) * 0.13)
        val price = ((((x1 + x2 + 7.40 + firstFee + secondFee + thirdFee + pdv)) * 100.0).roundToInt() / 100.0)
        savePrice = price
        binding.txtElectricityBill.text = ("Your price is $savePrice kn!")
    }

    private fun saveToFirebase() {
        val db = FirebaseFirestore.getInstance()
        val electricity: MutableMap<String, Any> = HashMap()
        val user = Firebase.auth.currentUser?.email.toString()

        electricity["Price"] = savePrice
        electricity["User"] = user
        electricity["Year"] = saveDateYear
        electricity["Month"] = saveDateMonth
        electricity["Day"] = saveDateDay

        db.collection("Electricity").add(electricity).addOnCompleteListener {
            Toast.makeText(this@CountElectricity, "You saved your data successfully!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDateView(){
        val dateView = Calendar.getInstance()
        val year = dateView.get(Calendar.YEAR)
        val month = dateView.get(Calendar.MONTH)
        val day = dateView.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, dateYear, dateMonth, dayOfMonth ->
            saveDateYear = dateYear
            saveDateMonth = dateMonth + 1
            saveDateDay = dayOfMonth },year,month,day)
            datePicker.show()
            ifPressedDate = true


    }
}