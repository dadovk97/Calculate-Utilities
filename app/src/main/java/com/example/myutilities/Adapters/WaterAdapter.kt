package com.example.myutilities.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Class.WaterData
import com.example.myutilities.R
import com.google.firebase.firestore.FirebaseFirestore

class WaterAdapter(private val waterList : ArrayList<WaterData> ): RecyclerView.Adapter<WaterAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_water,
            parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val water: WaterData = waterList[position]
        holder.billID.text = water.Bill_ID.toString()
        holder.date.text = water.Date
        holder.companyName.text = water.Company
        holder.price.text = water.Price.toString()
        holder.delete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Water").whereEqualTo("Bill_ID",holder.deleteByID.text.toString()).get()
            query.addOnCompleteListener {
                for(document in it.result){
                    db.collection("Water").document(document.id).delete()
                    waterList.removeAt(position)
                    notifyDataSetChanged()
                }

            }

        }
    }

    override fun getItemCount(): Int {
        return waterList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val billID : TextView = itemView.findViewById(R.id.tvBillID)
        val date : TextView = itemView.findViewById(R.id.tvDate)
        val companyName : TextView = itemView.findViewById(R.id.tvCompany)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        var delete : Button = itemView.findViewById(R.id.btnDeleteWater)
        var deleteByID : EditText = itemView.findViewById(R.id.txtDelID)


    }



}