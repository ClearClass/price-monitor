WITH last_date AS (SELECT date FROM prices ORDER BY date DESC LIMIT 1),
     prods_with_prices_per AS(
      SELECT name, price1, price2, price2-price1 AS delta, cat, CAST((price2-price1)/price1*100 AS numeric(6,2)) AS per FROM
         (SELECT prod_id, price AS price1 FROM prices WHERE date = (SELECT * FROM last_date)- INTERVAL '1 month') AS t1 NATURAL JOIN 
         (SELECT prod_id, price AS price2 FROM prices WHERE date = (SELECT * FROM last_date)) AS t2 JOIN prods ON prod_id=id
          WHERE cat>-1 AND price1<>0
     )
SELECT * FROM prods_with_prices_per WHERE per > -95 AND per < 900
ORDER BY per