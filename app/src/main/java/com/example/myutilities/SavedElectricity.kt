package com.example.myutilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Adapters.ElectricityAdapter
import com.example.myutilities.Class.ElectricityData
import com.example.myutilities.databinding.ActivitySavedElectricityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivitySavedElectricityBinding

private lateinit var recyclerView: RecyclerView
lateinit var electricityArrayList : ArrayList<ElectricityData>
lateinit var electricityAdapter : ElectricityAdapter
@SuppressLint("StaticFieldLeak")


class SavedElectricity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_electricity)
        binding = ActivitySavedElectricityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerViewElectricity
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        electricityArrayList = arrayListOf()

        electricityAdapter = ElectricityAdapter(electricityArrayList)

        recyclerView.adapter = electricityAdapter

        displayElectricity()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayElectricity(){
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser?.email
        db.collection("Electricity").whereEqualTo("User", user).
        addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
            }
            for (dc: DocumentChange in value?.documentChanges!!) {

                if (dc.type == DocumentChange.Type.ADDED)
                    electricityArrayList.add(dc.document.toObject(ElectricityData::class.java))
            }
           electricityAdapter.notifyDataSetChanged()
        }
    }

}