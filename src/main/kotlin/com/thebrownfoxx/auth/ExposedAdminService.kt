package com.thebrownfoxx.auth

import com.thebrownfoxx.auth.logic.hash
import com.thebrownfoxx.models.auth.Admin
import com.thebrownfoxx.models.auth.Base64
import com.thebrownfoxx.models.auth.Hash
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

class ExposedAdminService(database: Database): AdminService {
    object Admins: Table() {
        private val id = integer("id").autoIncrement()
        val passwordKey = long("password_key")
        val passwordHash = varchar("password_hash", length = 512)
        val passwordSalt = varchar("password_salt", length = 64)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Admins)

            if (Admins.selectAll().toList().isEmpty()) {
                Admins.insert {
                    val (hash, salt) = "password".hash()
                    it[passwordKey] = 1
                    it[passwordHash] = hash.value
                    it[passwordSalt] = salt.value
                }
            }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun get(): Admin = dbQuery {
        Admins.selectAll().last().let {
            Admin(
                passwordKey = it[Admins.passwordKey],
                passwordHash = Hash(
                    value = Base64(it[Admins.passwordHash]),
                    salt = Base64(it[Admins.passwordSalt]),
                ),
            )
        }
    }

    override suspend fun update(newPasswordHash: Hash) {
        val (hash, salt) = newPasswordHash
        dbQuery {
            Admins.update {
                it[passwordKey] = Random.nextLong()
                it[passwordHash] = hash.value
                it[passwordSalt] = salt.value
            }
        }
    }
}