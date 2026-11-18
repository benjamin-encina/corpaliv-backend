-- Una base de datos por microservicio (cada servicio es dueño de sus datos)
CREATE DATABASE IF NOT EXISTS corpaliv_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS corpaliv_oportunidades CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS corpaliv_aportes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON corpaliv_auth.* TO 'corpaliv'@'%';
GRANT ALL PRIVILEGES ON corpaliv_oportunidades.* TO 'corpaliv'@'%';
GRANT ALL PRIVILEGES ON corpaliv_aportes.* TO 'corpaliv'@'%';
FLUSH PRIVILEGES;
