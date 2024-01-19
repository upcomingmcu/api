package app.umcu.api.utils.extensions

import java.util.*

fun String.toSlug() =
	lowercase(Locale.getDefault()).replace("\n", " ").replace("[^a-z\\d\\s]".toRegex(), " ").split(" ")
		.joinToString("-").replace("-+".toRegex(), "-")
