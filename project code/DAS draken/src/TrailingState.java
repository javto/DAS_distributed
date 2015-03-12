import java.util.Date;
import java.util.LinkedList;

import distributed.systems.das.BattleField;
import distributed.systems.das.units.Unit;


public class TrailingState
{
	LinkedList<Move> moves;
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
		moves= new LinkedList<Move>();
	}
	
	//inserts a move into the trailing states and then signals whether the move caused a rollback. 
	public boolean insertMove(Move m)
	{
		boolean conflictFound = false;
		long timeDifferenceBetweenConflicts = 0;
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
			else
			{
				if(listedMove.conflictsWith(m))
				{
					conflictFound = true;
					timeDifferenceBetweenConflicts = listedMove.executionTimeDifference(m);
				}
			}
		}
		moves.add(consideringPosition, m);
		
		// test whether a rollback is required by:
		// first testing whether a conflict was discovered when inserting this move in the list.
		// if so whether the time-difference in the conflict was so great that the first more recent trailing state hasn't caught the conflict.
		// if so, do a sanity check whether we are the most recent trailing  state (by  testing whether 'firstMoreRecentState' is null).
		if(conflictFound 
				&& (delayDelta.getTime() - firstMoreRecentState.delayDelta.getTime() < timeDifferenceBetweenConflicts) 
				&& firstMoreRecentState != null)
		{
			firstMoreRecentState.rollBack(currentState, moves);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//perform rollback
	@SuppressWarnings("unchecked")
	public void rollBack(BattleField rollbackState, LinkedList<Move> movesToBeDone)
	{
		this.currentState = rollbackState.clone(); // we need a deep-copy method for BattleField.
		this.moves = (LinkedList<Move>)movesToBeDone.clone();
		// Note: as the moves are ordered chronologically in the time to be executed, we can stop as soon as we find a move that we can no longer execute right away.
		for(int i = 0; i < moves.size();i++)
		{
			Move moveConsidered = moves.get(i);
			if(moveConsidered.canBeExecutedNow(delayDelta))
			{
				moveConsidered.execute(currentState);
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