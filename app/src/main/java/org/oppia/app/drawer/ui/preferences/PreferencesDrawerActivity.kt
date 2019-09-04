package org.oppia.app.drawer.ui.preferences

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import org.oppia.app.activity.ParentDrawerActivity
import org.oppia.app.R


class PreferencesDrawerActivity : ParentDrawerActivity() {

  private lateinit var preferencesViewModel: PreferencesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_preferences)
    preferencesViewModel =
      ViewModelProviders.of(this).get(PreferencesViewModel::class.java)

    init(resources.getString(R.string.menu_preferences))

  }
}