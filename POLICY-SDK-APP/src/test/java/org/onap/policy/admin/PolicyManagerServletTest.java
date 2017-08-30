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
package org.onap.policy.admin;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.SimpleBindings;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.onap.policy.common.logging.flexlogger.FlexLogger;
import org.onap.policy.common.logging.flexlogger.Logger;
import org.onap.policy.controller.CreateDcaeMicroServiceController;
import org.onap.policy.controller.PolicyController;
import org.onap.policy.rest.dao.CommonClassDao;
import org.onap.policy.rest.jpa.ActionBodyEntity;
import org.onap.policy.rest.jpa.ConfigurationDataEntity;
import org.onap.policy.rest.jpa.GroupPolicyScopeList;
import org.onap.policy.rest.jpa.PolicyEditorScopes;
import org.onap.policy.rest.jpa.PolicyEntity;
import org.onap.policy.rest.jpa.PolicyVersion;
import org.onap.policy.rest.jpa.UserInfo;
import org.openecomp.policy.model.Roles;

public class PolicyManagerServletTest extends Mockito{
	
	private static Logger logger = FlexLogger.getLogger(PolicyManagerServletTest.class);
	private List<String> headers = new ArrayList<String>();

	private static List<Object> rolesdata;
	private static List<Object> basePolicyData;
	private static List<Object> policyEditorScopes;
	private static List<Object> policyVersion;
	private static CommonClassDao commonClassDao;
	
	@Before
	public void setUp() throws Exception{
		logger.info("setUp: Entering");
		UserInfo userinfo = new UserInfo();
		userinfo.setUserLoginId("Test");
		userinfo.setUserName("Test");
		//Roles Data
        rolesdata = new ArrayList<>();
        Roles roles = new Roles();
        roles.setLoginId("Test");
        roles.setRole("super-admin");
        Roles roles1 = new Roles();
        roles1.setLoginId("Test");
        roles1.setRole("admin");
        roles1.setScope("['com','Test']");
        rolesdata.add(roles);
        rolesdata.add(roles1);
        
        //PolicyEntity Data
        basePolicyData = new ArrayList<>();
        String policyContent = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_SampleTest1206.1.xml"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_SampleTest.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody("Sample Test");
        configurationEntity.setConfigType("OTHER");
        configurationEntity.setConfigurationName("com.Config_SampleTest1206.1.txt");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        basePolicyData.add(entity);
        
        //PolicyEditorScopes data
        policyEditorScopes = new ArrayList<>();
        PolicyEditorScopes scopes = new PolicyEditorScopes();
        scopes.setScopeName("com");
        scopes.setUserCreatedBy(userinfo);
        scopes.setUserModifiedBy(userinfo);
        PolicyEditorScopes scopes1 = new PolicyEditorScopes();
        scopes1.setScopeName("com\\Test");
        scopes1.setUserCreatedBy(userinfo);
        scopes1.setUserModifiedBy(userinfo);
        policyEditorScopes.add(scopes);
        policyEditorScopes.add(scopes1);
        
        //PolicyVersion data
        policyVersion = new ArrayList<>();
        PolicyVersion policy = new PolicyVersion();
        policy.setPolicyName("com\\Config_SampleTest1206");
        policy.setActiveVersion(1);
        policy.setHigherVersion(1);
        policy.setCreatedBy("Test");
        policy.setModifiedBy("Test");
        policyVersion.add(policy);
	}
	
	@Test
	public void testInit(){
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		ServletConfig servletConfig = mock(ServletConfig.class);       
        try {
        	when(servletConfig.getInitParameterNames()).thenReturn(Collections.enumeration(headers));
        	when(servletConfig.getInitParameter("XACML_PROPERTIES_NAME")).thenReturn("xacml.admin.properties");
        	System.setProperty("xacml.rest.admin.closedLoopJSON", new File(".").getCanonicalPath() + File.separator + "src"+ File.separator + "test" + File.separator + "resources" + File.separator + "JSONConfig.json");
			servlet.init(servletConfig);
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
			fail();
		}
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDescribePolicy(){
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);  
        PolicyController controller = mock(PolicyController.class);
        
        BufferedReader reader = new BufferedReader(new StringReader("{params: { mode: 'DESCRIBEPOLICYFILE', path: 'com.Config_SampleTest1206.1.xml'}}"));
        try {
			when(request.getReader()).thenReturn(reader);
			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_SampleTest1206.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(basePolicyData);
			servlet.setPolicyController(controller);
			servlet.doPost(request, response);
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
			fail();
		}
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	public void testPolicyScopeList(){
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'LIST', path: '/', onlyFolders: false}}");
        list.add("{params: { mode: 'LIST', path: '/com', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("from PolicyEditorScopes", new SimpleBindings())).thenReturn(policyEditorScopes);
    			when(controller.getDataByQuery("from PolicyEditorScopes where SCOPENAME like 'com%'", new SimpleBindings())).thenReturn(policyEditorScopes);
    			when(controller.getDataByQuery("from PolicyVersion where POLICY_NAME like 'com%'", new SimpleBindings())).thenReturn(policyVersion);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editBasePolicyTest(){
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_SampleTest1206.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_SampleTest1206.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(basePolicyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editBRMSParamPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.txt"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("OTHER");
        configurationEntity.setConfigurationName("com.Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.txt");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_BRMS_Param_BRMSParamvFWDemoPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}

	@SuppressWarnings("static-access")
	@Test
	public void editBRMSRawPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_BRMS_Raw_TestBRMSRawPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_BRMS_Raw_TestBRMSRawPolicy.1.txt"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_BRMS_Raw_TestBRMSRawPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("OTHER");
        configurationEntity.setConfigurationName("com.Config_BRMS_Raw_TestBRMSRawPolicy.1.txt");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_BRMS_Raw_TestBRMSRawPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_BRMS_Raw_TestBRMSRawPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editClosedLoopFaultPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_Fault_TestClosedLoopPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_Fault_TestClosedLoopPolicy.1.json"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_Fault_TestClosedLoopPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("JSON");
        configurationEntity.setConfigurationName("com.Config_Fault_TestClosedLoopPolicy.1.json");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_Fault_TestClosedLoopPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_Fault_TestClosedLoopPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editClosedLoopPMPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_PM_TestClosedLoopPMPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_PM_TestClosedLoopPMPolicy.1.json"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_PM_TestClosedLoopPMPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("JSON");
        configurationEntity.setConfigurationName("com.Config_PM_TestClosedLoopPMPolicy.1.json");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_PM_TestClosedLoopPMPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_PM_TestClosedLoopPMPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editMicroServicePolicyTest(){
		GroupPolicyScopeList groupData = new  GroupPolicyScopeList();
		groupData.setGroupName("Test");
		groupData.setGroupList("resource=SampleResource,service=SampleService,type=SampleType,closedLoopControlName=SampleClosedLoop");
		List<Object> groupListData = new ArrayList<>();
		groupListData.add(groupData);
		commonClassDao = mock(CommonClassDao.class);
		CreateDcaeMicroServiceController.setCommonClassDao(commonClassDao);
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_MS_vFirewall.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_MS_vFirewall.1.json"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_MS_vFirewall.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("JSON");
        configurationEntity.setConfigurationName("com.Config_MS_vFirewall.1.json");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_MS_vFirewall.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(commonClassDao.getDataById(GroupPolicyScopeList.class, "groupList", "resource=SampleResource,service=SampleService,type=SampleType,closedLoopControlName=SampleClosedLoop")).thenReturn(groupListData);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_MS_vFirewall.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editFirewallPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Config_FW_TestFireWallPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Config_FW_TestFireWallPolicy.1.json"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Config_FW_TestFireWallPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ConfigurationDataEntity configurationEntity = new ConfigurationDataEntity();
        configurationEntity.setConfigBody(configData);
        configurationEntity.setConfigType("JSON");
        configurationEntity.setConfigurationName("com.Config_FW_TestFireWallPolicy.1.json");
        configurationEntity.setDescription("test");
        entity.setConfigurationData(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Config_FW_TestFireWallPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Config_FW_TestFireWallPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editActionPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        String configData = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Action_TestActionPolicy.1.xml"));
			configData = IOUtils.toString(classLoader.getResourceAsStream("com.Action_TestActionPolicy.1.json"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Action_TestActionPolicy.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        ActionBodyEntity configurationEntity = new ActionBodyEntity();
        configurationEntity.setActionBody(configData);
        configurationEntity.setActionBodyName("com.Action_TestActionPolicy.1.json");
        entity.setActionBodyEntity(configurationEntity);
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Action_TestActionPolicy.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Action_TestActionPolicy.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void editDecisionPolicyTest(){
		List<Object> policyData = new ArrayList<>();
        String policyContent = "";
        try {
			ClassLoader classLoader = getClass().getClassLoader();
			policyContent = IOUtils.toString(classLoader.getResourceAsStream("Decision_TestDecisionPolicyWithRuleAlgorithms.1.xml"));
		} catch (Exception e1) {
			logger.error("Exception Occured"+e1);
		}
        PolicyEntity entity = new PolicyEntity();
        entity.setPolicyName("Decision_TestDecisionPolicyWithRuleAlgorithms.1.xml");
        entity.setPolicyData(policyContent);
        entity.setScope("com");
        policyData.add(entity);
		PolicyManagerServlet servlet = new PolicyManagerServlet();
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 
        PolicyController controller = mock(PolicyController.class);
        List<String> list = new ArrayList<>();
        list.add("{params: { mode: 'EDITFILE', path: '/com/Decision_TestDecisionPolicyWithRuleAlgorithms.1.xml', onlyFolders: false}}");
        for(int i =0; i < list.size(); i++){
        	BufferedReader reader = new BufferedReader(new StringReader(list.get(i)));
            try {
    			when(request.getReader()).thenReturn(reader);
    			when(controller.getRoles("Test")).thenReturn(rolesdata);
    			when(controller.getDataByQuery("FROM PolicyEntity where policyName = 'Decision_TestDecisionPolicyWithRuleAlgorithms.1.xml' and scope ='com'", new SimpleBindings())).thenReturn(policyData);
    			servlet.setPolicyController(controller);
    			servlet.setTestUserId("Test");
    			servlet.doPost(request, response);
    		} catch (Exception e1) {
    			logger.error("Exception Occured"+e1);
    			fail();
    		}
        }
	}
}