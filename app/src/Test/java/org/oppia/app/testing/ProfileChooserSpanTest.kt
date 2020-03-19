package org.oppia.app.testing



import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oppia.app.R
import org.robolectric.annotation.Config
import javax.inject.Inject


/**
 * It tests  [ProfileChooserFragment]
 * For reference https://github.com/codepath/android-robolectric-espresso-demo/blob/master/app/src/test/java/com.codepath.testingdemo/adapters/PostsAdapterTest.java
 */
@RunWith(AndroidJUnit4::class)
class ProfileChooserSpanTest {

  @Inject
  lateinit var context: Context
  @Before
  @ExperimentalCoroutinesApi
  fun setUp() {
    ApplicationProvider.getApplicationContext<Context>().resources.configuration.orientation =
      Configuration.ORIENTATION_LANDSCAPE
    context = ApplicationProvider.getApplicationContext()
  }


  @Test
  @Config(qualifiers = "lhdpi")
  fun testProfileChooserSpanTest_onConfigLandScapeAndScreenldpi() {
    assertThat(context.resources.getInteger(R.integer.profilechooser_span)).isEqualTo(3)
  }
  @Test
  @Config(qualifiers = "mhdpi")
  fun testProfileChooserSpanTest_onConfigLandScapeAndScreenMdpi() {
    assertThat(context.resources.getInteger(R.integer.profilechooser_span)).isEqualTo(3)
  }
  @Test
  @Config(qualifiers = "hdpi")
  fun testProfileChooserSpanTest_onConfigLandScapeAndScreenHdpi() {
    assertThat(context.resources.getInteger(R.integer.profilechooser_span)).isEqualTo(4)
  }

  @Test
  @Config(qualifiers = "xhdpi")
  fun testProfileChooserSpanTest_onConfigLandScapeAndScreenXhdpi() {
    assertThat(context.resources.getInteger(R.integer.profilechooser_span)).isEqualTo(5)
  }
  @Test
  @Config(qualifiers = "sw480dp")
  fun testProfileChooserSpanTest_onConfigLandScapeAndScreenSw480dp() {
    assertThat(context.resources.getInteger(R.integer.profilechooser_span)).isEqualTo(5)
  }



}