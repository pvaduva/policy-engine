/*-
 * ============LICENSE_START=======================================================
 * ONAP-PDP-REST
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.policy.pdp.rest.notifications;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RemovedTest {
    @Test
    public void testRemoved() {
        String testVal = "testVal";

        Removed removed = new Removed();
        removed.setVersionNo(testVal);
        assertEquals(removed.getVersionNo(), testVal);
        removed.setPolicyName(testVal);
        assertEquals(removed.getPolicyName(), testVal);
    }
}
