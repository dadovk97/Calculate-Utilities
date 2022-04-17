package com.example.myutilities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivityLoginUserBinding
import com.google.firebase.auth.FirebaseAuth


class LoginUser : AppCompatActivity() {
    private lateinit var binding: ActivityLoginUserBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener{ loginUser() }
    }

    private fun loginUser(){
        if(binding.txtLoginEmail.text.toString().isBlank() ||
                binding.txtLoginPassword.text.toString().isBlank())
        {
            Toast.makeText(this@LoginUser,"Please enter password or email!", Toast.LENGTH_LONG).show()
        }
        else
            mAuth.signInWithEmailAndPassword(binding.txtLoginEmail.text.toString(),
                binding.txtLoginPassword.text.toString()).addOnCompleteListener{task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this@LoginUser,"Log in successful!!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainMenu::class.java)
                    startActivity(intent)
                }
                else Toast.makeText(this@LoginUser, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
    }
}