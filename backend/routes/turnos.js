const express = require("express")
const router = express.Router()
const pool = require("../db")

// POST /api/turnos
router.post("/", async (req, res) => {

    const { nombre_cliente, documento, hora, fecha, servicio_id } = req.body

    // Validar campos requeridos
    if (!nombre_cliente || !documento || !hora || !fecha || !servicio_id) {
        return res.status(400).json({
            error: "Faltan campos requeridos",
            required: ["nombre_cliente", "documento", "hora", "fecha", "servicio_id"]
        })
    }

    // Validar formato de fecha
    const fechaRegex = /^\d{4}-\d{2}-\d{2}$/
    if (!fechaRegex.test(fecha)) {
        return res.status(400).json({
            error: "Formato de fecha inválido. Use YYYY-MM-DD"
        })
    }

    // Validar formato de hora
    const horaRegex = /^([0-1][0-9]|2[0-3]):[0-5][0-9]$/
    if (!horaRegex.test(hora)) {
        return res.status(400).json({
            error: "Formato de hora inválido. Use HH:MM"
        })
    }

    try {

        // verificar servicio
        const servicio = await pool.query(
            "SELECT * FROM servicios WHERE id=$1",
            [servicio_id]
        )

        if (servicio.rows.length === 0) {
            return res.status(404).json({
                error: "Servicio no existe"
            })
        }

        // Verificar si el horario está dentro del rango del servicio
        const servicioData = servicio.rows[0]
        const horaInicio = servicioData.hora_inicio
        const horaFin = servicioData.hora_fin
        
        if (hora < horaInicio || hora >= horaFin) {
            return res.status(400).json({
                error: `Horario fuera de rango. El servicio está disponible de ${horaInicio} a ${horaFin}`
            })
        }

        // verificar si el horario ya está ocupado
        const turnoExistente = await pool.query(
            `SELECT * FROM turnos 
             WHERE servicio_id=$1 AND fecha=$2 AND hora=$3`,
            [servicio_id, fecha, hora]
        )

        if (turnoExistente.rows.length > 0) {
            return res.status(400).json({
                error: "Este horario ya está reservado"
            })
        }

        // crear turno
        const turno = await pool.query(
            `INSERT INTO turnos
            (nombre_cliente, documento, hora, fecha, servicio_id)
            VALUES ($1,$2,$3,$4,$5)
            RETURNING *`,
            [nombre_cliente, documento, hora, fecha, servicio_id]
        )

        res.status(201).json({
            success: true,
            message: "Turno creado exitosamente",
            turno: turno.rows[0]
        })

    } catch (error) {

        console.error(error)

        res.status(500).json({
            error: "Error creando turno",
            details: error.message
        })

    }

})

// GET /api/turnos/verificar?servicio_id=1&fecha=2026-04-03&hora=10:00
router.get("/verificar", async (req, res) => {
    const { servicio_id, fecha, hora } = req.query

    if (!servicio_id || !fecha || !hora) {
        return res.status(400).json({
            error: "Faltan parámetros: servicio_id, fecha, hora"
        })
    }

    try {
        const turno = await pool.query(
            "SELECT * FROM turnos WHERE servicio_id=$1 AND fecha=$2 AND hora=$3",
            [servicio_id, fecha, hora]
        )

        res.json({
            disponible: turno.rows.length === 0,
            mensaje: turno.rows.length === 0 ? "Horario disponible" : "Horario ya reservado"
        })

    } catch (error) {
        console.error(error)
        res.status(500).json({ error: "Error verificando disponibilidad" })
    }
})

module.exports = router