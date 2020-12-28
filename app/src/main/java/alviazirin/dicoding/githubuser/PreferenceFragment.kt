package alviazirin.dicoding.githubuser


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import java.util.*

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var ALARM: String
    private lateinit var LANG: String

    private lateinit var userAlarmPreference: SwitchPreference
    private lateinit var userListLang: ListPreference
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
        LANG = resources.getString(R.string.key_lang)

        userAlarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference

        userListLang = findPreference<ListPreference>(LANG) as ListPreference
        alarmReceiver = AlarmReceiver()
    }
    private fun setPref() {
        val sh = preferenceManager.sharedPreferences

        userAlarmPreference.isChecked = sh.getBoolean(ALARM, false)
        if (userAlarmPreference.isChecked){
            val time = "09:00"
            val message = resources.getString(R.string.alarmMessage)
            alarmReceiver.setAlarm(context, AlarmReceiver.SETALARM, time, message)
            showToastMessage(resources.getString(R.string.alarmStateOn))

        } else {
            alarmReceiver.cancelAlarm(context, AlarmReceiver.SETALARM)
            showToastMessage(resources.getString(R.string.alarmStateOff))
        }


    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == ALARM){

            if (userAlarmPreference.isChecked){
                val time = "09:00"
                val message = resources.getString(R.string.alarmMessage)
                alarmReceiver.setAlarm(context, AlarmReceiver.SETALARM, time, message)
                showToastMessage(resources.getString(R.string.alarmStateOn))
            } else {
               alarmReceiver.cancelAlarm(context,AlarmReceiver.SETALARM)
                showToastMessage(resources.getString(R.string.alarmStateOff))
            }
        }

        if (key == LANG){

            val value = userListLang.value.toString()
            if (value.equals("English")){
                setLocale(context,"en")
            } else {
                setLocale(context,"in")
            }
            userListLang.summary = value
        }
    }

    private fun showToastMessage(message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    private fun setLocale(context: Context?, localeName: String){
        val locale = Locale(localeName)
        Locale.setDefault(locale)
        val resources = context?.resources
        val config = resources?.configuration
        config?.setLocale(locale)
        resources?.updateConfiguration(config, resources.displayMetrics)
    }
}