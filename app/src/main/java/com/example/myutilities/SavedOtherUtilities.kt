package com.example.myutilities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Adapters.OtherUtilityAdapter
import com.example.myutilities.Class.OtherUtilityData
import com.example.myutilities.databinding.ActivitySavedOtherUtilitiesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivitySavedOtherUtilitiesBinding

private lateinit var recyclerView: RecyclerView
lateinit var otherArrayUtilityList : ArrayList<OtherUtilityData>
lateinit var otherUtilityAdapter : OtherUtilityAdapter
@SuppressLint("StaticFieldLeak")


class SavedOtherUtilities : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_other_utilities)
        binding = ActivitySavedOtherUtilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerViewOther
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        otherArrayUtilityList = arrayListOf()

        otherUtilityAdapter = OtherUtilityAdapter(otherArrayUtilityList)

        recyclerView.adapter = otherUtilityAdapter

        displayOtherUtilities()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayOtherUtilities(){
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser?.email
        db.collection("Other").whereEqualTo("User", user).
        addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
            }

            for (dc: DocumentChange in value?.documentChanges!!) {

                if (dc.type == DocumentChange.Type.ADDED)
                   otherArrayUtilityList.add(dc.document.toObject(OtherUtilityData::class.java))
            }
           otherUtilityAdapter.notifyDataSetChanged()
        }
    }

}