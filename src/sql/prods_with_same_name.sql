-- товары с одинаковыми наименованиями, но разными артикулами, и их цена на две даты

WITH same_name_prods AS(
        SELECT * FROM prods WHERE name IN (SELECT name FROM prods GROUP BY name HAVING COUNT(name)>1)
     ),
     prices_on_dates AS (
        SELECT * FROM 
           (SELECT prod_id, price AS price1 FROM prices WHERE date='2020-10-15') AS p1 FULL JOIN
           (SELECT prod_id, price AS price2 FROM prices WHERE date='2020-11-15') AS p2 USING(prod_id)
     )
SELECT id, partnum, name, price1, price2 FROM same_name_prods LEFT JOIN prices_on_dates ON id=prod_id
ORDER BY name, id