package android.chi.jason.cleanarchitecturedemo.ui


import android.os.Bundle

import android.chi.jason.cleanarchitecturedemo.R
import androidx.preference.PreferenceFragmentCompat

class SettingFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
