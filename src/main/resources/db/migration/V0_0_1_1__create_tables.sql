CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4xqvdpkafb91tt3hsb67ga3fj` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1t68827l97cwyxo9r1u6t4p7d` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `professor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cpf` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `department_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pk1omryj5cud6uslkepgyfrca` (`cpf`),
  KEY `FKbxh9gr7acx9qalq9jjcj4j9tr` (`department_id`),
  CONSTRAINT `FKbxh9gr7acx9qalq9jjcj4j9tr` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `allocation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day` varchar(255) NOT NULL,
  `start` time NOT NULL,
  `end` time NOT NULL,
  `course_id` bigint NOT NULL,
  `professor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9v3xv73yong82i54eabplyabv` (`course_id`),
  KEY `FKnqw55qf7cu1g34wxv5rfjf53t` (`professor_id`),
  CONSTRAINT `FK9v3xv73yong82i54eabplyabv` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKnqw55qf7cu1g34wxv5rfjf53t` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
