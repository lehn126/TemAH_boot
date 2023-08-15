CREATE TABLE `alarm` (
                         `id` INT(11) NOT NULL AUTO_INCREMENT,
                         `eventTime` VARCHAR(50) NULL DEFAULT NULL,
                         `managedObject` VARCHAR(255) NULL DEFAULT NULL,
                         `alarmType` VARCHAR(50) NULL DEFAULT NULL,
                         `probableCause` VARCHAR(255) NULL DEFAULT NULL,
                         `perceivedSeverity` VARCHAR(50) NULL DEFAULT NULL,
                         `specificProblem` VARCHAR(255) NULL DEFAULT NULL,
                         `additionalText` LONGTEXT NULL DEFAULT NULL,
                         `clearFlag` INT(11) UNSIGNED NULL DEFAULT '0',
                         `terminateState` INT(11) UNSIGNED NULL DEFAULT '0',
                         PRIMARY KEY (`id`) USING BTREE
)
    COLLATE='utf8_general_ci'
AUTO_INCREMENT=1
;
