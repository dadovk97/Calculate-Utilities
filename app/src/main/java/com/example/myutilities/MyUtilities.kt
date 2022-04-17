package com.example.myutilities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityMyUtilitiesBinding


class MyUtilities : AppCompatActivity() {
    private lateinit var binding: ActivityMyUtilitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyUtilitiesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnGoLogin.setOnClickListener {
            val intent = Intent(this,LoginUser::class.java)
            startActivity(intent)
        }
        binding.btnGoSignUp.setOnClickListener {
            val intent = Intent(this,SignUpUser::class.java)
            startActivity(intent)
        }

        
    }

}