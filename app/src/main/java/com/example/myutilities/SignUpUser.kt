package com.example.myutilities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myutilities.databinding.ActivitySignUpUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpUser : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpUserBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding = ActivitySignUpUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignUp.setOnClickListener { signupUser() }
    }
    private fun signupUser(){
        if(binding.txtSignUpPassword.text.toString().isBlank()
            || binding.txtSignUpEmail.text.toString().isBlank()){
            Toast.makeText(this@SignUpUser,"Please enter password or email!",Toast.LENGTH_LONG).show()
        }
        else
            mAuth.createUserWithEmailAndPassword(
                binding.txtSignUpEmail.text.toString(),
                binding.txtSignUpPassword.text.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseUserID)
                    Toast.makeText(this@SignUpUser, "Sign Up Successful!", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@SignUpUser, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

}

