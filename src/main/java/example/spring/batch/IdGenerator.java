package example.spring.batch;

public interface IdGenerator<R> {
	
	R getId();
	
}
