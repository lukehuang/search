/************************************************************分割线************************************************************/
/* todo search_test（搜索） */

CREATE TABLE `search_test`  (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `property1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                         `property2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                         `property3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (1, 'a1', 'b4', 'c5');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (2, 'a2', 'b4', 'c6');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (3, 'a2', 'b4', 'c6');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (4, 'a3', 'b4', 'c7');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (5, 'a3', 'b3', 'c7');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (6, 'a3', 'b3', 'c7');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (7, 'a4', 'b3', 'c8');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (8, 'a4', 'b2', 'c8');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (9, 'a4', 'b2', 'c8');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (10, 'ab4', 'b1', 'c8');