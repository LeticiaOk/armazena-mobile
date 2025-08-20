<?php
$host = '';
$db = '';
$user = '';
$pass = '';
$charset = '';
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