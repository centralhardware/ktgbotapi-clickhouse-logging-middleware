package me.centralhardware.telegram

import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

suspend fun main() {
    telegramBotWithBehaviourAndLongPolling(
        System.getenv("BOT_TOKEN"),
        CoroutineScope(Dispatchers.IO),
        builder = {
            includeMiddlewares {
                loggingMiddleware("integrationTest")
            }
        }
    ) {

    }.second.join()
}