## � �������

2020 ��� ������������� ��������� ������������ ����������� �����. ������� � ������� 2020 �., ����� ���� ����� ��� �� 20-30% �� ��������� � ������� � ����. ������������ � ���� ��������� ������ ��������� ��� �� ������ �������� ������� � ����������� ������ ����� ��������������� �������. ������ ����������� ����������, ������� ��� ������������ ��� ������� ����������� ������ ��������, �� ������ �������� �������� ������� ��� �� ��������� ���������. 

��� ���� ����� �������� ����������� ������ ��������������� ��������, ��� ���������� ��������� ������. ���������� ������������ ������� � ���-�������� �������� ������������ ������������, � �������� (�� html-��������) �������� ������� � ���, ������� ����������� � ���� ������. �����, � �������������� ����������� sql-��������, �������������� ����������� ���������� �������� ���.

## �������� �������

### ����� ��

����� �� ������� �� ���� ������:

                                          +--------------+
                     +----------+         | prices       |
                     | prods    |         +--------------+
                     +----------+         | date         | +PK
    +--------+       | id (PK)  +-------->+ prod_id (FK) | +PK
    | cats   |       | partnum  |         | price        |
    +--------+       | name     |         +--------------+
    | cat(PK)+------>+ cat      |
    | name   |       +----------+
    +--------+
    
������� `cats` �������� ������ ��������� �������, ���������� ��� ����: `name` - ������������ ���������, � `cat`- �������� ���������� ����� ��������� (��������� ���� ������� `cats`). ���������� ������� ���������� ��� �������� ����� ��.
    
������� `prods` �������� ������ ������ ���� �������, �� ������� ������� ����. ���� `id` - ������������� ������������� ������ (��������� ����), ������������ ���� (��� `serial`). ���� `partnum` � `name` - ������� � ������������ ������ �������� �������� (���������� ����). ���� `cat` - ����� ��������� ������, ���������� ������� ������ �� ��������� � ������� `cats`.

������� `prices` �������� ������� ��� ������� �� ���������� �����: `date` - ���� �������, `prod_id` - ������������� ������ (������� ���� � ������� `prods`), `price` - ����. ������������ ��������� ��������� ���� �� �������� (`date`, `prod_id`).

����� ����, ������� ������� `sel_ins(partnum integer, name text, cat integer)`, ������� ���� ���������� id ������������� ������ �� ������� `prods`, ���� ������������ ������� � ������������ id.

### �������� ������

����� `OnlineCatalog` ������������ ��� ���������� ���������� � ������� �� ������-��������, � ���������� �� ������ ���������� �������� html-������� *Jsoup*. � ������ ���������� ��� ����������� ������:

* `int numOfPages(String cat)` - ���������� ���������� ������� �������� ��� �������� ���������. ��� ��������� `cat` ������������ ����� ����� ���� URL, �� �������� ���������� ������ ������. ������ ���� �������� � ������ `main()`.

* `Map<Product, Double> getPages(String cat, int m, int n)` - ���������� �������� ������� � ��� ��� �������� ��������� `cat`, ��� `n` - ���������� ������������� ������� (1..20), `m` - � ����� �������� ��������. ��� �������� ������������� ������������ (� ������������� ������).

����� `GetPageTask` ������������� ������ ��������� ����� ��������, �������� ��������� `Callable<Map<Product, Double>>`. ����� ������ �� ��������� ������ ������ `catalog`. ����� ������������ 5 ������� ��������� ��������; ��� ��������� ������ ������������ ����������.

����� `dao` �������� ����� � ��������� dao-�������, ����������� ������������ ����� ���������� ����������� `Map<Product, Double>` � ������� ��. ���������� ��� �������� (�� ����������� ������� ���������) �� ������ `JdbcTemplate` � `TransactionTemplate` (���� ����� ������ `save()` ������������� ����� ���������� � ��).

����� `entity` �������� ���������� ����� `Product`, �������������� ������������ ������ � ��� �������, ���������� �� ��������.

� ������ `main()` �������� ������ ���� ��������� ������� � ������������ � URL ������� ������-��������. ��� ������ ��������� (� �����) �������������� ������ ������� �������� (������� - �� 20, ����� - �������), ���������� ����������� ��������� (������� ��� ����� ���� ��������� � ��), � �������� ����������� � �� ����� dao-������.

### ���������� �������� sql-�������� *src/sql*

* `schema.sql` - �������� ����� ��;

* `drop.sql` - �������� ����� ��;

* `total_received_on_dates.sql` - ���������� �������� ������� (�� ���) �� �����;

* `prods_with_same_name.sql` - ������ �������, ������� ���������� ��������, �� ��������� �������. ����� ������ ������ ��������� �������, ���������, � ����� �������, ����� �������������� ���� � �������� ����� �������, � � ������ �������, ���������� ���������� ����������, ��������� �� ���� �������, ��� �� ��������� ��� �����. ������ ��������� �������� ������������� ��������, ���������, �������� �� ��������� ���������� ������ �������, �� ������� ����������� ������������ ��������� ������.

* `prods_with_same_partnum.sql` - ������ �������, ������� ���������� ��������, �� ��������� ������������. ��� �������, ����� ������� ���������� � ���������� �������������� ������� � ������� ��������� �������, �.�. �� ���� ������������ ����� ���� � ��� �� �����. ���, � ��������, ��������� ������������ � �������� ����������� �������������� ������ ��� `partnum`. ������ � ����� �� ��������� ��������, ����� ����� ����� �������� ������� �������. ������� � ���������� � �������� �������������� ������ ������������ ��� �� ��� `id`. ���������� �� `partnum` ����� ��������, ������ ��� ����� ���������� ����� ����� ���������� �������, ������� ����� ������������� ������ (`name`) �� ������� ���������� �������������� ������.

* `price_changes_for_all_prods.sql` - ���������� ��� �� ���� ������� �� ��� ������������� ����. ������, ��� ������� ��������� ���� ��������� ����� 900% ��� ����� -95%, ��������� ���������� � � ���������� ������� �� ����������.

* `avg_per_on_date.sql` - ������� ��� ������� �������� �������� ��� �� ���� ����� �� �������� ���� (�� ���� ���������� �������). � �������� ��������� ��������� ����� ������������ ���������� ����� (�������), �� ������� ����������� ����������.

* `prices_change_on_all_dates.sql` - ������, ������� ���������� ���������� ������� ��� ������� �������� �������� ��� (�� �����) �� ���� ����� ���������.

* `avg_per_on_date_in_cat.sql` - ������� ��� ������� �������� �������� ��� �� ���� ����� �� �������� ���� � �������� ���������. � �������� ��������� ��������� ����� ������������ ���������� ����� (�������), �� ������� ����������� ����������.

* `price_changes_in_cats_on_date.sql` - ������, ������� ���������� ���������� ������� ��� ������� �������� �������� ��� (�� �����) �� ������ ��������� � ����������� (�� �������� ����).

* `prices_change_on_full_time.sql` - ������� ������� ��� �� ��� ����� ��������� (�� ���� ���������� �������).

### ������� ������

* ������ 1.0 - ������� ������.
* ������ 1.1 - ��������� ��������� sql-�������; � ������ `GetPageTask` ������� 5 ������� ��� ��������� ��������.
