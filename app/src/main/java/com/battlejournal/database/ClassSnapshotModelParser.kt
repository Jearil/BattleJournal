// Copyright (c) Colin Miller 2019.

package com.battlejournal.database

import com.battlejournal.models.Model
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.DocumentSnapshot

class ClassSnapshotModelParser<T : Model>(val modelClass: Class<T>) : SnapshotParser<T> {

  override fun parseSnapshot(snapshot: DocumentSnapshot): T {
    return snapshot.toObject(modelClass)!!.withId(snapshot.id)
  }
}