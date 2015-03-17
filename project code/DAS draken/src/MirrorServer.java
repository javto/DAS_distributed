import java.util.Date;

import distributed.systems.das.BattleField;


public class MirrorServer
{
	//Warning: error if 'numberOfTrailingStates' is less than 1.
	private final static int numberOfTrailingStates = 8;
	private final static int delayOfFirstTrailingstate = 10;
	
	TrailingState[] trailingStates;
	
	public MirrorServer(BattleField initialField)
	{
		trailingStates = new TrailingState[numberOfTrailingStates];
		//The first trailing state has a delay of 0 and no 'more recent'-trailing state.
		trailingStates[0] = new TrailingState(new Date(0), null, initialField);

		TrailingState previousState = trailingStates[0];
		int millisDelay = delayOfFirstTrailingstate;		
		for(int i=0; i<numberOfTrailingStates; i++)
		{
			// creating the next trailing State.
			trailingStates[i] = new TrailingState(new Date(millisDelay), previousState, initialField);
			
			// Doing the bookkeeping for the next round. We double the delay of each successive state. 
			previousState = trailingStates[i];
			millisDelay *= 2;
		}
	}
	
	//insert a move into the traling-states. This is the method that needs to be called by the other servers.
	public void insertMove(Move m)
	{
		for(int i=0; 0 < numberOfTrailingStates; i--)
		{
			trailingStates[i].insertMove(m);
		}
	}
	
	// insert the move into the server network. This is the method that needs to be called by clients.
	public void doMove(Move m)
	{
		/* TODO: Send the move to all other mirror servers.*/
		
		//apply the move in this server.
		this.insertMove(m);
	}
}
