create table bot_stat
(
    appName   LowCardinality(String),
    msg_count SimpleAggregateFunction(sum, UInt64),
    users     SimpleAggregateFunction(groupUniqArrayArray, Array(UInt64))
) engine = SummingMergeTree ORDER BY appName;

CREATE MATERIALIZED VIEW bot_stat_mv TO bot_stat
(
    `appName` LowCardinality(String),
    `msg_count` UInt64,
    `users` Array(UInt64)
)
AS
SELECT appName,
       1                               AS msg_count,
       if(type = 'OUT', [user_id], []) AS users
FROM bot_log.bot_log
WHERE user_id > 0;

