package app.umcu.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
	val count: Int, val data: List<T>
) {
	constructor(vararg items: T) : this(count = items.size, data = items.asList())
}
