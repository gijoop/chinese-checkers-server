/*!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.6.18-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: chinese_checkers
-- ------------------------------------------------------
-- Server version	10.6.18-MariaDB-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_players` int(11) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT current_timestamp(),
  `ruleset` enum('STANDARD','FAST_PACED') NOT NULL DEFAULT 'STANDARD',
  `current_turn` enum('UPPER_LEFT','UPPER_RIGHT','LOWER_LEFT','LOWER_RIGHT','UPPER','LOWER') NOT NULL DEFAULT 'UPPER',
  `board_size` int(11) NOT NULL DEFAULT 5,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (14,2,'2025-01-25 22:10:26','STANDARD','LOWER',5),(15,2,'2025-01-25 22:13:50','STANDARD','UPPER',5),(16,2,'2025-01-25 22:32:27','STANDARD','UPPER',5),(17,2,'2025-01-25 22:33:18','STANDARD','LOWER',5);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `move`
--

DROP TABLE IF EXISTS `move`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `move` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `move_number` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `from_x` int(11) NOT NULL,
  `from_y` int(11) NOT NULL,
  `to_x` int(11) NOT NULL,
  `to_y` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `game_id` (`game_id`),
  CONSTRAINT `move_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `move`
--

LOCK TABLES `move` WRITE;
/*!40000 ALTER TABLE `move` DISABLE KEYS */;
INSERT INTO `move` VALUES (13,1,14,-3,-4,-2,-3),(14,2,14,3,4,3,3),(15,3,14,1,4,1,3),(16,4,14,-4,-4,-3,-3),(17,5,14,3,3,2,2),(18,6,14,-1,-4,0,-2),(19,7,14,-2,-4,-1,-2),(20,8,14,0,-4,1,-2),(21,9,14,0,-2,0,-2),(22,10,14,-1,-2,-1,-2),(23,11,14,-2,-3,-2,-2),(24,12,14,2,2,2,1),(25,13,14,1,3,0,1),(26,14,14,1,3,1,1),(27,15,14,2,4,0,2),(28,16,14,0,2,2,4),(29,17,14,2,4,0,2),(30,1,17,-2,-4,-1,-3),(31,2,17,2,4,1,3),(32,3,17,-1,-4,-1,-2),(33,4,17,1,4,1,2),(34,5,17,0,-4,1,-3),(35,6,17,4,4,4,3),(36,7,17,1,3,1,1);
/*!40000 ALTER TABLE `move` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'chinese_checkers'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-25 22:37:45
