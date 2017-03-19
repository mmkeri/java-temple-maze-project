package integration;

import game.GameState;
import org.junit.*;
import student.Explorer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Set of integration tests on the Game + Explorer
 */
public class ExplorerGameIntegrationShould {

    PrintStream originalOut, originalError;
    ByteArrayOutputStream testOut, testError;

    @Before
    public void setUp() {
        // capture current System.out and System.err
        originalOut = System.out;
        originalError = System.err;

        // now create test versions that record to memory buffers and redirect the global ones to the test ones
        testOut = new ByteArrayOutputStream();
        testError = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut, true));
        System.setErr(new PrintStream(testError, true));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalError);
    }

    @Test
    public void testWithSeed123() {
        verifySuccessWithSeed(123);
    }

    @Test
    public void testWithRandomSeed() {
        verifySuccessWithSeed(new Random().nextLong());
    }

    private void verifySuccessWithSeed(long seed) {
        GameState.runNewGame(seed, false);

        String testOutput = testOut.toString();
        String testErrors = testError.toString();


    }

}
