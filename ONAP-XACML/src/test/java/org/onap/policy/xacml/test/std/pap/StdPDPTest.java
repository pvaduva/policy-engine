/*-
 * ============LICENSE_START=======================================================
 * ONAP-XACML
 * ================================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.xacml.test.std.pap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.onap.policy.common.logging.flexlogger.FlexLogger;
import org.onap.policy.common.logging.flexlogger.Logger;
import org.onap.policy.xacml.std.pap.StdPDP;
import org.onap.policy.xacml.std.pap.StdPDPStatus;
import com.att.research.xacml.api.pap.PDPPIPConfig;
import com.att.research.xacml.api.pap.PDPPolicy;

public class StdPDPTest {

    private static Logger logger = FlexLogger.getLogger(StdPDPTest.class);

    private StdPDP stdPDP;

    @Before
    public void setUp() {

        try {
            stdPDP = new StdPDP();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetId() {
        try {
            stdPDP.setId("testId");
            assertTrue(stdPDP.getId() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetName() {
        try {
            stdPDP.setName("abc");
            assertTrue(stdPDP.getName() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetDescription() {
        try {
            stdPDP.setDescription("somee here");
            assertTrue(stdPDP.getDescription() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetStatus() {
        try {
            stdPDP.setStatus(new StdPDPStatus());
            assertTrue(stdPDP.getStatus() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetPipConfigs() {
        try {
            assertTrue(stdPDP.getPipConfigs() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testGetJmxPort() {
        try {
            stdPDP.setJmxPort(123);
            assertTrue(stdPDP.getJmxPort() != null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testPDP() {
        // Set up test data
        String id = "testID";
        String value = "testVal";
        Properties props = new Properties();
        props.setProperty(id + ".name", value);
        props.setProperty(id + ".description", value);
        props.setProperty(id + ".jmxport", "0");
        Set<PDPPIPConfig> pipConfigs = new HashSet<PDPPIPConfig>();
        Set<PDPPolicy> policies = new HashSet<PDPPolicy>();

        // Test constructors
        StdPDP pdp = new StdPDP(id, 0);
        assertNotNull(pdp);
        StdPDP pdp2 = new StdPDP(id, value, 0);
        assertNotNull(pdp2);
        StdPDP pdp3 = new StdPDP(id, value, value, 0);
        assertNotNull(pdp3);
        StdPDP pdp4 = new StdPDP(id, props);
        assertNotNull(pdp4);
        StdPDP pdp5 = new StdPDP(id, props);
        assertNotNull(pdp5);

        // Test set and get
        pdp.setPipConfigs(pipConfigs);
        assertEquals(pipConfigs, pdp.getPipConfigs());
        pdp.setPolicies(policies);
        assertEquals(policies, pdp.getPolicies());

        // Test equals combinations
        assertEquals(true, pdp4.equals(pdp5));
        assertEquals(true, pdp4.equals(pdp4));
        assertEquals(false, pdp4.equals(null));
        assertEquals(false, pdp4.equals(value));
        pdp4.setId(null);
        assertEquals(false, pdp4.equals(value));
        pdp5.setId(id);
        assertEquals(false, pdp4.equals(pdp5));
        pdp4.setId(value);
        assertEquals(false, pdp4.equals(pdp5));
        assertThat(pdp.hashCode(), is(not(0)));

        // Test compare
        assertEquals(-1, pdp4.compareTo(null));
        assertEquals(0, pdp4.compareTo(pdp4));
        pdp5.setName(null);
        assertEquals(-1, pdp4.compareTo(pdp5));
        pdp4.setName(null);
        pdp5.setName(value);
        assertEquals(1, pdp4.compareTo(pdp5));

        // Test toString
        assertThat(pdp.toString().length(), is(not(0)));
    }
}
