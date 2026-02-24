package me.jishuna.regionsystem;

import org.bukkit.Chunk;

public class Utils {

    public static long getChunkKey(Chunk chunk) {
        return ((long) chunk.getZ() << 32) | (chunk.getX() & 0xFFFFFFFFL);
    }

    public static long getChunkKey(int x, int z) {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

}
