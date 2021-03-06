/*-
 * ============LICENSE_START=======================================================
 * ONAP-PDP-REST
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Samsung Electronics Co., Ltd.
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
package org.onap.policy.pdp.rest.api.services;

import java.util.Map;

import org.onap.policy.api.AttributeType;
import org.onap.policy.api.PolicyException;
import org.onap.policy.api.PolicyParameters;
import org.onap.policy.common.logging.flexlogger.FlexLogger;
import org.onap.policy.common.logging.flexlogger.Logger;
import org.onap.policy.pdp.rest.api.utils.PolicyApiUtils;
import org.onap.policy.xacml.api.XACMLErrorConstants;
import org.onap.policy.xacml.std.pap.StdPAPPolicy;
import org.onap.policy.xacml.std.pap.StdPAPPolicyParams;

/**
 * BRMS Param Policy Implementation.
 *
 * @version 0.1
 */
public class BRMSParamPolicyService {
    private static final Logger LOGGER = FlexLogger.getLogger(BRMSParamPolicyService.class.getName());
    private PAPServices papServices = null;

    private PolicyParameters policyParameters = null;
    private String message = null;
    private String policyName = null;
    private String policyScope = null;
    private String date = null;
    private Map<AttributeType, Map<String, String>> drlRuleAndUIParams = null;

    public BRMSParamPolicyService(String policyName, String policyScope,
                                  PolicyParameters policyParameters, String date) {
        this.policyParameters = policyParameters;
        this.policyName = policyName;
        this.policyScope = policyScope;
        this.date = date;
        papServices = new PAPServices();
    }

    public Boolean getValidation() {
        boolean levelCheck = PolicyApiUtils.isNumeric(policyParameters.getRiskLevel());
        if (!levelCheck) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "Incorrect Risk Level given.";
            return false;
        }
        drlRuleAndUIParams = policyParameters.getAttributes();
        if (drlRuleAndUIParams == null || drlRuleAndUIParams.isEmpty()) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "No Rule Attributes given.";
            return false;
        }
        return true;
    }

    public String getMessage() {
        return message;
    }

    public String getResult(boolean updateFlag) throws PolicyException {
        String response = null;
        String operation = null;
        if (updateFlag) {
            operation = "update";
        } else {
            operation = "create";
        }
        // Create Policy
        // Creating BRMS Param Policies from the Admin Console
        StdPAPPolicy newPAPPolicy = new StdPAPPolicy(StdPAPPolicyParams.builder()
                .configPolicyType("BRMS_Param")
                .policyName(policyName)
                .description(policyParameters.getPolicyDescription())
                .configName("BRMS_PARAM_RULE")
                .editPolicy(updateFlag)
                .domain(policyScope)
                .dynamicFieldConfigAttributes(drlRuleAndUIParams.get(AttributeType.MATCHING))
                .highestVersion(0)
                .onapName("DROOLS")
                .configBodyData(null)
                .drlRuleAndUIParams(drlRuleAndUIParams.get(AttributeType.RULE))
                .riskLevel(policyParameters.getRiskLevel())
                .riskType(policyParameters.getRiskType())
                .guard(String.valueOf(policyParameters.getGuard()))
                .ttlDate(date)
                .brmsController(policyParameters.getControllerName())
                .brmsDependency(policyParameters.getDependencyNames())
                .build());
        // Send JSON to PAP
        response = (String) papServices.callPAP(newPAPPolicy, new String[]{"operation=" + operation, "apiflag=api",
                "policyType=Config"}, policyParameters.getRequestID(), "ConfigBrmsParam");
        LOGGER.info(response);
        return response;
    }

}