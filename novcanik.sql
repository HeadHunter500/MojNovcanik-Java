/*
SQLyog Community v11.33 (64 bit)
MySQL - 5.5.36 : Database - novcanik
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`novcanik` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `novcanik`;

/*Table structure for table `isplata` */

DROP TABLE IF EXISTS `isplata`;

CREATE TABLE `isplata` (
  `korisnik` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `nazivIsplate` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `iznos` decimal(7,2) DEFAULT NULL,
  `vrijemeIsplate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COLLATE=cp1250_croatian_ci;

/*Data for the table `isplata` */

insert  into `isplata`(`korisnik`,`nazivIsplate`,`iznos`,`vrijemeIsplate`) values ('matko','karta za vlak','39.80','2014-06-01 19:00:00'),('matko','karta za vlak','39.80','2014-06-01 19:00:00'),('matko','lizalica','1.50','2014-06-02 11:34:23'),('matko','platio sobu za 6. mjesec','200.00','2014-06-02 14:00:26'),('matko','sladoled','6.00','2014-06-02 18:00:26'),('matko','piva','10.00','2014-06-03 15:00:26'),('matko','piva','10.00','2014-06-03 15:30:46'),('matko','vecera','8.85','2014-06-03 19:30:46'),('matko','rucak','6.50','2014-06-04 12:10:46'),('matko','piva','10.00','2014-06-04 16:10:46'),('matko','vecera','9.50','2014-06-04 19:10:46'),('matko','party na Vrapcu','20.00','2014-06-04 19:10:46'),('matko','dorucak','6.05','2014-06-05 07:10:46'),('matko','sladoled','2.00','2014-06-05 12:10:46'),('matko','poklon za rodjendan','40.00','2014-06-06 17:10:46'),('matko','vecera','10.00','2014-06-06 19:17:46'),('matko','rucak','10.00','2014-06-07 12:17:46'),('matko','piva','10.00','2014-06-07 14:17:46'),('matko','pizza','20.00','2014-06-08 22:17:46'),('matko','rucak','6.92','2014-06-10 12:40:11'),('matko','majburger','5.43','2014-06-10 17:35:20'),('matko','sladoled','2.00','2014-06-10 17:35:55'),('matko','sendvic kulen','10.00','2014-06-11 01:13:18'),('matko','piva','10.00','2014-06-11 01:13:24'),('matko','rucak','6.50','2014-06-11 16:10:34');

/*Table structure for table `korisnici` */

DROP TABLE IF EXISTS `korisnici`;

CREATE TABLE `korisnici` (
  `ime` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `lozinka` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `vrijemeRegistracije` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin2 COLLATE=latin2_croatian_ci;

/*Data for the table `korisnici` */

insert  into `korisnici`(`ime`,`lozinka`,`vrijemeRegistracije`) values ('matko','7cb6e3cfe8d89ce9a0f745e25e9a9cde','2014-06-10 00:51:45');

/*Table structure for table `uplata` */

DROP TABLE IF EXISTS `uplata`;

CREATE TABLE `uplata` (
  `korisnik` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `nazivUplate` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `iznos` decimal(7,2) DEFAULT NULL,
  `vrijemeUplate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=cp1250 COLLATE=cp1250_croatian_ci;

/*Data for the table `uplata` */

insert  into `uplata`(`korisnik`,`nazivUplate`,`iznos`,`vrijemeUplate`) values ('matko','starci uplatili','300.00','2014-06-01 15:00:00'),('matko','starci uplatili','300.00','2014-06-09 15:00:00'),('matko','posudio ','1.00','2014-06-11 16:10:22');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
