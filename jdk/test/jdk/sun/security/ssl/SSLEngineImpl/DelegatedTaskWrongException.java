/*
 * Copyright (c) 2003, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

//
// SunJSSE does not support dynamic system properties, no way to re-use
// system properties in samevm/agentvm mode.
//

/*
 * @test
 * @bug 4969459
 * @summary Delegated tasks are not reflecting the subclasses of SSLException
 * @library /javax/net/ssl/templates
 * @run main/othervm DelegatedTaskWrongException
 */

import javax.net.ssl.*;
import java.security.*;

public class DelegatedTaskWrongException extends SSLEngineTemplate {

    private static boolean debug = false;

    @Override
    protected SSLEngine configureServerEngine(SSLEngine engine) {
        engine.setUseClientMode(false);
        engine.setEnabledProtocols(new String [] { "TLSv1" });
        return engine;
    }

    @Override
    protected SSLEngine configureClientEngine(SSLEngine engine) {
        engine.setUseClientMode(true);
        engine.setEnabledProtocols(new String [] { "SSLv3" });
        return engine;
    }

    private void runTest() throws Exception {

        clientEngine.wrap(clientOut, cTOs);
        cTOs.flip();

        serverEngine.unwrap(cTOs, serverIn);
        runDelegatedTasks(serverEngine);

        try {
            /*
             * We should be getting a SSLHandshakeException.
             * If this changes, we'll need to update this test.
             * Anything else and we fail.
             */
            serverEngine.unwrap(cTOs, serverIn);
            throw new Exception(
                "TEST FAILED:  Didn't generate any exception");
        } catch (SSLHandshakeException e) {
            System.out.println("TEST PASSED:  Caught right exception");
        } catch (SSLException e) {
            System.out.println("TEST FAILED:  Generated wrong exception");
            throw e;
        }
    }

    public static void main(String args[]) throws Exception {
        // reset the security property to make sure that the algorithms
        // and keys used in this test are not disabled.
        Security.setProperty("jdk.tls.disabledAlgorithms", "");

        DelegatedTaskWrongException test = new DelegatedTaskWrongException();
        test.runTest();

        System.out.println("Test Passed.");
    }

    /*
     * **********************************************************
     * Majority of the test case is above, below is just setup stuff
     * **********************************************************
     */

    public DelegatedTaskWrongException() throws Exception {
        super();
    }

    private static void log(String str) {
        if (debug) {
            System.out.println(str);
        }
    }
}
