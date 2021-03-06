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
app.controller('brmsDependencyDictGridController', function ($scope, PolicyAppService,modalService, $modal){
	$( "#dialog" ).hide();

	PolicyAppService.getData('getDictionary/get_BRMSDependencyData').then(function (data) {
		var j = data;
		$scope.data = JSON.parse(j.data);
		console.log($scope.data);
		$scope.brmsDependencyDictionaryDatas = JSON.parse($scope.data.brmsDependencyDictionaryDatas);
		console.log($scope.brmsDependencyDictionaryDatas);
	}, function (error) {
		console.log("failed");
	});


	PolicyAppService.getData('get_LockDownData').then(function(data){
		var j = data;
		$scope.data = JSON.parse(j.data);
		$scope.lockdowndata = JSON.parse($scope.data.lockdowndata);
		if($scope.lockdowndata[0].lockdown == true){
			$scope.brmsDependencyDictionaryGrid.columnDefs[0].visible = false;
			$scope.gridApi.grid.refresh();
		}else{
			$scope.brmsDependencyDictionaryGrid.columnDefs[0].visible = true;
			$scope.gridApi.grid.refresh();
		}
	},function(error){
		console.log("failed");
	});

	$scope.brmsDependencyDictionaryGrid = {
			data : 'brmsDependencyDictionaryDatas',
			enableFiltering: true,
			exporterCsvFilename: 'BRMSDependencyDictionary.csv',
			enableGridMenu: true,
			enableSelectAll: true,
			columnDefs: [{
				field: 'id', enableFiltering: false, headerCellTemplate: '' +
				'<button id=\'New\' ng-click="grid.appScope.createNewBRMSDependencyWindow()" class="btn btn-success">' + 'Create</button>',
				cellTemplate:
					'<button  type="button"  class="btn btn-primary"  ng-click="grid.appScope.viewBRMSDependencyWindow(row.entity)"><i class="glyphicon glyphicon-eye-open"></i></button> ' +
					'<button  type="button"  class="btn btn-primary"  ng-click="grid.appScope.editBRMSDependencyWindow(row.entity)"><i class="fa fa-pencil-square-o"></i></button> ' +
					'<button  type="button"  class="btn btn-danger"  ng-click="grid.appScope.deleteBRMSDependency(row.entity)" ><i class="fa fa-trash-o"></i></button> ',  width: '12%'
			},
			{ field: 'dependencyName', displayName : 'Dependency Name', sort: { direction: 'asc', priority: 0 }},
			{ field: 'description'},
			{ field: 'dependency' , visible : false},
			{field: 'userCreatedBy.userName', displayName : 'Created By'},
			{field: 'createdDate',type: 'date', cellFilter: 'date:\'yyyy-MM-dd\'' },
			{field: 'userModifiedBy.userName', displayName : 'Modified By'},
			{field: 'modifiedDate',type: 'date', cellFilter: 'date:\'yyyy-MM-dd\'' }
			],
			exporterMenuPdf: false,
			exporterPdfDefaultStyle: {fontSize: 9},
			exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
			exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
			exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
			exporterPdfFooter: function ( currentPage, pageCount ) {
				return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
			},
			exporterPdfCustomFormatter: function ( docDefinition ) {
				docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
				docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
				return docDefinition;
			},
			exporterFieldCallback: function(grid, row, col, input) {
				if( col.name == 'createdDate' || col.name == 'modifiedDate') {
					var date = new Date(input);
					return date.toString("yyyy-MM-dd HH:MM:ss a");
				} else {
					return input;
				}
			},
			exporterPdfOrientation: 'portrait',
			exporterPdfPageSize: 'LETTER',
			exporterPdfMaxGridWidth: 500,
			exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
			onRegisterApi: function(gridApi){
				$scope.gridApi = gridApi;
			}
	};

	$scope.editBRMSDependency = null;
	$scope.createNewBRMSDependencyWindow = function(){
		$scope.editBRMSDependency = null;
		var modalInstance = $modal.open({
			backdrop: 'static', keyboard: false,
			templateUrl : 'add_brmsDependency_popup.html',
			controller: 'editBRMSDependencyController',
			resolve: {
				message: function () {
					var message = {
							brmsDependencyDictionaryDatas: $scope.editBRMSDependency,
							disabled : false
					};
					return message;
				}
			}
		});
		modalInstance.result.then(function(response){
			console.log('response', response);
			$scope.brmsDependencyDictionaryDatas=response.brmsDependencyDictionaryDatas;
		});
	};

	$scope.viewBRMSDependencyWindow = function(brmsDependencyDictionaryData) {
		$scope.editBRMSDependency = brmsDependencyDictionaryData;
		var modalInstance = $modal.open({
			backdrop: 'static', keyboard: false,
			templateUrl : 'add_brmsDependency_popup.html',
			controller: 'editBRMSDependencyController',
			resolve: {
				message: function () {
					var message = {
							brmsDependencyDictionaryDatas: $scope.editBRMSDependency,
							disabled : true
					};
					return message;
				}
			}
		});
		modalInstance.result.then(function(response){
			console.log('response', response);
			$scope.brmsDependencyDictionaryDatas=response.brmsDependencyDictionaryDatas;
		});
	}; 

	$scope.editBRMSDependencyWindow = function(brmsDependencyDictionaryData) {
		$scope.editBRMSDependency = brmsDependencyDictionaryData;
		var modalInstance = $modal.open({
			backdrop: 'static', keyboard: false,
			templateUrl : 'add_brmsDependency_popup.html',
			controller: 'editBRMSDependencyController',
			resolve: {
				message: function () {
					var message = {
							brmsDependencyDictionaryDatas: $scope.editBRMSDependency,
							disabled : false
					};
					return message;
				}
			}
		});
		modalInstance.result.then(function(response){
			console.log('response', response);
			$scope.brmsDependencyDictionaryDatas=response.brmsDependencyDictionaryDatas;
		});
	};

	$scope.deleteBRMSDependency = function(data) {
		modalService.popupConfirmWin("Confirm","You are about to delete the BRMS Dependency "+data.dependencyName+". Do you want to continue?",
				function(){
			var uuu =  "deleteDictionary/brms_dictionary/remove_brmsDependency";
			var postData={data: data};
			$.ajax({
				type : 'POST',
				url : uuu,
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify(postData),
				success : function(data){
					$scope.$apply(function(){$scope.brmsDependencyDictionaryDatas=data.brmsDependencyDictionaryDatas;});
				},
				error : function(data){
					console.log(data);
					modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
				}
			});

		})
	};

});