package alviazirin.dicoding.githubuser

import alviazirin.dicoding.GitHubUser.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, PreferenceFragment()).commit()
    }
}