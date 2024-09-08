package services

import java.util.concurrent.atomic.AtomicInteger
import javax.inject._
import org.slf4j.LoggerFactory

/**
 * This trait represents a counter that returns an incremented number
 * each time it is called. It can be injected into a controller or
 * any other service using Guice.
 */
trait Counter {
  def nextCount(): Int
  def reset(): Unit
  def getCurrentCount(): Int
}

/**
 * A thread-safe implementation of the [[Counter]] trait using an
 * [[AtomicInteger]] for efficient atomic operations.
 *
 * This class is marked as a `@Singleton` to ensure only one instance
 * of the counter is shared across the entire application. It supports
 * incrementing, resetting, and retrieving the current count.
 */
@Singleton
class AtomicCounter extends Counter {

  private val atomicCounter = new AtomicInteger()
  private val logger = LoggerFactory.getLogger(classOf[AtomicCounter])

  // Increments the counter and returns the new value.
  override def nextCount(): Int = {
    val nextValue = atomicCounter.incrementAndGet()
    logger.info(s"Counter incremented to: $nextValue")
    nextValue
  }

  // Resets the counter to zero.
  override def reset(): Unit = {
    atomicCounter.set(0)
    logger.info("Counter has been reset to 0.")
  }

  // Returns the current value of the counter without incrementing.
  override def getCurrentCount(): Int = {
    val currentValue = atomicCounter.get()
    logger.info(s"Current counter value: $currentValue")
    currentValue
  }
}
