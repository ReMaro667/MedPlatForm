/*
 Navicat Premium Data Transfer

 Source Server         : Docker
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : 192.168.159.128:3306
 Source Schema         : IOMCP

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 19/04/2025 20:48:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appointment
-- ----------------------------
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment`  (
  `appointment_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `patient_id` bigint UNSIGNED NOT NULL COMMENT '患者ID',
  `schedule_id` bigint UNSIGNED NOT NULL COMMENT '排班ID',
  `status` enum('pending','paid','cancelled','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`appointment_id`) USING BTREE,
  INDEX `idx_patient`(`patient_id` ASC) USING BTREE,
  INDEX `idx_schedule`(`schedule_id` ASC) USING BTREE,
  CONSTRAINT `fk_appointment_patient` FOREIGN KEY (`patient_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appointment
-- ----------------------------

-- ----------------------------
-- Table structure for consultation
-- ----------------------------
DROP TABLE IF EXISTS `consultation`;
CREATE TABLE `consultation`  (
  `consultation_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '问诊ID',
  `patient_id` bigint UNSIGNED NOT NULL COMMENT '患者ID',
  `doctor_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '接诊医生ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '症状描述',
  `advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'AI预诊断建议',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `diagnosis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `status` enum('pending','paid','cancelled','completed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'pending',
  `appointment_id` bigint UNSIGNED NOT NULL COMMENT '预约ID',
  `total` double NOT NULL,
  PRIMARY KEY (`consultation_id`) USING BTREE,
  INDEX `idx_patient`(`patient_id` ASC) USING BTREE,
  INDEX `idx_doctor`(`doctor_id` ASC) USING BTREE,
  INDEX `appointment_id`(`appointment_id` ASC) USING BTREE,
  CONSTRAINT `fk_consultation_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_consultation_patient` FOREIGN KEY (`patient_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `consultation_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 445632157226893319 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of consultation
-- ----------------------------

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科室名称（如心血管内科）',
  `parent_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '上级科室ID（树形结构）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科室描述',
  `disease` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`department_id`) USING BTREE,
  INDEX `idx_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_department_parent` FOREIGN KEY (`parent_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '内科', NULL, '2025-03-27 20:08:47', NULL, NULL);
INSERT INTO `department` VALUES (2, '心血管内科', 1, '2025-03-27 20:08:47', NULL, NULL);
INSERT INTO `department` VALUES (3, '外科', NULL, '2025-03-27 20:08:47', NULL, NULL);
INSERT INTO `department` VALUES (4, '内科', NULL, '2025-03-30 17:44:52', '负责内科疾病诊断与治疗', NULL);
INSERT INTO `department` VALUES (5, '外科', NULL, '2025-03-30 17:44:52', '负责外科手术及创伤处理', NULL);
INSERT INTO `department` VALUES (6, '妇产科', NULL, '2025-03-30 17:44:52', '妇科与产科医疗服务', NULL);
INSERT INTO `department` VALUES (7, '儿科', NULL, '2025-03-30 17:44:52', '儿童疾病诊疗与保健', NULL);
INSERT INTO `department` VALUES (8, '眼科', NULL, '2025-03-30 17:44:52', '眼部疾病治疗与手术', NULL);
INSERT INTO `department` VALUES (9, '耳鼻喉科', NULL, '2025-03-30 17:44:52', '耳、鼻、喉部疾病诊疗', NULL);
INSERT INTO `department` VALUES (10, '皮肤科', NULL, '2025-03-30 17:44:52', '皮肤病与性病治疗', NULL);
INSERT INTO `department` VALUES (20, '中医科', NULL, '2025-03-30 17:51:38', '传统中医诊疗', NULL);
INSERT INTO `department` VALUES (22, '心血管内科', 4, '2025-03-30 17:55:40', '高血压、冠心病等心血管疾病', '胸痛, 心悸, 气短');
INSERT INTO `department` VALUES (23, '呼吸内科', 4, '2025-03-30 17:55:40', '肺炎、哮喘等呼吸道疾病', NULL);
INSERT INTO `department` VALUES (24, '消化内科', 4, '2025-03-30 17:55:40', '胃炎、肝炎等消化系统疾病', '腹痛, 腹泻, 恶心');
INSERT INTO `department` VALUES (25, '普通外科', 5, '2025-03-30 17:57:12', '阑尾炎、疝气等普通手术', NULL);
INSERT INTO `department` VALUES (26, '骨科', 5, '2025-03-30 17:57:12', '骨折、关节置换等骨科手术', NULL);
INSERT INTO `department` VALUES (27, '神经外科', 5, '2025-03-30 17:57:12', '脑部与脊髓手术', NULL);
INSERT INTO `department` VALUES (28, '妇科', 6, '2025-03-30 17:57:54', '女性生殖系统疾病诊疗', NULL);
INSERT INTO `department` VALUES (29, '产科', 6, '2025-03-30 17:57:54', '孕产妇保健与分娩服务', NULL);
INSERT INTO `department` VALUES (30, '儿童保健科', 7, '2025-03-30 17:58:40', '儿童生长发育监测', NULL);
INSERT INTO `department` VALUES (31, '白内障专科', 7, '2025-03-30 17:58:40', '白内障诊断与手术', NULL);

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `doctor_id` bigint UNSIGNED NOT NULL,
  `department_id` bigint UNSIGNED NOT NULL COMMENT '科室ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称（如主任医师）',
  `specialty` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '擅长领域',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '医生简介',
  `license_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医师执业证书编号',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`doctor_id`) USING BTREE,
  INDEX `idx_department`(`department_id` ASC) USING BTREE,
  CONSTRAINT `fk_doctor_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (1, 4, NULL, NULL, NULL, NULL, '2025-03-30 19:37:33', '舞舞舞');
INSERT INTO `doctor` VALUES (2, 6, NULL, NULL, NULL, NULL, '2025-03-30 19:37:40', '六六六');
INSERT INTO `doctor` VALUES (3, 4, NULL, NULL, NULL, NULL, '2025-03-30 19:38:31', '十六七');

-- ----------------------------
-- Table structure for drug
-- ----------------------------
DROP TABLE IF EXISTS `drug`;
CREATE TABLE `drug`  (
  `drug_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '药品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '药品名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类（如抗生素、中成药）',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `pharmacy_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '所属药房ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`drug_id`) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_pharmacy`(`pharmacy_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of drug
-- ----------------------------
INSERT INTO `drug` VALUES (1, '阿莫西林胶囊', '抗生素', 15.80, 0, NULL, '2025-03-27 20:09:44');
INSERT INTO `drug` VALUES (2, '连花清瘟胶囊', '中成药', 28.50, 188, NULL, '2025-03-27 20:09:44');

-- ----------------------------
-- Table structure for prescription
-- ----------------------------
DROP TABLE IF EXISTS `prescription`;
CREATE TABLE `prescription`  (
  `prescription_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '处方ID',
  `consultation_id` bigint UNSIGNED NOT NULL COMMENT '关联问诊ID',
  `pharmacist_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '审核药师ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `drug_id` bigint UNSIGNED NOT NULL COMMENT '药品ID',
  `quantity` int NULL DEFAULT NULL,
  PRIMARY KEY (`prescription_id`) USING BTREE,
  INDEX `idx_pharmacist`(`pharmacist_id` ASC) USING BTREE,
  INDEX `consultation_id`(`consultation_id` ASC) USING BTREE,
  INDEX `drug_id`(`drug_id` ASC) USING BTREE,
  CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`consultation_id`) REFERENCES `consultation` (`consultation_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `prescription_ibfk_2` FOREIGN KEY (`drug_id`) REFERENCES `drug` (`drug_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prescription
-- ----------------------------

-- ----------------------------
-- Table structure for queue
-- ----------------------------
DROP TABLE IF EXISTS `queue`;
CREATE TABLE `queue`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `queue_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排队号(Q20240315001)',
  `status` tinyint NOT NULL COMMENT '1:等待中 2:已叫号 3:就诊中 4:已完成 5:过号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `roomId` int NULL DEFAULT NULL COMMENT '问诊房间号',
  `appointment_id` bigint NULL DEFAULT NULL COMMENT '预约号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of queue
-- ----------------------------

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `schedule_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `doctor_id` bigint UNSIGNED NOT NULL COMMENT '医生ID',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `max_patients` int NOT NULL COMMENT '最大预约数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `work_date` date NULL DEFAULT NULL,
  `department_id` bigint NOT NULL,
  PRIMARY KEY (`schedule_id`) USING BTREE,
  INDEX `idx_doctor_date`(`doctor_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (1, 1, '08:00:00', '18:00:00', 0, '2025-04-01 19:29:19', '2025-04-02', 22);
INSERT INTO `schedule` VALUES (2, 2, '08:00:00', '18:00:00', 1, '2025-04-02 22:54:46', '2025-04-03', 23);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Tescal', '$2a$10$xyz...', '13800000004', NULL, '李四', NULL, '2025-03-27 20:08:58', '2025-04-19 20:02:19');
INSERT INTO `user` VALUES (2, 'CalmLion983', '$2a$10$pKLNowox7c2ZWAesYGSzcefHaSAw0.f1Acb1zosfYsR02UtNBAAQ2', '19197061091', NULL, '影子', NULL, '2025-03-28 21:41:56', '2025-03-28 21:41:56');

SET FOREIGN_KEY_CHECKS = 1;
