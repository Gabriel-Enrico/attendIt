# Relatório do Projeto – AttendIT Helpdesk de TI

**Disciplina:** Laboratório de Banco de Dados  
**Tema:** Helpdesk de TI – Gerenciamento de Chamados  

---

## 1. Descrição do Sistema

O AttendIT é um sistema de helpdesk voltado para o gerenciamento de chamados de suporte de TI. O sistema permite que usuários abram chamados descrevendo problemas técnicos, que técnicos sejam atribuídos para atendê-los, e que gestores acompanhem o ciclo de vida completo de cada chamado — desde a abertura até o fechamento.

---

## 2. Modelagem do Banco de Dados

### 2.1 Entidades e Relacionamentos

O banco é composto por 5 tabelas:

- **Usuario** — representa os solicitantes que abrem chamados. Possui perfil (`SOLICITANTE`, `TECNICO`, `ADMIN`), e-mail único e setor.
- **Tecnico** — representa os profissionais de TI que atendem os chamados. Possui especialidade, nível (`N1`, `N2`, `N3`) e disponibilidade.
- **Categoria** — classifica os chamados por tipo (ex: Hardware, Software, Rede) com SLA em horas.
- **Chamado** — entidade central do sistema. Registra título, descrição, status, prioridade e datas de abertura e fechamento. Referencia `Usuario` e `Categoria`.
- **Atendimento** — tabela associativa que resolve o relacionamento N:N entre `Chamado` e `Tecnico`. Registra datas de atribuição e resolução, além de observações.

### 2.2 Relacionamentos

- `Usuario` 1:N `Chamado` — um usuário pode abrir vários chamados
- `Categoria` 1:N `Chamado` — uma categoria agrupa vários chamados
- `Chamado` N:N `Tecnico` via `Atendimento` — um chamado pode ter vários técnicos; um técnico pode atender vários chamados

### 2.3 Restrições de Integridade

- Chaves primárias (`SERIAL PRIMARY KEY`) em todas as tabelas
- Chaves estrangeiras com `REFERENCES` garantindo integridade referencial
- `NOT NULL` nos campos obrigatórios
- `UNIQUE` no e-mail do usuário e no par `(id_chamado, id_tecnico)` em Atendimento
- `CHECK` constraints: perfil do usuário, nível do técnico, SLA > 0, status e prioridade do chamado

---

## 3. Implementação

### 3.1 Arquitetura

O projeto segue uma arquitetura em camadas:

- **model** — classes Java que representam as entidades do domínio
- **dao** — classes de acesso ao banco usando JDBC com `PreparedStatement`
- **service** — camada de regras de negócio e validações
- **controller** — intermediário entre a interface e os serviços
- **view** — interface gráfica construída com Java Swing

### 3.2 Tecnologias

- **Linguagem:** Java 21
- **Banco de dados:** PostgreSQL
- **Acesso a dados:** JDBC com driver `postgresql-42.7.3.jar`
- **Interface:** Java Swing

### 3.3 CRUD Implementado

| Entidade   | Criar | Listar | Editar | Excluir |
|------------|-------|--------|--------|---------|
| Chamado    | ✅    | ✅     | ✅     | ✅      |
| Atendimento| ✅    | ✅     | —      | ✅      |
| Técnico    | ✅    | ✅     | ✅     | ✅      |
| Usuário    | ✅    | ✅     | ✅     | ✅      |
| Categoria  | ✅    | ✅     | ✅     | ✅      |

---

## 4. Regras de Negócio

1. **Fechamento controlado** — um chamado só pode ser fechado se tiver ao menos um técnico atribuído via `Atendimento`
2. **Imutabilidade pós-fechamento** — chamados com status `Fechado` ou `Resolvido` não podem ser editados nem excluídos pela interface
3. **Exclusão restrita** — apenas chamados com status `Aberto` (sem atendimento vinculado) podem ser excluídos
4. **Fluxo de status** — o status do chamado avança automaticamente: `Aberto → Em_Atendimento` (ao atribuir técnico) `→ Resolvido` (ao registrar resolução) `→ Fechado` (ao fechar manualmente)
5. **Validação de e-mail** — o e-mail do usuário é validado antes do cadastro
6. **SLA obrigatório** — o SLA da categoria deve ser maior que zero
7. **Unicidade de atendimento** — um técnico não pode ser atribuído duas vezes ao mesmo chamado (constraint `UNIQUE`)

---

## 5. View SQL

Foi criada a view `vw_chamados_completo` que consolida em uma única consulta todas as informações relevantes de um chamado: dados do solicitante, categoria com SLA, técnico responsável, datas de atribuição e resolução, e observações. Essa view é utilizada na aba **Relatório** do sistema, que permite filtrar os chamados por status.

---

## 6. Organização dos Entregáveis

```
AttendIT/
├── db/schema.sql           — DDL completo com tabelas e view
├── docs/                   — DER, modelo lógico, diagrama de classes
├── src/                    — código-fonte Java
├── lib/                    — driver JDBC
└── README.md               — instruções de configuração e execução
```
