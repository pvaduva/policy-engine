/*-
 * ============LICENSE_START=======================================================
 * ONAP Policy Engine
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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


import java.util.List;

public class AutoPushTabAdapter {

    private List<Object> pdpDatas;
    private List<Object> policyDatas;
    public List<Object> getPdpDatas() {
        return pdpDatas;
    }
    public void setPdpDatas(List<Object> pdpDatas) {
        this.pdpDatas = pdpDatas;
    }
    public List<Object> getPolicyDatas() {
        return policyDatas;
    }
    public void setPolicyDatas(List<Object> policyDatas) {
        this.policyDatas = policyDatas;
    }
}
