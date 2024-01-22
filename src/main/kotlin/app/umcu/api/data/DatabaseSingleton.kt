package app.umcu.api.data

import app.umcu.api.models.Production
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
	private fun connect(environment: ApplicationEnvironment, embedded: Boolean = false): Database {
		if (embedded) {
			return Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver", "root", "")
		} else {
			Class.forName("org.postgresql.Driver")
			val db = environment.config.property("postgres.db").getString()
			val user = environment.config.property("postgres.user").getString()
			val password = environment.config.property("postgres.password").getString()
			val url = "jdbc:postgresql://db:5432/$db"
			return Database.connect(url, "org.postgresql.Driver", user, password)
		}
	}

	fun init(environment: ApplicationEnvironment, embedded: Boolean = false) {
		val database = connect(environment, embedded)
		TransactionManager.defaultDatabase = database

		val data = listOf(
			Production(tmdbId = 24428, title = "The Avengers", releaseDate = null),
			Production(tmdbId = 99861, title = "Avengers: Age of Ultron", releaseDate = null),
			Production(tmdbId = 299536, title = "Avengers: Infinity War", releaseDate = null),
			Production(tmdbId = 299534, title = "Avengers: Endgame", releaseDate = null),
			Production(tmdbId = 1003596, title = "Avengers 5", releaseDate = null),
			Production(tmdbId = 1003598, title = "Avengers: Secret Wars", releaseDate = null)
		)

		transaction {
			SchemaUtils.create(ProductionsTable)

			data.forEach {
				ProductionDao.new {
					slug = it.slug
					tmdbId = it.tmdbId
					title = it.title
				}
			}

			// TODO load data from remote source
		}
	}

	suspend fun <T> query(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}
