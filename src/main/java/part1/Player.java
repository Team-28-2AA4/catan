package part1;
import java.util.Random;

public abstract class Player {

    private final int playerId;

    private final int[] resourceCounts = new int[ResourceType.values().length];
    private int[] buildingsCounts = new int[BuildingKind.values().length];

    private int roadsCount;
    private int victoryPoints;
    private int longestRoadStreak;
    private int turns;

    private final Random r;

    public Player(int playerId){

        this.playerId = playerId;
        this.roadsCount = 0;
        this.victoryPoints = 0;
        this.longestRoadStreak = 0;
        this.turns = 0;
        this.r = new Random();

    }

    public abstract void turn(Board board);


    public int getPlayerId(){
        return playerId;
    }

    public int diceRoll(){

        int die1 = r.nextInt(6) + 1;
        int die2 = r.nextInt(6) + 1;
        return die1 + die2;

    }


























    // RESOURCES

    public int getResourceCount(ResourceType type){
        return resourceCounts[type.ordinal()];
    }

    public void addResource(ResourceType type, int amount) {
        resourceCounts[type.ordinal()] += amount;
    }

    public boolean hasAtLeastXResources(ResourceType type, int amount){
        return resourceCounts[type.ordinal()] >= amount;
    }
    
    public void spendResource(Resource type, int amount){
        int index = type.ordinal();
        if (resourceCounts[index] < amount) {
            throw new IllegalStateException("Not enough" + type);
        }
        resourceCounts[index] -= amount;
    }

    
    
    // BUILDINGS
    
    public int getBuildingCount(BuildingKind type){
        return buildingCounts[type.ordinal()];
    }

    public void addBuilding(BuildingKind type, int amount) {
        buildingCounts[type.ordinal()] += amount;
    }

    public boolean hasAtLeastXBuildings(BuildingKind type, int amount){
        return buildingCounts[type.ordinal()] >= amount;
    }
    
    public void spendBuilding(BuildingKind type, int amount){
        int index = type.ordinal();
        if (buildingCounts[index] < amount) {
            throw new IllegalStateException("Not enough" + type);
        }
        buildingCounts[index] -= amount;
    }



    // ROADS

    public int getRoadsCount() {
        return roadsCount;
    }

    public void addRoads(int amount){
        roadsCount += amount;
    }

    public boolean hasAtLeastXRoads(int amount){
        return roadsCount >= amount;
    }

    public void spendRoads(int amount){
        if (roadsCount < amount){
            throw new IllegalStateException("Not enough roads");
        }
        roadsCount -= amount;
    }
}