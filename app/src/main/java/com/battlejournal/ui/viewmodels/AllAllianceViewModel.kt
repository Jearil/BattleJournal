// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.battlejournal.database.FirestoreQueryLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AllAllianceViewModel : ViewModel() {

  private val db = FirebaseFirestore.getInstance()
    .collection("systems").document("aos")
    .collection("alliances")

  private val liveData = FirestoreQueryLiveData(db)

  fun getFirestoreSnapshot(): LiveData<QuerySnapshot> {
    return liveData
  }
}