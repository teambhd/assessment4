package unitTests;

import static org.junit.Assert.*;
import logicClasses.ScoreTracking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScoreTracking_Test {
	private ScoreTracking scoreTrackingInstance;
	
	
	@Before
	public void setUp() throws Exception {
		scoreTrackingInstance = new ScoreTracking();
	}

	@After
	public void tearDown() throws Exception {
		scoreTrackingInstance = null;
	}

	@Test
	public void testUpdateWaypointScore() {
		// Test values for each interval of the way point scoring system
		int closestDistance1 = 7;
		int closestDistance2 = 20;
		int closestDistance3 = 40;
		
		int waypointScore;
		
		if (closestDistance1 >= 0 && closestDistance1 <= 14){		//checks to see if the plane is within 14 pixels
			waypointScore = 100; //if yes, the score given is 100 points
			int actualWaypointScore = scoreTrackingInstance.updateWaypointScore(closestDistance1);
			assertEquals(waypointScore, actualWaypointScore);			
		}
					
		if (closestDistance2 >= 15 && closestDistance2 <= 28){	
			waypointScore = 50;
			int actualWaypointScore = scoreTrackingInstance.updateWaypointScore(closestDistance2);
			assertEquals(waypointScore, actualWaypointScore);
		}
					
		if (closestDistance3 >= 29 && closestDistance3 <= 42){
			waypointScore = 20;
			int actualWaypointScore = scoreTrackingInstance.updateWaypointScore(closestDistance3);
			assertEquals(waypointScore, actualWaypointScore);
		}
		
		
	}

	@Test
	public void testUpdateScore() {
		int updatedScore = 50;
		int anotherUpdatedScore = 50;
		int actualUpdatedScore;
		
		actualUpdatedScore = scoreTrackingInstance.updateScore(updatedScore);
		actualUpdatedScore = scoreTrackingInstance.updateScore(anotherUpdatedScore);
		
		assertEquals(100, actualUpdatedScore);
	}

	@Test
	public void testUpdateTimeScore() {
		int actualUpdateTimeScore = scoreTrackingInstance.updateTimeScore();
		assertEquals(2, actualUpdateTimeScore);
	}

	@Test
	public void testReduceScoreOnFlightplanChange() {
		int reducedScore = -10;
		int actualReducedScore = scoreTrackingInstance.reduceScoreOnFlightplanChange();
		assertEquals(reducedScore, actualReducedScore);
	}

	@Test
	public void testReduceScoreOnFlightLost() {
		int actualReducedScore = scoreTrackingInstance.reduceScoreOnFlightLost();
		assertEquals(-50, actualReducedScore);
		
	}

	@Test
	public void testResetScore() {
		scoreTrackingInstance.updateScore(100);
		scoreTrackingInstance.resetScore();
		assertEquals(0, scoreTrackingInstance.getScore());
	}

	@Test
	public void testGetScore() {
		scoreTrackingInstance.updateScore(100);
		assertEquals(100, scoreTrackingInstance.getScore());
	}

	@Test
	public void testToString() {
		scoreTrackingInstance.updateScore(100);
		assertEquals("Score = 100", scoreTrackingInstance.toString());
	}

}
