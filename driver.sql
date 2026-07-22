-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2026 at 04:09 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_ridehailing`
--

-- --------------------------------------------------------

--
-- Table structure for table `driver`
--

CREATE TABLE `driver` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `rating` decimal(2,1) DEFAULT 5.0,
  `status` varchar(20) DEFAULT 'Available',
  `earnings` decimal(10,2) DEFAULT 0.00,
  `plate_number` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `driver`
--

INSERT INTO `driver` (`id`, `name`, `phone`, `password`, `rating`, `status`, `earnings`, `plate_number`) VALUES
('D001', 'Marco Reyes', '0917-111-2222', '1234', 5.0, 'Available', 0.00, 'NBC-1234'),
('D002', 'Liza Cruz', '0917-333-4444', '1234', 5.0, 'Available', 0.00, 'ABC-5678'),
('D003', 'Roma', '963963', '852852', 5.0, 'Available', 0.00, '12345'),
('D004', 'AljunBayot', '741741', '456456', 5.0, 'Available', 0.00, '78963'),
('D005', 'RomaZ7', '7852', '2587', 5.0, 'Available', 0.00, '12369'),
('D006', 'Kenn', '090807', '0000', 5.0, 'Available', 0.00, '8579'),
('D007', 'RomaTHE', '080704', '1111', 5.0, 'Available', 0.00, '365486');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `driver`
--
ALTER TABLE `driver`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `phone` (`phone`),
  ADD KEY `plate_number` (`plate_number`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `driver`
--
ALTER TABLE `driver`
  ADD CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`plate_number`) REFERENCES `vehicle` (`plate_number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
