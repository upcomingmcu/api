package app.umcu.api

class DatabaseInitializationException(override val message: String? = null) :
	Exception(message ?: "The database is already initialized.")
