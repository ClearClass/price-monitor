-- количество принятых товаров по датам

SELECT date AS date_of_sample, COUNT(*) AS received FROM prices GROUP BY date ORDER BY date;