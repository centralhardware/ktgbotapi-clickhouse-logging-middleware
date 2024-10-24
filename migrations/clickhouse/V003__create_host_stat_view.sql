create table className_stat
(
    className LowCardinality(String),
    msg_count SimpleAggregateFunction(sum, UInt64)
)
    engine = SummingMergeTree ORDER BY className;

CREATE MATERIALIZED VIEW bot_log.className_stat_mv TO bot_log.className_stat
        (
         `className` LowCardinality(String),
         `msg_count` UInt64
            )
AS SELECT
       className,
       1 AS msg_count
   FROM bot_log.bot_log;



