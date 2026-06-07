# AttendIT – Helpdesk de TI

Sistema de gerenciamento de chamados de TI desenvolvido em Java com JDBC e interface gráfica Swing.

## Pré-requisitos

- Java 21 ou superior
- PostgreSQL 14 ou superior
- Driver JDBC PostgreSQL (já incluído em `lib/postgresql-42.7.3.jar`)

---

## Configuração do Banco de Dados

### Linux / macOS

1. Crie o banco de dados:
```bash
sudo -u postgres psql -c "CREATE DATABASE \"attendIt\";"
```

2. Execute o script DDL para criar as tabelas e a view:
```bash
sudo -u postgres psql -d attendIt -f db/schema.sql
```

### Windows

1. Abra o **SQL Shell (psql)** instalado junto com o PostgreSQL e faça login com o usuário `postgres`. Então execute:
```sql
CREATE DATABASE "attendIt";
```

2. Ainda no SQL Shell, conecte-se ao banco e execute o script DDL:
```sql
\c attendIt
\i 'C:/caminho/para/o/projeto/AttendIT/db/schema.sql'
```
> **Atenção:** use barras `/` (não `\`) no caminho dentro do psql, mesmo no Windows.

Alternativamente, via **Prompt de Comando** (cmd) ou **PowerShell**:
```powershell
psql -U postgres -c "CREATE DATABASE \"attendIt\";"
psql -U postgres -d attendIt -f "C:\caminho\para\o\projeto\AttendIT\db\schema.sql"
```
> O executável `psql` precisa estar no PATH. Por padrão fica em `C:\Program Files\PostgreSQL\<versão>\bin`.

---

### Credenciais de Conexão (todos os sistemas)

Configure as credenciais em `src/attendIt/dao/ConnectionFactory.java`:
```java
private static final String URL  = "jdbc:postgresql://localhost:5432/attendIt";
private static final String USER = "postgres";
private static final String PASS = "sua_senha";
```

---

## Compilação

### Linux / macOS

Na raiz do projeto, execute:
```bash
find src -name "*.java" ! -name "Main.java" | xargs javac -cp lib/postgresql-42.7.3.jar -d out
```

### Windows

No **Prompt de Comando (cmd)**, na raiz do projeto:
```cmd
for /r src %f in (*.java) do @if not "%~nxf"=="Main.java" echo %f >> fontes.txt
javac -cp lib\postgresql-42.7.3.jar -d out @fontes.txt
del fontes.txt
```

Ou, se preferir usar o **PowerShell**:
```powershell
$fontes = Get-ChildItem -Path src -Recurse -Filter "*.java" |
          Where-Object { $_.Name -ne "Main.java" } |
          Select-Object -ExpandProperty FullName

javac -cp lib\postgresql-42.7.3.jar -d out $fontes
```

> Certifique-se de que a pasta `out` existe antes de compilar:
> ```cmd
> mkdir out
> ```

---

## Execução

### Linux / macOS

```bash
java -cp out:lib/postgresql-42.7.3.jar attendIt.view.MainFrame
```

### Windows

```cmd
java -cp out;lib\postgresql-42.7.3.jar attendIt.view.MainFrame
```

> **Diferença importante:** no Windows o separador do classpath é `;` (ponto e vírgula), não `:`.

---

## Estrutura do Projeto

```
AttendIT/
├── db/
│   └── schema.sql              # DDL completo (tabelas + view)
├── docs/
│   ├── DER.png                 # Diagrama Entidade-Relacionamento
│   ├── modelo_logico.png       # Modelo lógico relacional
│   └── diagrama_classes.png    # Diagrama de classes
├── lib/
│   └── postgresql-42.7.3.jar  # Driver JDBC PostgreSQL
├── src/
│   └── attendIt/
│       ├── model/              # Entidades do domínio
│       ├── dao/                # Acesso ao banco de dados (JDBC)
│       ├── service/            # Regras de negócio
│       ├── controller/         # Intermediário entre view e service
│       └── view/               # Interface gráfica (Swing)
└── out/                        # Classes compiladas
```

---

## Funcionalidades

- **Chamados** — abrir, listar, editar, fechar e excluir chamados
- **Atendimento** — atribuir técnico a um chamado e registrar resolução
- **Técnicos** — cadastro completo com especialidade, nível e disponibilidade
- **Usuários** — cadastro de solicitantes com validação de e-mail
- **Categorias** — categorias de chamado com SLA em horas
- **Relatório** — visão consolidada de todos os chamados com filtro por status

---

## Regras de Negócio

- Chamado só pode ser fechado se tiver um técnico atribuído
- Chamados fechados ou resolvidos não podem ser editados ou excluídos
- Apenas chamados com status `Aberto` podem ser excluídos
- E-mail do usuário é validado antes do cadastro
- SLA da categoria deve ser maior que zero
- Um técnico só pode ser atribuído uma vez ao mesmo chamado