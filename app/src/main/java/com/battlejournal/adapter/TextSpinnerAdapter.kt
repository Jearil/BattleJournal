// Copyright (c) Colin Miller 2019.

package com.battlejournal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.battlejournal.R
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.ChangeEventListener
import com.firebase.ui.firestore.FirestoreArray
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException


open abstract class TextSpinnerAdapter<T>(var snapShots: FirestoreArray<T>) : BaseAdapter(), ChangeEventListener,
  LifecycleObserver {
  override fun onDataChanged() {
    this.notifyDataSetChanged()
  }

  override fun onChildChanged(type: ChangeEventType, snapshot: DocumentSnapshot, newIndex: Int, oldIndex: Int) {

  }

  override fun onError(e: FirebaseFirestoreException) {
    throw e
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view = convertView ?: LayoutInflater.from(parent!!.context).inflate(
      R.layout.support_simple_spinner_dropdown_item,
      parent,
      false
    )
    val model = getItem(position)
    populateView(view, model, position)
    return view
  }

  override fun getItem(position: Int): T {
    return snapShots[position]
  }

  override fun getItemId(position: Int): Long {
    return 0
  }

  override fun getCount(): Int {
    return snapShots.size
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun startListening() {
    if (!snapShots.isListening(this)) {
      snapShots.addChangeEventListener(this)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stopListening() {
    snapShots.removeChangeEventListener(this)
    notifyDataSetChanged()
  }

  fun updateListener(updated: FirestoreArray<T>) {
    snapShots.removeChangeEventListener(this)
    snapShots = updated
    startListening()
  }

  abstract fun populateView(view: View, model: T, position: Int)
}