-- Desactiva restricciones temporales
SET FOREIGN_KEY_CHECKS=0;

-- TRAINERS
INSERT INTO trainers (id, nombre, especialidad, experiencia, fecha_ingreso, activo) VALUES
                                                                                        (1, 'Pedro Sánchez', 'Fuerza', 10, '2015-05-10', 1),
                                                                                        (2, 'Sofía López', 'Cardio', 8, '2018-03-15', 1),
                                                                                        (3, 'Diego Torres', 'Flexibilidad', 12, '2010-07-20', 1),
                                                                                        (4, 'Carmen Muñoz', 'Resistencia', 6, '2020-01-05', 0);

-- ATHLETES (con trainer_id)
INSERT INTO athletes (id, nombre, categoria, edad, altura, fecha_registro, trainer_id) VALUES
                                                                                           (1, 'Carlos López', 'Profesional', 28, 1.85, '2023-01-10', 1),
                                                                                           (2, 'María García', 'Amateur', 24, 1.70, '2023-02-20', 2),
                                                                                           (3, 'Juan Martínez', 'Profesional', 30, 1.78, '2023-03-05', 3),
                                                                                           (4, 'Ana Fernández', 'Amateur', 22, 1.65, '2023-04-15', 2),
                                                                                           (5, 'Luis Pérez', 'Semiprofesional', 27, 1.80, '2023-05-25', 1);

-- FACILITIES
INSERT INTO facilities (id, nombre, tipo, capacidad, horario, fecha_apertura) VALUES
                                                                                  (1, 'Gimnasio Central', 'Gimnasio', 150, '06:00 - 22:00', '2015-09-01'),
                                                                                  (2, 'Piscina Olímpica', 'Piscina', 200, '08:00 - 20:00', '2010-06-15'),
                                                                                  (3, 'Pista Atlética', 'Pista', 100, '05:00 - 21:00', '2018-03-10');

-- EVENTS (con facility_id, obligatorio)
INSERT INTO events (id, nombre, ubicacion, capacidad, fecha_evento, facility_id) VALUES
                                                                                     (1, 'Maratón de Primavera', 'Madrid', 500, '2024-04-10', 1),
                                                                                     (2, 'Carrera Nocturna', 'Barcelona', 300, '2024-06-15', 2),
                                                                                     (3, 'Triatlón Costa', 'Valencia', 700, '2024-09-20', 3);

-- TRAININGS (con trainer_id)
INSERT INTO trainings (id, tipo, nivel, duracion, fecha, completado, latitude, longitude, trainer_id) VALUES
                                                                                                          (1, 'Cardio', 'Intermedio', 45, '2023-06-10', 1, 40.712776, -74.005974, 1),
                                                                                                          (2, 'Fuerza', 'Avanzado', 60, '2023-06-12', 0, 34.052235, -118.243683, 1),
                                                                                                          (3, 'Flexibilidad', 'Básico', 30, '2023-06-15', 1, 51.507351, -0.127758, 2);

-- TRAINING ↔ ATHLETES (asociación N:N)
INSERT INTO training_athletes (training_id, athlete_id) VALUES
                                                            (1, 1), (1, 2),
                                                            (2, 3), (2, 5),
                                                            (3, 4);

-- Reactiva las restricciones
SET FOREIGN_KEY_CHECKS=1;
