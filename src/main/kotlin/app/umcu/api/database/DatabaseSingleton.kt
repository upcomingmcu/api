package app.umcu.api.database

import app.umcu.api.database.tables.ProductionsTable
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

class DatabaseSingleton private constructor(private val applicationConfig: ApplicationConfig) {
	companion object {
		private var instance: DatabaseSingleton? = null
		val logger: Logger = LoggerFactory.getLogger(DatabaseSingleton::class.java.simpleName)

		fun getInstance(applicationConfig: ApplicationConfig): DatabaseSingleton = instance ?: synchronized(this) {
			instance ?: DatabaseSingleton(applicationConfig).also {
				instance = it
			}
		}
	}

	private var initialized = false
	private var database: Database? = null

	/**
	 * Connect to the database server.
	 *
	 * @param embedded True if the embedded database should be used rather than the external MariaDB server.
	 * @return [Database] The database instance.
	 */
	private fun connect(embedded: Boolean): Database? {
		logger.info("Attempting to connect to the database server.")

		if (embedded) {
			database = Database.connect(
				"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = ""
			)
		} else {
			try {
				val driver = "org.mariadb.jdbc.Driver"
				Class.forName(driver)

				val host = applicationConfig.property("db.host").getString()
				val port = applicationConfig.property("db.port").getString()
				val user = applicationConfig.property("db.user").getString()
				val pass = applicationConfig.property("db.pass").getString()
				val name = applicationConfig.property("db.name").getString()

				database = Database.connect("jdbc:mariadb://$host:$port/$name", driver, user, password = pass)
			} catch (e: Exception) {
				database = null
			}
		}

		return if (database == null) {
			logger.error("The connection to the database server failed.")
			null
		} else {
			logger.info("The connection to the database server was successful using ${database?.vendor} ${database?.version}.")
			database
		}
	}

	/**
	 * Initialize the Exposed database instance.
	 * Set the default database and create the table(s).
	 */
	fun init() {
		logger.info("Attempting to initialize the database.")

		if (initialized) throw DatabaseInitializationException()

		TransactionManager.defaultDatabase =
			connect(applicationConfig.property("ktor.environment").getString().equals("dev", true))

		transaction {
			SchemaUtils.create(ProductionsTable)
		}

		initialized = true

		logger.info("The database has been initialized.")
	}

	suspend fun <T> query(statement: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
		statement()
	}
}
