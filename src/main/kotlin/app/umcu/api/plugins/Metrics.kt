package app.umcu.api.plugins

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.logging.LogbackMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureMetrics() {
	val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

	install(MicrometerMetrics) {
		registry = appMicrometerRegistry
		timers { call, _ ->
			call.request.headers["regionId"]?.let { tag("region", it) }
		}
		meterBinders = listOf(
			JvmMemoryMetrics(), JvmGcMetrics(), ProcessorMetrics(), UptimeMetrics(), LogbackMetrics()
		)
	}

	routing {
		get("/metrics") {
			call.respond(appMicrometerRegistry.scrape())
		}
	}
}
