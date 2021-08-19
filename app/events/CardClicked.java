package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.WzyUtils;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor {

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int handPosition = message.get("position").asInt();
		gameState.currentCardPos = handPosition;
		gameState.ifCardClicked = true;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
					Tile tile = BasicObjectBuilders.loadTile(i,j);
					BasicCommands.drawTile(out, tile, 0);
			}
		}
		// drawCard [1] Highlight
		for (Card c : gameState.card) {
			if (c.getHandPosition() == handPosition && c.isUsed() == false) {
				BasicCommands.drawCard(out, c, handPosition, 1);

			}
		}
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		//如果不是咒术卡

		int cardID = 0;
		for (Card c : gameState.card) {
			if (c.getHandPosition() == gameState.currentCardPos) {
				cardID = c.getId();
				System.out.println(cardID);
			}
		}
		if (!WzyUtils.isMagic(gameState.card.get(cardID))) {
            if (gameState.card.get(handPosition - 1).getManacost() <= gameState.getHumanPlayer().getMana()) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 5; j++) {
					Tile tile = BasicObjectBuilders.loadTile(i, j);
					if (!gameState.unitCheck(tile)) {
						BasicCommands.drawTile(out, tile, 1);					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            }
		} else {

            if (gameState.card.get(handPosition - 1).getManacost() <= gameState.getHumanPlayer().getMana()) {
			gameState.setIfMagicClicked(true);
				gameState.setCurrentCard(gameState.card.get(cardID));
				Tile tile = new Tile();
				for(Unit u : gameState.unit){
					tile.setTilex(u.getPosition().getTilex());
					tile.setTiley(u.getPosition().getTiley());
					BasicCommands.drawTile(out, tile, 1);
				}
				for (Unit u:gameState.aiUnit){
					tile.setTilex(u.getPosition().getTilex());
					tile.setTiley(u.getPosition().getTiley());
					BasicCommands.drawTile(out, tile, 1);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
			}

	}
}
