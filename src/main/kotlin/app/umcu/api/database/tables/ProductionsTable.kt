package app.umcu.api.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ProductionsTable : IntIdTable("productions") {
	/**
	 * The human-friendly unique identifier of the production.
	 * Determined by the production's title. For example: "Avengers: Infinity War" would be "avengers-infinity-war".
	 */
	val slug = varchar("slug", 255).uniqueIndex()

	/**
	 * The identifier of the production on The Movie Database.
	 * Unique to TMDB but not necessarily for this application (for example, series with multiple seasons will share this value).
	 */
	val tmdbId = integer("tmdb_id")

	/**
	 * The title of the production.
	 */
	val title = varchar("title", 255)

	/**
	 * The release date of the production. In the format of an ISO 8601 date and time.
	 */
	val releaseDate = timestamp("release_date").nullable()
}
