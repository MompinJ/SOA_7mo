INSERT IGNORE INTO socio (numero_socio, nombre, correo, vencimiento_membresia) VALUES
('FZ000123', 'Laura Mendez Castro', 'laura.mendez@correo.com', '2027-06-30'),
('FZ000124', 'Jorge Salas Romero', 'jorge.salas@correo.com', '2027-01-15'),
('FZ000125', 'Paola Rios Vega', 'paola.rios@correo.com', '2026-09-30');

INSERT IGNORE INTO clase (id, nombre, sucursal, fecha_hora, cupo_maximo, inscritos, cancelada) VALUES
(1, 'Yoga', 'Centro', '2026-12-01 07:00:00', 20, 0, false),
(2, 'Spinning', 'Animas', '2026-12-01 19:00:00', 20, 19, false),
(3, 'Funcional', 'Xalapa 2000', '2026-12-02 08:00:00', 20, 20, false),
(4, 'Yoga', 'Animas', '2026-12-03 18:00:00', 20, 5, true),
(5, 'Spinning', 'Centro', '2026-10-01 07:00:00', 20, 12, false);
