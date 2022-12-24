CREATE TABLE IF NOT EXISTS statistics
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    application VARCHAR   NOT NULL,
    uri         VARCHAR   NOT NULL,
    ip          VARCHAR   NOT NULL,
    request_time   TIMESTAMP NOT NULL,
    CONSTRAINT pk_statistics_id PRIMARY KEY (id)
)