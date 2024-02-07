package app.umcu.api.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ProductionsTable : IntIdTable("productions") {
	val slug = varchar("slug", 255).uniqueIndex()
	val tmdbId = integer("tmdb_id")
	val title = varchar("title", 255)
	val overview = text("overview").nullable()
	val releaseDate = timestamp("release_date").nullable()
	val posterUrl = varchar("poster_url", 100).nullable()
	val mediaType = varchar("media_type", 5)
}
