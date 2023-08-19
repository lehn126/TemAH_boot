CREATE TABLE `alarm` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `eventTime` varchar(50) DEFAULT NULL,
                         `managedObject` varchar(255) DEFAULT NULL,
                         `alarmType` varchar(50) DEFAULT NULL,
                         `probableCause` varchar(255) DEFAULT NULL,
                         `perceivedSeverity` varchar(50) DEFAULT NULL,
                         `specificProblem` varchar(255) DEFAULT NULL,
                         `additionalText` longtext DEFAULT NULL,
                         `clearFlag` int(11) DEFAULT 0,
                         `terminateState` int(11) DEFAULT 0,
                         PRIMARY KEY (`id`) USING BTREE
)
    COLLATE='utf8_general_ci'
AUTO_INCREMENT=1
;
