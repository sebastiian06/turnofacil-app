const { Client } = require("pg")

async function initDB(){

    // conexión a BD por defecto
    const client = new Client({
        user: "postgres",
        host: "localhost",
        database: "postgres",
        password: "12345",
        port: 5432
    })

    await client.connect()

    // crear base de datos si no existe
    const dbCheck = await client.query(
        "SELECT 1 FROM pg_database WHERE datname='turnofacil'"
    )

    if(dbCheck.rowCount === 0){
        await client.query("CREATE DATABASE turnofacil")
        console.log("Base de datos creada")
    }

    await client.end()

    // ahora conectar a turnofacil
    const { Pool } = require("pg")

    const pool = new Pool({
        user: "postgres",
        host: "localhost",
        database: "turnofacil",
        password: "12345",
        port: 5432
    })

    // crear tablas
    await pool.query(`
        CREATE TABLE IF NOT EXISTS servicios (
            id SERIAL PRIMARY KEY,
            nombre VARCHAR(100),
            descripcion TEXT,
            duracion_minutos INT,
            hora_inicio TIME,
            hora_fin TIME
        );
    `)

    await pool.query(`
        CREATE TABLE IF NOT EXISTS turnos (
            id SERIAL PRIMARY KEY,
            nombre_cliente VARCHAR(100),
            documento VARCHAR(20),
            fecha DATE,
            hora TIME,
            servicio_id INT REFERENCES servicios(id)
        );
    `)

    // insertar servicios iniciales
    const servicios = await pool.query("SELECT * FROM servicios")

    if(servicios.rowCount === 0){

        await pool.query(`
            INSERT INTO servicios
            (nombre, descripcion, duracion_minutos, hora_inicio, hora_fin)
            VALUES
            ('Corte de cabello', 'Corte profesional', 30, '08:00', '20:00'),
            ('Corte de cabello + Barba', 'Combo', 60, '08:00', '20:00'),
            ('Barba', 'Perfilado de barba', 30, '08:00', '20:00'),
            ('Perfilado de cejas', 'Perfilado Estetico', 15, '08:00', '20:00'),
            ('Lavado de cabello', 'Lavado y acondicionamiento', 30, '08:00', '20:00'),
            ('Peinado', 'Peinado para eventos', 45, '08:00', '20:00')
        `)  

        console.log("Servicios iniciales creados")
    }

    console.log("Base de datos lista")

}

module.exports = initDB