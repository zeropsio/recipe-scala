package services

import java.time.{Clock, Instant, Duration}
import javax.inject._
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import akka.actor.Cancellable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
 * This class demonstrates how to run advanced code when the
 * application starts and stops. It starts a timer when the
 * application starts and logs more detailed information. When the
 * application stops, it calculates and logs the total running time,
 * along with additional performance metrics and error handling.
 *
 * This class is registered for Guice dependency injection in the
 * [[Module]] class and starts automatically when the application
 * starts, as it is an "eager singleton".
 */
@Singleton
class AdvancedApplicationTimer @Inject() (
  clock: Clock,
  appLifecycle: ApplicationLifecycle
)(implicit ec: ExecutionContext) {

  private val logger = org.slf4j.LoggerFactory.getLogger(classOf[AdvancedApplicationTimer])

  // Store the start time when the application starts.
  private val start: Instant = clock.instant
  logger.info(s"AdvancedApplicationTimer: Starting application at $start.")

  // Variable to track system metrics, such as memory and CPU usage.
  private var cancellableMetricsLogger: Option[Cancellable] = None

  // Schedule a task to log system metrics periodically.
  startMetricsLogger()

  // Register a stop hook with the ApplicationLifecycle object to run when the application stops.
  appLifecycle.addStopHook { () =>
    stopMetricsLogger()

    val stop: Instant = clock.instant
    val runningDuration: Duration = Duration.between(start, stop)

    Try {
      logPerformanceMetrics()
      logger.info(f"AdvancedApplicationTimer: Application ran for ${runningDuration.toSeconds} seconds.")
    } match {
      case Success(_) =>
        logger.info("Application shutdown process completed successfully.")
      case Failure(exception) =>
        logger.error("Error during application shutdown.", exception)
    }

    Future.successful(())
  }

  // Function to log periodic system metrics (e.g., memory usage).
  private def startMetricsLogger(): Unit = {
    val cancellable = akka.actor.ActorSystem().scheduler.scheduleWithFixedDelay(0.seconds, 1.minute) { () =>
      val runtime = Runtime.getRuntime
      val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
      val maxMemory = runtime.maxMemory() / 1024 / 1024
      val freeMemory = runtime.freeMemory() / 1024 / 1024

      logger.info(f"System Metrics: Used memory = $usedMemory MB, Free memory = $freeMemory MB, Max memory = $maxMemory MB")
    }(ec)

    cancellableMetricsLogger = Some(cancellable)
  }

  // Function to stop the periodic metrics logging when the application stops.
  private def stopMetricsLogger(): Unit = {
    cancellableMetricsLogger.foreach(_.cancel())
    logger.info("Stopped logging system metrics.")
  }

  // Function to log performance-related metrics at application stop.
  private def logPerformanceMetrics(): Unit = {
    val runtime = Runtime.getRuntime
    val processors = runtime.availableProcessors()
    val freeMemory = runtime.freeMemory() / 1024 / 1024
    val maxMemory = runtime.maxMemory() / 1024 / 1024

    logger.info(s"AdvancedApplicationTimer: Final metrics - Available processors: $processors, Free memory: $freeMemory MB, Max memory: $maxMemory MB.")
  }
}
