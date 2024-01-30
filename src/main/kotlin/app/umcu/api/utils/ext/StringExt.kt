package app.umcu.api.utils.ext

import java.util.*

fun String.toSlug() =
	lowercase(Locale.getDefault()).replace("\n", " ").replace("[^a-z\\d\\s]".toRegex(), " ").split(" ")
		.joinToString("-").replace("-+".toRegex(), "-")
