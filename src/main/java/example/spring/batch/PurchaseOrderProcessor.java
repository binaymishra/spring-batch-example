package example.spring.batch;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author Binay Mishra
 *
 */
public class PurchaseOrderProcessor implements ItemProcessor<PurchaseOrder, PurchaseOrder> {
	
	PurchaseOrderIdGenerator purchaseOrderIdGenerator;
	
	public PurchaseOrderProcessor(final PurchaseOrderIdGenerator purchaseOrderIdGenerator) {
		this.purchaseOrderIdGenerator = purchaseOrderIdGenerator;
	}

	@Override
	public PurchaseOrder process(final PurchaseOrder order) throws Exception {
		PurchaseOrder purchaseOrder = new PurchaseOrder(purchaseOrderIdGenerator.getId(), order.getName());
		System.err.println("Updating IDs (" + order + ") ==> (" + purchaseOrder + ")");
		return purchaseOrder;
	}

}
