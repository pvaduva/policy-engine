/*-
 * ============LICENSE_START=======================================================
 * PolicyEngineAPI
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright (C) 2019 Samsung
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.policy.test;

import org.junit.*;
import org.onap.policy.api.UpdateType;
import static org.junit.Assert.*;

/**
 * The class <code>UpdateTypeTest</code> contains tests for the class
 * <code>{@link UpdateType}</code>.
 *
 * @generatedBy CodePro at 6/1/16 1:39 PM
 * @version $Revision: 1.0 $
 */
public class UpdateTypeTest {
    /**
     * Run the String toString() method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:39 PM
     */
    @Test
    public void testToString_1() throws Exception {
        UpdateType fixture = UpdateType.NEW;

        String result = fixture.toString();

        // add additional test code here
        assertEquals("new", result);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception if the initialization fails for some reason
     *
     * @generatedBy CodePro at 6/1/16 1:39 PM
     */
    @Before
    public void setUp() throws Exception {
        // add additional set up code here
    }

    /**
     * Perform post-test clean-up.
     *
     * @throws Exception if the clean-up fails for some reason
     *
     * @generatedBy CodePro at 6/1/16 1:39 PM
     */
    @After
    public void tearDown() throws Exception {
        // Add additional tear down code here
    }

    /**
     * Launch the test.
     *
     * @param args the command line arguments
     *
     * @generatedBy CodePro at 6/1/16 1:39 PM
     */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(UpdateTypeTest.class);
    }
}
