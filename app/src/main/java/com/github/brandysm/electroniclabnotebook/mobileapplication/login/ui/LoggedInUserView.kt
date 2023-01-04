package com.github.brandysm.electroniclabnotebook.mobileapplication.login.ui

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val accessToken: String
    //... other data fields that may be accessible to the UI
)