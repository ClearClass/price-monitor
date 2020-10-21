package in.clearclass;

import java.util.HashMap;
import java.util.Map;

import in.clearclass.catalog.OnlineCatalog;
import in.clearclass.dao.ProductDAO;
import in.clearclass.dao.ProductDAOImpl;
import in.clearclass.entity.Product;

public class Main{
	public static final int N = 20; // количество одновременно запрашиваемых страниц (потоков)
	public static void main(String[] args) {

		// список категорий как часть url
		String[] cats = {"ovoschi-frukty-griby",
						 "ryba-i-moreprodukty",
						 "myaso-ptitsa-delikatesy",
						 "moloko-syr-yaytsa",
						 "soki-vody-napitki",
						 "kofe-chay-sahar",
						 "konservy-orehi-sousy",
						 "hleb-sladosti-sneki",
						 "makarony-krupy-spetsii",
						 "krasota-gigiena-bytovaya-himiya",
						 "bytovaya-himiya-i-hoztovary"};
		
		// dao для записи в БД
		ProductDAO dao = ProductDAOImpl.getInst();
		
		// перечень продуктов и их цен, которые уже были отправлены в БД
		Map<Product, Double> saved = new HashMap<>();
		
		for (int i=0; i<cats.length; i++) {
			int num = OnlineCatalog.numOfPages(cats[i]);
			int n = num/N;
			int m = num%N;
			for (int j = 0; j < n; j++){
				System.out.println("cat: " + i + ", getting pages: " + (1+N*j) + "-" + N*(j+1) + " of " + num);
				Map<Product, Double> prods = OnlineCatalog.getPages(cats[i], 1+N*j, N);
				prods.keySet().removeAll(saved.keySet());
				dao.save(prods, i);
				saved.putAll(prods);
			}
			System.out.println("cat: " + i + ", getting pages: " + (1+N*n) + "-" + (N*n+m) + " of " + num);
			Map<Product, Double> prods = OnlineCatalog.getPages(cats[i], 1+N*n, m);
			prods.keySet().removeAll(saved.keySet());
			dao.save(prods, i);
			saved.putAll(prods);
		}
		OnlineCatalog.shutdown();
	}
}