import distributed.systems.das.units.Unit;


public class MoveEffect
{
	boolean moveSuccesfull;
	Unit initiator, subject;
	Point target;
	int HealthChange =0;
	
	public boolean equals(Object o)
	{
		if(o instanceof MoveEffect)
		{
			MoveEffect effect = (MoveEffect)o;
			
			//TODO: write an equals method to check whether 2 units are 'the same'.
			return  (this.moveSuccesfull == effect.moveSuccesfull)
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
	
	public void setMoveSuccesfull(boolean b)
	{
		this.moveSuccesfull = b;
	}
	
	public void setInitiator(Unit initiator)
	{
		this.initiator = initiator;
	}
}
