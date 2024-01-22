@file:Suppress("unused")

package app.umcu.api.models

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val code: Int, val reason: String) {
	constructor(httpStatusCode: HttpStatusCode) : this(httpStatusCode.value, httpStatusCode.description)
	constructor(httpStatusCode: HttpStatusCode, reason: String) : this(httpStatusCode.value, reason)
}
