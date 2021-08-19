package events;


import java.util.ArrayList;

import java.util.Set;

import javax.swing.text.Highlighter.Highlight;

import org.checkerframework.checker.units.qual.h;
//import org.omg.CORBA.PUBLIC_MEMBER;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import akka.parboiled2.Position;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.DrawRedTile;
import utils.StaticConfFiles;
import utils.UnitUtils;
import utils.WzyUtils;
import views.html.helper.inputText;
import structures.basic.*;
import java.util.List;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 *
 * {
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	public void moveHighlightTile(ActorRef out, int highlight, ArrayList <ArrayList<Integer>> moveHighlighted) {
		if(moveHighlighted != null) {

			for(ArrayList<Integer> pos:moveHighlighted) {
				int x = pos.get(0);
				int y=pos.get(1);
				Tile tile = BasicObjectBuilders.loadTile(x,y);
				BasicCommands.drawTile(out, tile, highlight);
			}
		}
	}

	public boolean isMoving(int tilex, int tiley, ArrayList <ArrayList<Integer>> moveHighlighted) {//是否可以移动
		if(moveHighlighted != null) {
			for(ArrayList<Integer> pos:moveHighlighted) {
				int x = pos.get(0);
				int y=pos.get(1);
				if(x == tilex && y == tiley) {
					return true;

				}
			}
		}
		return false;
	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		BasicCommands.addPlayer1Notification(out, "TileClicked", 2);
		gameState.ifTileClicked = true;

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		boolean info = true;

		gameState.tile = BasicObjectBuilders.loadTile(tilex, tiley);
		BasicCommands.drawTile(out, gameState.tile, 0);

		//点击高亮
//		BasicCommands.drawTile(out, gameState.tile, 1);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				if (i==tilex && j==tiley){
				}else{
					Tile tile = BasicObjectBuilders.loadTile(i,j);
					BasicCommands.drawTile(out, tile, 0);
				}
			}
		}
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

//play: card
		if (gameState.ifCardClicked == true && gameState.ifTileClicked == true) {
			Unit unit = null;
			int cardID = 0;
			for (Card c : gameState.card) {
				if (c.getHandPosition() == gameState.currentCardPos) {
					cardID = c.getId();

					c.setUsed(true);   // This card has already been played
					// Write it down in case you don't use it
					WzyUtils.notUsedHandPosition = c.getHandPosition();
					c.setHandPosition(-1);
				}
			}
			int newMana = gameState.getHumanPlayer().getMana() - gameState.card.get(cardID).getManacost();
			if(newMana >= 0) {
				if (gameState.isIfMagicClicked() != true) {
//				BasicCommands.addPlayer1Notification(out, "play: unit card", 2);
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					for (Unit u : gameState.unit) {  //遍历unit List
//						System.out.println("uid"+u.getId());
//						System.out.println("cardID"+cardID);

						if (u.getId() == cardID - 1) {   // Unit starts at 1, card starts at 0



							//gameState.unit.get(u.getId()).setPositionByTile(gameState.tile);
							u.setPositionByTile(gameState.tile);
							unit = u;


							//gameState.targetUnit=null;
							info = false;
							EffectAnimation ef = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
//						BasicCommands.drawUnit(out, gameState.unit.get(u.getId()), gameState.tile);
							BasicCommands.drawUnit(out, u, gameState.tile);
							BasicCommands.playEffectAnimation(out, ef, gameState.tile);
							// Sets damage and health
							UnitUtils utils = new UnitUtils();
							try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setUnitAttack(out, u, utils.getUnitAttack(gameState.card.get(cardID)));
							try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.setUnitHealth(out, u, utils.getUnitHealth(gameState.card.get(cardID)));
							try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
							u.setRange(1);
							gameState.summonAbilityTrigger(out,unit);

							//设置被动技能给特定Unit
							if((gameState.card.get(cardID).getCardname().equals(UnitUtils.names[7]))|| (gameState.card.get(cardID).getCardname().equals(UnitUtils.names[5]))){
								BasicCommands.addPlayer1Notification(out, "It can provoke", 2);
								try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
								u.setProvoker(true);
							}
							if((gameState.card.get(cardID).getCardname().equals(UnitUtils.names[1]))) {
								BasicCommands.addPlayer1Notification(out, "It can attack twice", 2);
								try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
								u.setAttackTwice(true);
							}
							if(gameState.card.get(cardID).getCardname().equals(UnitUtils.names[7])) {
								BasicCommands.addPlayer1Notification(out, "It can airdrop", 2);
								try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
								u.setAirdrop(true);
							}
							if(gameState.card.get(cardID).getCardname().equals(UnitUtils.names[3])) {
								BasicCommands.addPlayer1Notification(out, "It has longer Range", 2);
								try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
								u.setRange(2);
							}
							if(gameState.card.get(cardID).getCardname().equals(UnitUtils.names[0])) {
								BasicCommands.addPlayer1Notification(out, "It can heal", 2);
								try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
								u.setHeal(true);
							}


							gameState.ifCardClicked = false;

						}
					}
				}
				else {
					//play spell card
					BasicCommands.addPlayer1Notification(out, "play: spell card", 2);
					gameState.setIfMagicClicked(false);
					if(gameState.unitCheck(gameState.tile)){
						WzyUtils.conjuration(gameState.currentCard,gameState.targetUnit,out);
						if(WzyUtils.isReturn){
							WzyUtils.isReturn=false;
							return;
						}
					}
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
				}
				gameState.getHumanPlayer().setMana(newMana);
				BasicCommands.setPlayer1Mana(out, gameState.getHumanPlayer());
				//delete Card
				BasicCommands.addPlayer1Notification(out, "deleteCard", 2);
				// Delete from the end
                // Find the position of the card with the largest position
				int maxPos = gameState.currentCardPos;
				for (Card c : gameState.card){
					if (c.getHandPosition() >= maxPos && c.isUsed() == false) maxPos = c.getHandPosition();
				}
				// Delete the card from back to front
				do {
					BasicCommands.deleteCard(out,maxPos);
					maxPos--;
				}while (maxPos == gameState.currentCardPos);
				//shift
				for (Card c : gameState.card) {
					if (c.getHandPosition() > gameState.currentCardPos && c.isUsed() == false) {
						c.setHandPosition(c.getHandPosition()-1);
						BasicCommands.drawCard(out,c,c.getHandPosition(),0);
					}
				}

				gameState.subNumOfhandCard();
				//gameState.summonAbilityTrigger(out,unit);

				if (WzyUtils.isStop){
					WzyUtils.isStop = false;
					return;
				}



				//	BasicCommands.drawCard(out,gameState.card.get(cardID+1),gameState.currentCardPos+1,0);

			}
			else {
				BasicCommands.addPlayer1Notification(out, "mana is not enough!", 2);
			}
		}


		//move unit to tile
		//unitCheck（）is in GameState
		//move
		if (gameState.unitCheck(gameState.tile) &&
				gameState.isHumanUnit(gameState.targetUnit) && info ){   // Check if there is a unit on this tile
			BasicCommands.addPlayer1Notification(out, "attacker selected", 2);
			if(gameState.attacker!=null) {
				//moveHighlightPos(gameState.getUnitx(), gameState.getUnity(), gameState);
				gameState.moveHighlightPos(gameState.getUnitx(), gameState.getUnity());
				ArrayList <ArrayList<Integer>> moveHighlighted=gameState.moveHighlight;
				moveHighlightTile(out, 0,  moveHighlighted);
			}
			gameState.attacker = gameState.targetUnit;
			//highlightTile(tilex, tiley, out, 1);
			//moveHighlightPos(tilex, tiley, gameState);
			gameState.moveHighlightPos(tilex, tiley);
			ArrayList <ArrayList<Integer>> moveHighlighted = gameState.moveHighlight;
			moveHighlightTile(out, 1, moveHighlighted);
			gameState.setUnitx(tilex);
			gameState.setUnity(tiley);
			gameState.unitSeclected=true;
			//add by red
			DrawRedTile.drawRedTile(out,gameState.redlight);
//			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			return;   // Save the unit, return, and proceed to the next click
		}else if (gameState.unitSeclected){
			gameState.unitSeclected=false;
			// If there are no tiles on it, the target tile will be saved because unitCheck is running
			if (gameState.targetUnit != null && !gameState.unitCheck(gameState.tile) ) { // If there is a target to move
				if(gameState.attacker.getMoved()) {
					BasicCommands.addPlayer1Notification(out, "can not move again in this turn", 2);
				}
				else if(gameState.provockCheck(gameState.attacker)){
					BasicCommands.addPlayer1Notification(out, "Unit is provoked", 2);
				}
				else {
					gameState.ifTileClicked = false;
					//moveHighlightPos(gameState.getUnitx(), gameState.getUnity(), gameState);
					gameState.moveHighlightPos(gameState.getUnitx(), gameState.getUnity());
					ArrayList <ArrayList<Integer>> moveHighlighted= gameState.moveHighlight;
					moveHighlightTile(out, 0, moveHighlighted);
					if(isMoving(tilex, tiley, moveHighlighted)) {
						BasicCommands.addPlayer1Notification(out, "move", 2);
						BasicCommands.moveUnitToTile(out, gameState.targetUnit, gameState.targetTile);
						gameState.targetUnit.setPositionByTile(gameState.targetTile);
						gameState.attacker.setMoved(true);
						if(gameState.attacker.getAttackTwice() && !gameState.attacker.getMovedTwice()) {
							gameState.attacker.setMovedTwice(true);
							gameState.attacker.setMoved(false);
						}
					}

					gameState.attacker = null;
					gameState.targetUnit = null;   // Set the target blank after moving
					//highlightTile(gameState.getUnitx(), gameState.getUnity(), out, 0);
					try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

				}
			}

			//If the first click selected a Unit (labeled as a attacker), the second click selected a Unit (labeled as Defender) would trigger an attack judgment
			if(gameState.attacker != null && gameState.unitCheck(gameState.tile) && gameState.attacker != gameState.targetUnit && !gameState.isHumanUnit(gameState.targetUnit)) {
				gameState.defender = gameState.targetUnit;
				for (Unit u:
						gameState.aiUnit) {
					System.out.println(u.getUnitName());
					System.out.println(u.getHealth());
				}

				BasicCommands.addPlayer1Notification(out, "defender selected", 2);
				// If within attack range, attacker plays attack animation, Defender dashes blood
				if(gameState.rangeCheck(gameState.attacker, gameState.defender)) {
					if(gameState.attacker.getAttacked()) {
						BasicCommands.addPlayer1Notification(out, "can not attack again in this turn", 2);
					}
					else if(gameState.provockCheck(gameState.attacker) && !gameState.defender.getProvoker()){
						BasicCommands.addPlayer1Notification(out, "can only attack the provoker", 2);
					}
					else {
						// Remote attack
						if (gameState.attacker.getUnitName().equals("Fire Spitter") ||gameState.attacker.getUnitName().equals("Pyromancer") ){
							// playProjectileAnimation
							BasicCommands.addPlayer1Notification(out, "playProjectileAnimation", 2);
							EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
							BasicCommands.playUnitAnimation(out, gameState.attacker, UnitAnimationType.attack);
							Tile tile = BasicObjectBuilders.loadTile(gameState.attacker.getPosition().getTilex(),gameState.attacker.getPosition().getTiley());
							Tile tile2 = BasicObjectBuilders.loadTile(gameState.defender.getPosition().getTilex(),gameState.defender.getPosition().getTiley());
							try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.playProjectileAnimation(out, projectile, 0, tile, tile2);
						}else{
							BasicCommands.addPlayer1Notification(out, "attack", 2);
							BasicCommands.playUnitAnimation(out, gameState.attacker, UnitAnimationType.attack);
						}
						gameState.moveHighlightPos(gameState.getUnitx(), gameState.getUnity());
						ArrayList <ArrayList<Integer>> moveHighlighted= gameState.moveHighlight;
						moveHighlightTile(out, 0, moveHighlighted);
						BasicCommands.setUnitHealth(out, gameState.defender, gameState.defender.getHealth() - gameState.attacker.getAttack());
				//		gameState.avatarHealthChange(out,gameState.defender,gameState.defender.getHealth());
						if(gameState.defender.getHealth() - gameState.attacker.getAttack() > 0 && gameState.rangeCheck(gameState.defender, gameState.attacker)){//>1 可以反击的情况ya
//							System.out.println(gameState.attacker.getAttack());
//							System.out.println(gameState.defender.getHealth());
							BasicCommands.addPlayer1Notification(out, "defender can attack back", 2);
							BasicCommands.playUnitAnimation(out, gameState.defender, UnitAnimationType.attack);



							if(gameState.attacker.getHealth() - gameState.defender.getAttack()>0) {// The attacker is fired back but still alive
//								System.out.println(gameState.defender.getAttack());
//								System.out.println(gameState.attacker.getHealth());
								BasicCommands.setUnitHealth(out, gameState.attacker, gameState.attacker.getHealth() - gameState.defender.getAttack());
								gameState.avatarHealthChange(out,gameState.attacker,gameState.attacker.getHealth());


							}else {//The attacker was killed by the counterattack
								//System.out.println(gameState.defender.getAttack());
								//System.out.println(gameState.attacker.getHealth());
								BasicCommands.setUnitHealth(out, gameState.attacker, 0);
								try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
								BasicCommands.playUnitAnimation(out, gameState.attacker, UnitAnimationType.death);// Play the death animation directly after the kill
								try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
								gameState.deathAbilityTrigger(out);
								BasicCommands.deleteUnit(out, gameState.attacker);
								gameState.unit.remove(gameState.attacker);

								try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
							}
						}else if(gameState.defender.getHealth() - gameState.attacker.getAttack()>0){// Defender has no health to fight back
							//System.out.println(gameState.attacker.getAttack());
							//System.out.println(gameState.defender.getHealth());
							BasicCommands.setUnitHealth(out, gameState.defender, gameState.defender.getHealth() - gameState.attacker.getAttack());
							gameState.avatarHealthChange(out,gameState.defender,gameState.defender.getHealth());
						}
						// If Defender's health is equal to or less than 0 then the death animation is played and Defender is deleted. Defender is attacked and killed
						else {
							BasicCommands.setUnitHealth(out, gameState.defender, 0);
							BasicCommands.addPlayer1Notification(out, "target down", 2);
							BasicCommands.playUnitAnimation(out, gameState.defender, UnitAnimationType.death);
							try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
							BasicCommands.deleteUnit(out, gameState.defender);
							gameState.aiUnit.remove(gameState.defender);
						}
						try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
						gameState.attacker.setAttacked(true);
						if(gameState.attacker.getAttackTwice() && !gameState.attacker.getAttackedTwice()) {
							gameState.attacker.setAttackedTwice(true);
							gameState.attacker.setAttacked(false);
						}
					}
				}

				// If you are not in range of the attack, the popup will indicate "out of range"
				else {
					BasicCommands.addPlayer1Notification(out, "out of range", 2);
					try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				}
				gameState.attacker = null;
				gameState.defender = null;
				gameState.targetUnit = null;
				gameState.targetTile = null;

			}

		}

	}
}