# APIs REST - SOA

En este repo vienen las 3 APIs, cada una en su carpeta y se corren por separado:

| Carpeta | API | Puerto | Base de datos |
|---|---|---|---|
| `libreria/` | Prestamo de libros de una biblioteca | 3505 | `libreria` |
| `apartamentos/` | Renta de departamentos y habitaciones | 10101 | `apartamentos_db` |
| `alquilaya/` | Renta de vehiculos (API + vistas con Thymeleaf) | 8282 | `alquilaya_db` |

**El frontend solicitado es el de AlquilaYa**

## Descripcion

**Libreria:** CRUD de libros, usuarios y prestamos. Cuando se presta un libro se le resta un ejemplar, y si ya no hay ejemplares no deja hacer el prestamo. Cuando el prestamo pasa a "devuelto" o se borra, el ejemplar se regresa.

**Apartamentos:** CRUD de clientes, propiedades, imagenes, unidades rentables, disponibilidades, reservaciones, pagos y resenas. Una unidad no se puede reservar si ya tiene una reservacion CONFIRMADA. No se puede borrar algo que tenga registros relacionados (por ejemplo un cliente con reservaciones).

**AlquilaYa:** CRUD de oficinas, vehiculos, clientes y alquileres. Cada vehiculo pertenece a una oficina. Al rentar un vehiculo pasa a ALQUILADO y ya no se puede volver a rentar hasta que se devuelve (al borrar el alquiler vuelve a DISPONIBLE). Ademas de la API tiene paginas web en `http://localhost:8282/` para hacer todo desde el navegador.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web, Data JPA)
- MySQL 8
- Maven (viene el wrapper `mvnw`, no hace falta instalarlo)
- Thymeleaf y Lombok (solo en AlquilaYa)

## Instalacion y ejecucion

1. Tener instalado Java 21 y MySQL corriendo en el puerto 3307.

2. Crear un archivo `.env` en la carpeta de la API con:

```
DB_USER=root
DB_PASSWORD=root123
```

3. Entrar a la carpeta de la API y correrla:

```bash
cd libreria
./mvnw spring-boot:run
```

Las bases de datos y las tablas se crean solas al arrancar cada API.

## Variables de entorno

Cada API lee el usuario y la contrasena de MySQL de su archivo `.env` (no se sube a git). El resto de la configuracion esta en `src/main/resources/application.properties`.

## Endpoints

### Libreria (`http://localhost:3505`)

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/libros` | Lista los libros |
| GET | `/libros/{isbn}` | Busca un libro |
| GET | `/libros/autor?autor=` | Busca por autor |
| GET | `/libros/titulo?titulo=` | Busca por titulo |
| POST | `/libros` | Crea un libro |
| PUT | `/libros/{isbn}` | Edita un libro |
| DELETE | `/libros/{isbn}` | Borra un libro |
| GET | `/usuarios` | Lista los usuarios |
| GET | `/usuarios/{id}` | Busca un usuario |
| GET | `/usuarios/correo?correo=` | Busca por correo |
| GET | `/usuarios/nombre?nombre=` | Busca por nombre |
| POST | `/usuarios` | Crea un usuario |
| PUT | `/usuarios/{id}` | Edita un usuario |
| DELETE | `/usuarios/{id}` | Borra un usuario |
| GET | `/prestamos` | Lista los prestamos |
| GET | `/prestamos/{id}` | Busca un prestamo |
| GET | `/prestamos/usuario?idUsuario=` | Prestamos de un usuario |
| GET | `/prestamos/libro?isbn=` | Prestamos de un libro |
| POST | `/prestamos` | Crea un prestamo (resta un ejemplar) |
| PUT | `/prestamos/{id}` | Edita un prestamo (si pasa a devuelto suma el ejemplar) |
| DELETE | `/prestamos/{id}` | Borra un prestamo |

### Apartamentos (`http://localhost:10101/api`)

Todas tienen el mismo CRUD:

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/{recurso}` | Lista todos |
| GET | `/{recurso}/{id}` | Busca uno |
| POST | `/{recurso}` | Crea |
| PUT | `/{recurso}/{id}` | Edita |
| DELETE | `/{recurso}/{id}` | Borra |

Recursos: `clientes`, `propiedades`, `propiedad-imagenes`, `unidades-rentables`, `disponibilidades`, `reservaciones`, `pagos`, `resenas`.

Extra: `GET /propiedades/tipo/{tipo}` busca propiedades por tipo (por ejemplo APARTAMENTO).

### AlquilaYa (`http://localhost:8282/api`)

Mismo CRUD que arriba para: `oficinas`, `vehiculos`, `clientes`, `alquileres`.

Extras:

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/vehiculos/estado/{estado}` | Vehiculos DISPONIBLE o ALQUILADO |
| GET | `/clientes/email/{email}` | Busca cliente por email |
| GET | `/clientes/licencia/{licencia}` | Busca cliente por licencia |

## Ejemplos

### Libreria - crear libro

`POST http://localhost:3505/libros`

```json
{
  "titulo": "cien años de soledad",
  "autor": "Gabriel garcia marquez",
  "anioPublicacion": "1967",
  "numeroEjemplares": 1
}
```

Respuesta `201`:

```json
{"anioPublicacion":"1967","autor":"Gabriel garcia marquez","isbn":1,"numeroEjemplares":1,"titulo":"cien años de soledad"}
```

### Libreria - prestamo sin ejemplares

`POST http://localhost:3505/prestamos`

```json
{
  "isbn": 1,
  "idUsuario": 1,
  "fechaPrestamo": "2026-10-12",
  "fechaDevolucion": "2026-10-19",
  "estado": "activo"
}
```

Si el libro ya no tiene ejemplares, respuesta `400`:

```json
{"status":"400 BAD_REQUEST","mensaje":"No hay ejemplares disponibles de este libro"}
```

### Libreria - no existe

`GET http://localhost:3505/libros/99`

Respuesta `404`:

```json
{"status":"404 NOT_FOUND","mensaje":"Este recurso no ha sido encontrado"}
```

### Apartamentos - crear cliente

`POST http://localhost:10101/api/clientes`

```json
{
  "nombres": "gibran",
  "apellidos": "Razo Toledo",
  "email": "gibran.razo@ucc.mx",
  "telefono": "228-555-0192",
  "tipo": "HUESPED"
}
```

Respuesta `201`:

```json
{"apellidos":"Razo Toledo","email":"gibran.razo@ucc.mx","id":2,"nombres":"gibran","telefono":"228-555-0192","tipo":"HUESPED"}
```

### Apartamentos - reservar una unidad

`POST http://localhost:10101/api/reservaciones`

```json
{
  "unidad": { "idUnidad": 1 },
  "cliente": { "id": 2 },
  "fechaEntrada": "2026-12-01",
  "fechaSalida": "2026-12-03",
  "importeTotal": 960
}
```

Si la unidad ya tiene una reservacion CONFIRMADA, respuesta `400`:

```json
{"mensaje":"La unidad ya tiene una reservacion confirmada","status":"400 BAD_REQUEST"}
```

### AlquilaYa - crear vehiculo

`POST http://localhost:8282/api/vehiculos`

```json
{
  "marca": "Nissan",
  "modelo": "Versa",
  "anio": 2019,
  "matricula": "YKP-482-B",
  "tarifaDiaria": 650,
  "oficina": { "idOficina": 1 }
}
```

Respuesta `201`:

```json
{
  "datos": {"anio":2019,"estado":"DISPONIBLE","idVehiculo":1,"marca":"Nissan","matricula":"YKP-482-B","modelo":"Versa","oficina":{"ciudad":"xalapa","direccion":"av 20 de noviembre #118","idOficina":1,"nombre":"sucursal centro","region":"centro"},"tarifaDiaria":650},
  "status": "201 CREATED",
  "mensaje": "El recurso ha sido creado correctamente"
}
```

### AlquilaYa - rentar un vehiculo

`POST http://localhost:8282/api/alquileres`

```json
{
  "vehiculo": { "idVehiculo": 1 },
  "cliente": { "idCliente": 1 },
  "fechaAlquiler": "2026-10-14",
  "fechaDevolucion": "2026-10-17",
  "precioTotal": 1950
}
```

Respuesta `201` (el vehiculo queda en ALQUILADO). Si se intenta rentar otra vez el mismo vehiculo, respuesta `400`:

```json
{"status":"400 BAD_REQUEST","mensaje":"El vehiculo ya esta alquilado"}
```




### Notas:
1. El README fue generado con IA
2. Algunos datos de prueba fueron hechos con IA
3. Se apoyo con uso de IA en partes que no se entendia
4. Se uso docker como base de datos con mySQL
5. Se uso bruno en lugar de postman por tema de Sistema Operativo y es el que frecuento personalmente
