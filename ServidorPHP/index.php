    <?php
    $serv = "localhost";
    $user = "root";
    $pass = "";
    $db = "distribuida";
    header("Access-Control-Allow-Origin: *");
    header('Content-Type: application/json; charset=utf-8');

    $conn = new mysqli($serv, $user, $pass, $db);
    if ($conn->connect_error) {
        http_response_code(500);
        echo json_encode(["error" => "DB connection failed", "detail" => $conn->connect_error]);
        exit;
    }
    $conn->set_charset("utf8mb4");

    $tipo = isset($_GET['tipo']) ? $_GET['tipo'] : null;

    function requireParam($key, $label = null)
    {
        if (!isset($_GET[$key]) || $_GET[$key] === "") {
            http_response_code(400);
            echo json_encode(["error" => "Falta el parametro '" . ($label ?? $key) . "'"]);
            exit;
        }
        return $_GET[$key];
    }

    function requireEmailParam()
    {
        if (isset($_GET['gmail']) && $_GET['gmail'] !== "") {
            return $_GET['gmail'];
        }
        if (isset($_GET['email']) && $_GET['email'] !== "") {
            return $_GET['email'];
        }
        http_response_code(400);
        echo json_encode(["error" => "Falta el parametro 'email'"]);
        exit;
    }

    function consultaPorId($id, $conn)
    {
        $sql = "select * from users where id= '" . $id . "'";
        $result = $conn->query($sql);
        if (!$result) {
            http_response_code(400);
            echo json_encode(["error" => $conn->error]);
            return;
        }

        echo '{"dato":[';
        $x = 0;
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $x++;
                echo ($x == 1 ? "" : ",");
                echo "{"; 

                echo '"id":"' . $row["id"] . '",';
                echo '"nombre":"' . $row["nombre"] . '",';
                echo '"apellidos":"' . $row["apellidos"] . '",';
                echo '"telefono":"' . $row["telefono"] . '",';
                echo '"email":"' . $row["email"] . '",';
                echo '"clave":"' . $row["clave"] . '"';
                echo "}";

            }
        }
        echo "]}";
    }

    function consulta($conn)
    {
        $sql = "select * from users";
        $result = $conn->query($sql);
        if (!$result) {
            http_response_code(400);
            echo json_encode(["error" => $conn->error]);
            return;
        }

        echo '{"dato":[';
        $x = 0;
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $x++;
                echo ($x == 1 ? "" : ",");
                echo "{";

                echo '"id":"' . $row["id"] . '",';
                echo '"nombre":"' . $row["nombre"] . '",';
                echo '"apellidos":"' . $row["apellidos"] . '",';
                echo '"telefono":"' . $row["telefono"] . '",';
                echo '"email":"' . $row["email"] . '",';
                echo '"clave":"' . $row["clave"] . '"';
                echo "}";

            }
        }
        echo "]}";
    }

    function inserta($nombre, $apellidos, $telefono, $email, $clave, $conn)
    {
        $sql = "insert into users (nombre,apellidos,telefono,email,clave)
        values('" . $nombre . "','" . $apellidos . "','" . $telefono . "','" . $email . "','" . $clave . "')";
        $result = $conn->query($sql);
        if (!$result) {
            http_response_code(400);
            echo json_encode(["error" => $conn->error]);
            return;
        }
        consulta($conn);
    }

    function eliminar($id, $conn)
    {
        $sql="delete from users where id = '" . $id . "'";
        $result = $conn->query($sql);
        if (!$result) {
            http_response_code(400);
            echo json_encode(["error" => $conn->error]);
            return;
        }
        if ($conn->affected_rows === 0) {
            http_response_code(404);
            echo json_encode(["error" => "No se encontró registro con id/clave dados"]);
            return;
        }
        consulta($conn);
    }

    function modificar($id, $nombre, $apellidos,$telefono, $email, $clave, $conn)
    {
        $sql = "update users set nombre= '" . $nombre. "', apellidos='" . $apellidos . "', telefono='" .$telefono . "', email='" . $email . "', clave='" . $clave . "' where id='" . $id . "'";
        $result = $conn->query($sql);
        if (!$result) {
            http_response_code(400);
            echo json_encode(["error" => $conn->error]);
            return;
        }
        if ($conn->affected_rows === 0) {
            http_response_code(404);
            echo json_encode(["error" => "No se actualizó registro"]);
            return;
        }
        consulta($conn);
    }

    if($tipo === null){
        http_response_code(400);
        echo json_encode(["error" => "Falta el parametro 'tipo'"]);
        exit;
    }

    if($tipo == 1){
        consulta($conn);
    }else if($tipo==2){
        //crear
        $nombre=requireParam('nombre');
        $apellidos=requireParam('apellidos');
        $telefono=requireParam('telefono');
        $email=requireEmailParam();
        $clave=requireParam('clave');

        inserta($nombre, $apellidos, $telefono, $email, $clave, $conn);
    }else if($tipo==3)
    {
        //modificar
        $clave=requireParam('clave');
        $nombre=requireParam('nombre');
        $apellidos=requireParam('apellidos');
        $telefono=requireParam('telefono');
        $email=requireEmailParam('email');
        $id=requireParam('id');
        
        modificar($id, $nombre, $apellidos, $telefono, $email, $clave, $conn);
    }else if($tipo==4){
        //eliminar
        $id=requireParam('id');
        eliminar($id,$conn);
    }else if($tipo==5){
        // Consultar todo
        $id=requireParam('id');
        consultaPorId($id, $conn);
    }else{
        http_response_code(400);
        echo json_encode(["error" => "Tipo no soportado"]);
    }

    //consultar todos los resgistros
    //http://localhost/index.php?tipo=1
    //crear
    //http://localhost/index.php?tipo=2&nombre=pepe&apellidos=luna&telefono=2222222222&gmail=pepe@gmail.com&clave=0
    //modificar
    //http://localhost/index.php?tipo=3&nombre=fran&apellidos=perez&telefono=2322364748&gmail=fran@gmail.com&clave=0&id=2
    //eliminar
    //http://localhost/index.php?tipo=4&clave=0&id=2
    //consultar por id
    //http://localhost/index.php?tipo=5&id=1

    ?>
