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

package org.onap.policy.std.test;

import org.junit.*;
import org.onap.policy.std.StdRemovedPolicy;
import static org.junit.Assert.*;

/**
 * The class <code>StdRemovedPolicyTest</code> contains tests for the class
 * <code>{@link StdRemovedPolicy}</code>.
 *
 * @generatedBy CodePro at 6/1/16 1:40 PM
 * @version $Revision: 1.0 $
 */
public class StdRemovedPolicyTest {
    /**
     * Run the StdRemovedPolicy() constructor test.
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testStdRemovedPolicy_1() throws Exception {
        StdRemovedPolicy result = new StdRemovedPolicy();
        assertNotNull(result);
        // add additional test code here
    }

    /**
     * Run the String getPolicyName() method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testGetPolicyName_1() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName("");

        String result = fixture.getPolicyName();

        // add additional test code here
        assertEquals("", result);
    }

    /**
     * Run the String getPolicyName() method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testGetPolicyName_2() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName((String) null);

        String result = fixture.getPolicyName();

        // add additional test code here
        assertEquals(null, result);
    }

    /**
     * Run the String getPolicyName() method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testGetPolicyName_3() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName("");

        String result = fixture.getPolicyName();

        // add additional test code here
        assertEquals("", result);
    }

    /**
     * Run the String getVersionNo() method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testGetVersionNo_1() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName("");

        String result = fixture.getVersionNo();

        // add additional test code here
        assertEquals("", result);
    }

    /**
     * Run the void setPolicyName(String) method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testSetPolicyName_1() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName("");
        String policyName = "";

        fixture.setPolicyName(policyName);

        // add additional test code here
    }

    /**
     * Run the void setVersionNo(String) method test.
     *
     * @throws Exception
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    @Test
    public void testSetVersionNo_1() throws Exception {
        StdRemovedPolicy fixture = new StdRemovedPolicy();
        fixture.setVersionNo("");
        fixture.setPolicyName("");
        String versionNo = "";

        fixture.setVersionNo(versionNo);

        // add additional test code here
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception if the initialization fails for some reason
     *
     * @generatedBy CodePro at 6/1/16 1:40 PM
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
     * @generatedBy CodePro at 6/1/16 1:40 PM
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
     * @generatedBy CodePro at 6/1/16 1:40 PM
     */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(StdRemovedPolicyTest.class);
    }
}
