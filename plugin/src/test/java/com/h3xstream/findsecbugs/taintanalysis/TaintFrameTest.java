/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class TaintFrameTest {

    @BeforeClass
    public void setUp() {
        //Make sure their are not side effect when print the stack frames
        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(false);
    }

    @Test
    public void validateSimpleTaintFrame() {
        TaintFrame frame = new TaintFrame(0);
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String debugOutput = frame.toString();
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));
    }

    @Test
    public void validateSimpleTaintFrameWithLocal() {

        TaintFrame frame = new TaintFrame(4);
        frame.setValue(0, new Taint(Taint.State.TAINTED));
        frame.setValue(1, new Taint(Taint.State.TAINTED));
        frame.setValue(2, new Taint(Taint.State.UNKNOWN));
        frame.setValue(3, new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String[] variables = new String[] {"safe","null_value","external","modifyMe"};
        String debugOutput = frame.toString(variables);
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));

        for(String variable : variables) {
            assertTrue(debugOutput.contains("| "+variable));
        }
    }


    @Test
    public void validateSimpleTaintFrameWithLocalUnSet() {

        TaintFrame frame = new TaintFrame(4);
        frame.setValue(0, new Taint(Taint.State.TAINTED));
        frame.setValue(1, new Taint(Taint.State.TAINTED));
        //Not all slot are set
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String[] variables = new String[] {"safe","null_value","external","modifyMe"};
        String debugOutput = frame.toString(variables);
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));

        for(String variable : variables) {
            assertTrue(debugOutput.contains("| "+variable));
        }
    }


}
