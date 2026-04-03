const express = require("express")
const router = express.Router()
const pool = require("../db")


// GET /api/servicios
router.get("/", async (req, res) => {

    try {

        const result = await pool.query(
            "SELECT * FROM servicios ORDER BY id"
        )

        res.json(result.rows)

    } catch (error) {

        console.error(error)
        res.status(500).json({ error: "Error servidor" })

    }

})


// GET /api/servicios/:id
router.get("/:id", async (req, res) => {

    const id = req.params.id

    try {

        const result = await pool.query(
            "SELECT * FROM servicios WHERE id=$1",
            [id]
        )

        if (result.rows.length === 0) {
            return res.status(404).json({ error: "Servicio no existe" })
        }

        res.json(result.rows[0])

    } catch (error) {

        console.error(error)
        res.status(500).json({ error: "Error servidor" })

    }

})


// GET /api/servicios/:id/horarios-disponibles?fecha=2026-04-03
router.get("/:id/horarios-disponibles", async (req, res) => {

    const id = req.params.id
    const fecha = req.query.fecha

    if (!fecha) {
        return res.status(400).json({ error: "Debe enviar la fecha" })
    }

    // Validar formato de fecha
    const fechaRegex = /^\d{4}-\d{2}-\d{2}$/
    if (!fechaRegex.test(fecha)) {
        return res.status(400).json({ error: "Formato de fecha inválido. Use YYYY-MM-DD" })
    }

    try {

        // obtener servicio
        const servicioResult = await pool.query(
            "SELECT * FROM servicios WHERE id=$1",
            [id]
        )

        if (servicioResult.rows.length === 0) {
            return res.status(404).json({ error: "Servicio no existe" })
        }

        const servicio = servicioResult.rows[0]

        const duracion = servicio.duracion_minutos
        const horaInicio = servicio.hora_inicio
        const horaFin = servicio.hora_fin

        let horarios = []

        // Crear objetos Date para calcular los horarios
        let inicio = new Date(`1970-01-01T${horaInicio}`)
        const fin = new Date(`1970-01-01T${horaFin}`)

        // Validar que la hora de inicio sea menor que la hora de fin
        if (inicio >= fin) {
            return res.status(400).json({ error: "Horario de inicio debe ser menor que horario de fin" })
        }

        // generar slots
        while (inicio < fin) {
            const horaString = inicio.toTimeString().substring(0, 5)
            horarios.push(horaString)
            inicio = new Date(inicio.getTime() + duracion * 60000)
        }

        // obtener turnos ocupados para esa fecha
        const turnosResult = await pool.query(
            "SELECT hora FROM turnos WHERE servicio_id=$1 AND fecha=$2",
            [id, fecha]
        )

        const ocupados = turnosResult.rows.map(t => t.hora.substring(0,5))

        // filtrar disponibles
        const disponibles = horarios.filter(h => !ocupados.includes(h))

        // También devolver información adicional
        res.json({
            fecha: fecha,
            horarios_disponibles: disponibles,
            total_horarios: horarios.length,
            horarios_ocupados: ocupados.length,
            horarios_libres: disponibles.length
        })

    } catch (error) {

        console.error(error)
        res.status(500).json({ error: "Error servidor" })

    }

})

module.exports = router