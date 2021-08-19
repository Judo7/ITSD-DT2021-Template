package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.*;
import utils.*;
//test
/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
//		CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution

		//draw board
//		BasicCommands.addPlayer1Notification(out, "drawTile", 2);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				Tile tile = BasicObjectBuilders.loadTile(i, j);
				BasicCommands.drawTile(out, tile, 0);
			}
		}

		
		//draw human avatar
		Tile tile = BasicObjectBuilders.loadTile(1, 2);
		Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit1.setUnitName("humanAvatar");
		gameState.unit.add(unit1);
		gameState.unit.get(0).setPositionByTile(tile);
		BasicCommands.drawUnit(out, gameState.unit.get(0), tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit1, 2);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit1, 20);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		unit1.setRange(1);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}


		//draw ai avatar
		Tile tile_ai = BasicObjectBuilders.loadTile(7, 2);
		Unit unit2 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 9, Unit.class);
		unit2.setUnitName("aiAvatar");
		gameState.aiUnit.add(unit2);
		gameState.aiUnit.get(0).setPositionByTile(tile_ai);
		BasicCommands.drawUnit(out, gameState.aiUnit.get(0), tile_ai);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit2, 2);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit2, 20);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		unit2.setRange(1);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		Player humanPlayer = gameState.getHumanPlayer();
		Player aiPlayer = gameState.getAiPlayer();
		//setPlayer1Mana
		humanPlayer.setMana(2);
		BasicCommands.setPlayer1Mana(out, humanPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		//setPlayer2Mana
		aiPlayer.setMana(2);
		BasicCommands.setPlayer2Mana(out, aiPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// setPlayer1Health
		humanPlayer.setHealth(20);
		BasicCommands.setPlayer1Health(out, humanPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// setPlayer2Health
		aiPlayer.setHealth(20);
		BasicCommands.setPlayer2Health(out, aiPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		//human cards and units
		String[] deck1Cards = {
				StaticConfFiles.c_sundrop_elixir,
				StaticConfFiles.c_truestrike,
				StaticConfFiles.c_azure_herald,
				StaticConfFiles.c_azurite_lion,				
				StaticConfFiles.c_comodo_charger,
				StaticConfFiles.c_fire_spitter,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_ironcliff_guardian,
				StaticConfFiles.c_pureblade_enforcer,
				StaticConfFiles.c_silverguard_knight,

		};

		String[] deck1Units = {
				StaticConfFiles.u_azure_herald,
				StaticConfFiles.u_azurite_lion,
				StaticConfFiles.u_comodo_charger,
				StaticConfFiles.u_fire_spitter,
				StaticConfFiles.u_hailstone_golem,
				StaticConfFiles.u_ironcliff_guardian,
				StaticConfFiles.u_pureblade_enforcer,
				StaticConfFiles.u_silverguard_knight,
		};

		for (int i = 0; i <= 2; i++) {
			// drawCard [1]
//			BasicCommands.addPlayer1Notification(out, deck1Cards[i], 2);
			Card card = BasicObjectBuilders.loadCard(deck1Cards[i], i, Card.class);
			card.setHandPosition(i + 1);//初始手牌位置即ID+1
			BasicCommands.drawCard(out, card, i + 1, 0);
			gameState.card.add(card);
			// load unit
			if (!WzyUtils.isMagic(card)) {
				Unit unit = BasicObjectBuilders.loadUnit(deck1Units[i-2], i - 1, Unit.class);//0是avatar 所以从1开始
				unit.setUnitName(card.getCardname());
				gameState.unit.add(unit);
				Tile tile1 = new Tile(-1, -1);
				unit.setPositionByTile(tile1);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gameState.addPosOfUnitDeck();

				System.out.println("uid"+unit.getId());
				}
			gameState.addNumOfHandCard();
			gameState.addPosOfDeck();
			System.out.println("cardID"+card.getId());
			}
	}

}


