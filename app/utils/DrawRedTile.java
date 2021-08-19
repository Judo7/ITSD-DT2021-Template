package utils;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.ArrayList;

public class DrawRedTile {
    public static void drawRed(ActorRef out, GameState gameState, Unit unit){
        int x = unit.getPosition().getTilex();
        int y = unit.getPosition().getTiley();
        drawTile(x,y+1,gameState,out);
        drawTile(x+1,y+1,gameState,out);
        drawTile(x+1,y-1,gameState,out);
        drawTile(x+1,y,gameState,out);
        drawTile(x-1,y+1,gameState,out);
        drawTile(x-1,y+1,gameState,out);
        drawTile(x-1,y-1,gameState,out);
        drawTile(x-1,y,gameState,out);
        drawTile(x,y-1,gameState,out);


    }
    public static boolean isTrue(int x,int y){
        if (x>=0&&x<=8&&y>=0&&y<=4){
            return true;
        }
        return false;
    }

    public static void drawTile(int x,int y,GameState gameState,ActorRef out){
        if (isTrue(x,y)&&gameState.aiUnitCheck(BasicObjectBuilders.loadTile(x, y))) {
            BasicCommands.drawTile(out, BasicObjectBuilders.loadTile(x, y ), 2);
        }
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    public static void  drawRedTile(ActorRef out, ArrayList <ArrayList<Integer>> redlighted){   	
        for(ArrayList<Integer> pos:redlighted) {
            int x = pos.get(0);
            int y=pos.get(1);
            Tile tile = BasicObjectBuilders.loadTile(x,y);
            BasicCommands.drawTile(out, tile, 2);
        }
    }
}
