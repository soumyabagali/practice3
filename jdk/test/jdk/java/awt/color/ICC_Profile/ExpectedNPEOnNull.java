/*
 * Copyright Amazon.com Inc. or its affiliates. All Rights Reserved.
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

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @test
 * @bug 6211108 6211105 8299333
 */
public final class ExpectedNPEOnNull {

    public static void main(String[] args) throws Exception {
        // static methods
        try {
            ICC_Profile.getInstance((String) null);
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException ignored) {
            // expected
        }
        try {
            ICC_Profile.getInstance((InputStream) null);
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException ignored) {
            // expected
        }
        // instance methods
        test(ICC_Profile.getInstance(ColorSpace.CS_sRGB));
        test(ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB));
        test(ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ));
        test(ICC_Profile.getInstance(ColorSpace.CS_PYCC));
        test(ICC_Profile.getInstance(ColorSpace.CS_GRAY));
    }

    private static void test(ICC_Profile profile) throws Exception {
        try {
            profile.write((String) null);
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException ignored) {
            // expected
        }
        try {
            profile.write((OutputStream) null);
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException ignored) {
            // expected
        }
    }
}
