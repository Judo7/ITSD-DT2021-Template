package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.AiUnitUtils;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.WzyUtils;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	int turnNum = 0;
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		Player humanPlayer = gameState.getHumanPlayer();
		Player aiPlayer = gameState.getAiPlayer();
		int[] tileX = {4,5,6,7,8};
		int[] tileY = {0,1,2,3,4};
		Tile tile = BasicObjectBuilders.loadTile(tileX[(int)(Math.round(Math.random()*4))], tileY[(int)(Math.round(Math.random()*4))]);


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
		String[] deck2Cards = {
				StaticConfFiles.c_blaze_hound,
				StaticConfFiles.c_bloodshard_golem,
				StaticConfFiles.c_hailstone_golem,
				StaticConfFiles.c_planar_scout,
				StaticConfFiles.c_pyromancer,
				StaticConfFiles.c_serpenti,
				StaticConfFiles.c_rock_pulveriser,
				StaticConfFiles.c_windshrike,
				StaticConfFiles.c_staff_of_ykir,
				StaticConfFiles.c_entropic_decay,
		};

		String[] deck2Units = {
				StaticConfFiles.u_blaze_hound,
				StaticConfFiles.u_bloodshard_golem,
				StaticConfFiles.u_hailstone_golemR,
				StaticConfFiles.u_planar_scout,
				StaticConfFiles.u_pyromancer,
				StaticConfFiles.u_rock_pulveriser,
				StaticConfFiles.u_serpenti,
				StaticConfFiles.u_windshrike
		};

		Card card = null;
		Unit unit = null;

		if (gameState.getPosOfDeck() < deck1Cards.length){
			card = BasicObjectBuilders.loadCard(deck1Cards[gameState.getPosOfDeck()], gameState.getPosOfDeck(), Card.class);
			BasicCommands.addPlayer1Notification(out, card.getCardname(), 2);
			card.setHandPosition(gameState.getNumOfHandCard()+1);
			BasicCommands.drawCard(out, card, gameState.getNumOfHandCard()+1, 0);
			gameState.card.add(card);

			if (!WzyUtils.isMagic(card)) {
				unit = BasicObjectBuilders.loadUnit(deck1Units[gameState.getPosOfUnitDeck() ], gameState.getPosOfUnitDeck()+1 , Unit.class);
				unit.setUnitName(card.getCardname());
				System.out.println(unit.getUnitName());
				BasicCommands.addPlayer1Notification(out, unit.getUnitName(), 2);
				gameState.unit.add(unit);
				Tile tile1 = new Tile(-1, -1);
				unit.setPositionByTile(tile1);
				try { Thread.sleep(2000); } catch (InterruptedException e) {e.printStackTrace(); }
				gameState.addPosOfUnitDeck();
				System.out.println("uid"+unit.getId());
			}

			gameState.addNumOfHandCard();
			gameState.addPosOfDeck();
			System.out.println("cardID"+card.getId());
		}



		if(gameState.getNumOfHandCard()==7){
			BasicCommands.addPlayer1Notification(out, "deleteCard", 2);
			BasicCommands.deleteCard(out,gameState.getNumOfHandCard());
			//gameState.card.remove(card);
			card.setUsed(true);
			//gameState.unit.remove(unit);
			gameState.subNumOfhandCard();
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}

		gameState.healCheck();//The Heal Unit is called once every round
		BasicCommands.setUnitHealth(out, gameState.unit.get(0), gameState.unit.get(0).getHealth());
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		//manaDrain
		//human manaDrain The human player's mana is cleared to zero at the end of each turn
		BasicCommands.addPlayer1Notification(out, "manaDrain", 2);
		humanPlayer.setMana(0);
		BasicCommands.setPlayer1Mana(out, humanPlayer);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		//After clicking End Turn, the humanplayer's mana value is +1 for the number of turns
		gameState.addTurns(gameState.getTurns()+1);
		if(gameState.getTurns()<9) {
			BasicCommands.addPlayer1Notification(out, "manaGain", 2);
			humanPlayer.setMana(gameState.getTurns()+1);
			BasicCommands.setPlayer1Mana(out, humanPlayer);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}else {
			BasicCommands.addPlayer1Notification(out, "manaGain", 2);
			humanPlayer.setMana(9);
			BasicCommands.setPlayer1Mana(out, humanPlayer);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}

		////ai manaDrain The aiplayer's mana is cleared to zero at the end of each turn
		BasicCommands.addPlayer1Notification(out, "manaDrain", 2);
		aiPlayer.setMana(0);
		BasicCommands.setPlayer2Mana(out, aiPlayer);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		////After clicking End Turn, the aiplayer's mana value is +1 for the number of turns
		gameState.addTurns(gameState.getTurns()+1);
		if(gameState.getTurns()<9) {
			BasicCommands.addPlayer1Notification(out, "manaGain", 2);
			aiPlayer.setMana(gameState.getTurns()+1);
			BasicCommands.setPlayer2Mana(out, aiPlayer);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}else {
			BasicCommands.addPlayer1Notification(out, "manaGain", 2);
			aiPlayer.setMana(9);
			BasicCommands.setPlayer2Mana(out, aiPlayer);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}


		// Resets the movement and attack flags of all Units
		for(Unit u: gameState.unit) {
			u.setAttacked(false);
			u.setMoved(false);
			if(u.getAttackedTwice()) {
				u.setAttackedTwice(false);
			}
			if(u.getMovedTwice()) {
				u.setMovedTwice(false);
			}
		}


		//draw AI unit
		if(turnNum <= 7 ) {
		while(true) {	
			if (gameState.unitCheck(tile)) {
			 tile = BasicObjectBuilders.loadTile(tileX[(int)(Math.round(Math.random()*4))], tileY[(int)(Math.round(Math.random()*4))]);
			 continue;
			}
			break;
		}
		Card newCard = BasicObjectBuilders.loadCard(deck2Cards[turnNum], turnNum, Card.class);
		gameState.aiCard.add(newCard);
		Unit aiunit = BasicObjectBuilders.loadUnit(deck2Units[turnNum],turnNum+10 , Unit.class);
		aiunit.setUnitName(newCard.getCardname());
		gameState.aiUnit.add(aiunit);
		aiunit.setPositionByTile(tile);
		System.out.println("turn number:"+turnNum);
		BasicCommands.drawUnit(out,aiunit,tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out,aiunit,AiUnitUtils.getUnitHealth(newCard));
			System.out.println(aiunit.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out,aiunit,AiUnitUtils.getUnitAttack(newCard));
			System.out.println(aiunit.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		aiunit.setRange(1);
		}
		turnNum++;

		//ai unit move
		for(Unit u: gameState.aiUnit) {
			Tile temp = BasicObjectBuilders.loadTile(-1, -1);
			if(u.getPosition().getTilex() == gameState.unit.get(0).getPosition().getTilex()) {
				if(u.getPosition().getTiley() > gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex(), u.getPosition().getTiley() - 1);
				}
				if(u.getPosition().getTiley() < gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex(), u.getPosition().getTiley() + 1);
				
				}
			}
			else if(u.getPosition().getTiley() == gameState.unit.get(0).getPosition().getTiley()) {
				if(u.getPosition().getTilex() > gameState.unit.get(0).getPosition().getTilex()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() - 1, u.getPosition().getTiley());
			
				}
				if(u.getPosition().getTilex() < gameState.unit.get(0).getPosition().getTilex()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() + 1, u.getPosition().getTiley());
			
				}			
			}
			else if(u.getPosition().getTilex() > gameState.unit.get(0).getPosition().getTilex()) {
				if(u.getPosition().getTiley() > gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() - 1, u.getPosition().getTiley() - 1);
		
				}
				if(u.getPosition().getTiley() < gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() - 1, u.getPosition().getTiley() + 1);
			
				}
			}
			else if(u.getPosition().getTilex() < gameState.unit.get(0).getPosition().getTilex()) {
				if(u.getPosition().getTiley() > gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() + 1, u.getPosition().getTiley() - 1);
				
				}
				if(u.getPosition().getTiley() < gameState.unit.get(0).getPosition().getTiley()) {
					temp = BasicObjectBuilders.loadTile(u.getPosition().getTilex() + 1, u.getPosition().getTiley() + 1);
				
				}				
			}
			if(!gameState.unitCheck(temp)) {
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.moveUnitToTile(out, u, temp);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			u.setPositionByTile(temp);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		// ai attack
		for(Unit ai: gameState.aiUnit){
			for(Unit u:gameState.unit ) {
				if(gameState.rangeCheck(ai, u)){
					BasicCommands.playUnitAnimation(out, ai, UnitAnimationType.attack);
					try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
					if(u.getHealth() - ai.getAttack() <= 0) {		//The human player's Unit was killed by the AI
						BasicCommands.setUnitHealth(out, u, 0);
						try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
						BasicCommands.playUnitAnimation(out, u, UnitAnimationType.death);//Play a death animation after killing
						try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
						gameState.deathAbilityTrigger(out);
						BasicCommands.deleteUnit(out, u);
//						gameState.unit.remove(u);
						Tile temp = BasicObjectBuilders.loadTile(-5,-5);
						u.setPositionByTile(temp);
						try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
					}
					else {
						//A human player needs to counterattack if his Unit is not killed
						BasicCommands.setUnitHealth(out, u, u.getHealth() - ai.getAttack());
						BasicCommands.playUnitAnimation(out, u, UnitAnimationType.attack);
						BasicCommands.setUnitHealth(out, ai, ai.getHealth() - u.getAttack());
						if (ai.getHealth() <= 0){
							BasicCommands.setUnitHealth(out, u, 0);
							BasicCommands.playUnitAnimation(out, u, UnitAnimationType.death);//打死后直接播放死亡动画
							try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
							gameState.deathAbilityTrigger(out);
							BasicCommands.deleteUnit(out,ai);
						}
					}
					break;					
				}
			}
		}
		for (Unit u:
			 gameState.aiUnit) {
			System.out.println(u.getUnitName());
			System.out.println(u.getHealth());
		}
	}
}















