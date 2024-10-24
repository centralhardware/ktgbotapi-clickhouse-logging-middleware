create table bot_log
(
    date_time  DateTime,
    type       LowCardinality(String),
    data       String,
    appName    LowCardinality(String),
    className  LowCardinality(String),
    username   String          default extract(data, '"username":"([^"]+)'),
    first_name String          default extract(data, '"firstName":"([^"]+)'),
    last_name  String          default extract(data, '"lastName":"([^"]*)'),
    user_id    Nullable(Int64) default toInt64OrNull(extract(data, '"(?:id|chat_id)":(-?\\d+)')),
    host       LowCardinality(Nullable(String))
)
    engine = MergeTree ORDER BY date_time;

