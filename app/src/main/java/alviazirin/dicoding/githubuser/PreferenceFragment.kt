package alviazirin.dicoding.githubuser

import alviazirin.dicoding.GitHubUser.R
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var ALARM: String
    private lateinit var LANG: String

    private lateinit var userAlarmPreference: SwitchPreference
    private lateinit var userLangPreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preferences)
        initialize()
        setPref()


    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }



    private fun initialize() {
        ALARM = resources.getString(R.string.key_alarm)
        Log.d("Init ALARM: ", ALARM)
        LANG = resources.getString(R.string.key_lang)

        userAlarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference
        userLangPreference = findPreference<Preference>(LANG) as Preference
        alarmReceiver = AlarmReceiver()
    }
    private fun setPref() {
        val sh = preferenceManager.sharedPreferences

        userAlarmPreference.isChecked = sh.getBoolean(ALARM, false)
        if (userAlarmPreference.isChecked){
            val time = "09:00"
            val message = "Klik disini untuk buka kembali aplikasi"
            alarmReceiver.setAlarm(context, AlarmReceiver.SETALARM, time, message)

        } else {
            alarmReceiver.cancelAlarm(context, AlarmReceiver.SETALARM)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == ALARM){
            userAlarmPreference.isChecked = sharedPreferences.getBoolean(ALARM, false)
            Toast.makeText(context,"Alarm state:"+ALARM,Toast.LENGTH_SHORT).show()
            if (userAlarmPreference.isChecked){
                val time = "09:00"
                val message = "Klik disini untuk buka kembali aplikasi"
                alarmReceiver.setAlarm(context, AlarmReceiver.SETALARM, time, message)
            } else {
               alarmReceiver.cancelAlarm(context,AlarmReceiver.SETALARM)
            }
        }
    }
}