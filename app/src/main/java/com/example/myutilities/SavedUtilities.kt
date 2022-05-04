package com.example.myutilities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.myutilities.databinding.ActivitySavedUtilitiesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SavedUtilities : AppCompatActivity() {
    private lateinit var binding: ActivitySavedUtilitiesBinding
    private lateinit var utility: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedUtilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pickUtility()
        binding.btnShowUtilities.setOnClickListener {
            retrieveUserUtilities()
        }
        binding.btnDeleteUtilities.setOnClickListener {
            deleteUtilities()
            binding.txtDeleteUtility.isVisible = true
        }
    }

    private fun retrieveUserUtilities() {
        when (utility) {
            "Water" -> {
                showUtilities(getCompany = true)
            }
            "Gas" -> {
                showUtilities(getCompany = true, getTariffModel = true)
            }
            "Electricity" -> {
                showUtilities()
            }
        }
    }
    private fun showUtilities(getCompany: Boolean = false, getTariffModel: Boolean = false) {
        val db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser?.email
        db.collection(utility).whereEqualTo("User", user).get().addOnCompleteListener {
            val result = StringBuffer()
            if (it.isSuccessful) {
                for (document in it.result) {
                    if (getCompany) {
                        result.append("Company: ").append(document.data.getValue("Company"))
                            .append("\n\n")
                    }
                    result.append("Date: ").append(document.data.getValue("Date")).append("\n\n")
                        .append("Price: ").append(document.data.getValue("Price")).append("\n\n")
                    if (getTariffModel) {
                        result.append("Tariff Model:").append(document.data.getValue("TM"))
                            .append("\n\n")
                    }
                }
                binding.txtShowUtilities.text = result
            }
        }
    }

    private fun pickUtility() {
        val chooseUtility = resources.getStringArray(R.array.choose_utilities)
        val spinnerUtility = binding.spinnerUtilities
        val adapterUtility = ArrayAdapter(this, android.R.layout.simple_spinner_item, chooseUtility)
        spinnerUtility.adapter = adapterUtility
        spinnerUtility.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (chooseUtility[p2].toString()) {
                    "Water" -> {
                        utility = "Water"
                    }
                    "Gas" -> {
                        utility = "Gas"
                    }
                    "Electricity" -> {
                        utility = "Electricity"
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(
                    this@SavedUtilities,
                    "You have to select utility!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun deleteUtilities() {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(utility).whereEqualTo("Date", binding.txtDeleteUtility.text.toString()).get()
        query.addOnCompleteListener {
            for(document in it.result){
                db.collection(utility).document(document.id).delete()
            }
        }
    }
}









