package org.integrallis.drools.examples;

public class TestObject {
	
	private String name;
	

	public TestObject(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
