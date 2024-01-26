package app.umcu.api.data

import app.umcu.api.data.productions.ProductionsTable
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Initialize a connection to the database server and perform database queries.
 *
 * Before using the DatabaseSingleton, you must initialize the database.
 * Put this line before any of your queries will be run.
 * ```
 * # Initialize the database.
 * DatabaseSingleton.instance.init(embedded)
 * ```
 *
 * From there, you may query the database using the [query] function.
 * ```kotlin
 * DatabaseSingleton.instance.query {
 * 	...
 * }
 * ```
 */
class DatabaseSingleton private constructor() {
	companion object {
		val instance: DatabaseSingleton by lazy { DatabaseSingleton() }
	}

	private var logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
	private var initialized = false

	/**
	 * Connect to the database server.
	 *
	 * @param embedded `true` if an embedded H2 database should be used, otherwise use an external Postgres database.
	 * @param config Optional, must be passed if [embedded] is `false`.
	 * @return [Database] The database server instance.
	 */
	private fun connect(embedded: Boolean = true, config: ApplicationConfig? = null): Database? {
		return try {
			if (embedded) {
				Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver", "root", "")
			} else {
				Class.forName("org.postgresql.Driver")
				if (config != null) {
					val db = config.property("postgres.db").getString()
					val user = config.property("postgres.user").getString()
					val password = config.property("postgres.password").getString()
					val url = "jdbc:postgresql://db:5432/$db"
					Database.connect(url, "org.postgresql.Driver", user, password)
				} else null
			}
		} catch (e: Exception) {
			logger.error(e.message)
			null
		}
	}

	/**
	 * Initialize the database.
	 *
	 * @param embedded `true` if an embedded H2 database should be used, otherwise use an external Postgres database.
	 * @throws DatabaseInitializationException If the database is already initialized.
	 */
	fun init(embedded: Boolean = true) {
		if (initialized) throw DatabaseInitializationException("The database is already initialized.")

		val database = connect(embedded)
		TransactionManager.defaultDatabase = database

		transaction {
			SchemaUtils.create(ProductionsTable)

			// todo load data from remote data source
		}

		initialized = true
	}

	suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
		statement()
	}
}

class DatabaseInitializationException(override val message: String? = null) :
	Exception(message ?: "The database could not be initialized.")
