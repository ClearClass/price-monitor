SELECT id, partnum, name, price FROM prods JOIN prices ON id=prod_id 
WHERE name IN (SELECT name FROM prods GROUP BY name HAVING COUNT(name)>1) 
ORDER BY name;