package com.example.myutilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Adapters.GasAdapter
import com.example.myutilities.Class.GasData
import com.example.myutilities.databinding.ActivitySavedGasBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivitySavedGasBinding

    private lateinit var recyclerView: RecyclerView
    lateinit var gasArrayList : ArrayList<GasData>
    lateinit var gasAdapter : GasAdapter
    @SuppressLint("StaticFieldLeak")
    lateinit var db: FirebaseFirestore

class SavedGas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_gas)
        binding = ActivitySavedGasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerViewGas
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        gasArrayList = arrayListOf()

        gasAdapter = GasAdapter(gasArrayList)

        recyclerView.adapter = gasAdapter

       displayGas()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayGas(){
        db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser?.email
        db.collection("Gas").whereEqualTo("User", user).
                addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED)
                            gasArrayList.add(dc.document.toObject(GasData::class.java))
                    }
                    gasAdapter.notifyDataSetChanged()
                }
    }

}