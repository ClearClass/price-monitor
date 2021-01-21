package in.clearclass.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import in.clearclass.Main;
import in.clearclass.entity.Product;

public class OnlineCatalog {
	static final String url = "https://www.perekrestok.ru/catalog/";
	private static final ExecutorService es = Executors.newFixedThreadPool(Main.N);
		
	public static int numOfPages(String cat) {
		try {
			Document page = Jsoup.connect(url + cat).data("page", "2").get();
			String st = page.select("[name=description]").first().attr("content");
			return Integer.parseInt(st.substring(st.indexOf("Страница 2 из")+14).replace(".", ""));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	// запрос страниц: m - с какой начинать, n - количество страниц
	public static Map<Product, Double> getPages(String cat, int m, int n){
		if(n>Main.N) throw new RuntimeException("количество страниц n превышает " + Main.N);
		
		// результирующий список товаров (на каждой странице - 30 товаров)
		Map<Product, Double> prods = new LinkedHashMap<>(30*n);
		try {
			List<Future<Map<Product, Double>>> pageTasks = new ArrayList<>(n);
			for (int i=m; i<=m+n-1; i++)
				pageTasks.add(es.submit(new GetPageTask(cat, i)));
	
			for (Future<Map<Product, Double>> pageTask : pageTasks)
				prods.putAll(pageTask.get());
			
		} catch (Exception e) {
			es.shutdown();
			throw new RuntimeException(e);
		}
		return prods;
	}
	
	public static void shutdown(){
		es.shutdown();
	}
}