package app.umcu.api.data.productions

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ProductionsTable : IntIdTable() {
	/**
	 * The human-friendly unique identifier of the production.
	 * Determined by the production's title. For example: "Avengers: Infinity War" would be "avengers-infinity-war".
	 */
	val slug: Column<String> = text("slug").uniqueIndex()

	/**
	 * The identifier of the production on The Movie Database.
	 * Unique to TMDB but not necessarily for this application (for example, series with multiple seasons will share this value).
	 */
	val tmdbId: Column<Int> = integer("tmdbId")

	/**
	 * The title of the production.
	 */
	val title: Column<String> = text("title")

	/**
	 * The release date of the production. In the format of an ISO 8601 date and time.
	 */
	val releaseDate: Column<Instant?> = timestamp("releaseDate").nullable()
}
