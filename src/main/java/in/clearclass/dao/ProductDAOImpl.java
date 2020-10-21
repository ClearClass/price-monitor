package in.clearclass.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import in.clearclass.entity.Product;

public class ProductDAOImpl implements ProductDAO {
	private static final ProductDAO inst = new ProductDAOImpl();
	public static ProductDAO getInst(){
		return inst;
	}
	
	private JdbcTemplate jdbcTemplate;
	private TransactionTemplate txTemplate;
	
	private ProductDAOImpl(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/prices");
		dataSource.setUsername("postgres");
		dataSource.setPassword("post");
		jdbcTemplate = new JdbcTemplate(dataSource);
		DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		txTemplate = new TransactionTemplate(txManager);
	}

	@Override
	public void save(Map<Product, Double> prods, Integer cat) {
		txTemplate.execute(status->{
			String query = "INSERT INTO prices VALUES(?,?,?)";
			List<Object[]> queryData = new ArrayList<>();
			for (Map.Entry<Product, Double> entryProd : prods.entrySet()) {
				Product prod = entryProd.getKey();
				Double price = entryProd.getValue();
				int prod_id = jdbcTemplate.queryForObject("SELECT sel_ins(?, ?, ?)", Integer.class, prod.getPartnum(), prod.getName(), cat);
				queryData.add(new Object[]{Date.valueOf(LocalDate.now()), prod_id, price});
			}
			jdbcTemplate.batchUpdate(query, queryData);
			return null;
		});
	}
}