package app.umcu.api.plugins

import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureDatabases() {
	val dbConnection: Connection = connectToPostgres(embedded = true)
}

fun Application.connectToPostgres(embedded: Boolean): Connection {
	Class.forName("org.postgresql.Driver")
	if (embedded) {
		return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
	} else {
		val url = environment.config.property("postgres.url").getString()
		val user = environment.config.property("postgres.user").getString()
		val password = environment.config.property("postgres.password").getString()
		return DriverManager.getConnection(url, user, password)
	}
}
