/**
 * Created by majewskm on 2016-03-01.
 */

var companyRegisterApp = angular.module('companyRegister', ['ui.bootstrap']);

companyRegisterApp.controller('CompaniesController', function($scope, $http, $uibModal, $location, $anchorScroll) {
    function getCompanies() {
        $http.get('companies').success(function(data) {
            $scope.companies = data;
        });
    };

    getCompanies();

    $scope.selectedCompany = null
    $scope.beneficials = {}
    $scope.showBeneficialOwners = function(company) {
        getBeneficialOwners(company.id)
        $scope.selectedCompany = company;
        $scope.goTo("beneficialOwners");
    };

    function getBeneficialOwners(companyId){
        $http.get('companies/' + companyId + '/beneficialOwners').success(function(data) {
            $scope.beneficials = data;
        });
    }

    $scope.openAddBeneficialOwnerModal = function (company) {
        $scope.selectedCompany = null;
        var currentCompany = company;
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'addBeneficialOwnerModal.html',
            controller: 'AddBeneficialOwnerInstanceController',
            resolve: {
                company: function () {
                    return currentCompany;
                }
            }
        });

        modalInstance.result.then(function(){
            getCompanies();
            $scope.addAlert('Beneficial owner saved');
        });
    }

    $scope.openCompanyModal = function (company){
        $scope.selectedCompany = null;
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'companyModal.html',
            controller: 'CompanyInstanceController',
            resolve: {
                company: function () {
                    return company;
                }
            }
        });

        modalInstance.result.then(function(){
            getCompanies();
            $scope.addAlert('Company saved');
        });
    }

    $scope.goTo = function(elementId){
        $location.hash(elementId);
        $anchorScroll();
    }

    $scope.saveDataAlerts = [];
    $scope.addAlert = function(message) {
        $scope.saveDataAlerts.push({msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.saveDataAlerts.splice(index, 1);
    };

});

companyRegisterApp.controller('CompanyInstanceController', function ($scope, $http, $uibModalInstance, $log, company) {
    $scope.phoneNoRegex = '\\+?[0-9 ]{4,}';
    $scope.errorMessage = null;
    $scope.company = company;

    var http_method = company == undefined ? 'POST' : 'PUT';
    var url = company == undefined ? '/companies' : '/companies/' + company.id;
    $scope.btn_label = company == undefined ? 'Add' : 'Save';

    $scope.saveCompany = function(company) {
        $http({
            method: http_method,
            url: url,
            data : company
        }).then(function successCallback(response) {
            $uibModalInstance.close();
        }, function errorCallback(response) {
            $scope.errorMessage = 'Response status: ' + response.data.status + '. Message: ' + response.data.message;
            $log.error($scope.errorMessage);
        });
    };
});

companyRegisterApp.controller('AddBeneficialOwnerInstanceController', function ($scope, $http, $uibModalInstance, $log, company) {
    $scope.company = company;
    $scope.errorMessage = null;
    $scope.addBeneficial = function(beneficial) {
        $scope.beneficial = {}
        $http({
            method: 'POST',
            url: '/companies/' + company.id + '/beneficialOwners',
            data : beneficial
        }).then(function successCallback(response) {
            $uibModalInstance.close();
        }, function errorCallback(response) {
            $scope.errorMessage = 'Response status: ' + response.data.status + '. Message: ' + response.data.message;
            $log.error($scope.errorMessage);
        });
    };
});

