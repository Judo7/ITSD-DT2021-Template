package utils;

import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;

public class UnitUtils {
    public static String[] names ={
            "Azure Herald",
            "Azurite Lion",
            "Comodo Charger",
            "Fire Spitter",
            "Hailstone Golem",
            "Ironcliff Guardian",
            "Pureblade Enforcer",
            "Silverguard Knight"
    };

    public static int getUnitHealth(Card card){
        if (card.getCardname().equals(names[0]) || card.getCardname().equals(names[6]) ){

            return 4;
            
        }
        if (card.getCardname().equals(names[1]) || card.getCardname().equals(names[2]) ){
            return 3;
        }

        if (card.getCardname().equals(names[3])){
            return 2;
        }
        if (card.getCardname().equals(names[4])){
            return 6;
        }
        if (card.getCardname().equals(names[5])){
            return 10;
        }

        if (card.getCardname().equals(names[7])){
            return 5;
        }

        return 0;
    }

    public static int getUnitHealth(Unit unit){
        if (unit.getUnitName().equals(names[0]) || unit.getUnitName().equals(names[6]) ){

            return 4;
        }
        if (unit.getUnitName().equals(names[1]) || unit.getUnitName().equals(names[2]) ){
            return 3;
        }

        if (unit.getUnitName().equals(names[3])){
            return 2;
        }
        if (unit.getUnitName().equals(names[4])){
            return 6;
        }
        if (unit.getUnitName().equals(names[5])){
            return 10;
        }

        if (unit.getUnitName().equals(names[7])){
            return 5;
        }

        if (unit.getUnitName().equals("humanAvatar") || unit.getUnitName().equals("aiAvatar")){
            return 20;
        }

        return 0;
    }


    public static int getUnitAttack(Card card){
        if (card.getCardname().equals(names[0]) || card.getCardname().equals(names[2]) || card.getCardname().equals(names[6]) || card.getCardname().equals(names[7])){
            return 1;
        }
        if (card.getCardname().equals(names[1]) ){
            return 2;
        }
        if (card.getCardname().equals(names[3]) || card.getCardname().equals(names[5])){
            return 3;
        }
        if (card.getCardname().equals(names[4])){
            return 4;
        }
        return 0;
    }

}
