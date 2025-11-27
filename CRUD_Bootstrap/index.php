<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agenda</title>
    <link rel="stylesheet" href="css/bootstrap.css">
</head>

<body>

    <div class="container">
        <div class="row">
            <div class="col-12 text-end">
                <button class="btn btn-primary btn-sm">
                    <img src="img/add.png" onclick="mostrarGuardar()" alt="">
                </button>
            </div>
        </div>
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div id="mitabla"></div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>

    <div class="modal" id="modalAdd">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Agregar</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="nom" class="form-label">Nombre:</label>
                        <input type="text" class="form-control" id="nom" placeholder="Nombre">
                    </div>
                    <div class="mb-3">
                        <label for="app" class="form-label">Apellido:</label>
                        <input type="text" class="form-control" id="app" placeholder="Apellido">
                    </div>
                    
                    <div class="mb-3">
                        <label for="tel" class="form-label">Telefono:</label>
                        <input type="text" class="form-control" id="tel" placeholder="Telefono">
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email:</label>
                        <input type="email" class="form-control" id="email" placeholder="correo@ejemplo.com">
                    </div>
                    <div class="mb-3">
                        <label for="clave" class="form-label">Clave:</label>
                        <input type="text" class="form-control" id="clave" placeholder="Clave de usuario">
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Salir</button>
                    <button type="button" class="btn btn-primary" onclick="guardar()">Guardar</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal" id="modalUpdate">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Modificar</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="nom2" class="form-label">Nombre:</label>
                        <input type="hidden" class="form-control" id="id2">
                        <input type="text" class="form-control" id="nom2" placeholder="Nombre">
                    </div>
                    <div class="mb-3">
                        <label for="app2" class="form-label">Apellido:</label>
                        <input type="text" class="form-control" id="app2" placeholder="Apellido">
                    </div>
                    <div class="mb-3">
                        <label for="clave2" class="form-label">Clave:</label>
                        <input type="text" class="form-control" id="clave2" placeholder="Clave de usuario">
                    </div>
                    <div class="mb-3">
                        <label for="tel2" class="form-label">Telefono:</label>
                        <input type="text" class="form-control" id="tel2" placeholder="Telefono">
                    </div>
                    <div class="mb-3">
                        <label for="email2" class="form-label">Email:</label>
                        <input type="email" class="form-control" id="email2" placeholder="correo@ejemplo.com">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Salir</button>
                    <button type="button" class="btn btn-primary" onclick="modificar()">Modificar</button>
                </div>
            </div>
        </div>
    </div>

    <script src="js/jquery-3.7.1.min.js"></script>
    <script src="js/bootstrap.js"></script>
    <script>
        const API_URL = "http://localhost:8000/index.php";
        const DEFAULT_CLAVE = "0";

        function callApi(params, onSuccess) {
            $.ajax({
                url: API_URL,
                method: "GET",
                dataType: "json",
                data: params,
                success: function (json) {
                    if (json && json.error) {
                        $("#mitabla").html("<div class='text-danger'>" + json.error + "</div>");
                        return;
                    }
                    onSuccess(json || {});
                },
                error: function (xhr, status, err) {
                    let message = "Error al consultar el servidor.";
                    if (xhr.responseText) {
                        try {
                            const resp = JSON.parse(xhr.responseText);
                            if (resp.error) {
                                message = resp.error;
                            }
                        } catch (_) {
                            // deja el mensaje por defecto
                        }
                    }
                    console.error("Error en la llamada", status, err, xhr.responseText);
                    $("#mitabla").html("<div class='text-danger'>" + message + "</div>");
                }
            });
        }

        (function () {
            mostrarTabla();
        })();

        function mostrarGuardar() {
            $("#clave").val(DEFAULT_CLAVE);
            $("#modalAdd").modal("show");
        }
        function modalModificar(id, nom, app, tel, email, clave) {
            $("#id2").val(id);
            $("#nom2").val(nom);
            $("#app2").val(app);
            $("#tel2").val(tel);
            $("#email2").val(email);
            $("#clave2").val(clave);
            $("#modalUpdate").modal("show");
        }

        function eliminar(id, clave) {
            callApi(
                { tipo: 4, id: id, clave: clave || DEFAULT_CLAVE, r: Date.now() },
                function () { mostrarTabla(); }
            );
        }
        function guardar() {
            var nom = $("#nom").val();
            var app = $("#app").val();
            var tel = $("#tel").val();
            var email = $("#email").val();
            var clave = $("#clave").val() || DEFAULT_CLAVE;
            callApi(
                {
                    tipo: 2,
                    nombre: nom,
                    apellidos: app,
                    telefono: tel,
                    email: email,
                    clave: clave,
                    r: Date.now()
                },
                function () {
                    $("#nom").val("");
                    $("#app").val("");
                    $("#tel").val("");
                    $("#email").val("");
                    $("#clave").val("");
                    mostrarTabla();
                }
            );
            $("#modalAdd").modal("hide");
        }

        function mostrarTabla() {
            $("#mitabla").html(
                '<div class="text-center py-4">'
                + '<div class="spinner-border text-primary" role="status" aria-label="Cargando"></div>'
                + '<div class="mt-2 text-muted">Cargando...</div>'
                + '</div>'
            );
            callApi({ tipo: 1 }, function (result) {
                var linea = "";
                var data = result.dato || [];
                for (let index = 0; index < data.length; index++) {
                    var usuario = data[index];
                    var id = usuario.id;
                    var nombre = usuario.nombre || "";
                    var apellidos = usuario.apellidos || "";
                    var clave = usuario.clave || "";
                    var telefono = usuario.telefono || "";
                    var email = usuario.email || "";
                    linea += "<tr>";
                    linea += "<td>" + id + "</td>";
                    linea += "<td>" + nombre + "</td>";
                    linea += "<td>" + apellidos + "</td>";
                    linea += "<td>" + clave + "</td>";
                    linea += "<td>" + telefono + "</td>";
                    linea += "<td>" + email + "</td>";
                    linea += '<td class="text-center"><button class="btn btn-success btn-sm"><img src="img/pen2.png" onclick="modalModificar(\'' + id + '\',\'' + nombre + '\',\'' + apellidos + '\',\'' + telefono + '\',\'' + email + '\',\'' + clave + '\')" alt=""></button></td>';
                    linea += '<td class="text-center"><button class="btn btn-danger btn-sm"><img src="img/delete2.png" onclick="eliminar(\'' + id + '\', \'' + clave + '\')"  alt=""></button></td>';
                    linea += "</tr>";
                }
                var cuerpo = linea || '<tr><td colspan="8" class="text-center text-muted">Sin datos para mostrar</td></tr>';
                var tablaweb = ''
                    + '<div class="table-responsive">'
                    + '<table class="table table-striped table-hover align-middle mb-0">'
                    + '<thead class="table-light">'
                    + '<tr>'
                    + '<th scope="col">#</th>'
                    + '<th scope="col">Nombre</th>'
                    + '<th scope="col">Apellido</th>'
                    + '<th scope="col">Clave</th>'
                    + '<th scope="col">Telefono</th>'
                    + '<th scope="col">Email</th>'
                    + '<th scope="col" class="text-center"><img src="img/pen.png" alt=""></th>'
                    + '<th scope="col" class="text-center"><img src="img/delete.png" alt=""></th>'
                    + '</tr>'
                    + '</thead>'
                    + '<tbody>'
                    + cuerpo
                    + '</tbody>'
                    + '</table>'
                    + '</div>';
                $("#mitabla").html(tablaweb);
            });
        }
        function modificar() {
            var id = $("#id2").val();
            var nom = $("#nom2").val();
            var app = $("#app2").val();
            var tel = $("#tel2").val();
            var email = $("#email2").val();
            var clave = $("#clave2").val();
            callApi(
                {
                    tipo: 3,
                    id: id,
                    nombre: nom,
                    apellidos: app,
                    telefono: tel,
                    email: email,
                    clave: clave,
                    r: Date.now()
                },
                function () {
                    $("#nom2").val("");
                    $("#app2").val("");
                    $("#tel2").val("");
                    $("#email2").val("");
                    $("#clave2").val("");
                    mostrarTabla();
                    $("#modalUpdate").modal("hide");
                }
            );
        }
    </script>

</body>

</html>
