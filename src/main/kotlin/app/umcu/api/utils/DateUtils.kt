package app.umcu.api.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.DateTimeParseException

object DateUtils {
	/**
	 * Get the current date from the system clock and convert it to a [kotlinx.datetime.LocalDate] instance.
	 *
	 * @return [kotlinx.datetime.LocalDate] The current date.
	 */
	fun today(): LocalDate {
		return java.time.LocalDate.now().toKotlinLocalDate()
	}

	/**
	 * Trim an ISO 8601 formatted date string to only date (`YYYY-MM-DD`).
	 *
	 * ```
	 * val trimmed = trimDateString("2008-05-02T00:00:00.000Z")
	 * println(trimmed) # 2008-05-02
	 * ```
	 *
	 * @param dateString An ISO 8601 formatted date string.
	 * @return The date string, trimmed down to just the date.
	 */
	fun trimDateString(dateString: String): String? {
		if (dateString.length < 10) return null
		val trimmed = dateString.take(10)
		return trimmed
	}

	/**
	 * Parse an ISO 8601 formatted date string into a [LocalDate] object.
	 *
	 * @param dateString An ISO 8601 formatted date string.
	 * @return The resulting [LocalDate] object, or null if [dateString] is invalid.
	 */
	fun parseDateString(dateString: String?): LocalDate? {
		return try {
			dateString?.let { LocalDate.parse(it) }
		} catch (e: DateTimeParseException) {
			null
		}
	}
}
