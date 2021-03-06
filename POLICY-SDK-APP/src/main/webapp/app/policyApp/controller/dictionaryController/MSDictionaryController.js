/*-
 * ============LICENSE_START=======================================================
 * ONAP Policy Engine
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
app.controller('editMSDictController' ,  function ($scope, $modalInstance, message, UserInfoServiceDS2, Notification){
    if(message.microServiceDictionaryDatas==null)
        $scope.label='Add Dictionary input'
    else{
        $scope.label='Edit Dictionary input'
        $scope.disableCd=true;
    }
    
	
	/*getting user info from session*/
	var userid = null;
	UserInfoServiceDS2.getFunctionalMenuStaticDetailSession()
	  	.then(function (response) {	  		
	  		userid = response.userid;	  	
	 });
	
    $scope.editMSDictData = message.microServiceDictionaryDatas;

    $scope.saveMSDictionatyData = function(microServiceDictionaryDatas) {
    	var regex = new RegExp("^[a-zA-Z0-9_]*$");
    	if(!regex.test(microServiceDictionaryDatas.name)) {
    		Notification.error("Enter Valid Dictionary Name without spaces or special characters");
    	}else{
    		var uuu = "saveDictionary/ms_dictionary/save_DictionaryData";
    		var postData={microServiceDictionaryDatas: microServiceDictionaryDatas, userid: userid};
    		$.ajax({
    			type : 'POST',
    			url : uuu,
    			dataType: 'json',
    			contentType: 'application/json',
    			data: JSON.stringify(postData),
    			success : function(data){
    				$scope.$apply(function(){
    					$scope.microServiceDictionaryDatas=data.microServiceDictionaryDatas;});
    				if($scope.microServiceDictionaryDatas == "Duplicate"){
    					Notification.error("MS Dictionary data exists with Same Name.")
    				}else{      
    					console.log($scope.microServiceDictionaryDatas);
    					$modalInstance.close({microServiceDictionaryDatas:$scope.microServiceDictionaryDatas});
    				}
    			},
    			error : function(data){
    				alert("Error while saving.");
    			}
    		});	
    	}
    };

    $scope.close = function() {
        $modalInstance.close();
    };
});