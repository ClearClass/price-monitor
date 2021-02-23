WITH last_date AS (SELECT date FROM prices ORDER BY date DESC LIMIT 1),
      received AS (SELECT cat, COUNT(*) AS amount FROM prices JOIN prods ON id=prod_id 
                   WHERE date = (SELECT * FROM last_date) GROUP BY cat)

SELECT cat, name AS full_category_name, amount FROM received NATURAL RIGHT JOIN cats ORDER BY cat;