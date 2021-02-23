package in.clearclass.dao;

import java.util.Map;

import in.clearclass.entity.Product;

public interface ProductDAO {
	void save(Map<Product, Double> prods, int cat);
}