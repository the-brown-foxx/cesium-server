package com.thebrownfoxx.auth

import com.thebrownfoxx.auth.logic.hash
import com.thebrownfoxx.auth.models.Admin
import com.thebrownfoxx.auth.models.Base64
import com.thebrownfoxx.auth.models.Hash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedAdminService(private val database: Database): AdminService {
    object Admins: Table() {
        private val id = integer("id").autoIncrement()
        val passwordHash = varchar("password_hash", length = 512)
        val passwordSalt = varchar("password_salt", length = 64)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        val scope = CoroutineScope(Dispatchers.IO)

        transaction(database) {
            SchemaUtils.create(Admins)
            scope.launch {
                dbQuery {
                    val (hash, salt) = "password".hash()
                    Admins.insert {
                        it[passwordHash] = hash.value
                        it[passwordSalt] = salt.value
                    }
                }
            }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun get(): Admin = dbQuery {
        Admins.selectAll().last().let {
            Admin(
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
                it[passwordHash] = hash.value
                it[passwordSalt] = salt.value
            }
        }
    }
}