// Copyright (c) Colin Miller 2019.

package com.battlejournal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class BaseAuthActivity : AppCompatActivity() {

  lateinit var uid: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
      uid = currentUser.uid
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