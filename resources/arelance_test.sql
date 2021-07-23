-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 23, 2021 at 02:31 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 7.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `arelance_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `id` int(11) NOT NULL,
  `active` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `active`, `created`, `description`, `name`, `updated`) VALUES
(5, 1, '2021-07-23 06:06:41', 'Equipo encargado del ámbito informático de la empresa', 'Desarrollo', '2021-07-23 06:06:41'),
(3, 1, '2021-07-22 20:34:55', 'Se encarga del manejo de finanzas de la empresa.', 'Contabilidad', '2021-07-22 21:10:21'),
(4, 1, '2021-07-23 01:26:57', 'Realiza y supervisa las operaciones administrativas de la empresa', 'Administración', '2021-07-23 01:26:57');

-- --------------------------------------------------------

--
-- Table structure for table `department_user`
--

CREATE TABLE `department_user` (
  `department_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `department_user`
--

INSERT INTO `department_user` (`department_id`, `user_id`) VALUES
(3, 6),
(3, 7),
(3, 8),
(3, 9),
(4, 1),
(4, 3),
(4, 5),
(4, 10),
(5, 6),
(5, 9),
(5, 11),
(5, 12),
(5, 13);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `active` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `updated` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `active`, `created`, `role_name`, `updated`) VALUES
(1, 1, '2021-07-22 16:34:52', 'ROLE_ADMIN', '2021-07-23 02:30:45'),
(2, 1, '2021-07-22 16:34:52', 'ROLE_CONSULTANT', '2021-07-22 16:34:52');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `active` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  `created` datetime DEFAULT NULL,
  `dni` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `last` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `updated` datetime DEFAULT NULL,
  `util` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `active`, `address`, `created`, `dni`, `email`, `last`, `name`, `password`, `updated`, `util`, `role_id`) VALUES
(1, 1, 'San Salvador, El Salvador', '2021-07-22 08:46:04', '12345678a', 'jeffrey.sdev01@gmail.com', 'Morán', 'Jeffrey', '$2a$10$i64TMesCy9HctXOqElKWEOvjYBCvpA/kqt0UT47vJVh0D8AHm3ODC', '2021-07-23 06:30:03', 0, 1),
(3, 1, 'Santa Ana, El Salvador', '2021-07-22 11:57:44', '00000000a', 'jacinto.gomez@gmail.com', 'Gómez', 'Jacinto', '$2a$10$/u6pVUdDqdDkbvhCmlhf9e4L5lIiZ9aJQ0BFyjyL9rPe8z4vV8J6K', '2021-07-23 06:30:31', 0, 1),
(5, 1, 'San Miguuel, El Salvador', '2021-07-23 01:57:38', '47856328h', 'natalie.velasco@gmail.com', 'Velasco', 'Natalie', '$2a$10$DCzSvPxxMcM0FuWHYzMjZus4QqQCUyXMk5TVgZ5FnfkVgrq6Ct1y.', '2021-07-23 01:57:38', 0, 1),
(9, 1, 'La Páz, El Salvador', '2021-07-23 06:19:42', '45786985m', 'sandra.lara@gmail.com', 'Lara', 'Sandra', '$2a$10$OfpnTtDl/8OGBrZd/ZffBuVDitBUTXQsaGH2jWeUEH3WXUqOav.hm', '2021-07-23 06:19:42', 0, 2),
(6, 1, 'La Unión, El Salvador', '2021-07-23 06:16:23', '45789652g', 'fernando.martinez@gmail.com', 'Martínez', 'Fernando', '$2a$10$qln8g5AaPC77Bfz.I2mTDOGHbmRNEEpToT.ZzuIYhl6xEFSOj5Bw2', '2021-07-23 06:16:23', 0, 2),
(7, 1, 'Usulután, El Salvador', '2021-07-23 06:16:55', '78915423c', 'gabriela.valle@gmail.com', 'Valle', 'Gabriela', '$2a$10$w9RxArPmtu1evhVIasvf8eG88SEPjQbT9TgtDmg3NqoBQfjXwcjhW', '2021-07-23 06:16:55', 0, 2),
(8, 1, 'Sonsonate, El Salvador', '2021-07-23 06:17:27', '57984125b', 'david.bonilla@gmail.com', 'Bonilla', 'David', '$2a$10$ESsutSXEE1SN2qpY4Rzc8.GuFbkneRGViL.2aE.4EUkhtrasWHmRC', '2021-07-23 06:17:27', 0, 2),
(10, 1, 'Chalatenango, El Salvador', '2021-07-23 06:20:23', '09875478t', 'karla.larín@gmail.com', 'larín', 'karla', '$2a$10$2G1xtVp1KC3Sypf/XEPHt.txOF.hm8PTnWqBsXZFfJu0qPpi7PKzO', '2021-07-23 06:20:23', 0, 2),
(11, 1, 'Cabañas, El Salvador', '2021-07-23 06:20:57', '78567409r', 'oliverio.castaneda@gmail.com', 'castaneda', 'oliverio', '$2a$10$wYxo7SLCjVhsjJ4O9DfrB.cIUBNLaqbyNaYQt6PtgpCE.DFVacbWm', '2021-07-23 06:20:57', 0, 2),
(12, 1, 'San Vicente, El Salvador', '2021-07-23 06:21:40', '25368902f', 'hector.mendoza@gmail.com', 'Mendoza', 'Hector', '$2a$10$ZRYTfQOvkJdsWj.3vmJIs.224mOMMoUMAyHik4/ZGCzHhj4eb89Ue', '2021-07-23 06:21:40', 0, 2),
(13, 1, 'San Miguel, El Salvador', '2021-07-23 06:22:27', '78956231s', 'carlos.carrillo@gmail.com', 'Carrillo', 'Carlos', '$2a$10$3vOsSSuGHspKauQ5r9KPZeQwsQp4ak0FhK9G6CmXGHuS3MAslQuji', '2021-07-23 06:22:27', 0, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `department_user`
--
ALTER TABLE `department_user`
  ADD PRIMARY KEY (`department_id`,`user_id`),
  ADD KEY `FK7daeqeyhma78i8lxvgni7yqdf` (`user_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
