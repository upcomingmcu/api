package app.umcu.api.data

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ProductionsTable : IntIdTable() {
	val slug: Column<String> = text("slug").uniqueIndex()
	val tmdbId: Column<Int> = integer("tmdbId")
	val title: Column<String> = text("title")
	val releaseDate: Column<Instant?> = timestamp("releaseDate").nullable()
}
