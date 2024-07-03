DROP TABLE IF EXISTS `url_statistics`;
CREATE TABLE `url_statistics` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `click_count` bigint(20) DEFAULT NULL,
                                  `short_code` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
