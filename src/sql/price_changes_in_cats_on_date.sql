SELECT name AS category_name, (avg_per_in_cat('2021-01-15', cat)).per AS prices_change, 
                              (avg_per_in_cat('2021-01-15', cat)).mnt AS prods_in_avg 
FROM cats ORDER BY prices_change;