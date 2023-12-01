/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package app.umcu.api.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateParsingUtils {
	/**
	 * The pattern to use when parsing the date string into a LocalDate.
	 * See: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/format/DateTimeFormatter.html
	 */
	private val shortDatePattern = "yyyy-MM-dd"

	/**
	 * The time zone to use when converting a LocalDate object into a ZonedDateTime object.
	 * See: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/ZonedDateTime.html
	 */
	private val timeZone = "America/New_York"

	/**
	 * Trims a date string down to the first 10 characters.
	 *
	 * ```kotlin
	 * val trimmed = trimDate("2008-05-02T00:00:00.000Z")
	 * println(trimmed) // "2008-05-02"
	 * ```
	 */
	fun trimDate(dateString: String?): String? {
		if (dateString.isNullOrEmpty() || dateString.length < 9) return null
		return dateString.slice(IntRange(0, 9))
	}

	/**
	 * Parses a date string to a LocalDate object.
	 * The `dateString` parameter must be in the format as specified by `shortDatePattern`.
	 *
	 * ```kotlin
	 * val localDate = parseLocalDate("2008-05-02")
	 * println(localDate) // 2008-05-02
	 * ```
	 */
	fun parseLocalDate(dateString: String?): LocalDate? {
		val trimmed = dateString?.let { trimDate(it) } ?: return null
		return runCatching {
			trimmed.takeIf { it.isNotEmpty() }?.let {
				LocalDate.parse(it, DateTimeFormatter.ofPattern(shortDatePattern))
			}
		}.getOrNull()
	}

	/**
	 * Parses a date string to a ZonedDateTime object.
	 * The `dateString` parameter must be in the format as specified by `shortDatePattern`.
	 * The date will be zoned to the time zone specified by `timeZone`.
	 *
	 * ```kotlin
	 * val zonedDateTime = parseZonedDateTime("2008-05-02")
	 * println(zonedDateTime) // 2008-05-02T00:00-04:00[America/New_York]
	 * ```
	 */
	fun parseZonedDateTime(dateString: String?): ZonedDateTime? {
		val localDate = parseLocalDate(dateString) ?: return null
		return ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.of(timeZone))
	}
}
