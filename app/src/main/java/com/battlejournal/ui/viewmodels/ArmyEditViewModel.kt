// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.battlejournal.database.FirestoreDocumentLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ArmyEditViewModel(uid: String, armyId: String) : ViewModel() {

  private val firestoreDb = FirebaseFirestore.getInstance()
    .collection("users").document(uid)
    .collection("armies").document(armyId)

  private val liveData = FirestoreDocumentLiveData(firestoreDb)

  fun getFirestoreDocument(): LiveData<DocumentSnapshot> {
    return liveData
  }

  fun delete(): Task<Void> {
    return firestoreDb.delete()
  }

  class ArmyEditViewModelFactory(private val uid: String, private val armyId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(ArmyEditViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return ArmyEditViewModel(uid, armyId) as T
      }
      throw IllegalArgumentException("Unknown viewmodel class")
    }

  }
}
