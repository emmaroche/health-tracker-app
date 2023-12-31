package ie.setu.config

import org.jetbrains.exposed.sql.Database

/**
 * Configuration class for managing database connections.
 */
class DbConfig {

    /**
     * Establishes a connection to the database using the provided credentials.
     *
     * @return An instance of [Database] representing the database connection.
     */
    fun getDbConnection(): Database {

        val PGHOST = "flora.db.elephantsql.com"
        val PGPORT = "5432"
        val PGUSER = "owtppwqw"
        val PGPASSWORD = "L874S6WjC9qa1k2r-kxDaZAcFnS4GLV-"
        val PGDATABASE = "owtppwqw"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )

        return dbConfig
    }

}