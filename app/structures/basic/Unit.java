package structures.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import commands.BasicCommands;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile. 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	int id;
	int range;
	int health;
	int attack;
	boolean attacked = false;
	boolean moved = false;
	boolean flying = false;
	boolean heal = false;
	boolean provoker = false;
	boolean attackTwice = false;
	boolean attackedTwice = false;
	boolean movedTwice = false;
	boolean airdrop = false;
	boolean spellThief;
	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;
	String unitName;
	
	public Unit() {}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(0,0,0,0);
		this.correction = correction;
		this.animations = animations;
	}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}
	
	
	
	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction, int range) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
		this.range = range;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UnitAnimationType getAnimation() {
		return animation;
	}
	public void setAnimation(UnitAnimationType animation) {
		this.animation = animation;
	}

	public ImageCorrection getCorrection() {
		return correction;
	}

	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public UnitAnimationSet getAnimations() {
		return animations;
	}

	public void setAnimations(UnitAnimationSet animations) {
		this.animations = animations;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setAttack(int attack){
		this.attack = attack;
	}
	
	public int getAttack() {
		return attack;
	}

	public String getUnitName() {return unitName; }

	public void setUnitName(String unitName) {this.unitName = unitName; }
	
	public boolean getProvoker() {
		return provoker;
	}

	public void setProvoker(boolean provoker) {
		this.provoker = provoker;
	}
	
	public boolean getAttackTwice() {
		return attackTwice;
	}

	public void setAttackTwice(boolean attackTwice) {
		this.attackTwice = attackTwice;
	}
	
	public boolean getAirdrop() {
		return airdrop;
	}

	public void setAirdrop(boolean airdrop) {
		this.airdrop = airdrop;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}


	public boolean isHeal() {
		return heal;
	}

	public void setHeal(boolean heal) {
		this.heal = heal;
	}
		
	public boolean getAttacked() {
		return attacked;
	}

	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	public boolean getMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public boolean isSpellThief() {
		return spellThief;
	}

	public void setSpellThief(boolean spellThief) {
		this.spellThief = spellThief;
	}


	public boolean getAttackedTwice() {
		return attackedTwice;
	}

	public void setAttackedTwice(boolean attackedTwice) {
		this.attackedTwice = attackedTwice;
	}

	public boolean getMovedTwice() {
		return movedTwice;
	}

	public void setMovedTwice(boolean movedTwice) {
		this.movedTwice = movedTwice;
	}

	/**
	 * This command sets the position of the Unit to a specified
	 * tile.
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		this.position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
	}
	
	
}
