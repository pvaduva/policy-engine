/*-
 * ============LICENSE_START=======================================================
 * ONAP-PAP-REST
 * ================================================================================
 * Copyright (C) 2018-2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.pap.xacml.rest.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.onap.policy.common.logging.OnapLoggingContext;
import org.onap.policy.pap.xacml.rest.XACMLPapServlet;
import org.onap.policy.pap.xacml.rest.daoimpl.CommonClassDaoImpl;
import org.onap.policy.pap.xacml.rest.elk.client.PolicyElasticSearchController;
import org.onap.policy.rest.jpa.PolicyEntity;
import org.onap.policy.xacml.api.pap.PAPPolicyEngine;
import org.onap.policy.xacml.std.pap.StdEngine;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class DeleteHandlerTest {
    @Before
    public void setUp() {
        SessionFactory mockedSessionFactory = Mockito.mock(SessionFactory.class);
        Session mockedSession = Mockito.mock(Session.class);
        Transaction mockedTransaction = Mockito.mock(Transaction.class);
        Mockito.when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
        Mockito.when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
        CommonClassDaoImpl.setSessionfactory(mockedSessionFactory);
        new DeleteHandler(new CommonClassDaoImpl());
    }

    @Test
    public void testGets() {
        DeleteHandler handler = new DeleteHandler();
        assertNotNull(handler);
        assertEquals(handler.preSafetyCheck(null), true);
        assertNull(handler.getDeletedGroup());
    }

    @Test
    public void testGetInstance() {
        DeleteHandler handler = DeleteHandler.getInstance();
        assertNotNull(handler);
    }

    @PrepareForTest({DeleteHandler.class, XACMLPapServlet.class})
    @Test
    public void testDeletes() throws Exception {
        // Mock request
        DeleteHandler handler = new DeleteHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setBodyContent("{\n\"PAPPolicyType\": \"StdPAPPolicy\"\n}\n");

        // Mock servlet
        PAPPolicyEngine engine = Mockito.mock(StdEngine.class);
        PowerMockito.mockStatic(XACMLPapServlet.class);
        when(XACMLPapServlet.getPAPEngine()).thenReturn(engine);
        when(engine.getGroup(any())).thenReturn(null);

        // Mock elastic search
        PolicyElasticSearchController controller = Mockito.mock(PolicyElasticSearchController.class);
        PowerMockito.whenNew(PolicyElasticSearchController.class).withNoArguments().thenReturn(controller);

		// Test deletion from PAP
		MockHttpServletResponse response = new MockHttpServletResponse();
		try {
			handler.doApiDeleteFromPap(request, response);
		} catch (Exception ex) {
			fail("Not expecting an exception: " + ex);
		}

        // Test deletion from PDP
        OnapLoggingContext loggingContext = Mockito.mock(OnapLoggingContext.class);
        try {
            handler.doApiDeleteFromPdp(request, response, loggingContext);
        }
        catch (Exception ex) {
            fail("Not expecting an exception: " + ex);
        }

		// Test delete entity
		PolicyEntity policyEntity = new PolicyEntity();
		policyEntity.setPolicyName("testVal");
		String result = DeleteHandler.deletePolicyEntityData(policyEntity);
		assertEquals(result, "success");

		// Test check entity
		List<?> peResult = Collections.emptyList();
		assertEquals(DeleteHandler.checkPolicyGroupEntity(peResult), false);
	}
}
