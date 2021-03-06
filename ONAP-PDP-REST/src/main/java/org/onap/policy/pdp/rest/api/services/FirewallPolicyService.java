/*-
 * ============LICENSE_START=======================================================
 * ONAP-PDP-REST
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modified Copyright (C) 2018 Samsung Electronics Co., Ltd.
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

import javax.json.JsonException;
import javax.json.JsonObject;

import org.onap.policy.api.PolicyException;
import org.onap.policy.api.PolicyParameters;
import org.onap.policy.common.logging.flexlogger.FlexLogger;
import org.onap.policy.common.logging.flexlogger.Logger;
import org.onap.policy.pdp.rest.api.utils.PolicyApiUtils;
import org.onap.policy.xacml.api.XACMLErrorConstants;
import org.onap.policy.xacml.std.pap.StdPAPPolicy;
import org.onap.policy.xacml.std.pap.StdPAPPolicyParams;

/**
 * Firewall Policy Implementation.
 *
 * @version 0.1
 */
public class FirewallPolicyService {
    private static final Logger LOGGER = FlexLogger.getLogger(FirewallPolicyService.class.getName());

    private PAPServices papServices = null;
    private PolicyParameters policyParameters = null;
    private String message = null;
    private String policyName = null;
    private String policyScope = null;
    private String date = null;
    private JsonObject firewallJson = null;

    public FirewallPolicyService(String policyName, String policyScope,
                                 PolicyParameters policyParameters, String date) {
        this.policyParameters = policyParameters;
        this.policyName = policyName;
        this.policyScope = policyScope;
        this.date = date;
        papServices = new PAPServices();
    }

    public Boolean getValidation() {
        if (policyParameters.getConfigBody() == null) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "No Config Body given.";
            return false;
        }
        try {
            firewallJson = PolicyApiUtils.stringToJsonObject(policyParameters.getConfigBody());
        } catch (JsonException | IllegalStateException e) {
            message =
                    XACMLErrorConstants.ERROR_DATA_ISSUE + " improper JSON object : " +
                            policyParameters.getConfigBody();
            LOGGER.error("Error while parsing JSON body for creating Firewall Policy ", e);
            return false;
        }
        if (firewallJson == null || firewallJson.isEmpty()) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "No Config-Body given.";
            return false;
        }
        boolean levelCheck = false;
        levelCheck = PolicyApiUtils.isNumeric(policyParameters.getRiskLevel());
        if (!levelCheck) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "Incorrect Risk Level given.";
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
        //set values for basic policy information
        if (!firewallJson.containsKey("configName")) {
            message = XACMLErrorConstants.ERROR_DATA_ISSUE + "No configName given in firwall JSON.";
            LOGGER.error(message);
            return message;
        }
        String configName = firewallJson.get("configName").toString();
        String configDescription = "";
        String json = firewallJson.toString();
        // Create Policy.
        StdPAPPolicy newPAPPolicy = new StdPAPPolicy(StdPAPPolicyParams.builder()
                .configPolicyType("Firewall Config")
                .policyName(policyName)
                .description(configDescription)
                .configName(configName)
                .editPolicy(updateFlag)
                .domain(policyScope)
                .jsonBody(json)
                .highestVersion(0)
                .riskLevel(policyParameters.getRiskLevel())
                .riskType(policyParameters.getRiskType())
                .guard(String.valueOf(policyParameters.getGuard()))
                .ttlDate(date)
                .build());
        // Send Json to PAP.
        response = (String) papServices.callPAP(newPAPPolicy, new String[]{"operation=" + operation, "apiflag=api",
                "policyType=Config"}, policyParameters.getRequestID(), "ConfigFirewall");
        LOGGER.info(response);
        return response;
    }

}
