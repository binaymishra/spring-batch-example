package example.spring.batch;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Binay Mishra
 *
 */
public class PurchaseOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Integer id;
	String name;
	
	public PurchaseOrder() {
		
	}

	public PurchaseOrder(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [id=" + id + ", name=" + name + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
	 @Override
	public boolean equals(Object obj) {
		 if (obj == null)
			 return false;
		 PurchaseOrder other  = (PurchaseOrder) obj;
		 if(other.getId() == null || this.getId() == null)
			 return false;
		return this.id.intValue() == other.getId().intValue() ? true : false;
	}
	
}
