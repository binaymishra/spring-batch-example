package example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Binay Mishra
 *
 */
public class Application {

	public static void main(String[] args) throws Throwable{
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		try {
			
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			Job job = (Job) context.getBean("insertIntoDbFromCSV");
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
			
			if(jobExecution.isRunning()){
				System.err.println( "Still running...!!, waiting 10sec to complete.");
				Thread.sleep( 10 * 1000 ); // 10 seconds
			}
		
		} finally{
			context.close();
		}
	}

}
