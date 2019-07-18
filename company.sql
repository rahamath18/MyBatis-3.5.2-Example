CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);



DELIMITER //
CREATE PROCEDURE company.getUserById
(IN user_id int)
BEGIN
  SELECT * FROM company.USER
  WHERE id = user_id;
END //
DELIMITER ;


CALL company.getUserById(2);

