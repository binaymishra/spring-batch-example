package example.spring.component;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class PurchaseOrderIdGenerator implements IdGenerator<Integer> {
	
	AtomicInteger id;
	
	public PurchaseOrderIdGenerator(final DataSource dataSource) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Integer maxid = template.queryForObject("SELECT MAX(id) FROM PURCHASE_ORDER", Integer.class);
		System.err.println(String.format("maxid = %s", maxid));
		if(maxid == null)
			id = new AtomicInteger(0);
		else
			id = new AtomicInteger(maxid.intValue());
	}

	@Override
	public Integer getId() {
		return id.incrementAndGet();
	}

}