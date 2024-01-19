package app.umcu.api.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimit() {
	val limit = environment.config.property("ktor.application.rate_limit").getString().toInt()
	install(RateLimit) {
		/**
		 * Sets a rate limit for a specific route.
		 * See: https://ktor.io/docs/rate-limit.html#rate-limiting-scope
		 */
		register {
			rateLimiter(limit = limit, refillPeriod = 60.seconds)
		}
	}
}
