import distributed.systems.das.units.Unit;


public class MoveEffect
{
	boolean moveSuccessful;
	Unit initiator, subject;
	Point target;
	int HealthChange =0;
	
	public boolean equals(Object o)
	{
		if(o instanceof MoveEffect)
		{
			MoveEffect effect = (MoveEffect)o;
			
			//TODO: write an equals method to check whether 2 units are 'the same'.
			return  (this.moveSuccessful == effect.moveSuccessful)
					&& (this.target.equals(effect.target))
					&& (this.initiator.Equals(effect.initiator))
					&& (this.subject.Equals(effect.subject))
					&& (this.HealthChange == effect.HealthChange);
		}
		else
		{
			return false;
		}
	}
	
	public void setMoveSuccessful(boolean b)
	{
		this.moveSuccessful = b;
	}
	
	public void setInitiator(Unit initiator)
	{
		this.initiator = initiator;
	}
	
	public void setHealthChange(int healthChange)
	{
		this.HealthChange = healthChange;
	}
}
