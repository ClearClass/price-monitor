package in.clearclass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import in.clearclass.catalog.OnlineCatalog;
import in.clearclass.dao.ProductDAO;
import in.clearclass.dao.ProductDAOImpl;
import in.clearclass.entity.Product;

public class Main{
	public static final int N = 20; // количество одновременно запрашиваемых страниц (потоков)
	public static void main(String[] args) {

		// список категорий как часть url
		Map<Integer, String> cats = new LinkedHashMap<>();
		cats.put(0,  "/1301/ovoschi-frukty-griby");
		cats.put(1,  "/1304/ryba-i-moreprodukty");
		cats.put(2,  "/1307/myaso-ptitsa-delikatesy");
		cats.put(3,  "/1303/moloko-syr-yaytsa");
		cats.put(4,  "/1312/soki-vody-napitki");
		cats.put(5,  "/1302/kofe-chay-sahar");
		cats.put(6,  "/1310/konservy-orehi-sousy");
		cats.put(7,  "/1309/hleb-sladosti-sneki");
		cats.put(8,  "/1300/makarony-krupy-spetsii");
		cats.put(9,  "/1306/krasota-gigiena-bytovaya-himiya");
		cats.put(10, "/2348/bytovaya-himiya-i-hoztovary");
		cats.put(11, "/4019/sladosti-i-sneki"); // merges with cat.7 in dao
		
		// dao для записи в БД
		ProductDAO dao = ProductDAOImpl.getInst();
		
		// перечень продуктов и их цен, которые уже были отправлены в БД
		Map<Product, Double> saved = new HashMap<>();
		
		for (Entry<Integer, String> cat : cats.entrySet()) { // по всем категориям ..
			int num = OnlineCatalog.numOfPages(cat.getValue());
			int n = num/N;
			int m = num%N;
			for (int j = 0; j < n; j++){
				System.out.println("cat: " + cat.getKey() + ", getting pages: " + (1+N*j) + "-" + N*(j+1) + " of " + num);
				Map<Product, Double> prods = OnlineCatalog.getPages(cat.getValue(), 1+N*j, N);
				prods.keySet().removeAll(saved.keySet());
				dao.save(prods, cat.getKey());
				saved.putAll(prods);
			}
			
			if(m!=0){ // остаток
				System.out.println("cat: " + cat.getKey() + ", getting pages: " + (1+N*n) + "-" + (N*n+m) + " of " + num);
				Map<Product, Double> prods = OnlineCatalog.getPages(cat.getValue(), 1+N*n, m);
				prods.keySet().removeAll(saved.keySet());
				dao.save(prods, cat.getKey());
				saved.putAll(prods);
			}
		}
		
		OnlineCatalog.shutdownThreads();
	}
}