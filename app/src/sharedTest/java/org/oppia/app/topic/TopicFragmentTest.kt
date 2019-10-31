package org.oppia.app.topic

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.UiThread
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.oppia.app.R
import org.oppia.app.recyclerview.RecyclerViewMatcher.Companion.atPosition
import org.oppia.app.utility.EspressoTestsMatchers.matchCurrentTabTitle
import org.oppia.util.threading.BackgroundDispatcher
import org.oppia.util.threading.BlockingDispatcher
import javax.inject.Singleton

/** Tests for [TopicActivity]. */
@RunWith(AndroidJUnit4::class)
class TopicFragmentTest {
  private val topicName = "Second Test Topic"

  @get:Rule
  var activityTestRule: ActivityTestRule<TopicActivity> = ActivityTestRule(
    TopicActivity::class.java, /* initialTouchMode= */ true, /* launchActivity= */ false
  )

  private fun getResourceString(id: Int): String {
    val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
    return resources.getString(id)
  }

  @Test
  fun testTopicFragment_showsTopicFragmentWithMultipleTabs() {
    activityTestRule.launchActivity(null)
    onView(withId(R.id.topic_tabs_container)).perform(click()).check(matches(isDisplayed()))
  }

  @Test
  fun testTopicFragment_swipePage_hasSwipedPage() {
    activityTestRule.launchActivity(null)
    onView(withId(R.id.topic_tabs_viewpager)).check(matches(isDisplayed()))
    onView(withId(R.id.topic_tabs_viewpager)).perform(ViewActions.swipeLeft())
  }

  @Test
  @UiThread
  fun testTopicFragment_overviewTopicIsDisplayedInTabLayout_worksAsExpected() {
    activityTestRule.launchActivity(null)

    onView(
      allOf(
        withText(R.string.overview),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    )
  }

  @Test
  @UiThread
  fun testTopicFragment_defaultTabIsOverview_isSuccessful() {
    activityTestRule.launchActivity(null)
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.overview))))
  }

  @Test
  @UiThread
  fun testTopicFragment_showsOverviewTabSelectedAndContentMatched() {
    activityTestRule.launchActivity(null)
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.overview))))
    onView(withId(R.id.topic_name_text_view)).check(
      matches(
        withText(
          Matchers.containsString(topicName)
        )
      )
    )
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnPlayTab_showsPlayTabSelected() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.play),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.play))))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnPlayTab_showsPlayTabWithContentMatched() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.play),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withText("First Story")).check(matches(isDisplayed()))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnTrainTab_showsTrainTabSelected() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.train),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.train))))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnTrainTab_showsTrainTabWithContentMatched() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.train),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withText("Master These Skills")).check(matches(isDisplayed()))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnReviewTab_showsReviewTabSelected() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.review),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.review))))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnReviewTab_showsReviewTabWithContentMatched() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.review),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(
      allOf(
        atPosition(R.id.review_skill_recycler_view, 0).also { withText("An important skill") },
        isDescendantOfA(withId(R.id.review_skill_recycler_view))
      ).also { withId(R.id.skill_name) })
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnOverviewTab_showsOverviewTabSelected() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.review),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(
      allOf(
        withText(R.string.overview),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.overview))))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnOverviewTab_showsOverviewTabWithContentMatched() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.review),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(
      allOf(
        withText(R.string.overview),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_name_text_view)).check(
      matches(
        withText(
          Matchers.containsString(topicName)
        )
      )
    )
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnPlayTab_configurationChange_showsSameTabAndItsData() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.play),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withText("First Story")).check(matches(isDisplayed()))
    activityTestRule.activity.requestedOrientation = Configuration.ORIENTATION_LANDSCAPE
    onView(withId(R.id.topic_tabs_container)).check(matches(matchCurrentTabTitle(getResourceString(R.string.play))))
    onView(withText("First Story")).check(matches(isDisplayed()))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnTrainTab_configurationChange_showsSameTabAndItsData() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.train),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withText("Master These Skills")).check(matches(isDisplayed()))
    activityTestRule.activity.requestedOrientation = Configuration.ORIENTATION_LANDSCAPE
    onView(
      allOf(
        withText(R.string.train),
        isDisplayed()
      )
    )
    onView(withText("Master These Skills")).check(matches(isDisplayed()))
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnReviewTab_configurationChange_showsSameTabAndItsData() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.review),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(allOf(
      atPosition(R.id.review_skill_recycler_view, 0).also { withText("An important skill") },
      isDescendantOfA(withId(R.id.review_skill_recycler_view))
    ).also { withId(R.id.skill_name) })
    activityTestRule.activity.requestedOrientation = Configuration.ORIENTATION_LANDSCAPE
    onView(
      allOf(
        withText(R.string.review),
        isDisplayed()
      )
    )
    onView(allOf(
      atPosition(R.id.review_skill_recycler_view, 0).also { withText("An important skill") },
      isDescendantOfA(withId(R.id.review_skill_recycler_view))
    ).also { withId(R.id.skill_name) })
  }

  @Test
  @UiThread
  fun testTopicFragment_clickOnOverviewTab_configurationChange_showsSameTabAndItsData() {
    activityTestRule.launchActivity(null)
    onView(
      allOf(
        withText(R.string.overview),
        isDescendantOfA(withId(R.id.topic_tabs_container))
      )
    ).perform(click())
    onView(withId(R.id.topic_name_text_view)).check(
      matches(
        withText(
          Matchers.containsString(topicName)
        )
      )
    )
    activityTestRule.activity.requestedOrientation = Configuration.ORIENTATION_LANDSCAPE
    onView(
      allOf(
        withText(R.string.overview),
        isDisplayed()
      )
    )
    onView(withId(R.id.topic_name_text_view)).check(
      matches(
        withText(
          Matchers.containsString(topicName)
        )
      )
    )
  }

  @Module
  class TestModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
      return application
    }

    // TODO(#89): Introduce a proper IdlingResource for background dispatchers to ensure they all complete before
    //  proceeding in an Espresso test. This solution should also be interoperative with Robolectric contexts by using a
    //  test coroutine dispatcher.

    @Singleton
    @Provides
    @BackgroundDispatcher
    fun provideBackgroundDispatcher(@BlockingDispatcher blockingDispatcher: CoroutineDispatcher): CoroutineDispatcher {
      return blockingDispatcher
    }
  }

  @Singleton
  @Component(modules = [TestModule::class])
  interface TestApplicationComponent {
    @Component.Builder
    interface Builder {
      @BindsInstance
      fun setApplication(application: Application): Builder

      fun build(): TestApplicationComponent
    }
  }
}