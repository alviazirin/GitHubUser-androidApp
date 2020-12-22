package alviazirin.dicoding.githubuser


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_setting.*

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var ALARM: String
    private lateinit var LANG: String
    private lateinit var currentLanguage: String


    private lateinit var userAlarmPreference: SwitchPreference
    private lateinit var userLangPreference: Preference
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
        Log.d("Init ALARM: ", ALARM)
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
            userAlarmPreference.isChecked = sharedPreferences.getBoolean(ALARM, false)
            Toast.makeText(context,"Alarm state:"+ALARM,Toast.LENGTH_SHORT).show()

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

            Log.d("valueIndex", value)

        }
    }
    private fun showSnackBarMessage(message: String) {
        Snackbar.make(setting_holder, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun showToastMessage(message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    /*private fun setLocale(localeName: String){

        currentLanguage =
        if (localeName != currentLanguage)
    }*/
}