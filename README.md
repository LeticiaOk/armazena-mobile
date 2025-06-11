# Código PHP para conexão com o banco de dados:

## login.php:
```PHP
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES => false,
];
try {
    $pdo = new PDO($dsn, $user, $pass, $options);
    $usuario = $_GET['usuario'] ?? '';
    $senha = $_GET['senha'] ?? '';
    // Query para verificar as credenciais
    $sql = "SELECT USUARIO_ID, USUARIO_NOME, USUARIO_EMAIL FROM USUARIO WHERE USUARIO_EMAIL = :usuario AND USUARIO_SENHA = :senha";

    $stmt = $pdo->prepare($sql);
    $stmt->execute(['usuario' => $usuario, 'senha' => $senha]);
    $usuarios = $stmt->fetchAll();
    header('Content-Type: application/json');
    echo json_encode($usuarios);
} catch (\PDOException $e) {
    echo "Erro de conexão: " . $e->getMessage();
    exit;
}
```
## usuario.php:
```php
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES => false,
];
try {
    $pdo = new PDO($dsn, $user, $pass, $options);
    // Query para verificar as credenciais
    $sql = "SELECT * FROM USUARIO";
    $stmt = $pdo->prepare($sql);
    $usuarios = $stmt->fetchAll();
    header('Content-Type: application/json');
    echo json_encode($usuarios);
} catch (\PDOException $e) {
    echo "Erro de conexão: " . $e->getMessage();
    exit;
}
```

## usuario_cadastro.php:
```php
<?php
header("Content-Type: application/json");

// Configurações do banco de dados
$host = "www.thyagoquintas.com.br:3306";
$db   = "engenharia_21";
$user = "engenharia_21";
$pass = "tamanduabandeira";
$charset = "utf8mb4";
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    // Conexão com o banco de dados
    $pdo = new PDO($dsn, $user, $pass, $options);

    // Verifica se os dados foram enviados via POST
    if ($_SERVER['REQUEST_METHOD'] === "POST") {
        // Obtém os dados do corpo da requisição
        $input = json_decode(file_get_contents('php://input'), true);

        $usuario_tipo_id = $input['usuario_tipo_id'] ?? null;
        $nome_usuario = $input['nome_usuario'] ?? '';
        $email_usuario = $input['email_usuario'] ?? '';
        $senha_usuario = $input['senha_usuario'] ?? '';
        $empresa_usuario = $input['empresa_usuario'] ?? '';

        // Validação básica dos dados
        if (empty($nome_usuario) || empty($email_usuario) || empty($senha_usuario) || empty($empresa_usuario)) {
            echo json_encode(['error' => 'Dados incompletos.']);
            exit;
        }

        // Validação do e-mail
        if (!filter_var($email_usuario, FILTER_VALIDATE_EMAIL)) {
            echo json_encode(['error' => 'E-mail inválido.']);
            exit;
        }

        // Criptografa a senha antes de armazenar
        // $senha_criptografada = password_hash($senha_usuario, PASSWORD_BCRYPT);

        // Prepara e executa a consulta
       $sql = "INSERT INTO USUARIO (USUARIO_NOME, USUARIO_EMAIL, USUARIO_SENHA, USUARIO_EMPRESA, USUARIO_TIPO_ID) 
        VALUES (:nome_usuario, :email_usuario, :senha_usuario, :empresa_usuario, :usuario_tipo_id)";
$stmt = $pdo->prepare($sql);
$stmt->execute([
    'nome_usuario' => $nome_usuario,
    'email_usuario' => $email_usuario,
    'senha_usuario' => $senha_usuario,
    'empresa_usuario' => $empresa_usuario,
    'usuario_tipo_id' => $usuario_tipo_id
]);

        // Retorna uma resposta de sucesso
        echo json_encode(['success' => true, 'message' => 'Usuário cadastrado com sucesso.']);
    } else {
        echo json_encode(['error' => 'Método não permitido. Use POST.']);
    }
} catch (\PDOException $e) {
    // Log do erro (não exibir diretamente ao usuário em produção)
    error_log("Erro de conexão: " . $e->getMessage());
    echo json_encode(['error' => 'Erro interno no servidor.']);
    exit;
}
```

## produto.php:
```php
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
    $sql = "SELECT * FROM PRODUTO";
    $stmt = $pdo->query($sql);

    $usuarios = $stmt->fetchAll();
    header('Content-Type: application/json');
    echo json_encode($usuarios);
} catch (\PDOException $e) {
    echo "Erro de conexÃ£o: " . $e->getMessage();
    exit;
}
```

## incluir_produto.php:
```php
<?php 
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    header('Content-Type: application/json');
    $pdo = new PDO($dsn, $user, $pass, $options);

    if (isset($_POST['PRODUTO_NOME'], $_POST['PRODUTO_DESC'], $_POST['PRODUTO_PRECO'], $_POST['PRODUTO_IMAGEM_URL'], $_POST['CATEGORIA_ID'], $_POST['PRODUTO_QTD']
)) {
        $produtoNome = $_POST['PRODUTO_NOME'];
        $produtoDesc = $_POST['PRODUTO_DESC'];
        $produtoPreco = $_POST['PRODUTO_PRECO'];
        $produtoImagem = $_POST['PRODUTO_IMAGEM_URL'];
        $categoria_id = $_POST['CATEGORIA_ID'];
        $produto_qtd = $_POST['PRODUTO_QTD'];

       

        $sql = "INSERT INTO PRODUTO (PRODUTO_NOME, PRODUTO_DESC, PRODUTO_PRECO, PRODUTO_IMAGEM_URL, CATEGORIA_ID, PRODUTO_QTD )
                VALUES (:produtoNome, :produtoDesc, :produtoPreco, :produtoImagem, :categoria_id, :produto_qtd)";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            'produtoNome' => $produtoNome,
            'produtoDesc' => $produtoDesc,
            'produtoPreco' => $produtoPreco,
            'produtoImagem' => $produtoImagem,
            'categoria_id' => $categoria_id,
            'produto_qtd' => $produto_qtd
            
        ]);

        echo json_encode(['status' => 'Produto incluído com sucesso']);
    } else {
        echo json_encode(['error' => 'Dados incompletos']);
    }

} catch (PDOException $e) {
    echo json_encode(['error' => 'Erro de conexão: ' . $e->getMessage()]);
    exit;
}
?>
```

## editar_produto.php:
```php
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);

    if (isset($_POST['PRODUTO_ID'], $_POST['PRODUTO_NOME'], $_POST['PRODUTO_DESC'], $_POST['PRODUTO_PRECO'], $_POST['PRODUTO_IMAGEM_URL'], $_POST['CATEGORIA_ID'], $_POST['PRODUTO_QTD'])) {
        $produtoId = $_POST['PRODUTO_ID'];
        $produtoNome = $_POST['PRODUTO_NOME'];
        $produtoDesc = $_POST['PRODUTO_DESC'];
        $produtoPreco = $_POST['PRODUTO_PRECO'];
        $produtoImagem = $_POST['PRODUTO_IMAGEM_URL'];
        $categoria_id = $_POST['CATEGORIA_ID'];
        $produto_qtd = $_POST['PRODUTO_QTD'];

        

        // Atualizar o produto no banco de dados
        $sql = "UPDATE PRODUTO SET PRODUTO_NOME = :produtoNome, PRODUTO_DESC = :produtoDesc, PRODUTO_PRECO = :produtoPreco, PRODUTO_IMAGEM_URL = :produtoImagem, CATEGORIA_ID = :categoria_id, PRODUTO_QTD = :produto_qtd
                WHERE PRODUTO_ID = :produtoId";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            'produtoNome' => $produtoNome,
            'produtoDesc' => $produtoDesc,
            'produtoPreco' => $produtoPreco,
            'produtoImagem' => $produtoImagem,
            'produtoId' => $produtoId,
            'categoria_id' => $categoria_id,
            'produto_qtd' => $produto_qtd

            

        ]);

        echo json_encode(['status' => 'Produto atualizado com sucesso']);
    } else {
        echo json_encode(['error' => 'Dados incompletos']);
    }

} catch (PDOException $e) {
    echo "Erro de conexão: " . $e->getMessage();
    exit;
}
?>
```

## deletar_produto.php:
```php
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';
$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);

    if (isset($_POST['PRODUTO_ID'])) {
        $produtoId = $_POST['PRODUTO_ID'];

        // Deletar o produto
        $sql = "DELETE FROM PRODUTO WHERE PRODUTO_ID = :produtoId";
        $stmt = $pdo->prepare($sql);
        $stmt->execute(['produtoId' => $produtoId]);

        echo json_encode(['status' => 'Produto deletado com sucesso']);
    } else {
        echo json_encode(['error' => 'ID do produto não informado']);
    }

} catch (PDOException $e) {
    echo "Erro de conexão: " . $e->getMessage();
    exit;
}
?>
```

## categoria.php:
```php
<?php
$host = 'www.thyagoquintas.com.br:3306';
$db = 'engenharia_21';
$user = 'engenharia_21';
$pass = 'tamanduabandeira';
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
    $pdo = new PDO($dsn, $user, $pass, $options);
    $sql = "SELECT * FROM CATEGORIA";
    $stmt = $pdo->query($sql);

    $usuarios = $stmt->fetchAll();
    header('Content-Type: application/json');
    echo json_encode($usuarios);
} catch (\PDOException $e) {
    echo "Erro de conexÃ£o: " . $e->getMessage();
    exit;
}

```
