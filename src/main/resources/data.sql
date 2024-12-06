-- Datos para la tabla ATHLETES
INSERT INTO athletes (id, nombre, categoria, edad, altura, fecha_registro) VALUES
                                                                               (1, 'Carlos López', 'Profesional', 28, 1.85, '2023-01-10'),
                                                                               (2, 'María García', 'Amateur', 24, 1.70, '2023-02-20'),
                                                                               (3, 'Juan Martínez', 'Profesional', 30, 1.78, '2023-03-05'),
                                                                               (4, 'Ana Fernández', 'Amateur', 22, 1.65, '2023-04-15'),
                                                                               (5, 'Luis Pérez', 'Semiprofesional', 27, 1.80, '2023-05-25');

-- Datos para la tabla TRAINERS
INSERT INTO trainers (id, nombre, especialidad, experiencia, fecha_ingreso, activo) VALUES
                                                                                        (1, 'Pedro Sánchez', 'Fuerza', 10, '2015-05-10', true),
                                                                                        (2, 'Sofía López', 'Cardio', 8, '2018-03-15', true),
                                                                                        (3, 'Diego Torres', 'Flexibilidad', 12, '2010-07-20', true),
                                                                                        (4, 'Carmen Muñoz', 'Resistencia', 6, '2020-01-05', false);

-- Datos para la tabla TRAININGS
INSERT INTO trainings (id, tipo, nivel, duracion, fecha, completado) VALUES
                                                                        (1, 'Cardio', 'Intermedio', 45, '2023-06-10', true),
                                                                        (2, 'Fuerza', 'Avanzado', 60, '2023-06-12', false),
                                                                        (3, 'Flexibilidad', 'Básico', 30, '2023-06-15', true);

-- Datos para la tabla EVENTS
INSERT INTO events (id, nombre, ubicacion, capacidad, fecha_evento) VALUES
                                                                        (1, 'Maratón de Primavera', 'Madrid', 500, '2024-04-10'),
                                                                        (2, 'Carrera Nocturna', 'Barcelona', 300, '2024-06-15'),
                                                                        (3, 'Triatlón Costa', 'Valencia', 700, '2024-09-20');

-- Datos para la tabla FACILITIES
INSERT INTO facilities (id, nombre, tipo, capacidad, horario, fecha_apertura) VALUES
                                                                                  (1, 'Gimnasio Central', 'Gimnasio', 150, '06:00 - 22:00', '2015-09-01'),
                                                                                  (2, 'Piscina Olímpica', 'Piscina', 200, '08:00 - 20:00', '2010-06-15'),
                                                                                  (3, 'Pista Atlética', 'Pista', 100, '05:00 - 21:00', '2018-03-10');
