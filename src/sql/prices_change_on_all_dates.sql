WITH dates AS (
      SELECT DISTINCT date FROM prices WHERE date <> (SELECT date FROM prices ORDER BY date LIMIT 1) ORDER BY date
     ) 
SELECT date AS date_of_sample, (avg_per(date)).per AS prices_change, (avg_per(date)).mnt AS prods_in_avg FROM dates;