create table user_stat
(
    user_id     UInt64,
    username    SimpleAggregateFunction(anyLast, LowCardinality(Nullable(String))),
    first_name  SimpleAggregateFunction(anyLast, LowCardinality(Nullable(String))),
    second_name SimpleAggregateFunction(anyLast, LowCardinality(Nullable(String))),
    msg_count   SimpleAggregateFunction(sum, UInt64),
    bots        SimpleAggregateFunction(groupUniqArrayArray, Array(String))
)
    engine = SummingMergeTree ORDER BY user_id;

CREATE MATERIALIZED VIEW bot_log.user_stat_mv TO bot_log.user_stat
        (
         `user_id` UInt64,
         `username` LowCardinality(Nullable(String)),
         `first_name` LowCardinality(Nullable(String)),
         `second_name` LowCardinality(Nullable(String)),
         `msg_count` UInt64,
         `bots` Array(String)
            )
AS SELECT
       user_id,
       if(username = '', NULL, username) AS username,
       if(first_name = '', NULL, first_name) AS first_name,
       if(last_name = '', NULL, last_name) AS second_name,
       1 AS msg_count,
       [appName] AS bots
   FROM bot_log.bot_log
   WHERE user_id > 0;


