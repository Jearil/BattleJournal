// Copyright (c) Colin Miller 2019.

package com.battlejournal.database

import android.arch.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class FirebaseQueryLiveData(private val query: Query) : LiveData<DataSnapshot>() {
  private val listener = LiveValueEventListener()

  override fun onInactive() {
    query.removeEventListener(listener)
  }

  override fun onActive() {
    query.addValueEventListener(listener)
  }

  inner class LiveValueEventListener : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(data: DataSnapshot) {
      value = data
    }

  }
}