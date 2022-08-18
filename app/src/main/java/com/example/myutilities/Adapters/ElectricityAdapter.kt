package com.example.myutilities.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Class.ElectricityData
import com.example.myutilities.R
import com.google.firebase.firestore.FirebaseFirestore

class ElectricityAdapter(private val electricityList : ArrayList<ElectricityData> ): RecyclerView.Adapter<ElectricityAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_electricity,
            parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val electricity: ElectricityData = electricityList[position]
        holder.billID.text = electricity.Bill_ID.toString()
        holder.date.text = electricity.Date
        holder.price.text = electricity.Price.toString()
        holder.delete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Electricity").whereEqualTo("Bill_ID",holder.deleteByID.text.toString()).get()
            query.addOnCompleteListener {
                for(document in it.result){
                    db.collection("Electricity").document(document.id).delete()
                    electricityList.removeAt(position)
                    notifyDataSetChanged()
                }

            }

        }
    }

    override fun getItemCount(): Int {
        return electricityList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val billID : TextView = itemView.findViewById(R.id.tvBillID)
        val date : TextView = itemView.findViewById(R.id.tvDate)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        var delete : Button = itemView.findViewById(R.id.btnDeleteElectricity)
        var deleteByID : EditText = itemView.findViewById(R.id.txtDelID)


    }



}