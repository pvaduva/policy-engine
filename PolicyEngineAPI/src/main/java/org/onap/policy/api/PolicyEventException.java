/*-
 * ============LICENSE_START=======================================================
 * PolicyEngineAPI
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

package org.onap.policy.api;

/**
 * PolicyEventException extends <code>Exception</code> to implement exceptions thrown by {@link org.onap.policy.api.PolicyEngine}
 * 
 * @version 0.1
 */
public class PolicyEventException extends Exception {
	private static final long serialVersionUID = -1477625011320634608L;
	
	public PolicyEventException() {
		// Empty constructor
	}
	
	public PolicyEventException(String message) {
		super(message);
	}
	
	public PolicyEventException(Throwable cause){
		super(cause);
	}
	
	public PolicyEventException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PolicyEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
