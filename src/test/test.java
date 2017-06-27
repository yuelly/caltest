package test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calframe.MyMath;

public class test {
	private static MyMath cal = new MyMath();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		double result = cal.add(1, 2);
		assertEquals(3.0,cal.add(1, 2),0);
	}

	@Test
	public void testSubtract() {
		double result = cal.subtract(10, 5);
		assertEquals(5.0,result,0);
	}

	@Test
	public void testMultiply() {
		double result = cal.multiply(5, 5);
		assertEquals(25.0,result,0);
		
	}

	@Test
	public void testDivide() {
		double result  = cal.divide(1, 0);
		assertEquals(0,result,0);
	}

}
