package in.clearclass.catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.clearclass.entity.Product;

// ... задача получения одной страницы
class GetPageTask implements Callable<Map<Product, Double>>{
	private String cat;
	private Integer n; // номер получаемой страницы
	
	GetPageTask(String cat, Integer n) {
		this.cat = cat;
		this.n = n;
	}
	
	@Override
	public Map<Product, Double> call() throws Exception {
		String url = OnlineCatalog.url;
		Document page = Jsoup.connect(url + cat).data("page", n.toString()).get();
		Elements prods = page.select("div.xf-product.js-product");
		Map<Product, Double> prodsMap = new HashMap<>(30);
		
		for (Element prod : prods){
			if(prod.attr("data-owox-product-price").isEmpty()) continue;
			prodsMap.put(new Product(prod.attr("data-owox-product-id"), prod.attr("data-owox-product-name")), 
					Double.parseDouble(prod.attr("data-owox-product-price")));
		}
		return prodsMap;
	}
}