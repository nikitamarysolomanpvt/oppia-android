package org.oppia.util.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.oppia.util.threading.BlockingDispatcher
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An in-memory cache that provides blocking CRUD operations such that each operation is guaranteed to operate exactly
 * after any prior started operations began, and before any future operations. This class is thread-safe. Note that it's
 * safe to execute long-running operations in lambdas passed into the methods of this class.
 */
class InMemoryBlockingCache<T : Any> private constructor(blockingDispatcher: CoroutineDispatcher, initialValue: T?) {
  private val blockingScope = CoroutineScope(blockingDispatcher)

  /**
   * The value of the cache. Note that this does not require a lock since it's only ever accessed via the blocking
   * dispatcher's single thread.
   */
  private var value: T? = initialValue

  /**
   * Returns a [Deferred] that, upon completion, guarantees that the cache has been recreated and initialized to the
   * specified value. The [Deferred] will be passed the most up-to-date state of the cache.
   */
  fun createAsync(newValue: T): Deferred<T> {
    return blockingScope.async {
      value = newValue
      newValue
    }
  }

  /**
   * Returns a [Deferred] that provides the most-up-to-date value of the cache, after either retrieving the current
   * state (if defined), or calling the provided generator to create a new state and initialize the cache to that state.
   * The provided function must be thread-safe and should have no side effects.
   */
  fun createIfAbsentAsync(generate: suspend () -> T): Deferred<T> {
    return blockingScope.async {
      val initedValue = value ?: generate()
      value = initedValue
      initedValue
    }
  }

  /**
   * Returns a [Deferred] that will provide the most-up-to-date value stored in the cache, or null if it's not yet
   * initialized.
   */
  fun readAsync(): Deferred<T?> {
    return blockingScope.async {
      value
    }
  }

  /**
   * Returns a [Deferred] similar to [readAsync], except this assumes the cache to have been created already otherwise
   * an exception will be thrown.
   */
  fun readIfPresentAsync(): Deferred<T> {
    return blockingScope.async {
      checkNotNull(value) { "Expected to read the cache only after it's been created" }
    }
  }

  /**
   * Returns a [Deferred] that provides the most-up-to-date value of the cache, after atomically updating it based on
   * the specified update function. Note that the update function provided here must be thread-safe and should have no
   * side effects. This function is safe to call regardless of whether the cache has been created, meaning it can be
   * used also to initialize the cache.
   */
  fun updateAsync(update: suspend (T?) -> T): Deferred<T> {
    return blockingScope.async {
      val updatedValue = update(value)
      value = updatedValue
      updatedValue
    }
  }

  /**
   * Returns a [Deferred] in the same way as [updateAsync], excepted this update is expected to occur after cache
   * creation otherwise an exception will be thrown.
   */
  fun updateIfPresentAsync(update: suspend (T) -> T): Deferred<T> {
    return blockingScope.async {
      val updatedValue = update(checkNotNull(value) { "Expected to update the cache only after it's been created" })
      value = updatedValue
      updatedValue
    }
  }

  /**
   * Returns a [Deferred] that executes when this cache has been fully cleared, or if it's already been cleared.
   */
  fun deleteAsync(): Deferred<Unit> {
    return blockingScope.async {
      value = null
    }
  }

  /**
   * Returns a [Deferred] that executes when checking the specified function on whether this cache should be deleted,
   * and returns whether it was deleted.
   *
   * Note that the provided function will not be called if the cache is already cleared.
   */
  fun maybeDeleteAsync(shouldDelete: suspend (T) -> Boolean): Deferred<Boolean> {
    return blockingScope.async {
      val valueSnapshot = value
      if (valueSnapshot != null && shouldDelete(valueSnapshot)) {
        value = null
        true
      } else false
    }
  }

  /**
   * Returns a [Deferred] in the same way as [maybeDeleteAsync], except the deletion function provided is guaranteed to
   * be called regardless of the state of the cache, and whose return value will be returned in this method's
   * [Deferred].
   */
  fun maybeForceDeleteAsync(shouldDelete: suspend (T?) -> Boolean): Deferred<Boolean> {
    return blockingScope.async {
      if (shouldDelete(value)) {
        value = null
        true
      } else false
    }
  }

  /** An injectable factory for [InMemoryBlockingCache]es. */
  @Singleton
  class Factory @Inject constructor(@BlockingDispatcher private val blockingDispatcher: CoroutineDispatcher) {
    /** Returns a new [InMemoryBlockingCache] with, optionally, the specified initial value. */
    fun <T : Any> create(initialValue: T? = null): InMemoryBlockingCache<T> {
      return InMemoryBlockingCache(blockingDispatcher, initialValue)
    }
  }
}
