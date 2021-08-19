package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Player;

/**
 * In the user’s browser, the game is running in an infinite loop, where there is around a 1 second delay 
 * between each loop. Its during each loop that the UI acts on the commands that have been sent to it. A 
 * heartbeat event is fired at the end of each loop iteration. As with all events this is received by the Game 
 * Actor, which you can use to trigger game logic.
 *
 * {
 *   String messageType = “heartbeat”
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class Heartbeat implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		Player humanPlayer = gameState.getHumanPlayer();
		Player aiPlayer = gameState.getAiPlayer();

		if (humanPlayer.getHealth()<=0){
			BasicCommands.addPlayer1Notification(out, "AIPlayer Win", 2);
			humanPlayer.setHealth(0);
			BasicCommands.setPlayer1Health(out,humanPlayer);
			try {Thread.sleep(100000000);} catch (InterruptedException e) {e.printStackTrace();}
		}

		if (aiPlayer.getHealth()<=0){
			BasicCommands.addPlayer1Notification(out, "HumanPlayer Win", 2);
			aiPlayer.setHealth(0);
			BasicCommands.setPlayer2Health(out,aiPlayer);
			try {Thread.sleep(100000000);} catch (InterruptedException e) {e.printStackTrace();}
		}

		if (gameState.humanPlayer.getHealth()!=gameState.unit.get(0).getHealth()){
			if(gameState.unit.get(0).getHealth()<gameState.humanPlayer.getHealth()){
				gameState.damageAbilityTrigger(out);
			}
			gameState.avatarHealthChange(out,gameState.unit.get(0),gameState.unit.get(0).getHealth());
		}

		if (gameState.aiPlayer.getHealth()!=gameState.aiUnit.get(0).getHealth()){
			gameState.avatarHealthChange(out,gameState.aiUnit.get(0),gameState.aiUnit.get(0).getHealth());
		}
	}
}
