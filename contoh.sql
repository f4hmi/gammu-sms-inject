-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 29, 2015 at 05:10 AM
-- Server version: 5.6.14
-- PHP Version: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `contoh`
--

-- --------------------------------------------------------

--
-- Table structure for table `content`
--

CREATE TABLE IF NOT EXISTS `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nomor` int(15) NOT NULL,
  `pesan` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `content`
--

INSERT INTO `content` (`id`, `nomor`, `pesan`) VALUES
(1, 817170820, 'ADUAN#FAHMI HILMANSYAH#ADA PEMBUNUHAN');

--
-- Triggers `content`
--
DROP TRIGGER IF EXISTS `insert_content`;
DELIMITER //
CREATE TRIGGER `insert_content` AFTER INSERT ON `content`
 FOR EACH ROW BEGIN
	SET @textDecoded = NEW.pesan;
	SET @SenderNumber = NEW.nomor;
#mengambil nama
	SET @nama	=	(select SUBSTRING_INDEX(SUBSTRING_INDEX(@textDecoded,"#",-2),"#",1));
#mengambil content
	SET @isi = (select SUBSTRING_INDEX(@textDecoded,"#",-1));
#untuk mengecek form
	SET @form = (select SUBSTRING_INDEX(@textDecoded,"#",1));
	
	IF UPPER(@form) = "ADUAN" THEN
		INSERT INTO output (nama,keterangan) values(@nama,@isi);
	END IF;
END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `output`
--

CREATE TABLE IF NOT EXISTS `output` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(20) NOT NULL,
  `keterangan` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `output`
--

INSERT INTO `output` (`id`, `nama`, `keterangan`) VALUES
(1, 'FAHMI HILMANSYAH', 'ADA PEMBUNUHAN');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
