package org.oppia.data.backends.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.oppia.data.backends.ApiUtils
import org.oppia.data.backends.gae.NetworkInterceptor
import org.oppia.data.backends.gae.NetworkSettings
import org.oppia.data.backends.gae.api.ExplorationService
import org.oppia.data.backends.gae.model.GaeExplorationContainer
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate

/**
 * Mock ExplorationService with dummy data from [exploration.json]
 */
class MockExplorationService(private val delegate: BehaviorDelegate<ExplorationService>) :
  ExplorationService {
  override fun getExplorationById(explorationId: String): Call<GaeExplorationContainer> {
    val explorationContainer = createMockGaeExplorationContainer()
    return delegate.returningResponse(explorationContainer).getExplorationById(explorationId)
  }

  /**
   * This function creates a mock GaeExplorationContainer with data from dummy json.
   * This function also tests the removeXSSIPrefix function from [NetworkInterceptor]
   * @return GaeExplorationContainer: GaeExplorationContainer with mock data
   */
  private fun createMockGaeExplorationContainer(): GaeExplorationContainer {
    val networkInterceptor = NetworkInterceptor()
    var explorationResponseWithXssiPrefix =
      NetworkSettings.XSSI_PREFIX + ApiUtils.getFakeJson("exploration.json")
    explorationResponseWithXssiPrefix =
      networkInterceptor.removeXSSIPrefix(explorationResponseWithXssiPrefix)

    val moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<GaeExplorationContainer> =
      moshi.adapter(GaeExplorationContainer::class.java)
    val mockGaeExplorationContainer = adapter.fromJson(explorationResponseWithXssiPrefix)

    return mockGaeExplorationContainer!!
  }
}
