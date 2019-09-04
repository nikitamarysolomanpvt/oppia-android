package org.oppia.app.home

import android.os.Bundle
import org.oppia.app.activity.ParentDrawerActivity
import org.oppia.app.R
import org.oppia.app.drawer.FragmentTransactions
import javax.inject.Inject

/** The central activity for all users entering the app. */
class HomeDrawerActivity : ParentDrawerActivity(),
  FragmentTransactions {

  @Inject lateinit var homeActivityController: HomeActivityController


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



    activityComponent.inject(this)
    homeActivityController.handleOnCreate()
    init(resources.getString(R.string.menu_home))
  }




}
