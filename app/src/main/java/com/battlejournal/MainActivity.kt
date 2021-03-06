// Copyright (c) Colin Miller 2019.

package com.battlejournal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.battlejournal.models.User
import com.crashlytics.android.Crashlytics
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MainActivity : AppCompatActivity() {
  private val RC_SIGN_IN = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    showLogin()

    val logout = findViewById<Button>(R.id.logout_button)
    logout.setOnClickListener {
      AuthUI.getInstance().signOut(this)
        .addOnCompleteListener {
          showLogin()
        }
    }
  }

  private fun showLogin() {
    val providers = arrayListOf(
      AuthUI.IdpConfig.GoogleBuilder().build(),
      AuthUI.IdpConfig.FacebookBuilder()
        .setPermissions(arrayListOf("email", "public_profile")).build()
    )

    if (FirebaseAuth.getInstance().currentUser == null) {
      startActivityForResult(
        AuthUI.getInstance()
          .createSignInIntentBuilder()
          .setAvailableProviders(providers)
          .build(), RC_SIGN_IN
      )
    } else {
      loginDone()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
      // val response = IdpResponse.fromResultIntent(data)

      if (resultCode == Activity.RESULT_OK) {
        loginDone()

      } else {
        Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
        showLogin()
      }
    } else {
      finish()
    }
  }

  private fun loginDone() {
    val auth = FirebaseAuth.getInstance().currentUser
    if (auth != null) {
      val db = FirebaseFirestore.getInstance().collection("users").document(auth.uid)
      val user = User(auth.displayName, auth.email, auth.photoUrl.toString())

      db.set(user, SetOptions.merge())
        .addOnCompleteListener {
          val recordSheet = Intent(this, ArmyActivity::class.java)
          startActivity(recordSheet)
          finish()
        }
        .addOnFailureListener {
          Toast.makeText(this, "Saving user failed", Toast.LENGTH_SHORT).show()
          Crashlytics.logException(it)
        }
    }
  }
}
