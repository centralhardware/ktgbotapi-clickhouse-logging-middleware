# KTGBotAPI ClickHouse Logging Middleware

A middleware for [KTGBotAPI](https://github.com/InsanusMokrassar/ktgbotapi) that logs all Telegram bot requests and responses to ClickHouse database. This allows for comprehensive analytics and monitoring of your Telegram bot's activity.

## Features

- Logs all incoming and outgoing messages to ClickHouse
- Automatically extracts user information from messages
- Provides several materialized views for analytics:
  - Bot statistics (message count, unique users)
  - Host statistics (message count per server)
  - Class statistics (message count per request/response type)
  - User statistics (message count, user details, bot interactions)
- Configurable application name for multi-bot environments

## Installation

### Gradle

Add JitPack repository and dependency to your build.gradle.kts:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.centralhardware:ktgbotapi-clickhouse-logging-middleware:latest-version")
}
```

### Environment Variables

Set up the ClickHouse connection URL:

```shell
export CLICKHOUSE_URL=jdbc:ch:http://<username>:<password>@<host>:8123/<database>
```

Optionally, you can set a custom hostname:

```shell
export HOST=your-custom-hostname
```

If not set, the system's hostname will be used.

## Usage

Add the middleware to your Telegram bot:

```kotlin
// Add the middleware to your bot configuration
telegramBotWithBehaviourAndLongPolling(
    "<BOT_TOKEN>",
    CoroutineScope(Dispatchers.IO),
    builder = {
        includeMiddlewares {
            clickhouseLogging("<appName>")
        }
    }
) {
    // Your bot logic here
}.second.join()
```

Replace `<appName>` with a unique identifier for your bot. This will be used in the database to distinguish between different bots.

## Database Schema

The middleware creates the following tables and views in ClickHouse:

### Main Table

- `bot_log` - Stores all raw log entries
  - `date_time` - Timestamp of the log entry
  - `type` - Type of log (IN/OUT)
  - `data` - Raw JSON data
  - `appName` - Application name
  - `className` - Class name of the request/response
  - `username`, `first_name`, `last_name` - Extracted user information
  - `user_id` - Extracted user ID
  - `host` - Hostname where the application is running

### Materialized Views

- `bot_stat` - Statistics per bot
  - `appName` - Application name
  - `msg_count` - Message count
  - `users` - Array of unique user IDs

- `className_stat` - Statistics per class
  - `className` - Class name
  - `msg_count` - Message count

- `host_stat` - Statistics per host
  - `host` - Hostname
  - `msg_count` - Message count

- `user_stat` - Statistics per user
  - `user_id` - User ID
  - `username`, `first_name`, `second_name` - User information
  - `msg_count` - Message count
  - `bots` - Array of bots the user has interacted with

## License

This project is licensed under the terms of the license provided in the repository.
