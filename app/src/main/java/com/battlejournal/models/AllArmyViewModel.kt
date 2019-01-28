// Copyright (c) Colin Miller 2019.

package com.battlejournal.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.battlejournal.database.FirestoreQueryLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AllArmyViewModel(uid: String) : ViewModel() {

  private val firestoreDb = FirebaseFirestore.getInstance()
    .collection("users").document(uid)
    .collection("armies")

  private val liveData = FirestoreQueryLiveData(firestoreDb)

  fun getFirestoreSnapshot(): LiveData<QuerySnapshot> {
    return liveData
  }

  class AllArmyViewModelFactory(private val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(AllArmyViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return AllArmyViewModel(uid) as T
      }
      throw IllegalArgumentException("Unknown viewmodel class")
    }
  }
}