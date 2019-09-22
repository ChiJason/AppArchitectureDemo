package android.chi.jason.cleanarchitecturedemo

import android.chi.jason.cleanarchitecturedemo.ui.SettingFragment
import android.chi.jason.cleanarchitecturedemo.ui.TaskFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setFragment(TaskFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            setFragment(SettingFragment.newInstance())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        val visibleFragment = supportFragmentManager.fragments[0]
        if (visibleFragment is TaskFragment) {
            finish()
        } else {
            setFragment(TaskFragment.newInstance())
        }
    }
}
