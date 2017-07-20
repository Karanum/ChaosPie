package eu.teamtime.chaospie;

public interface IChaosEffect {
	
	public void start();
	public void stop();
	
	public int lengthInSeconds();
	public int getWeight();
	
}
