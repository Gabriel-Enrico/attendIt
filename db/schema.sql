CREATE TABLE Usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    setor VARCHAR(60),
    perfil VARCHAR(20) NOT NULL DEFAULT 'Solicitante',
    CONSTRAINT chk_usuario_perfil CHECK (perfil IN ('SOLICITANTE','TECNICO','ADMIN'))
);

CREATE TABLE Tecnico (
    id_tecnico SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(80) NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_tecnico_nivel CHECK (nivel IN ('N1', 'N2', 'N3'))
);

CREATE TABLE Categoria (
    id_categoria SERIAL PRIMARY KEY,
    descricao VARCHAR(80) NOT NULL,
    sla_horas INTEGER NOT NULL,
    CONSTRAINT chk_categoria_sla CHECK (sla_horas > 0)
);

CREATE TABLE Chamado (
    id_chamado SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL REFERENCES Usuario(id_usuario),
    id_categoria INTEGER NOT NULL REFERENCES Categoria(id_categoria),
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'Aberto',
    prioridade VARCHAR(20) NOT NULL,
    dt_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_fechamento TIMESTAMP,
    CONSTRAINT chk_chamado_status CHECK (status IN ('Aberto', 'Em_Atendimento', 'Resolvido', 'Fechado')),
    CONSTRAINT chk_chamado_prioridade CHECK (prioridade IN ('Baixa', 'Media', 'Alta', 'Critica'))
);

CREATE TABLE Atendimento (
    id_atendimento SERIAL PRIMARY KEY,
    id_chamado INTEGER NOT NULL REFERENCES Chamado(id_chamado),
    id_tecnico INTEGER NOT NULL REFERENCES Tecnico(id_tecnico),
    dt_atribuicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dt_resolucao TIMESTAMP,
    observacao TEXT,
    CONSTRAINT uq_atendimento UNIQUE (id_chamado, id_tecnico)
);

CREATE VIEW vw_chamados_completo AS
SELECT
    c.id_chamado,
    c.titulo,
    c.status,
    c.prioridade,
    c.dt_abertura,
    c.dt_fechamento,
    u.nome        AS solicitante,
    u.setor,
    cat.descricao AS categoria,
    cat.sla_horas,
    t.nome        AS tecnico_responsavel,
    t.nivel       AS nivel_tecnico,
    a.dt_atribuicao,
    a.dt_resolucao,
    a.observacao  AS observacao_resolucao
FROM Chamado c
     JOIN Usuario u        ON u.id_usuario    = c.id_usuario
     JOIN Categoria cat    ON cat.id_categoria = c.id_categoria
     LEFT JOIN Atendimento a ON a.id_chamado  = c.id_chamado
     LEFT JOIN Tecnico t   ON t.id_tecnico    = a.id_tecnico;
