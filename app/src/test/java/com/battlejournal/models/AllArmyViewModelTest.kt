// Copyright (c) Colin Miller 2019.

package com.battlejournal.models

import com.battlejournal.ui.viewmodels.AllArmyViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Some very basic tests on the AllArmyViewModel class
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(FirebaseFirestore::class)
class AllArmyViewModelTest {

  @Mock lateinit var db : FirebaseFirestore
  @Mock lateinit var userCollection: CollectionReference
  @Mock lateinit var uidDoc: DocumentReference
  @Mock lateinit var armiesCollection: CollectionReference
  val uid = "uid1"

  @Before
  fun setUp() {
    PowerMockito.mockStatic(FirebaseFirestore::class.java)
    `when`(FirebaseFirestore.getInstance()).thenReturn(db)
    `when`(db.collection("users")).thenReturn(userCollection)
    `when`(userCollection.document(uid)).thenReturn(uidDoc)
    `when`(uidDoc.collection("armies")).thenReturn(armiesCollection)
  }

  @Test
  fun  firestoreCalledCorrectly() {
    val model = AllArmyViewModel(uid)
    verify(uidDoc).collection("armies")

    val liveData = model.getFirestoreSnapshot()
    assertThat(liveData).isNotNull()
  }

  @Test
  fun factoryMakesModel() {
    val factory = AllArmyViewModel.AllArmyViewModelFactory(uid)
    val model = factory.create(AllArmyViewModel::class.java)

    verify(uidDoc).collection("armies")
    assertThat(model).isNotNull()
  }
}