package logicClasses;

public class Achievements {

	//FIELDS
	
	private int numberOfAchievements = 0;
	private static final int MAXACHIEVEMENTS = 9;
	private static final int ACHIEVEMENTTIME = 600000;
	private String achievementMessage = "";
	
	//achievements gained booleans
	private boolean silverAchievementGained = false;
	private boolean goldAchievementGained = false;
	private boolean timeAchievementGained = false;
	private boolean noPlaneLossAchievementGained = false;
	private boolean planesLandedAchievementGained = false;
	private boolean flightPlanChangedAchievementGained = false;
		
	//CONSTRUCTOR
	
	public Achievements(){

	}
	
	//METHODS
	
	public String pointsAchievement(int pointsTotal){
		
		if (silverAchievementGained == false){
			if (pointsTotal >= 1000){
				//then display silver achievement
				achievementMessage = "silver achievement gained";
				silverAchievementGained = true;
				completeAchievement();
			}
		}
		
		if (goldAchievementGained == false){
			if (pointsTotal >= 2000){
				//then display gold achievement
				achievementMessage = "gold achievement gained";
				goldAchievementGained = true;
				completeAchievement();
			}	
		}
		
		return achievementMessage;
	}
	
	public void completeAchievement(){
		if (numberOfAchievements >= MAXACHIEVEMENTS){
			System.out.println("complete achievement gained");
			//then display achievement
		}
		numberOfAchievements += 1;
	}
	
	public void timeAchievement(int time){
		
		if (timeAchievementGained == false){
			if (time >= ACHIEVEMENTTIME){
				//then display achievement
				System.out.println("time achievement gained");
				completeAchievement();
				timeAchievementGained = true;
			}
		}
	}
	
	public String crashAchievement(int gameTime){
		
		if (gameTime <= 40000){
			//then display achievement
			achievementMessage = "crash achievement gained";
			completeAchievement();
		}
		
		return achievementMessage;
	}
	
	public String changeFlightPlanAchievement(){
		if (flightPlanChangedAchievementGained == false){
			//display achievement
			achievementMessage = "change flight plan achievement gained";
			completeAchievement();
			flightPlanChangedAchievementGained = true;
		}
		return achievementMessage;
	}
	
	public void completeFlightPlanAchievement(){
		//display achievement
		System.out.println("complete flight plan achievement gained");
		completeAchievement();
	}
	
	public void minsWithoutPlaneLossAchievement(int timeWithoutLoss){
		if (noPlaneLossAchievementGained == false){
			if (timeWithoutLoss >= 10){
				//display achievement
				System.out.println("time without losing plane achievement gained");
				completeAchievement();
				noPlaneLossAchievementGained = true;
			}
		}
	}
	
	public void planesLandedAchievement(int planesLanded){
		if (planesLandedAchievementGained == false){
			if (planesLanded >= 10){
				//display achievement
				System.out.println("landing achievement gained");
				completeAchievement();
				planesLandedAchievementGained = true;
			}
		}
	}
	
	//GETTERS AND SETTERS
	
	public int getNumberOfAchievements(){
		return numberOfAchievements;
	}
	
	public void setNumberOfAchievements(int noAchieved){
		numberOfAchievements += noAchieved;
	}
}
