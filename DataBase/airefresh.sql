CREATE DATABASE  IF NOT EXISTS `airefresh` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `airefresh`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: airefresh
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `equipos`
--

DROP TABLE IF EXISTS `equipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL DEFAULT b'1',
  `btu` int NOT NULL,
  `destacado` bit(1) NOT NULL DEFAULT b'0',
  `existencias` int NOT NULL,
  `foto` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `marca` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `modelo` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `precio` decimal(14,2) NOT NULL,
  `precio_anterior` decimal(14,2) DEFAULT NULL,
  `tipo` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `equipos_chk_1` CHECK ((`btu` >= 1000)),
  CONSTRAINT `equipos_chk_2` CHECK ((`existencias` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipos`
--

LOCK TABLES `equipos` WRITE;
/*!40000 ALTER TABLE `equipos` DISABLE KEYS */;
INSERT INTO `equipos` VALUES (1,_binary '\0',12000,_binary '\0',2,'aac64038-238c-4edc-9e6e-3997a837163b.png','Mirage inverter','Mini Split Mirage',10000000.00,2000000.00,'Mini Split Inverter'),(2,_binary '',18000,_binary '',0,'13eca686-2299-4c97-a452-f9f4f25e8b00.png','Mirage inverter','Mini Split Mirage',10000118.00,2000000.00,'Mini Split ON/OFF');
/*!40000 ALTER TABLE `equipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_items`
--

DROP TABLE IF EXISTS `pedido_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `descripcion` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `precio_unitario` decimal(14,2) NOT NULL,
  `equipo_id` bigint DEFAULT NULL,
  `pedido_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe4399jn49usmq4go3p0c5hacg` (`equipo_id`),
  KEY `FKnchocvgm3pbl25qxityq2whip` (`pedido_id`),
  CONSTRAINT `FKe4399jn49usmq4go3p0c5hacg` FOREIGN KEY (`equipo_id`) REFERENCES `equipos` (`id`),
  CONSTRAINT `FKnchocvgm3pbl25qxityq2whip` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_items`
--

LOCK TABLES `pedido_items` WRITE;
/*!40000 ALTER TABLE `pedido_items` DISABLE KEYS */;
INSERT INTO `pedido_items` VALUES (1,1,'Mirage inverter Mini Split Mirage',10000000.00,1,1),(2,1,'Mirage inverter Mini Split Mirage',10000000.00,1,2),(3,1,'Mirage inverter Mini Split Mirage',10000000.00,1,3);
/*!40000 ALTER TABLE `pedido_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creado_en` datetime(6) NOT NULL,
  `direccion` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `metodo_pago` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notas` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total` decimal(14,2) NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5g0es69v35nmkmpi8uewbphs2` (`usuario_id`),
  CONSTRAINT `FK5g0es69v35nmkmpi8uewbphs2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,'2026-10-14 19:39:40.702626','dadasdas','PENDIENTE','CONTRAENTREGA','skafdhkljhfalbvyvdehlzskahvnvsa','3123120120',10000000.00,1),(2,'2026-10-14 19:49:10.599537','dadasdas','ENVIADO','CONTRAENTREGA','salhfdñhfcasldfhasdhakjshdakjshdakjhsdakhd','3212312213',10000000.00,3),(3,'2026-10-14 20:35:02.889114','puto el que lo leea','CANCELADO','CONTRAENTREGA','we','3123120120',10000000.00,4);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clave` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `correo` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `creado_en` datetime(6) NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcdmw5hxlfj78uf4997i3qyyw5` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'$2a$10$3SngHBMbIMDV7.mr.akVyef9VMEcbSib2clW.R.VdTueQbRKPCfgy','juan123@gmail.com','2026-10-14 18:24:48.666719','juan','CLIENTE'),(2,'$2a$10$ZE1cGZv1b7U4XaYdTVnlfOJZaFioGv/WE5V3u2zmD9IF3sK2b6qVm','admin321@gmail.com','2026-10-14 18:30:51.524537','admin','ADMIN'),(3,'$2a$10$e/3D/WhW4ac4lWqTx7p35e2BlnUuVT4m5c93CRHT6bbtlT4JW75ua','jhon123@gmail.com','2026-10-14 19:48:08.978430','jhon','CLIENTE'),(4,'$2a$10$N6Bqvg0zIMFG4/qsC1XBEuLuwhFGichtdi2fFzAd3.p7kbwsd63V6','jelen4321@gmail.com','2026-10-14 20:32:11.874970','jelen','CLIENTE');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-27 21:54:30
