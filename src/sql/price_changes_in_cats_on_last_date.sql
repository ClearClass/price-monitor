WITH last_date AS (SELECT date FROM prices ORDER BY date DESC LIMIT 1)
SELECT name AS category_name, (avg_per_in_cat((SELECT * FROM last_date), cat)).per AS prices_change, 
                              (avg_per_in_cat((SELECT * FROM last_date), cat)).mnt AS prods_in_avg 
FROM cats ORDER BY prices_change;