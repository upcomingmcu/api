package app.umcu.api.data

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

		transaction {
			SchemaUtils.create(ProductionsTable)

			// TODO load data
		}
	}

	suspend fun <T> query(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}
