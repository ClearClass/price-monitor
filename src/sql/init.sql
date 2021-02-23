CREATE TABLE cats (
   cat    integer  PRIMARY KEY,
   name   text     NOT NULL UNIQUE
);

INSERT INTO cats VALUES(0, 'Овощи, фрукты, грибы');
INSERT INTO cats VALUES(1, 'Рыба и морепродукты');
INSERT INTO cats VALUES(2, 'Мясо, птица, деликатесы');
INSERT INTO cats VALUES(3, 'Молоко, сыр, яйца');
INSERT INTO cats VALUES(4, 'Соки, воды, напитки');
INSERT INTO cats VALUES(5, 'Кофе, чай, сахар');
INSERT INTO cats VALUES(6, 'Консервы, орехи, соусы');
INSERT INTO cats VALUES(7, 'Хлеб, сладости, снеки');
INSERT INTO cats VALUES(8, 'Макароны, крупы, специи');
INSERT INTO cats VALUES(9, 'Красота, гигиена, бытовая химия');
INSERT INTO cats VALUES(10, 'Бытовая химия и хозтовары');

CREATE TABLE prods (
   id      serial        PRIMARY KEY,
   partnum integer       NOT NULL,
   name    text          NOT NULL,
   cat     integer       NOT NULL,
   CONSTRAINT prods_uniq UNIQUE (partnum, name),
   CONSTRAINT prods_fkey FOREIGN KEY (cat) REFERENCES cats(cat)
);

CREATE TABLE prices (
   date    date           NOT NULL,
   prod_id integer        NOT NULL,
   price   numeric(7,2)   NOT NULL,
   CONSTRAINT prices_pkey PRIMARY KEY (date, prod_id),
   CONSTRAINT prices_fkey FOREIGN KEY (prod_id) REFERENCES prods(id)
);

CREATE FUNCTION sel_ins(partnum integer, name text, cat integer) returns integer AS $$ 
#variable_conflict use_variable
DECLARE
  prod_id integer;
BEGIN
  SELECT id INTO prod_id FROM prods WHERE prods.partnum = partnum AND prods.name = name;
  IF FOUND THEN
    RETURN prod_id;
  ELSE
    INSERT INTO prods(partnum, name, cat) VALUES (partnum, name, cat) RETURNING id INTO prod_id;
    RETURN prod_id;
  END IF;
END
$$ LANGUAGE plpgsql;

-- ------------------------------------------------------------------------------------------------------------------
-- функции, используемые в запросах

CREATE FUNCTION avg_per(curr_date date, OUT per numeric(6,1), OUT mnt bigint) AS $$
WITH prices_change AS(
      SELECT (price2-price1)/price1*100 AS per FROM
         (SELECT prod_id, price AS price1 FROM prices WHERE date = curr_date - INTERVAL '1 month') AS t1 NATURAL JOIN 
         (SELECT prod_id, price AS price2 FROM prices WHERE date = curr_date) AS t2
          WHERE price1<>0
     )
SELECT CAST(AVG(per) AS numeric(6,1)), COUNT(per) FROM prices_change WHERE per > -95 AND per < 900
$$ LANGUAGE SQL;

CREATE FUNCTION avg_per_in_cat(curr_date date, prod_cat int, OUT per numeric(6,1), OUT mnt bigint) AS $$
WITH prices_change AS(
      SELECT (price2-price1)/price1*100 AS per FROM
         (SELECT prod_id, price AS price1 FROM prices WHERE date = curr_date - INTERVAL '1 month') AS t1 NATURAL JOIN 
         (SELECT prod_id, price AS price2 FROM prices WHERE date = curr_date) AS t2 JOIN prods ON prod_id=id 
          WHERE cat=prod_cat AND price1<>0
     )
SELECT CAST(AVG(per) AS numeric(6,1)), COUNT(per) FROM prices_change WHERE per > -95 AND per < 900
$$ LANGUAGE SQL;
