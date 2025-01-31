package me.centralhardware.telegram

import com.clickhouse.jdbc.ClickHouseDataSource
import com.google.gson.Gson
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.info
import dev.inmo.micro_utils.common.Warning
import dev.inmo.tgbotapi.bot.ktor.middlewares.TelegramBotMiddlewaresPipelinesHandler
import dev.inmo.tgbotapi.requests.GetUpdates
import dev.inmo.tgbotapi.requests.bot.GetMe
import dev.inmo.tgbotapi.requests.webhook.DeleteWebhook
import java.net.InetAddress
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.ArrayList
import javax.sql.DataSource
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotliquery.queryOf
import kotliquery.sessionOf

private fun save(data: String, clazz: KClass<*>, income: Boolean, appName: String) {
    sessionOf(dataSource)
        .execute(
            queryOf(
                """
                              INSERT INTO bot_log.bot_log
                              ( date_time,
                                appName,
                                type,
                                data,
                                className,
                                host
                              )
                              VALUES (
                                :date_time,
                                :appName,
                                :type,
                                :data,
                                :className,
                                :host)
            """,
                mapOf(
                    "date_time" to LocalDateTime.now(),
                    "appName" to appName,
                    "type" to if (income) "IN" else "OUT",
                    "data" to data,
                    "className" to clazz.simpleName,
                    "host" to (System.getenv("HOST") ?: InetAddress.getLocalHost().hostName),
                ),
            )
        )
}

fun <T : Any> getSerializer(data: T): SerializationStrategy<T> {
    val property = data::class.declaredMemberProperties.find { it.name == "requestSerializer" }

    return (property!!.call(data)) as SerializationStrategy<T>
}

private val dataSource: DataSource =
    try {
        ClickHouseDataSource(System.getenv("CLICKHOUSE_URL"))
    } catch (e: SQLException) {
        throw RuntimeException(e)
    }

@OptIn(Warning::class)
fun TelegramBotMiddlewaresPipelinesHandler.Builder.clickhouseLogging(appName: String) {
    addMiddleware {
        val gson = Gson()
        val nonstrictJsonFormat = Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = true
            encodeDefaults = true
        }
        doOnRequestReturnResult { result, request, _ ->
            if (request !is GetUpdates && request !is DeleteWebhook && request !is GetMe) {
                runCatching {
                        save(
                            nonstrictJsonFormat
                                .encodeToJsonElement(getSerializer(request), request)
                                .toString(),
                            request::class,
                            false,
                            appName,
                        )
                    }
                    .onFailure { KSLog.info("Failed to save request ${request::class.simpleName}") }
            }

            if (result.getOrNull() == null) return@doOnRequestReturnResult null
            if (request is GetUpdates) {
                (result.getOrNull() as ArrayList<Any>).forEach {
                    save(gson.toJson(it), it::class, true, appName)
                }
            } else if (request !is DeleteWebhook && request !is GetMe && result.getOrNull() !is ByteArray) {
                save(gson.toJson(result), result.getOrNull()!!::class, true, appName)
            }
            null
        }
    }
}
