package com.example.controlevendas

import android.content.Context

/** Preferências independentes do SQLite e da licença. Não integram o backup SQLite. */
class BusinessPreferences(context: Context) {
    private val preferences = context.getSharedPreferences("business_identity", Context.MODE_PRIVATE)
    var businessName: String
        get() = BusinessIdentity.normalize(preferences.getString("businessName", "").orEmpty())
        set(value) { preferences.edit().putString("businessName", BusinessIdentity.normalize(value)).apply() }
    val displayName: String get() = BusinessIdentity.displayName(businessName)
    val initialSetupOffered: Boolean get() = preferences.getBoolean("initialSetupOffered", false)
    fun markSetupOffered() { preferences.edit().putBoolean("initialSetupOffered", true).apply() }
}
