package example.spring.batch.component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import example.spring.domain.PurchaseOrder;


public class JobCompletionNotificationListener extends JobExecutionListenerSupport {


	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.err.println("!!! JOB FINISHED !!!");

			List<PurchaseOrder> results = jdbcTemplate.query("SELECT ID, ORDER_NAME FROM PURCHASE_ORDER", new RowMapper<PurchaseOrder>() {
				@Override
				public PurchaseOrder mapRow(ResultSet rs, int row) throws SQLException {
					return new PurchaseOrder(rs.getInt(1), rs.getString(2));
				}
			});

			for (PurchaseOrder purchaseOrder : results) {
				System.err.println(purchaseOrder + " inside DB.");
			}

		}
	}
}