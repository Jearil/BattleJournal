// Copyright (c) Colin Miller 2019.

package com.battlejournal.ui.viewmodels

import androidx.lifecycle.*
import com.battlejournal.database.FirestoreDocumentLiveData
import com.battlejournal.models.Army
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ArmyViewModel(uid: String) : ViewModel() {

  private val firestoreDb = FirebaseFirestore.getInstance()
    .collection("users").document(uid)
    .collection("armies")

  private lateinit var document: DocumentReference
  private lateinit var liveData: LiveData<DocumentSnapshot>
  private lateinit var armyLiveData: ArmyLiveData
  private var updatedArmy: Army? = null
  private var existing = false

  fun start(armyId: String?) {
    if (armyId == null) {
      document = firestoreDb.document()
      liveData = FirestoreDocumentLiveData(document)
      existing = true
    } else {
      document = firestoreDb.document(armyId)
      liveData = FirestoreDocumentLiveData(document)
      existing = false
    }
  }

  fun getFirestoreDocument(lifecycleOwner: LifecycleOwner): LiveData<Army> {
    if (::armyLiveData.isInitialized) {
      return armyLiveData
    }
    armyLiveData = ArmyLiveData(liveData, lifecycleOwner)
    armyLiveData.observe(lifecycleOwner, Observer { armySnapshot ->
      updatedArmy = armySnapshot
    })
    return armyLiveData
  }

  fun updateData(army: Army) {
    armyLiveData.updateData(army)
  }

  fun save(): Task<Void> {
    val armyData = updatedArmy
    if (existing && armyData != null) {
      return document.set(armyData, SetOptions.merge())
    } else if (armyData != null) {
      return document.set(armyData)
    }
    throw IllegalStateException("No data ready to save")
  }

  fun delete(): Task<Void> {
    return document.delete()
  }

  fun isExisting(): Boolean {
    return existing
  }

  class ArmyLiveData(liveData: LiveData<DocumentSnapshot>, lifecycleOwner: LifecycleOwner) : LiveData<Army>() {

    init {
      liveData.observe(lifecycleOwner, Observer { snapshot ->
        if (snapshot == null) {
          return@Observer
        }
        val army = snapshot.toObject(Army::class.java)?.withId<Army>(snapshot.id)
        value = army
      })
    }

    fun updateData(army: Army) {
      value = army
    }
  }

  class ArmyEditViewModelFactory(private val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(ArmyViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return ArmyViewModel(uid) as T
      }
      throw IllegalArgumentException("Unknown viewmodel class")
    }

  }
}
