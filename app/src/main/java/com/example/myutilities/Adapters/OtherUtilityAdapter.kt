package com.example.myutilities.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Class.OtherUtilityData
import com.example.myutilities.R
import com.google.firebase.firestore.FirebaseFirestore

class OtherUtilityAdapter(private val otherUtilityList : ArrayList<OtherUtilityData> ): RecyclerView.Adapter<OtherUtilityAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_other_utilities,
            parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val otherUtility: OtherUtilityData = otherUtilityList[position]
        holder.billID.text = otherUtility.Bill_ID.toString()
        holder.date.text = otherUtility.Date
        holder.companyName.text = otherUtility.Company
        holder.utility.text = otherUtility.Utility
        holder.price.text = otherUtility.Price.toString()
        holder.delete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Other").whereEqualTo("Bill_ID",holder.deleteByID.text.toString()).get()
            query.addOnCompleteListener {
                for(document in it.result){
                    db.collection("Other").document(document.id).delete()
                    otherUtilityList.removeAt(position)
                    notifyDataSetChanged()
                }

            }

        }
    }

    override fun getItemCount(): Int {
        return otherUtilityList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val billID : TextView = itemView.findViewById(R.id.tvBillID)
        val date : TextView = itemView.findViewById(R.id.tvDate)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        val companyName :TextView = itemView.findViewById(R.id.tvCompany)
        val utility: TextView = itemView.findViewById(R.id.tvUtility)
        var delete : Button = itemView.findViewById(R.id.btnDeleteOther)
        var deleteByID : EditText = itemView.findViewById(R.id.txtDelID)


    }



}