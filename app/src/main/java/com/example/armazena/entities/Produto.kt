import androidx.xr.scenecore.Entity

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = false)
    val usuario_id: Int,
    val usuario_nome: String,
    val usuario_email: String,
    val usuario_senha: String,
    val usuario_empresa: String