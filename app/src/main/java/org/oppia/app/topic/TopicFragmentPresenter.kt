package org.oppia.app.topic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import org.oppia.app.R
import org.oppia.app.databinding.TopicFragmentBinding
import org.oppia.app.fragment.FragmentScope
import org.oppia.app.model.Topic
import org.oppia.domain.topic.TEST_TOPIC_ID_0
import org.oppia.domain.topic.TopicController
import org.oppia.util.data.AsyncResult
import org.oppia.util.logging.Logger
import javax.inject.Inject

/** The presenter for [TopicFragment]. */
@FragmentScope
class TopicFragmentPresenter @Inject constructor(
  private val fragment: Fragment,
  private val logger: Logger,
  private val topicController: TopicController
) {
  private lateinit var tabLayout: TabLayout
  private lateinit var toolbar: Toolbar
  private lateinit var viewPager: ViewPager
  private val tabIcons =
    intArrayOf(
      R.drawable.ic_overview_white_24dp,
      R.drawable.ic_play_icon_24,
      R.drawable.ic_train_icon_24,
      R.drawable.ic_review_icon_24
    )

  fun handleCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    topicId: String
  ): View? {
    val binding = TopicFragmentBinding.inflate(inflater, container, /* attachToRoot= */ false)
    binding.lifecycleOwner = fragment
    viewPager = binding.root.findViewById(R.id.topic_tabs_viewpager) as ViewPager
    tabLayout = binding.root.findViewById(R.id.topic_tabs_container) as TabLayout
    toolbar = binding.root.findViewById(R.id.toolbar) as Toolbar
    setUpViewPager(viewPager, topicId)
    subscribeToTopicLiveData()
    return binding.root
  }

  private fun setUpViewPager(viewPager: ViewPager, topicId: String?) {
    val adapter = ViewPagerAdapter(fragment.fragmentManager!!, topicId!!)
    viewPager.adapter = adapter
    tabLayout.setupWithViewPager(viewPager)
    tabLayout.getTabAt(0)!!.setText(TopicTab.getTabForPosition(0).name).setIcon(tabIcons[0])
    tabLayout.getTabAt(1)!!.setText(TopicTab.getTabForPosition(1).name).setIcon(tabIcons[1])
    tabLayout.getTabAt(2)!!.setText(TopicTab.getTabForPosition(2).name).setIcon(tabIcons[2])
    tabLayout.getTabAt(3)!!.setText(TopicTab.getTabForPosition(3).name).setIcon(tabIcons[3])
  }

  private val topicLiveData: LiveData<Topic> by lazy { getTopic() }

  private fun subscribeToTopicLiveData() {
    topicLiveData.observe(fragment, Observer<Topic> { result ->
      val topicName = result.name
      toolbar.title = topicName
    })
  }

  // TODO(#135): Get this topic-id from [TopicFragment].
  private val topicResultLiveData: LiveData<AsyncResult<Topic>> by lazy {
    topicController.getTopic(TEST_TOPIC_ID_0)
  }

  private fun getTopic(): LiveData<Topic> {
    return Transformations.map(topicResultLiveData, ::processTopicResult)
  }

  private fun processTopicResult(topic: AsyncResult<Topic>): Topic {
    if (topic.isFailure()) {
      logger.e("TopicFragment", "Failed to retrieve topic", topic.getErrorOrNull()!!)
    }
    return topic.getOrDefault(Topic.getDefaultInstance())
  }
}
