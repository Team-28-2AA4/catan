
/* This interface represents the base of undoable 
*moves
@author Team 28


*/
package part1;

public interface GameCommand {
    void execute(); //performs action

    void undo(); //reverses action
}
