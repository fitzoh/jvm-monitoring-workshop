--Some simple examples demonstrating SQL queries which behave similar to PromQL selectors

SELECT *
FROM http_server_requests
--http_server_requests

SELECT *
FROM http_server_requests
WHERE method = 'GET'
--http_server_requests{method="GET"}

SELECT *
FROM http_server_requests
WHERE method IN ('POST', 'PUT')
--http_server_requests{method=~"POST|PUT"}

SELECT *
FROM http_server_requests
WHERE uri NOT LIKE '/rest/%'
--http_server_requests{method!~"/rest/.*"}

SELECT *
FROM http_server_requests
WHERE method != 'GET'
AND uri LIKE '/rest/%'
--http_server_requests{method!="GET",uri=~"/rest/.*"}
