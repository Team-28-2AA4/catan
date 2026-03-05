package part1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes JSON files for the Python visualizer.
 * Call writeBaseMap() once at game start, writeState() after each turn.
 */
public class StateJsonWriter
{
    // Cube coords for each tile (matches Board.terrainTilesSetup order)
    private static final int[][] TILE_CUBE_COORDS = {
        { 0,  0,  0},
        { 0, -1,  1},
        {-1,  0,  1},
        {-1,  1,  0},
        { 0,  1, -1},
        { 1,  0, -1},
        { 1, -1,  0},
        { 0, -2,  2},
        {-1, -1,  2},
        {-2,  0,  2},
        {-2,  1,  1},
        {-2,  2,  0},
        {-1,  2, -1},
        { 0,  2, -2},
        { 1,  1, -2},
        { 2,  0, -2},
        { 2, -1, -1},
        { 2, -2,  0},
        { 1, -2,  1}
    };

    // Player colours for visualizer
    private static final String[] PLAYER_COLOURS = {"RED", "BLUE", "ORANGE", "WHITE"};

    // Maps Java node IDs to catanatron node IDs.
    // Java starts corners at NE, catanatron starts at N, so we need this lookup.
    private static final int[] JAVA_TO_CATANATRON_NODE = {
         1,  2,  3,  4,  5,  0,  6,  7,  8,  9,
        10, 11, 12, 14, 15, 13, 17, 18, 16, 20,
        21, 19, 22, 23, 24, 25, 26, 27, 28, 29,
        30, 31, 32, 33, 34, 36, 37, 35, 39, 38,
        41, 42, 40, 44, 43, 45, 47, 46, 48, 49,
        50, 51, 52, 53
    };

    /**
     * Writes base_map.json (static board layout).
     * Also increments game_id.txt for organizing renders.
     */
    public static void writeBaseMap(Board board, String outputPath)
    {
        String dir = outputPath.substring(0, outputPath.lastIndexOf('/') + 1);
        writeGameId(dir);
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"tiles\": [\n");

        for (int tileIndex = 0; tileIndex < Board.TILE_COUNT; tileIndex++)
        {
            Board.TerrainTile tile = board.getTile(tileIndex);
            int[] coords = TILE_CUBE_COORDS[tileIndex];

            int q = coords[0];
            int s = coords[1];
            int r = coords[2];

            String resourceValue = toVisualizerResourceName(tile.resourceType);
            String numberValue = (tile.diceToken == 0) ? "null" : String.valueOf(tile.diceToken);

            json.append("    { \"q\": ");
            json.append(q);
            json.append(", \"s\": ");
            json.append(s);
            json.append(", \"r\": ");
            json.append(r);
            json.append(", \"resource\": ");
            json.append(resourceValue);
            json.append(", \"number\": ");
            json.append(numberValue);
            json.append(" }");

            if (tileIndex < Board.TILE_COUNT - 1)
            {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n}");

        writeFile(outputPath, json.toString());
    }

    /**
     * Writes state.json with current roads/buildings.
     * Call after each turn to keep visualizer in sync.
     */
    public static void writeState(Board board, String outputPath)
    {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("  \"roads\": [\n");
        boolean firstRoad = true;
        for (int edgeIndex = 0; edgeIndex < Board.EDGE_COUNT; edgeIndex++)
        {
            Board.Road road = board.getRoad(edgeIndex);
            if (road == null)
            {
                continue;
            }

            Board.Edge edge = board.getEdge(edgeIndex);
            String ownerColour = toPlayerColour(road.ownerPlayerId);

            int nodeA = toCatanatronNodeId(edge.node1);
            int nodeB = toCatanatronNodeId(edge.node2);

            if (!firstRoad)
            {
                json.append(",\n");
            }
            json.append("    { \"a\": ");
            json.append(nodeA);
            json.append(", \"b\": ");
            json.append(nodeB);
            json.append(", \"owner\": \"");
            json.append(ownerColour);
            json.append("\" }");
            firstRoad = false;
        }
        json.append("\n  ],\n");

        json.append("  \"buildings\": [\n");
        boolean firstBuilding = true;
        for (int nodeId = 0; nodeId < Board.NODE_COUNT; nodeId++)
        {
            Board.Building building = board.getBuilding(nodeId);
            if (building == null)
            {
                continue;
            }

            String ownerColour = toPlayerColour(building.ownerPlayerId);
            String buildingType = (building.kind == BuildingKind.CITY) ? "CITY" : "SETTLEMENT";
            int catNodeId = toCatanatronNodeId(building.nodeId);

            if (!firstBuilding)
            {
                json.append(",\n");
            }
            json.append("    { \"node\": ");
            json.append(catNodeId);
            json.append(", \"owner\": \"");
            json.append(ownerColour);
            json.append("\", \"type\": \"");
            json.append(buildingType);
            json.append("\" }");
            firstBuilding = false;
        }
        json.append("\n  ]\n}");

        writeFile(outputPath, json.toString());
    }

    // Reads/increments/writes game_id.txt (starts at 1 if file missing)
    private static void writeGameId(String dir)
    {
        String path = dir + "game_id.txt";
        int currentId = 0;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            reader.close();
            if (line != null)
            {
                currentId = Integer.parseInt(line.trim());
            }
        }
        catch (IOException | NumberFormatException ignored)
        {
            currentId = 0;
        }

        writeFile(path, String.valueOf(currentId + 1));
    }

    // Converts Java node ID to catanatron node ID
    private static int toCatanatronNodeId(int javaNodeId)
    {
        return JAVA_TO_CATANATRON_NODE[javaNodeId];
    }

    // Converts Java ResourceType to visualizer string (returns "null" for desert)
    private static String toVisualizerResourceName(ResourceType resourceType)
    {
        if (resourceType == null)
        {
            return "null";
        }
        if (resourceType == ResourceType.LUMBER)
        {
            return "\"WOOD\"";
        }
        if (resourceType == ResourceType.WOOL)
        {
            return "\"SHEEP\"";
        }
        if (resourceType == ResourceType.GRAIN)
        {
            return "\"WHEAT\"";
        }
        if (resourceType == ResourceType.BRICK)
        {
            return "\"BRICK\"";
        }
        return "\"ORE\"";
    }

    // Converts player ID to colour string for visualizer
    private static String toPlayerColour(int playerId)
    {
        if (playerId >= 0 && playerId < PLAYER_COLOURS.length)
        {
            return PLAYER_COLOURS[playerId];
        }
        return "WHITE";
    }

    // Writes file, suppresses errors so missing visualize dir doesn't crash game
    private static void writeFile(String path, String content)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(content);
            fileWriter.close();
        }
        catch (IOException ioException)
        {
            System.out.println("Warning: could not write visualizer file at " + path + " (" + ioException.getMessage() + ")");
        }
    }
}
