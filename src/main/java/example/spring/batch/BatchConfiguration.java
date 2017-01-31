package example.spring.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
    public DataSource dataSource;
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public PurchaseOrderIdGenerator getPurchaseOrderIdGenerator(){
		return new PurchaseOrderIdGenerator(dataSource);
	}
	
	@Bean
	public PurchaseOrderProcessor getPurchaseOrderProcessor(){
		return new PurchaseOrderProcessor(getPurchaseOrderIdGenerator());
	}

	public FlatFileItemReader<PurchaseOrder> reader() {
		FlatFileItemReader<PurchaseOrder> reader = new FlatFileItemReader<PurchaseOrder>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<PurchaseOrder>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<PurchaseOrder>() {{
                setTargetType(PurchaseOrder.class);
            }});
        }});
        return reader;
	}
	
	 @Bean
    public JdbcBatchItemWriter<PurchaseOrder> writer() {
        JdbcBatchItemWriter<PurchaseOrder> writer = new JdbcBatchItemWriter<PurchaseOrder>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<PurchaseOrder>());
        writer.setSql("INSERT INTO PURCHASE_ORDER (ID, ORDER_NAME) VALUES (:id, :name)");
        writer.setDataSource(dataSource);
        return writer;
    }
	 
	    @Bean
	    public Job importPurchaseOrderJob(JobCompletionNotificationListener listener) {
	        return jobBuilderFactory.get("importPurchaseOrderJob")
	                .incrementer(new RunIdIncrementer())
	                .listener(listener)
	                .flow(step1())
	                .end()
	                .build();
	    }

	    @Bean
	    public Step step1() {
	        return stepBuilderFactory.get("step1")
	                .<PurchaseOrder, PurchaseOrder> chunk(10)
	                .reader(reader())
	                .processor(getPurchaseOrderProcessor())
	                .writer(writer())
	                .build();
	    }
}
