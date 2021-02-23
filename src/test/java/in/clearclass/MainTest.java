package in.clearclass;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import in.clearclass.catalog.OnlineCatalog;

public class MainTest {

	@Test
	public void onlineCatalogTest(){
		String cat = "/1302/kofe-chay-sahar";
		assertTrue(OnlineCatalog.numOfPages(cat)>1);
		assertTrue(OnlineCatalog.getPages(cat, 3, 20).size()>550);
	}
}