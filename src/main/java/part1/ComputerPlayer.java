package part1;

public class ComputerPlayer extends Player {




    public ComputerPlayer(){

        super();

    } 
    
    @Override
    public void turn(Board board){

        // start turn with dice roll
        int roll = diceRoll();

        // add resources to hand if applicable based on roll and board
        for (int i = 0; tileId < Board.TILE_COUNT; tileId++) {

            Board.TerrainTile tile = board.getTile(tileId);

        }
        
        getResourceTypesForDiceToken

    }
}