package app.umcu.api.data

import app.umcu.api.data.productions.ProductionsTable
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseSingleton private constructor(applicationEnvironment: ApplicationEnvironment) {
	companion object {
		private var instance: DatabaseSingleton? = null

		fun getInstance(applicationEnvironment: ApplicationEnvironment): DatabaseSingleton {
			return instance ?: synchronized(this) {
				instance ?: DatabaseSingleton(applicationEnvironment).also {
					instance = it
				}
			}
		}

		private fun getDatabase(applicationEnvironment: ApplicationEnvironment): Database? {
			val embedded = applicationEnvironment.developmentMode
			val config = applicationEnvironment.config
			return try {
				if (embedded) {
					Database.connect(
						url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
						user = "root",
						driver = "org.h2.Driver",
						password = ""
					)
				} else {
					val pgDriver = "org.postgresql.Driver"
					Class.forName(pgDriver)
					val db = config.property("postgres.db").getString()
					val user = config.property("postgres.user").getString()
					val password = config.property("postgres.password").getString()
					val url = "jdbc:postgresql://db:5432/$db"
					Database.connect(url, "org.postgresql.Driver", user, password)
				}
			} catch (e: Exception) {
				null
			}
		}
	}

	private var initialized = false
	private val database: Database? = getDatabase(applicationEnvironment)

	fun init() {
		if (initialized) throw DatabaseInitializationException()

		TransactionManager.defaultDatabase = database

		transaction {
			SchemaUtils.create(ProductionsTable)
		}

		initialized = true
	}

	suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
		statement()
	}
}

class DatabaseInitializationException(override val message: String? = null) :
	Exception(message ?: "The database is already initialized.")
