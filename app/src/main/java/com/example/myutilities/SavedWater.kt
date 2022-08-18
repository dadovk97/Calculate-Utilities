package com.example.myutilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Adapters.WaterAdapter
import com.example.myutilities.Class.WaterData
import com.example.myutilities.databinding.ActivitySavedWaterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivitySavedWaterBinding

private lateinit var recyclerView: RecyclerView
lateinit var waterArrayList : ArrayList<WaterData>
lateinit var waterAdapter : WaterAdapter
@SuppressLint("StaticFieldLeak")


class SavedWater: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_water)
        binding = ActivitySavedWaterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerViewWater
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        waterArrayList = arrayListOf()

        waterAdapter = WaterAdapter(waterArrayList)

        recyclerView.adapter = waterAdapter

        displayWater()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayWater(){
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser?.email
        db.collection("Water").whereEqualTo("User", user).
        addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
            }
            for (dc: DocumentChange in value?.documentChanges!!) {

                if (dc.type == DocumentChange.Type.ADDED)
                    waterArrayList.add(dc.document.toObject(WaterData::class.java))
            }
            waterAdapter.notifyDataSetChanged()
        }
    }

}