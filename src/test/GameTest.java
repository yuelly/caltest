package test;

import static org.junit.Assert.fail;
import game.AnimalGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private static AnimalGame a = new AnimalGame();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBlack() {
		a.GetBlack();
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRed() {
		a.GetRed();
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNext() {
		a.GetNext();
		//fail("Not yet implemented");
	}

	@Test
	public void testMove() {
		a.move(1, 2);
	//	fail("Not yet implemented");
	}

}
