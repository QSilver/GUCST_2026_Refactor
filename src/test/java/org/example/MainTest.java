package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void name() {
        String expected = """
                Book (x1): 11.61
                Music CD (x2): 29.43
                Headache pills (x3): 26.33
                Chocolate bar (x5): 3.97
                Sales Taxes: 5.55
                Total: 71.34
                You saved: 10.18
                Loyalty points earned: 160
                Status: Gold Member
                """.replace("\n", lineSeparator());

        new BillCalc().calculate(false, true);

        assertEquals(expected, outContent.toString());
    }
}