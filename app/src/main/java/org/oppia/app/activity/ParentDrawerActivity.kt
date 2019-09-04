package org.oppia.app.activity

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import org.oppia.app.R
import org.oppia.app.drawer.DrawerFragment
import org.oppia.app.drawer.FragmentTransactions
import org.oppia.app.drawer.ui.preferences.PreferencesDrawerActivity
import org.oppia.app.home.HomeDrawerActivity

/** The ParentDrawerActivity is the parent of all activity with navigation drawer for all users entering the app. */
open class ParentDrawerActivity : InjectableAppCompatActivity(), FragmentTransactions {

  var toolbar: Toolbar? = null
  var drawerFragment: DrawerFragment? = null
  var previousMenuId: Int? = null

  fun init(title: String) {
    toolbar = findViewById<View>(R.id.toolbar) as Toolbar?

    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayShowHomeEnabled(true)
    drawerFragment =
      supportFragmentManager.findFragmentById(R.id.fragment_navigation_drawer) as DrawerFragment
    drawerFragment!!.setUpDrawer(
      R.id.fragment_navigation_drawer,
      findViewById<View>(R.id.drawer_layout) as DrawerLayout,
      toolbar!!
    )
    setTitle(title)
  }

  override fun openFragment(menuId: Int) {
    if (menuId != null && previousMenuId != menuId && menuId != 0) {
      var intent: Intent = Intent(this, HomeDrawerActivity::class.java)

      when (menuId) {
        R.id.nav_home -> intent = Intent(this, HomeDrawerActivity::class.java)
        R.id.nav_preferences -> intent = Intent(this, PreferencesDrawerActivity::class.java)

        else -> {
        }
      }

      startActivity(intent)
      overridePendingTransition(
        R.anim.from_right_in,
        R.anim.slide_out_left
      )
    } else {
    }
    previousMenuId = menuId
  }

}
