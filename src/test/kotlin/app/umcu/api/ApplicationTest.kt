package app.umcu.api

import app.umcu.api.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class ApplicationTest {

	@Test
	fun testRoot() = testApplication {
		application {
			configureRouting()
		}

		client.get("/ping").apply {
			assertEquals(HttpStatusCode.OK, status)
			assertEquals(HttpStatusCode.OK.toString(), bodyAsText())
		}
	}
}
