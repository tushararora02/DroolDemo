package com.javacodegeeks.drools;

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;

import com.javacodegeeks.drools.model.Customer;
import com.javacodegeeks.drools.model.Product;

public class DroolsDecisionTableExample {
	private static StatelessKnowledgeSession session;

	public static void main(String[] args) throws Exception {
		KnowledgeBase knowledgeBase = createKnowledgeBaseFromSpreadsheet();
		session = knowledgeBase.newStatelessKnowledgeSession();

		Customer customer = new Customer();
		Product p1 = new Product("Laptop", 15000);
		Product p2 = new Product("Mobile", 5000);
		Product p3 = new Product("Books", 2000);
		customer.addItem(p1, 1);
		customer.addItem(p2, 2);
		customer.addItem(p3, 5);
		customer.setCoupon("DISC01");

		session.execute(customer);

		System.out.println("First Customer\n" + customer);

		Customer newCustomer = Customer.newCustomer();
		newCustomer.addItem(p1, 1);
		newCustomer.addItem(p2, 2);
		
		session.execute(newCustomer);

		System.out.println("*********************************");
		System.out.println("Second Customer\n" + customer);
	}

	private static KnowledgeBase createKnowledgeBaseFromSpreadsheet()
			throws Exception {
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		dtconf.setInputType(DecisionTableInputType.XLS);

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory
				.newClassPathResource("shopping_cart_customer.xls"),
				ResourceType.DTABLE, dtconf);

		if (knowledgeBuilder.hasErrors()) {
			throw new RuntimeException(knowledgeBuilder.getErrors().toString());
		}		

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder
				.getKnowledgePackages());
		return knowledgeBase;
	}
}
