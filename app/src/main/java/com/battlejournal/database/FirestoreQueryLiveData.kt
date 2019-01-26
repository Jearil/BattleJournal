package com.battlejournal.database

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.firestore.*

class FirestoreQueryLiveData(val collectionReference: CollectionReference) : LiveData<QuerySnapshot>() {
    private val listener = FirestoreValueEventListener()
    private var registration : ListenerRegistration? = null

    override fun onInactive() {
        registration?.remove()
    }

    override fun onActive() {
        registration = collectionReference.addSnapshotListener(listener)
    }

    inner class FirestoreValueEventListener : EventListener<QuerySnapshot> {
        override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
            if (exception != null) {
                Log.w("FirestoreQueryLiveData", "Listen failed. ", exception)
                return
            }

            if (snapshot != null && !snapshot.isEmpty) {
                value = snapshot
            } else {
                value = null
            }
        }

    }
}