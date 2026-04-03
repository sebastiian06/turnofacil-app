# 💈 TurnoFacil - Barbería

Sistema de gestión de turnos para barbería desarrollado con **Node.js/Express** para el backend y **Android/Kotlin** para el frontend. Permite a los clientes reservar turnos para diferentes servicios de barbería, ver disponibilidad en tiempo real y gestionar sus reservas.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
  - [Backend](#backend)
  - [Base de Datos](#base-de-datos)
  - [Frontend Android](#frontend-android)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
  - [Levantar el Backend](#levantar-el-backend)
  - [Ejecutar la App Android](#ejecutar-la-app-android)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [API Endpoints](#api-endpoints)
- [Uso de la Aplicación](#uso-de-la-aplicación)
- [Solución de Problemas](#solución-de-problemas)
- [Capturas de Pantalla](#capturas-de-pantalla)
- [Contribución](#contribución)
- [Licencia](#licencia)

## ✨ Características

- ✅ Listado de servicios de barbería
- ✅ Detalle de servicios con descripción y duración
- ✅ Cálculo automático de cupos disponibles
- ✅ Selección de fecha para reserva
- ✅ Visualización de horarios disponibles en tiempo real
- ✅ Registro de reservas con nombre y documento
- ✅ Pantalla de confirmación con detalles de la reserva
- ✅ Validación para evitar dobles reservas
- ✅ Interfaz intuitiva y fácil de usar

## 🛠 Tecnologías Utilizadas

### Backend
- **Node.js** - Entorno de ejecución JavaScript
- **Express.js** - Framework web
- **PostgreSQL** - Base de datos relacional
- **pg** - Cliente PostgreSQL para Node.js
- **cors** - Middleware para habilitar CORS

### Frontend Android
- **Kotlin** - Lenguaje de programación
- **Retrofit2** - Cliente HTTP para consumir la API
- **Gson** - Convertidor JSON
- **AndroidX** - Librerías de soporte
- **Material Design** - Diseño de interfaz

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Node.js** (v14 o superior)
- **npm** (v6 o superior)
- **PostgreSQL** (v12 o superior)
- **Android Studio** (Última versión)
- **Git** (para clonar el repositorio)

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/turnofacil.git
cd turnofacil
```
## Ejecución del Proyecto

### 1. Levantar el Backend

- cd backend
- npm install
- npm start

### Respuesta esperada:
Servidor corriendo en puerto 3000
Base de datos lista
Servicios iniciales creados

### Ejecutar la App Android
#### Usando el Emulador de Android Studio
- Abrir Android Studio
- Abrir el proyecto (ubicado en la carpeta app-Android)
- Esperar a que se sincronicen las dependencias
- Crear un emulador (si no tienes uno):
- Tools → AVD Manager
- Create Virtual Device
- Seleccionar un dispositivo (ej: Pixel 4)
- Seleccionar una imagen del sistema (API 30 o superior)

#### Ejecutar la app:
- Botón ▶️ "Run" (o Shift + F10)

## Verificar que todo funciona
1. ✅ El backend muestra "Servidor corriendo en puerto 3000"

2. ✅ La base de datos PostgreSQL está corriendo

3. ✅ La app Android muestra la lista de servicios

4. ✅ Puedes ver detalles y reservar turnos

