<?php 
$host = '';
$db = '';
$user = '';
$pass = '';
$charset = '';
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