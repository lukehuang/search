/************************************************************分割线************************************************************/
/* todo search_test（搜索） */

CREATE TABLE `search_test`  (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `property1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                              `property2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                              `property3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

/* Lucene测试数据（单字段） */
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (11, '飞利浦led灯泡e27螺口暖白球泡灯家用照明超亮节能灯泡转色温灯泡', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (12, '飞利浦led灯泡e14螺口蜡烛灯泡3W尖泡拉尾节能灯泡暖黄光源Lamp', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (13, '雷士照明 LED灯泡 e27大螺口节能灯3W球泡灯 Lamp led节能灯泡', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (14, '飞利浦 led灯泡 e27螺口家用3w暖白球泡灯节能灯5W灯泡LED单灯7w', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (15, '飞利浦led小球泡e14螺口4.5w透明款led节能灯泡照明光源lamp单灯', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (16, '飞利浦蒲公英护眼台灯工作学习阅读节能灯具30508带光源', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (17, '欧普照明led灯泡蜡烛节能灯泡e14螺口球泡灯超亮照明单灯光源', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (18, '欧普照明led灯泡节能灯泡超亮光源e14e27螺旋螺口小球泡暖黄家用', '', '');
INSERT INTO `search_test`(`id`, `property1`, `property2`, `property3`) VALUES (19, '聚欧普照明led灯泡节能灯泡e27螺口球泡家用led照明单灯超亮光源', '', '');