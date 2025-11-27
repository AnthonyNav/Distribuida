-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-11-2025 a las 09:22:28
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `distribuida`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellidos` varchar(200) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `clave` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `nombre`, `apellidos`, `email`, `telefono`, `clave`) VALUES
(3, 'Luis', 'Hernández Díaz', 'luis.hernandez@example.com', '5534567890', 'USR0003'),
(4, 'Ana', 'Ramírez Torres', 'ana.ramirez@example.com', '5545678901', 'USR0004'),
(5, 'Carlos', 'Sánchez Gómez', 'carlos.sanchez@example.com', '5556789012', 'USR0005'),
(6, 'Lucía', 'Flores Martínez', 'lucia.flores@example.com', '5567890123', 'USR0006'),
(7, 'Pedro', 'López Rivera', 'pedro.lopez@example.com', '5578901234', 'USR0007'),
(8, 'Sofía', 'Morales Campos', 'sofia.morales@example.com', '5589012345', 'USR0008'),
(9, 'Diego', 'Cruz Navarro', 'diego.cruz@example.com', '5590123456', 'USR0009'),
(10, 'Elena', 'Vegas Castillo', 'elena.vargas@example.com', '5510987653', 'USR0010');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
