import java.util.Date;

import distributed.systems.das.BattleField;
import distributed.systems.das.units.Unit;

// A self-contained move on the Field, including a execution-time. 
// Note: Move-objects are used in multiple Trailing States at the same time, 
//       so after creation the fields need to be read only.
public class Move
{
	Date executionTime;
	Unit mover;
	int targetX, targetY;
	Action action;
	
	public Move(Unit mover, int targetX, int targetY, Action action, Date executionTime)
	{
		this.executionTime = executionTime;
		this.mover = mover;
		this.targetX = targetX;
		this.targetY = targetY;
		this.action = action;
	}
	
	//tests whether this move needs to be applied earlier than the given move.
	public boolean earlierThan(Move m)
	{
		return executionTime.before(m.executionTime);
	}
	
	// Given a certain delay, is this move ready to be executed now?
	public boolean canBeExecutedNow(Date delay)
	{
		// Note: 'new Date()' gives the current time.
		// So we test: (executionTime + delay) < currentTime
		return (executionTime.getTime() + delay.getTime()) < (new Date()).getTime();
	}
	
	//get the absolute time difference between the execution times of these 2 moves in milliseconds.
	public long executionTimeDifference(Move m)
	{
		return Math.abs(executionTime.getTime() - m.executionTime.getTime());
	}
	
	//tests whether this moves conflicts with the given move. So if applying them in different order could result in a different gamestate.
	public boolean conflictsWith(Move m)
	{
		//TODO: this is still the most conservative placeholder answer: moves conflict always.
		return true;
	}
	
	// Apply the move to the state.
	// This does not check whether the move is valid!
	public void execute(BattleField beforeState)
	{
		//TODO: actually execute the move.
		/*optional:
		 * if(! this.valid(beforeState) ) { return; }
		 * */
		switch(action)
		{
			case Heal:
				//add the movers attack points to the hp of the unit of the target square
				break;
			case Attack:
				//subtract the movers attack points to the hp of the unit of the target square
				break;
			case Move:
				//change the current square of the mover to null and the target square to the mover.
				break;
			default: 
				break;
			}
	}
	
	//TODO: Check whether this move is valid in the current context.
	public boolean valid(BattleField currentState)
	{
		/*first check whether the targetX and targetY are inside the Field*/
		
		// check specific requirements for the action:
		switch(action)
		{
			case Heal:
				/* Check:
				 * whether the mover is a player
				 * whether the target contains a player
				 * whether the target is at most at distance 5.
				 * */
				break;
			case Attack:
				/* Check:
				 * whether the target is at most at distance 2.
				 * that the target is not-empty
				 * that a dragon only attacks a player and vice versa
				 * */
				break;
			case Move:
				/* Check:
				 * whether the mover is a player
				 * whether the target is at most distance 1
				 * whether the target is empty
				 * */
				break;
			default: break;
		}
	}
	
	public enum Action
	{
		Attack, Heal, Move
	};
}