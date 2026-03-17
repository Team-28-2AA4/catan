package part1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stores command history for undo/redo.
 */
public class CommandManager {

    private final Deque<GameCommand> undoStack;
    private final Deque<GameCommand> redoStack;

    public CommandManager() {
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
    }

    /**
     * Executes a command, pushes it to undo history, and clears redo history.
     *
     * @param command command to execute
     */
    public void executeCommand(GameCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    /**
     * Undoes the most recent command.
     *
     * @return true if undo happened, false otherwise
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        GameCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }

    /**
     * Redoes the most recently undone command.
     *
     * @return true if redo happened, false otherwise
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }

        GameCommand command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}