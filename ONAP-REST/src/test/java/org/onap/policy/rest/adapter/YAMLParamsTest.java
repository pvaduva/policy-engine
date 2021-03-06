/*-
 * ============LICENSE_START=======================================================
 * ONAP Policy Engine
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.policy.rest.adapter;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class YAMLParamsTest {

    @Test
    public void testYAMLParams(){
        YAMLParams yamlParams = new YAMLParams();
        yamlParams.setActor("Test");
        assertTrue("Test".equals(yamlParams.getActor()));
        yamlParams.setRecipe("Test");
        assertTrue("Test".equals(yamlParams.getRecipe()));
        yamlParams.setClname("Test");
        assertTrue("Test".equals(yamlParams.getClname()));
        yamlParams.setGuardActiveEnd("Test");
        assertTrue("Test".equals(yamlParams.getGuardActiveEnd()));
        yamlParams.setGuardActiveStart("Test");
        assertTrue("Test".equals(yamlParams.getGuardActiveStart()));
        yamlParams.setLimit("Test");
        assertTrue("Test".equals(yamlParams.getLimit()));
        yamlParams.setBlackList(new ArrayList<>());
        assertTrue(yamlParams.getBlackList() != null);
        yamlParams.setTargets(new ArrayList<>());
        assertTrue(yamlParams.getTargets() != null);
        yamlParams.setTimeUnits("Test");
        assertTrue("Test".equals(yamlParams.getTimeUnits()));
        yamlParams.setTimeWindow("Test");
        assertTrue("Test".equals(yamlParams.getTimeWindow()));
    }

}
