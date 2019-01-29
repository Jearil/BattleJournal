// Copyright (c) Colin Miller 2019.

package com.battlejournal.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirestoreDocumentLiveData(private val documentReference: DocumentReference) : LiveData<DocumentSnapshot>() {
  private val listener = LiveDocumentEventListener()
  private var registration: ListenerRegistration? = null

  override fun onInactive() {
    registration?.remove()
  }

  override fun onActive() {
    registration = documentReference.addSnapshotListener(listener)
  }

  inner class LiveDocumentEventListener : EventListener<DocumentSnapshot> {
    override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
      if (exception != null) {
        Log.w("FSDocumentLiveData", "Document listen failed.", exception)
        return
      }

      value = if (snapshot != null && snapshot.exists()) {
        snapshot
      } else {
        null
      }
    }

  }
}