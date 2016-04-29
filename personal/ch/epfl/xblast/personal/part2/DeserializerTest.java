package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class DeserializerTest {

    @Test
    public void test() {
            Level lvl=Level.DEFAULT_LEVEL;
            List<Byte> ser= GameStateSerializer.serialize(lvl.boardPainter(),lvl.initialGameState());
            GameState game = GameStateDeserializer.deserializeGameState(ser);
    }

}
