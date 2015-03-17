import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import distributed.systems.das.BattleField;
import distributed.systems.das.units.Unit;

// A self-contained move on the Field, including a execution-time. 
// Note: Move-objects are used in multiple Trailing States at the same time, 
//       so after creation the fields need to be read only.
public class Move
{
	Date executionTime;
	Unit mover;
	Point target;
	Action action;
	
	Map<TrailingState, MoveEffect> effectsOfThisMove;
	
	public Move(Unit mover, Point target, Action action, Date executionTime)
	{
		this.executionTime = executionTime;
		this.mover = mover;
		this.target = target;
		this.action = action;
		
		effectsOfThisMove = Collections.synchronizedMap(new HashMap<TrailingState, MoveEffect>());
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
	
	
	
	// Apply the move to the state.
	// This does not check whether the move is valid!
	public void execute(TrailingState executingState, BattleField beforeState)
	{
		MoveEffect effect = new MoveEffect();
		effect.setInitiator(mover);//TODO: get a copy of the mover instead of the mover itself.
		//TODO: get the content of the target square (unit or null) and set a copy of it as the subject of the MoveEffect.
		// if the move isn't valid, don't execute it.
		if(! this.valid(beforeState) )
		{
			effect.setMoveSuccesfull(false);
			return;
		}
		
		//TODO: actually execute the move.
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