CREATE TABLE prods (
   id      serial        PRIMARY KEY,
   partnum integer       NOT NULL,
   name    text          NOT NULL,
   cat     integer       NOT NULL,
   CONSTRAINT prods_uniq UNIQUE (partnum, name)
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