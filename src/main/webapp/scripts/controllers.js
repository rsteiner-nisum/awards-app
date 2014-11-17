'use strict';

/* Controllers */

awardsApp.controller('MainController', function ($scope) {
    });

awardsApp.controller('AdminController', function ($scope) {
    });

awardsApp.controller('LanguageController', function ($scope, $translate, LanguageService) {
        $scope.changeLanguage = function (languageKey) {
            $translate.use(languageKey);

            LanguageService.getBy(languageKey).then(function(languages) {
                $scope.languages = languages;
            });
        };

        LanguageService.getBy().then(function (languages) {
            $scope.languages = languages;
        });
    });

awardsApp.controller('MenuController', function ($scope) {
    });

awardsApp.controller('LoginController', function ($scope, $location, AuthenticationSharedService) {
        $scope.rememberMe = true;
        $scope.login = function () {
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            });
        }
    });

awardsApp.controller('LogoutController', function ($location, AuthenticationSharedService) {
        AuthenticationSharedService.logout();
    });

awardsApp.controller('SettingsController', function ($scope, Account) {
        $scope.success = null;
        $scope.error = null;
        $scope.settingsAccount = Account.get();

        $scope.save = function () {
            $scope.success = null;
            $scope.error = null;
            $scope.errorEmailExists = null;
            Account.save($scope.settingsAccount,
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = 'OK';
                    $scope.settingsAccount = Account.get();
                },
                function (httpResponse) {
                    if (httpResponse.status === 400 && httpResponse.data === "e-mail address already in use") {
                        $scope.errorEmailExists = "ERROR";
                    } else {
                        $scope.error = "ERROR";
                    }
                });
        };
    });

awardsApp.controller('RegisterController', function ($scope, $translate, Register) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.register = function () {
            if ($scope.registerAccount.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                $scope.success = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.errorEmailExists = null;
                Register.save($scope.registerAccount,
                    function (value, responseHeaders) {
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        if (httpResponse.status === 400 && httpResponse.data === "login already in use") {
                            $scope.error = null;
                            $scope.errorUserExists = "ERROR";
                        } else if (httpResponse.status === 400 && httpResponse.data === "e-mail address already in use") {
                            $scope.error = null;
                            $scope.errorEmailExists = "ERROR";
                        } else {
                            $scope.error = "ERROR";
                        }
                    });
            }
        }
    });

awardsApp.controller('ActivationController', function ($scope, $routeParams, Activate) {
        Activate.get({key: $routeParams.key},
            function (value, responseHeaders) {
                $scope.error = null;
                $scope.success = 'OK';
            },
            function (httpResponse) {
                $scope.success = null;
                $scope.error = "ERROR";
            });
    });

awardsApp.controller('PasswordController', function ($scope, Password) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password != $scope.confirmPassword) {
                $scope.doNotMatch = "ERROR";
            } else {
                $scope.doNotMatch = null;
                Password.save($scope.password,
                    function (value, responseHeaders) {
                        $scope.error = null;
                        $scope.success = 'OK';
                    },
                    function (httpResponse) {
                        $scope.success = null;
                        $scope.error = "ERROR";
                    });
            }
        };
    });

awardsApp.controller('SessionsController', function ($scope, resolvedSessions, Sessions) {
        $scope.success = null;
        $scope.error = null;
        $scope.sessions = resolvedSessions;
        $scope.invalidate = function (series) {
            Sessions.delete({series: encodeURIComponent(series)},
                function (value, responseHeaders) {
                    $scope.error = null;
                    $scope.success = "OK";
                    $scope.sessions = Sessions.get();
                },
                function (httpResponse) {
                    $scope.success = null;
                    $scope.error = "ERROR";
                });
        };
    });

 awardsApp.controller('HealthController', function ($scope, HealthCheckService) {
     $scope.updatingHealth = true;

     $scope.refresh = function() {
         $scope.updatingHealth = true;
         HealthCheckService.check().then(function(promise) {
             $scope.healthCheck = promise;
             $scope.updatingHealth = false;
         },function(promise) {
             $scope.healthCheck = promise.data;
             $scope.updatingHealth = false;
         });
     }

     $scope.refresh();

     $scope.getLabelClass = function(statusState) {
         if (statusState == 'UP') {
             return "label-success";
         } else {
             return "label-danger";
         }
     }
 });

awardsApp.controller('ConfigurationController', function ($scope, resolvedConfiguration) {
    $scope.configuration = resolvedConfiguration;
});

awardsApp.controller('MetricsController', function ($scope, MetricsService, HealthCheckService, ThreadDumpService) {
        $scope.metrics = {};
		$scope.updatingMetrics = true;

        $scope.refresh = function() {
			$scope.updatingMetrics = true;
			MetricsService.get().then(function(promise) {
        		$scope.metrics = promise;
				$scope.updatingMetrics = false;
        	},function(promise) {
        		$scope.metrics = promise.data;
				$scope.updatingMetrics = false;
        	});
        };

		$scope.$watch('metrics', function(newValue, oldValue) {
			$scope.servicesStats = {};
            $scope.cachesStats = {};
            angular.forEach(newValue.timers, function(value, key) {
                if (key.indexOf("web.rest") != -1 || key.indexOf("service") != -1) {
                    $scope.servicesStats[key] = value;
                }

                if (key.indexOf("net.sf.ehcache.Cache") != -1) {
                    // remove gets or puts
                    var index = key.lastIndexOf(".");
                    var newKey = key.substr(0, index);

                    // Keep the name of the domain
                    index = newKey.lastIndexOf(".");
                    $scope.cachesStats[newKey] = {
                        'name': newKey.substr(index + 1),
                        'value': value
                    };
                };
            });
		});

        $scope.refresh();

        $scope.threadDump = function() {
            ThreadDumpService.dump().then(function(data) {
                $scope.threadDump = data;

                $scope.threadDumpRunnable = 0;
                $scope.threadDumpWaiting = 0;
                $scope.threadDumpTimedWaiting = 0;
                $scope.threadDumpBlocked = 0;

                angular.forEach(data, function(value, key) {
                    if (value.threadState == 'RUNNABLE') {
                        $scope.threadDumpRunnable += 1;
                    } else if (value.threadState == 'WAITING') {
                        $scope.threadDumpWaiting += 1;
                    } else if (value.threadState == 'TIMED_WAITING') {
                        $scope.threadDumpTimedWaiting += 1;
                    } else if (value.threadState == 'BLOCKED') {
                        $scope.threadDumpBlocked += 1;
                    }
                });

                $scope.threadDumpAll = $scope.threadDumpRunnable + $scope.threadDumpWaiting +
                    $scope.threadDumpTimedWaiting + $scope.threadDumpBlocked;

            });
        };

        $scope.getLabelClass = function(threadState) {
            if (threadState == 'RUNNABLE') {
                return "label-success";
            } else if (threadState == 'WAITING') {
                return "label-info";
            } else if (threadState == 'TIMED_WAITING') {
                return "label-warning";
            } else if (threadState == 'BLOCKED') {
                return "label-danger";
            }
        };
    });

awardsApp.controller('LogsController', function ($scope, resolvedLogs, LogsService) {
        $scope.loggers = resolvedLogs;

        $scope.changeLevel = function (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                $scope.loggers = LogsService.findAll();
            });
        }
    });

awardsApp.controller('CategoryController', ['$scope','CategoryService', function ($scope, CategoryService) {
    CategoryService.all().then(function(data){
        $scope.categories = data;
    });

}]);

awardsApp.controller('NomineeController', [' $scope','NomineeService','CategoryService', function ($scope, NomineeService,CategoryService) {
    $scope.nominees = [];
    $scope.categories = [];
    $scope.getNomineesByCategory = function(categoryId){
        NomineeService.getNomineesByCategoryId(categoryId).then(function(data){
            $scope.nominees = data;
        })
    };

    CategoryService.all().then(function(data){
        console.log(data);
        $scope.categories = data;
    });
}]);

awardsApp.controller('VoteController', ['$scope','VoteService', function ($scope, VoteService) {
    $scope.vote = {};

    $scope.castVote = function(categoryId, nomineeId){
        $scope.vote.userId = "user id";
        $scope.vote.categoryId = categoryId;
        $scope.vote.nomineeId = nomineeId;

        VoteService.castVote($scope.vote);
    };
}]);

awardsApp.controller('TabController', ['$scope', function($scope){
    $scope.tab = -1;
    $scope.selectedTab = function(id){
        $scope.tab = id;
    };
}]);

awardsApp.controller('AuditsController', function ($scope, $translate, $filter, AuditsService) {
        $scope.onChangeDate = function() {
            AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
                $scope.audits = data;
            });
        };

        // Date picker configuration
        $scope.today = function() {
            // Today + 1 day - needed if the current day must be included
            var today = new Date();
            var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate()+1); // create new increased date

            $scope.toDate = $filter('date')(tomorrow, "yyyy-MM-dd");
        };

        $scope.previousMonth = function() {
            var fromDate = new Date();
            if (fromDate.getMonth() == 0) {
                fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
            } else {
                fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
            }

            $scope.fromDate = $filter('date')(fromDate, "yyyy-MM-dd");
        };

        $scope.today();
        $scope.previousMonth();

        AuditsService.findByDates($scope.fromDate, $scope.toDate).then(function(data){
            $scope.audits = data;
        });
    });
