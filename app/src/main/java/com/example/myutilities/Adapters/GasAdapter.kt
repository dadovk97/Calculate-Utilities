package com.example.myutilities.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myutilities.Class.GasData
import com.example.myutilities.R
import com.google.firebase.firestore.FirebaseFirestore

class GasAdapter(private val gasList : ArrayList<GasData> ): RecyclerView.Adapter<GasAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_gas,
        parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gas: GasData = gasList[position]
        holder.billID.text = gas.Bill_ID.toString()
        holder.date.text = gas.Date
        holder.companyName.text = gas.Company
        holder.tariffModel.text = gas.TM
        holder.price.text = gas.Price.toString()
        holder.delete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Gas").whereEqualTo("Bill_ID",holder.deleteByID.text.toString()).get()
            query.addOnCompleteListener {
                for(document in it.result){
                    db.collection("Gas").document(document.id).delete()
                    gasList.removeAt(position)
                    notifyDataSetChanged()
                }

        }

        }
    }

    override fun getItemCount(): Int {
         return gasList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val billID : TextView = itemView.findViewById(R.id.tvBillID)
        val date : TextView = itemView.findViewById(R.id.tvDate)
        val companyName : TextView = itemView.findViewById(R.id.tvCompany)
        val tariffModel : TextView = itemView.findViewById(R.id.tvTM)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        var delete : Button = itemView.findViewById(R.id.btnDeleteGas)
        var deleteByID : EditText = itemView.findViewById(R.id.txtDelID)


    }



}