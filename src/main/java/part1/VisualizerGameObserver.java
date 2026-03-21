package part1;

/**
 * VisualizerGameObserver
 * Keeps the Python visualizer in sync with the current board state.
 * Writes:
 * - base_map.json once at game start
 * - state.json whenever the board state changes
 *
 * @author Team 28
 */
public class VisualizerGameObserver implements GameObserver {

    private final String visualizerDir;

    /**
     * Creates a visualizer observer that writes JSON files into the given directory.
     *
     * @param visualizerDir path to the visualizer directory
     */
    public VisualizerGameObserver(String visualizerDir) {
        this.visualizerDir = visualizerDir;
    }

    @Override
    public void onGameStarted(Board board) {
        StateJsonWriter.writeBaseMap(board, visualizerDir + "base_map.json");
    }

    @Override
    public void onStateChanged(Board board) {
        StateJsonWriter.writeState(board, visualizerDir + "state.json");
    }
}
