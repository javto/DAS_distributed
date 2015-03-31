import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import distributed.systems.das.BattleField;
import distributed.systems.das.units.Unit;


public class TrailingState
{
	List<Move> moves;
	TrailingState firstMoreRecentState;
	Date delayDelta;
	BattleField currentState;
	
	public TrailingState(Date delayDelta, TrailingState firstMoreRecentState, BattleField initialState)
	{
		//initialization
		this.delayDelta = delayDelta;
		this.firstMoreRecentState = firstMoreRecentState;
		// TODO: we need a deep-copy method for BattleField.
		this.currentState = initialState.clone(); 
		moves = Collections.synchronizedList(new LinkedList<Move>());
	}
	
	//inserts a move into the trailing states.
	public void insertMove(Move m)
	{
		int consideringPosition = 0;

		// We look though the list trying to find the position to put 'm'. If we find that position, we break. 
		// If we don't, we finish the loop and put 'm' at the end of the list.
		for(consideringPosition = 0; consideringPosition<moves.size(); consideringPosition++)
		{
			Move listedMove = moves.get(consideringPosition);
			if(m.earlierThan(listedMove))
			{
				break;
			}
		}
		moves.add(consideringPosition, m);
	}
	
	public void executionMethod()
	{
		while( !moves.isEmpty() )
		{
			Move nextMove = moves.get(0);
			if(nextMove.canBeExecutedNow(delayDelta))
			{
				nextMove.execute(this, currentState);
				if(nextMove.differentEffects(this, this.firstMoreRecentState))
				{
					this.firstMoreRecentState.rollBack(currentState, moves);
				}
			}
			else
			{
				try {
					Thread.sleep(moves.get(0).timeUntillExecution(delayDelta));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//perform rollback
	@SuppressWarnings("unchecked")
	public void rollBack(BattleField rollbackState, List<Move> movesToBeDone)
	{
		this.currentState = rollbackState.clone(); // we need a deep-copy method for BattleField.
		//TODO: we need a clone method for a synchronised list.
		this.moves = (LinkedList<Move>)movesToBeDone.clone();
		// Note: as the moves are ordered chronologically in the time to be executed, we can stop as soon as we find a move that we can no longer execute right away.
		for(int i = 0; i < moves.size();i++)
		{
			Move moveConsidered = moves.get(i);
			if(moveConsidered.canBeExecutedNow(delayDelta))
			{
				moveConsidered.execute(this, currentState);
				moves.remove(i--);
			}
			else
			{
				break;
			}
		}
		
		// A rollback needs to be applied to all even more recent states.
		// The more recent state can start recalculating from this trailing state's position.
		if(firstMoreRecentState != null)
		{
			firstMoreRecentState.rollBack(currentState, moves);
		}
	}
}