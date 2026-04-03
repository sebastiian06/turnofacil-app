const express = require("express")
const cors = require("cors")

const initDB = require("./initDB")

const serviciosRoutes = require("./routes/servicios")
const turnosRoutes = require("./routes/turnos")

const app = express()

app.use(cors())
app.use(express.json())

app.use("/api/servicios", serviciosRoutes)
app.use("/api/turnos", turnosRoutes)

initDB()

app.listen(3000, ()=>{
    console.log("Servidor corriendo en puerto 3000")
})