DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` IDENTITY NOT NULL PRIMARY KEY,
  `product_name` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `description` text,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
);



INSERT INTO `product` (`id`, `product_name`, `price`, `description`, `group_id`, `user_id`) VALUES
(1, 'Телефон', 228, 'Nokia 330', 1, 1);



DROP TABLE IF EXISTS `product_group`;

CREATE TABLE `product_group` (
  `id` IDENTITY NOT NULL PRIMARY KEY,
  `group_name` varchar(50) DEFAULT NULL
);



INSERT INTO `product_group` (`id`, `group_name`) VALUES
(1, 'Техника');


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` IDENTITY NOT NULL PRIMARY KEY,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL
);


INSERT INTO `user` (`id`, `name`, `email`, `password`) VALUES
(1, 'Roma', 'roma@mail.ru', '123');




ALTER TABLE PRODUCT 
   ADD CONSTRAINT FK_product_user FOREIGN KEY (USER_ID ) REFERENCES USER  (ID);
ALTER TABLE PRODUCT 
   ADD CONSTRAINT FK_product_product_group FOREIGN KEY (GROUP_ID) REFERENCES PRODUCT_GROUP (ID) ;
