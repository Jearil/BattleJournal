// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.battlejournal.database.FirestoreQueryLiveData
import com.battlejournal.models.Army
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class RecordViewModel(uid: String, army: Army) : ViewModel() {

  private val firestoreDb = FirebaseFirestore.getInstance()
    .collection("users").document(uid)
    .collection("armies").document(army.id)
    .collection("games")

  private val liveData = FirestoreQueryLiveData(firestoreDb)

  fun getFirestoreSnapshot(): LiveData<QuerySnapshot> {
    return liveData
  }

  class AllArmyViewModelFactory(private val uid: String, private val army: Army) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return RecordViewModel(uid, army) as T
      }
      throw IllegalArgumentException("Unknown viewmodel class")
    }
  }
}