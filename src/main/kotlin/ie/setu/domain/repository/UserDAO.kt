package ie.setu.domain.repository

import ie.setu.domain.User

/**
 * UserDAO for the Health Tracker App
 */
class UserDAO {

    private val users = arrayListOf(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun getAll() : ArrayList<User>{
        return users
    }

    fun findById(id: Int): User?{
        return users.find {it.id == id}
    }

    fun save(user: User){
        users.add(user)
    }

    fun findByEmail(email: String) :User?{
        return users.find {it.email == email}
    }

    fun delete(id: Int): User? {
        val user = users.find { it.id == id }
        if (user != null) {
            users.remove(user)
        }
        return user
    }

    fun update(id: Int, user: User){
        val foundUser = findById(id)
        foundUser?.email = user.email
        foundUser?.name = user.name
        foundUser?.id = user.id
    }

}