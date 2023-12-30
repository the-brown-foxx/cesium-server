package com.thebrownfoxx.auth

import com.thebrownfoxx.auth.logic.hash
import com.thebrownfoxx.auth.models.Base64
import com.thebrownfoxx.auth.models.Hash
import com.thebrownfoxx.auth.models.SavedAdmin
import com.thebrownfoxx.auth.models.UnsavedAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedAdminService(private val database: Database): AdminService {
    object Admins: Table() {
        val id = integer("id").autoIncrement()
        val username = varchar("username", length = 20).uniqueIndex()
        val name = varchar("name", length = 50)
        val passwordHash = varchar("password_hash", length = 512)
        val passwordSalt = varchar("password_salt", length = 64)
        val master = bool("master").default(false)
        val locked = bool("locked").default(false)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        val scope = CoroutineScope(Dispatchers.IO)

        transaction(database) {
            SchemaUtils.create(Admins)
            scope.launch {
                add(
                    UnsavedAdmin(
                        username = "justine",
                        name = "Justine Manalansan",
                        passwordHash = "password".hash(),
                        master = true,
                    )
                )
                add(
                    UnsavedAdmin(
                        username = "jericho",
                        name = "Jericho Diaz",
                        passwordHash = "12345678".hash(),
                    )
                )
                add(
                    UnsavedAdmin(
                        username = "jonel",
                        name = "Jonel David",
                        passwordHash = "qwerty".hash(),
                    )
                )
            }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun getAll(): List<SavedAdmin> = dbQuery {
        Admins.selectAll()
            .map {
                SavedAdmin(
                    id = it[Admins.id],
                    username = it[Admins.username],
                    name = it[Admins.name],
                    passwordHash = Hash(
                        value = Base64(it[Admins.passwordHash]),
                        salt = Base64(it[Admins.passwordSalt]),
                    ),
                    master = it[Admins.master],
                    locked = it[Admins.locked],
                )
            }
    }

    override suspend fun get(id: Int): SavedAdmin? = dbQuery {
        Admins.select { Admins.id eq id }
            .map {
                SavedAdmin(
                    id = it[Admins.id],
                    username = it[Admins.username],
                    name = it[Admins.name],
                    passwordHash = Hash(
                        value = Base64(it[Admins.passwordHash]),
                        salt = Base64(it[Admins.passwordSalt]),
                    ),
                    master = it[Admins.master],
                    locked = it[Admins.locked],
                )
            }
            .singleOrNull()
    }

    override suspend fun getByUsername(username: String): SavedAdmin? = dbQuery {
        Admins.select { Admins.username eq username }
            .map {
                SavedAdmin(
                    id = it[Admins.id],
                    username = it[Admins.username],
                    name = it[Admins.name],
                    passwordHash = Hash(
                        value = Base64(it[Admins.passwordHash]),
                        salt = Base64(it[Admins.passwordSalt]),
                    ),
                    master = it[Admins.master],
                    locked = it[Admins.locked],
                )
            }
            .singleOrNull()
    }

    override suspend fun add(admin: UnsavedAdmin): SavedAdmin = dbQuery {
        val savedAdmin = Admins.insert {
            it[username] = admin.username
            it[name] = admin.name
            it[passwordHash] = admin.passwordHash.value.value
            it[passwordSalt] = admin.passwordHash.salt.value
        }
        SavedAdmin(
            id = savedAdmin[Admins.id],
            username = savedAdmin[Admins.username],
            name = savedAdmin[Admins.name],
            passwordHash = Hash(
                value = Base64(savedAdmin[Admins.passwordHash]),
                salt = Base64(savedAdmin[Admins.passwordSalt]),
            ),
            master = savedAdmin[Admins.master],
            locked = savedAdmin[Admins.locked],
        )
    }

    override suspend fun update(admin: SavedAdmin) {
        dbQuery {
            Admins.update({ Admins.id eq admin.id }) {
                it[username] = admin.username
                it[name] = admin.name
                it[passwordHash] = admin.passwordHash.value.value
                it[passwordSalt] = admin.passwordHash.salt.value
                it[master] = admin.master
                it[locked] = admin.locked
            }
        }
    }

    override suspend fun delete(id: Int) {
        dbQuery {
            Admins.deleteWhere { Admins.id eq id }
        }
    }
}