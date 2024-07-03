DROP TABLE IF EXISTS `url`;
CREATE TABLE `url` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `click_count` bigint(20) DEFAULT NULL,
                       `original_url` varchar(2048) DEFAULT NULL,
                       `short_code` varchar(255) NOT NULL,
                       `short_url` varchar(255) DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `UKrnuqssct6dihd9xis72slp91f` (`short_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
