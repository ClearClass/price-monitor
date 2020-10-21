## � �������

2020 ��� ������������� ��������� ������������ ����������� �����. ������� � ������� 2020 �., ����� ���� ����� ��� �� 20-30% �� ��������� ������� � ����. ��, �������� �� ���, ����������� ���������� �������, ��� �������� � ������ �� ��������� "������� 4%", ��� "������ ������������� � ������� ���� �������, ��� ������� � ���������".

��� ���� ����� �������� ����������� ������ ��������������� ��������, ��� ���������� ��������� ������. ���������� ������������ ������� � ���-�������� �������� ������������ ������������, � ��������� �� html-�������� �������� ������� � ���, ������� ����������� � ��.

## �������� �������

### ��������� ��

����� �� ������� �� ���� ������:
    
                         +--------------+
    +----------+         | prices       |
    | prods    |         +--------------+
    +----------+         | date         | -PK
    | id (PK)  +-------->+ prod_id (FK) | -PK
    | partnum  |         | price        |
    | name     |         +--------------+
    | cat      |
    +----------+
    
������� `prods` �������� ������ ������ ���� �������, �� ������� ������� ����. ���� `id` - ������������� ������������� ������ (��������� ����), ������������ ���� (��� `serial`). ���� `partnum` � `name` - ������� � ������������ ������ � ������������ � ��������� (���������� ����). ���� `cat` - �������� ����� ��������� ������.

������� `prices` �������� ������� ��� ������� �� ���������� �����: `date` - ���� �������, `prod_id` - ������������� ������ (������� ���� � ������� `prods`), `price` - ����. ������������ ��������� ��������� ���� �� �������� (`date`, `prod_id`).

����� ����, ������� ������� `sel_ins(partnum integer, name text, cat integer)`, ������� ���� ���������� id ������������� ������, ���� ������������ ������� � ������������ id.

### �������� ������

����� `OnlineCatalog` ������������ ��� ���������� ���������� � ������� �� ������-��������, � ���������� �� ������ ���������� �������� html-������� *Jsoup*. � ������ ���������� ��� ����������� ������:

* `int numOfPages(String cat)` - ���������� ���������� ������� �������� ��� �������� ���������. ��� ��������� ������������ ����� ����� ���� URL, �� �������� ���������� ������ ������. ������ ���� �������� � ������ main() ������ Main. ������� ������������ ����������� �������� ���������� �����, ������� ����������� � ������� �� � ���� `cat`.

* `Map<Product, Double> getPages(String cat, int m, int n)` - ���������� �������� ������� � ��� ��� �������� ���������, ��� `n` - ���������� ������������� ������� (1..20), `m` - � ����� �������� ��������. ��� �������� ������������� ������������ (� ������������� ������).

����� `GetPageTask` ������������� ������ ��������� ����� ��������, �������� ��������� `Callable<Map<Product, Double>>`. ����� ������ �� ��������� ������ ������ `catalog`.

����� `dao` �������� ����� � ��������� dao-�������, ����������� ������������ ����� ���������� ����������� `Map<>` � ������� ��. ���������� ��� �������� (�� ����������� ������� ���������) �� ������ `JdbcTemplate` � `TransactionTemplate` (���� ����� ������ `save()` ������������� ����� ���������� � ��).

����� `entity` �������� ���������� ����� `Product`, �������������� ������������ ������ � ��� �������, ���������� �� ��������.

� ������ main() �������� ������ ���� ��������� ������� � ������������ � URL ������� ������-��������. ��� ������ ��������� (� �����) �������������� ������ ������� �������� (������� - �� 20, ����� - �������), ���������� ����������� ��������� (������� ��� ����� ���� ���������� � ��), � �������� ����������� � �� ����� dao-������.

### ���������� �������� sql-�������� *src/sql*

* `schema.sql` - �������� ����� ��;
* `drop.sql` - �������� ����� ��;
* `total_received.sql` - ���������� �������� ������� �� �����;
* `view_double_products.sql` - �������� ������� � ����������� �������������� (�� ������� ����������).