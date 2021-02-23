DROP TABLE prices;
DROP TABLE prods;
DROP TABLE cats;
DROP FUNCTION sel_ins(partnum integer, name text, cat integer);
DROP FUNCTION avg_per(IN curr_date date, OUT per numeric, OUT mnt bigint);
DROP FUNCTION avg_per_in_cat(IN curr_date date, IN prod_cat integer, OUT per numeric, OUT mnt bigint);
