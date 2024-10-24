add dependency:
```gradle
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    //https://jitpack.io/#centralhardware/telegram-bot-commons
    implementation("com.github.centralhardware:ktgbotapi-clickhouse-logging-middleware:<lastst ver>")
}
```
use 
```kotlin
telegramBotWithBehaviourAndLongPolling(
        "<TOKEN>",
        CoroutineScope(Dispatchers.IO),
        builder = {
            includeMiddlewares {
                loggingMiddleware("<appName>")
            }
        }) {}
```