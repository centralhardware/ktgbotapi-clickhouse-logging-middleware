create table host_stat
(
    host      LowCardinality(String),
    msg_count SimpleAggregateFunction(sum, UInt64)
)
    engine = SummingMergeTree ORDER BY host;

CREATE MATERIALIZED VIEW bot_log.host_stat_mv TO bot_log.host_stat
        (
         `host` LowCardinality(String),
         `msg_count` UInt64
            )
AS SELECT
       host,
       1 AS msg_count
   FROM bot_log.bot_log;

