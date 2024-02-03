package app.umcu.api.database

class DatabaseInitializationException(override val message: String? = null) :
	Exception(message ?: "The database is already initialized.")
