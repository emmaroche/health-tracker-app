package ie.setu

import ie.setu.config.DbConfig
import ie.setu.config.JavalinConfig

/**
 * The main entry point for the application.
 */
fun main() {
    DbConfig().getDbConnection()
    JavalinConfig().startJavalinService()
}