add environment variable;
```shell
export CLICKHOUSE_URL=jdbc:ch:http://<username>:<password>@<host>:8123/<database>
```
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
add middleware in Behaviour builder 
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