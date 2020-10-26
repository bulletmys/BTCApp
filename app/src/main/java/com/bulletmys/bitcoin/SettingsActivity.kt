package com.bulletmys.bitcoin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.root_preferences)

            val editPref: EditTextPreference? =
                preferenceScreen.findPreference(getString(R.string.limit_key))
            editPref?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference: Preference, any: Any ->
                    onPreferenceChange(
                        preference,
                        any
                    )
                }
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            val value = newValue.toString().toIntOrNull()
            if (value == null || value < 2) {
                Toast.makeText(this.context, "Please enter correct value (>2)", Toast.LENGTH_LONG)
                    .show()
                return false
            }
            return true
        }
    }
}

