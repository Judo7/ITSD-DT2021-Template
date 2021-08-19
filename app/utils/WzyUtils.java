package utils;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;

import java.util.HashMap;
import java.util.Map;

public class WzyUtils {

    public static boolean isStop=false;
    public static boolean isReturn=false;
    public static int notUsedHandPosition=-1;


    //Spell card
    public static String[] names ={
            "Sundrop Elixir",
            "Truestrike",
            "Entropic Decay",
            "Staff of Y'Kir'"
    };

    public static String[] deck1Units = {
            StaticConfFiles.u_azure_herald,
            StaticConfFiles.u_azurite_lion,
            StaticConfFiles.u_comodo_charger,
            StaticConfFiles.u_fire_spitter,
            StaticConfFiles.u_hailstone_golem,
            StaticConfFiles.u_ironcliff_guardian,
            StaticConfFiles.u_pureblade_enforcer,
            StaticConfFiles.u_silverguard_knight,
    };

    public static Map<String,String> deck1UnitsMaps = new HashMap<String, String>();
    public static Map<String,Integer> deckUnitsHealthMaps = new HashMap<String, Integer>();
    public static boolean isMagic(Card card){
        for(String name:names){
            if (card.getCardname().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void unitDeath(Unit unit, ActorRef out){
        BasicCommands.setUnitHealth(out, unit, 0);
        BasicCommands.playUnitAnimation(out,unit, UnitAnimationType.death);
        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
        BasicCommands.deleteUnit(out, unit);
    }

    public static void conjuration(Card card,Unit unit,ActorRef out){
//        card.getCardname().
        System.out.println(card.getCardname());
        if (card.getCardname().equals("Sundrop Elixir")){
            if (deckUnitsHealthMaps.get(unit.getUnitName())!=null && unit.getHealth()+5>=deckUnitsHealthMaps.get(unit.getUnitName())){
                BasicCommands.setUnitHealth(out, unit, deckUnitsHealthMaps.get(unit.getUnitName()));
            } else {
                BasicCommands.setUnitHealth(out, unit, unit.getHealth()+5);
            }
            try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            isStop=true;
        }
        if(card.getCardname().equals("Entropic Decay")){
            unitDeath(unit,out);
            isStop=true;
        }
        if (card.getCardname().equals("Truestrike")){
            if ((deck1UnitsMaps.get(unit.getUnitName())==null)){
                if (unit.getHealth()-2<=0){
                    unitDeath(unit,out);
                }else {
                    BasicCommands.setUnitHealth(out, unit, unit.getHealth()-2);
                    try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                }
                isStop=true;
            }else{
                BasicCommands.addPlayer1Notification(out, "Can't be used on friend", 2);
                card.setUsed(false);
                card.setHandPosition(WzyUtils.notUsedHandPosition);
                isReturn=true;
            }
        }
        if(card.getCardname().equals("Staff of Y'Kir'")){
            BasicCommands.setUnitHealth(out, unit, unit.getAttack()+2);
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            isStop=true;
        }
    }

    public static String[] deck1Cards = {
            StaticConfFiles.c_azure_herald,
            StaticConfFiles.c_azurite_lion,
            StaticConfFiles.c_comodo_charger,
            StaticConfFiles.c_fire_spitter,
            StaticConfFiles.c_hailstone_golem,
            StaticConfFiles.c_ironcliff_guardian,
            StaticConfFiles.c_pureblade_enforcer,
            StaticConfFiles.c_silverguard_knight,
            StaticConfFiles.c_sundrop_elixir,
            StaticConfFiles.c_truestrike,
            StaticConfFiles.humanAvatar
    };

    static {
        deck1UnitsMaps.put("Azure Herald",StaticConfFiles.u_azure_herald);
        deck1UnitsMaps.put("Azurite Lion",StaticConfFiles.u_azurite_lion);
        deck1UnitsMaps.put("Comodo Charger",StaticConfFiles.u_comodo_charger);
        deck1UnitsMaps.put("Fire Spitter",StaticConfFiles.u_fire_spitter);
        deck1UnitsMaps.put("Hailstone Golem",StaticConfFiles.u_hailstone_golem);
        deck1UnitsMaps.put("Ironcliff Guardian",StaticConfFiles.u_ironcliff_guardian);
        deck1UnitsMaps.put("Pureblade Enforcer",StaticConfFiles.u_pureblade_enforcer);
        deck1UnitsMaps.put("Silverguard Knight",StaticConfFiles.u_silverguard_knight);
        deck1UnitsMaps.put("humanAvatar",StaticConfFiles.humanAvatar);

        deckUnitsHealthMaps.put("Azure Herald",4);
        deckUnitsHealthMaps.put("Azurite Lion",3);
        deckUnitsHealthMaps.put("Comodo Charger",3);
        deckUnitsHealthMaps.put("Fire Spitter",2);
        deckUnitsHealthMaps.put("Hailstone Golem",6);
        deckUnitsHealthMaps.put("Ironcliff Guardian",10);
        deckUnitsHealthMaps.put("Pureblade Enforcer",4);
        deckUnitsHealthMaps.put("Silverguard Knight",5);
        deckUnitsHealthMaps.put("humanAvatar",20);

        deckUnitsHealthMaps.put("Planar Scout",1);
        deckUnitsHealthMaps.put("Blaze Hound",3);
        deckUnitsHealthMaps.put("Bloodshard Golem",3);
        deckUnitsHealthMaps.put("Pyromancer",1);
        deckUnitsHealthMaps.put("Rock Pulveriser",4);
        deckUnitsHealthMaps.put("Serpenti",4);
        deckUnitsHealthMaps.put("WindShrike",3);
        deckUnitsHealthMaps.put("aiAvatar",20);
    }

}
