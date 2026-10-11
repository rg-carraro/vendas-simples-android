package com.example.controlevendas

import android.content.Context

/** Preferências independentes da licença; somente identidade é exportada no backup. */
class BusinessPreferences(context: Context) : BusinessPreferenceStore {
    private val preferences = context.getSharedPreferences("business_identity", Context.MODE_PRIVATE)
    var businessName: String
        get() = BusinessIdentity.normalize(preferences.getString("businessName", "").orEmpty())
        set(value) { preferences.edit().putString("businessName", BusinessIdentity.normalize(value)).apply() }
    val displayName: String get() = BusinessIdentity.displayName(businessName)
    val initialSetupOffered: Boolean get() = preferences.getBoolean("initialSetupOffered", false)
    fun markSetupOffered() { preferences.edit().putBoolean("initialSetupOffered", true).apply() }
    override fun snapshot() = BusinessPreferenceState(businessName, initialSetupOffered)
    override fun restoreState(state: BusinessPreferenceState) {
        check(preferences.edit()
            .putString("businessName", BusinessIdentity.normalize(state.name))
            .putBoolean("initialSetupOffered", state.setupOffered)
            .commit()) { "Não foi possível persistir o nome do negócio" }
    }
}
