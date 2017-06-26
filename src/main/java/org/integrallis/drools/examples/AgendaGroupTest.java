package org.integrallis.drools.examples;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

/**
 * This is a sample class to launch a rule.
 */
public class AgendaGroupTest {

	public static final void main(String[] args) {
		KieSession knowledgeSession = null;
		try {
			// load up the knowledge base
	        // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
		    KieContainer kContainer = ks.getKieClasspathContainer();
		    knowledgeSession = kContainer.newKieSession("ksession-rules");
		    
			// go !
			String[] agendaGroups = {"Group One", "Group Two" };
			TestObject testObjectA = new TestObject("A");
			TestObject testObjectB = new TestObject("B");
			for (String group : agendaGroups) {
				System.out.println("Running with " + group + " group in focus");
				knowledgeSession.insert(testObjectA);
				knowledgeSession.insert(testObjectB);
				knowledgeSession.getAgenda().getAgendaGroup(group).setFocus();
				knowledgeSession.fireAllRules();
			}
			
			QueryResults results = knowledgeSession.getQueryResults( "FindClassNameStartingWith", new Object[] { TestObject.class } );
			
			for ( QueryResultsRow row : results ) {
				TestObject testObject = (TestObject) row.get( "object" );
				System.out.println("Found " + testObject);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}