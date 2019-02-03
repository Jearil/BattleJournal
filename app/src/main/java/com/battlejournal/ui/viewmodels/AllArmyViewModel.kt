// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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