CREATE FUNCTION avg_per(curr_date date, OUT per numeric(6,1), OUT mnt bigint) AS $$
WITH prices_change AS(
      SELECT (price2-price1)/price1*100 AS per FROM
         (SELECT prod_id, price AS price1 FROM prices WHERE date = curr_date - INTERVAL '1 month') AS t1 NATURAL JOIN 
         (SELECT prod_id, price AS price2 FROM prices WHERE date = curr_date) AS t2
          WHERE price1<>0
     )
SELECT CAST(AVG(per) AS numeric(6,1)), COUNT(per) FROM prices_change WHERE per > -95 AND per < 900
$$ LANGUAGE SQL;