package com.example.myutilities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myutilities.databinding.ActivityMainMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val user = Firebase.auth.currentUser
        if (user != null) binding.txtCurrentUser.text = user.email

        binding.btnLogOffUser.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MyUtilities::class.java)
            startActivity(intent)
        }
        binding.btnGoElectricity.setOnClickListener {
            val intent = Intent(this, CountElectricity::class.java)
            startActivity(intent)
        }
        binding.btnGoGas.setOnClickListener {
            val intent = Intent(this, CountGas::class.java)
            startActivity(intent)
        }
        binding.btnGoWater.setOnClickListener {
            val intent = Intent(this, CountWater::class.java)
            startActivity(intent)
        }
        binding.btnAllUtilities.setOnClickListener {
            val intent = Intent(this, SavedWater::class.java)
            startActivity(intent)


        }
    }
}