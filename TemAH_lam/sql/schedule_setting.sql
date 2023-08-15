CREATE TABLE `schedule_setting` (
                                    `job_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
                                    `bean_name` VARCHAR(255) NULL DEFAULT NULL COMMENT 'bean名称',
                                    `method_name` VARCHAR(255) NULL DEFAULT NULL COMMENT '方法名称',
                                    `method_params` VARCHAR(255) NULL DEFAULT NULL COMMENT '方法参数',
                                    `cron_expression` VARCHAR(255) NULL DEFAULT NULL COMMENT 'cron表达式',
                                    `remark` VARCHAR(255) NULL DEFAULT NULL COMMENT '备注',
                                    `job_status` INT(11) NULL DEFAULT '1' COMMENT '状态(1正常 0暂停)',
                                    `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_time` DATETIME NULL DEFAULT NULL COMMENT '修改时间',
                                    PRIMARY KEY (`job_id`) USING BTREE
)
    COLLATE='utf8_general_ci'
;
