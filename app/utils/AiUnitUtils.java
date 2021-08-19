package utils;

import structures.basic.Card;

public class AiUnitUtils {
    public static String[] names ={
            "Blaze Hound",
            "Bloodshard Golem",
            "Hailstone Golem",
            "Planar Scout",
            "Pyromancer",
            "Serpenti",
            "Rock Pulveriser",
            "Windshrike"
    };

    public static int getUnitAttack(Card card) {
        if (card.getCardname().equals(names[0]) || card.getCardname().equals(names[1]) || card.getCardname().equals(names[2]) || card.getCardname().equals(names[7])) {
            return 4;
        }
        if (card.getCardname().equals(names[3]) || card.getCardname().equals(names[4])){
            return 2;
        }
        if (card.getCardname().equals(names[6])){
            return 1;
        }
        if (card.getCardname().equals(names[5])){
            return 7;
        }
        return 0;
    }

    public static int getUnitHealth(Card card) {
        if (card.getCardname().equals(names[0]) || card.getCardname().equals(names[1]) || card.getCardname().equals(names[7])) {
            return 3;
        }
        if (card.getCardname().equals(names[2]) ){
            return 6;
        }
        if (card.getCardname().equals(names[3]) ||  card.getCardname().equals(names[4])){
            return 1;
        }
        if (card.getCardname().equals(names[5]) || card.getCardname().equals(names[6])){
            return 4;
        }
        return 0;
    }
}
