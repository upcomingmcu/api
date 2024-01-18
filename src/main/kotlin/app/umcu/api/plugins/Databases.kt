package app.umcu.api.plugins

import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases() {
	val dbConnection: Connection = connectToPostgres(embedded = false)
}

fun Application.connectToPostgres(embedded: Boolean): Connection {
	Class.forName("org.postgresql.Driver")
	if (embedded) {
		return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
	} else {
		val db = environment.config.property("postgres.db").getString()
		val user = environment.config.property("postgres.user").getString()
		val password = environment.config.property("postgres.password").getString()
		val url = "jdbc:postgresql://db:5432/$db"
		return DriverManager.getConnection(url, user, password)
	}
}
