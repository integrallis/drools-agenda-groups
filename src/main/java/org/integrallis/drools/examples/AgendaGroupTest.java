package org.integrallis.drools.examples;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

/**
 * This is a sample class to launch a rule.
 */
public class AgendaGroupTest {

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			String[] agendaGroups = {"Group One", "Group Two" };
			TestObject testObjectA = new TestObject("A");
			TestObject testObjectB = new TestObject("B");
			for (String group : agendaGroups) {
				System.out.println("Running with " + group + " group in focus");
				ksession.insert(testObjectA);
				ksession.insert(testObjectB);
				ksession.getAgenda().getAgendaGroup(group).setFocus();
				ksession.fireAllRules();
			}
			
			QueryResults results = ksession.getQueryResults( "FindClassNameStartingWith", new Object[] { TestObject.class } );
			
			for ( QueryResultsRow row : results ) {
				TestObject testObject = (TestObject) row.get( "object" );
				System.out.println("Found " + testObject);
			}
			
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("sample_rules.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}