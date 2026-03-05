package part1;

import java.io.FileWriter;
import java.io.IOException;

/**
 * StateJsonWriter
 * Writes the two JSON files consumed by the Python visualizer:
 * - base_map.json  — static board layout (tiles, resources, dice numbers)
 * - state.json     — dynamic game state (placed roads and buildings)
 *
 * Call writeBaseMap() once at game start and writeState() after every turn.
 *
 * @author Team 28
 */
public class StateJsonWriter
{
    /**
     * Cube coordinates (q, s, r) for each tile index 0–18.
     * Order matches Board.terrainTilesSetup exactly.
     */
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

    /**
     * Player colour names indexed by player ID (0–3).
     * Matches the colours supported by the Python visualizer.
     */
    private static final String[] PLAYER_COLOURS = {"RED", "BLUE", "ORANGE", "WHITE"};

    /**
     * Writes base_map.json from the current board's tile layout.
     * This file is static for the lifetime of a game and only needs to be written once.
     *
     * @param board      the game board
     * @param outputPath full path to write base_map.json
     */
    public static void writeBaseMap(Board board, String outputPath)
    {
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
     * Writes state.json reflecting all currently placed roads and buildings.
     * Call this after every turn so the visualizer stays in sync.
     *
     * @param board      the game board
     * @param outputPath full path to write state.json
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

    /**
     * Remaps a Java board node ID to the equivalent catanatron node ID.
     *
     * The two implementations agree on all 54 nodes except for a swap of
     * nodes 45 and 47 in the outer ring.  Applying this remap before writing
     * roads and buildings keeps the visualizer's edge-validity check happy.
     *
     * @param javaNodeId node ID from the Java Board
     * @return node ID as used by catanatron
     */
    private static int toCatanatronNodeId(int javaNodeId)
    {
        if (javaNodeId == 45)
        {
            return 47;
        }
        if (javaNodeId == 47)
        {
            return 45;
        }
        return javaNodeId;
    }

    /**
     * Maps a ResourceType to the name string expected by the Python visualizer.
     * Returns "null" (the JSON literal) for the desert tile.
     *
     * @param resourceType resource type from the Java enum
     * @return JSON value string
     */
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

    /**
     * Maps a player ID to a colour name string recognised by the Python visualizer.
     *
     * @param playerId integer player ID (0–3)
     * @return colour name string
     */
    private static String toPlayerColour(int playerId)
    {
        if (playerId >= 0 && playerId < PLAYER_COLOURS.length)
        {
            return PLAYER_COLOURS[playerId];
        }
        return "WHITE";
    }

    /**
     * Writes content to a file, overwriting it if it already exists.
     * Silently suppresses IO errors so a missing visualize directory
     * does not crash the game.
     *
     * @param path    file path to write
     * @param content string content to write
     */
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
