// Copyright (c) Colin Miller 2019.

package com.battlejournal.models

/**
 * Defines the logged in user, or any other user that you might get data for
 */
data class User(
  val name: String?,
  val email: String?,
  val photoUrl: String?
) : Model()