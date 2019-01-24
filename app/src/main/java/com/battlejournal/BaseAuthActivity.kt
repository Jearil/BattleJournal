package com.battlejournal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

abstract class BaseAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().addAuthStateListener { auth ->
                if (auth.currentUser == null) {
                    // not signed in
                    wasLoggedOut()
                }
            }
        } else {
            // not logged in, can't use this
            wasLoggedOut()
        }
    }

    private fun wasLoggedOut() {
        val signIn = Intent(this, MainActivity::class.java)
        startActivity(signIn)
        Toast.makeText(this, R.string.was_loggedout, Toast.LENGTH_SHORT).show()
        finish()
    }
}