package structures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.UnitUtils;
import utils.WzyUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {

    public List<Unit> unit = new ArrayList<Unit>();
    public List<Unit> aiUnit = new ArrayList<>();
    public Tile tile = new Tile();
    public List<Card> card = new ArrayList<Card>();
    public List<Card> aiCard = new ArrayList<Card>();
    public boolean ifCardClicked;
    public boolean ifTileClicked;
    public Unit targetUnit;
    public Tile targetTile;
    public Unit attacker;
    public Unit defender;
    public boolean ifMagicClicked;
    public boolean unitSeclected=false;
    public ArrayList <ArrayList<Integer>> moveHighlight;
    public ArrayList <ArrayList<Integer>> redlight;

    public int currentCardPos;
    public Card currentCard;

    private int unitx;
    private int unity;


    private int numOfHandCard = 0;
    private int posOfDeck = 0;
    private int posOfUnitDeck = 0;

    public int getPosOfUnitDeck() {
        return posOfUnitDeck;
    }

    public void addPosOfUnitDeck() {
        this.posOfUnitDeck++;
    }


    String[] deck1Cards = {
            StaticConfFiles.c_sundrop_elixir,
            StaticConfFiles.c_truestrike,
            StaticConfFiles.c_azure_herald,
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

    //Game round number
    private int turns = 1;

    public int getTurns() {
        return turns;
    }
    public void addTurns(int turn) {
        this.turns=turn;
    }

     public int getUnitx() {
        return unitx;
    }
    public void setUnitx(int unitx) {
        this.unitx = unitx;
    }
    public int getUnity() {
        return unity;
    }
    public void setUnity(int unity) {
        this.unity = unity;
    }


    public Player humanPlayer = new Player(0, 0);

    public Player aiPlayer = new Player(0, 0);

    public int getNumOfHandCard() {
        return numOfHandCard;
    }

    public void addNumOfHandCard() {
        this.numOfHandCard++;
    }

    public void subNumOfhandCard() {
        this.numOfHandCard--;
    }


    public int getPosOfDeck() {
        return posOfDeck;
    }

    public void addPosOfDeck() {
        this.posOfDeck++;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    // Check if there is a unit on the tile
    public boolean unitCheck(Tile tile) {
        for (Unit tu : unit) {
            if (tu.getPosition().getTilex() == tile.getTilex() && tu.getPosition().getTiley() == tile.getTiley()) {
                targetUnit = tu;  // If so, this unit is listed as the target unit to be moved
                return true;
            }         
        }
        for (Unit tu : aiUnit) {
            if (tu.getPosition().getTilex() == tile.getTilex() && tu.getPosition().getTiley() == tile.getTiley()) {
                targetUnit = tu;  // If so, this unit is listed as the target unit to be moved
                return true;
            }
        }
        targetTile = tile;    // If not, the tile will be the target tile
        return false;
    }

    //判断这个tile上有没有aiunit
    public boolean aiUnitCheck(Tile tile) {
        for (Unit tu : aiUnit) {
            if (tu.getPosition().getTilex() == tile.getTilex() && tu.getPosition().getTiley() == tile.getTiley()) {
                return true;
            }
        }
        targetTile = tile;    // If not, the tile will be the target tile
        return false;
    }

    //判断这个tile上有没有aiunit
    public boolean aiUnitCheck(int x,int y) {
        for (Unit tu : aiUnit) {
            if ((tu.getPosition().getTilex() == x && tu.getPosition().getTiley() == y) && rangeCheck(targetUnit,tu)) {
                return true;
            }
        }
        targetTile = tile;    // If not, the tile will be the target tile
        return false;
    }


    //用于判断defender是否再attacker的攻击范围内
    public boolean rangeCheck(Unit attacker, Unit defender) {
        if(attacker.getRange() >= Math.abs(attacker.getPosition().getTilex() - defender.getPosition().getTilex()) && attacker.getRange() >= Math.abs(attacker.getPosition().getTiley() - defender.getPosition().getTiley())) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void addRedlight(Unit attacker) {
    	for(Unit u:aiUnit) {
        if(attacker.getRange() >= Math.abs(attacker.getPosition().getTilex() - u.getPosition().getTilex()) && attacker.getRange() >= Math.abs(attacker.getPosition().getTiley() - u.getPosition().getTiley())) {
        	ArrayList<Integer> pos = new ArrayList<Integer>();
            pos.add(u.getPosition().getTilex());
            pos.add(u.getPosition().getTiley());
            redlight.add(pos);
        }
    	}
    
    }
    
     //story24
     //flying-ai unit
     public void flyingCheck(ActorRef out, Tile tile) {//XY is the target tile
         int x = tile.getTilex();
         int y = tile.getTiley();
         if(!notInUnit(x, y)) {
             return;
         }
         for(Unit u: aiUnit) {
             if(u.isFlying()) {
                 BasicCommands.addPlayer1Notification(out, "flying", 2);
                 BasicCommands.moveUnitToTile(out, u, tile);
                 u.setPositionByTile(tile);
                 break;
             }
         }
     }

    //heal
    public boolean healCheck() {
         for (Unit u: unit) {
             if(u.isHeal()) {
                 if(unit.get(0).getHealth()<20) {
                    if(unit.get(0).getHealth()>=17) {
                        unit.get(0).setHealth(20);
                    }else {
                        unit.get(0).setHealth(unit.get(0).getHealth()+3);
                    }
                    return true;
                 }
             }
         }
         return false;
     }

    // Determined if the player is taunted like Unit on the move
    public boolean provockCheck(Unit attacker) {
        for (Unit u: aiUnit) {
            if(rangeCheck(attacker, u) && u != attacker) {
                if(u.getProvoker()) {
                    return true;
                }
            }
        }
        return false;
    }

    //Move the highlighted judgment
    public  boolean notInUnit(int x, int y) {
        for(Unit u: unit) {
            if (u.getPosition().getTilex() == x && u.getPosition().getTiley() == y) {
                return false;
            }  
        }
        for(Unit u: aiUnit) {
            if (u.getPosition().getTilex() == x && u.getPosition().getTiley() == y) {
             
                return false;
            }  
        }
        return true;
    }


    //Move the highlighted judgment
    public void   moveHighlightPos(int tilex, int tiley) {
        moveHighlight = new ArrayList<ArrayList<Integer>>();
        redlight = new ArrayList<ArrayList<Integer>>();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                int x=tilex+i;
                int y=tiley+j;
                
                if((i==-1 || i==1) && (j==-1 || j==1)){
                    if(!(x == tilex && y == tiley)) {
                        if(x>=0 && x <9 && y>=0 && y<5) {
                            
                            if(notInUnit(x, y)) {
                                ArrayList<Integer> pos = new ArrayList<Integer>();
                                pos.add(x);
                                pos.add(y);
                                moveHighlight.add(pos);
                            }

                        }
                    }
                }else {
                    if((x==tilex && y!=tiley) || (x!=tilex && y==tiley)) {
                        if(x>=0 && x <9 && y>=0 && y<5) {
                            if(notInUnit(x, y)) {
                                ArrayList<Integer> pos = new ArrayList<Integer>();
                                pos.add(x);
                                pos.add(y);
                                moveHighlight.add(pos);
                            }

                        }
                    }
                }
            }
        }
        for(Unit u:aiUnit) {
            if(attacker.getRange() >= Math.abs(attacker.getPosition().getTilex() - u.getPosition().getTilex()) && attacker.getRange() >= Math.abs(attacker.getPosition().getTiley() - u.getPosition().getTiley())) {
            	ArrayList<Integer> pos = new ArrayList<Integer>();
                pos.add(u.getPosition().getTilex());
                pos.add(u.getPosition().getTiley());
                redlight.add(pos);
            }
        	}
    }


    public boolean isIfMagicClicked() {
        return ifMagicClicked;
    }

    public void setIfMagicClicked(boolean ifMagicClicked) {
        this.ifMagicClicked = ifMagicClicked;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    // This is the Unit of the player
    public boolean isHumanUnit(Unit target) {
    	for (Unit u : unit) {
    		if(target == u) {
    			return true;
    		}  		
    	}
    	return false;
    }

    public void summonAbilityTrigger(ActorRef out,Unit u) {


        if ("Azure Herald".equals(u.getUnitName()) && u.getPosition().getTilex() != -1 && u.getPosition().getTiley() != -1) {
            BasicCommands.setUnitHealth(out, this.unit.get(0), this.unit.get(0).getHealth() + 3);
            if (this.unit.get(0).getHealth() > UnitUtils.getUnitHealth(this.unit.get(0))) {
                BasicCommands.setUnitHealth(out, this.unit.get(0), UnitUtils.getUnitHealth(this.unit.get(0)));
            }
        }



    }

    public void deathAbilityTrigger(ActorRef out) {
        for (Unit u : unit) {
            if ("Windshrike".equals(u.getUnitName()) && u.getPosition().getTilex() != -1 && u.getPosition().getTiley() != -1) {
                Card card = BasicObjectBuilders.loadCard(deck1Cards[this.getPosOfDeck()], this.getPosOfDeck(), Card.class);
                BasicCommands.addPlayer1Notification(out, u.getUnitName(), 2);
                BasicCommands.drawCard(out, card, this.getNumOfHandCard() + 1, 0);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.addNumOfHandCard();
                this.addPosOfDeck();
                return;
            }
        }
    }

    public void damageAbilityTrigger(ActorRef out) {
        for (Unit u : unit) {
            if ("Silverguard Knight".equals(u.getUnitName()) && u.getPosition().getTilex() != -1 && u.getPosition().getTiley() != -1) {
                BasicCommands.setUnitAttack(out, u, u.getAttack()+2);
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }

    public void SpellThiefTrigger (ActorRef out) {
        for(Unit u:unit) {
            if(u.isSpellThief() ) {
                int attackValue = u.getAttack()+1;
                u.setAttack(attackValue);
                int healthValue = u.getHealth()+1;
                u.setHealth(healthValue);
                BasicCommands.setUnitHealth(out, u, healthValue);
                BasicCommands.setUnitAttack(out, u, attackValue);
                try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }

    }

    String[] effects = {
            StaticConfFiles.f1_buff,
            StaticConfFiles.f1_inmolation,
            StaticConfFiles.f1_martyrdom,
            StaticConfFiles.f1_summon
    };

    public void avatarHealthChange(ActorRef out,Unit defender,int n){
        if (defender==this.unit.get(0)){
            humanPlayer.setHealth(n);
            BasicCommands.setPlayer1Health(out, humanPlayer);
            try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        }
        if (defender==this.aiUnit.get(0)){
            aiPlayer.setHealth(n);
            BasicCommands.setPlayer2Health(out, aiPlayer);
            try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}

