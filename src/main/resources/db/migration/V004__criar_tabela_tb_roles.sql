CREATE TABLE usuario (
   usuario_codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) ,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE tb_roles (
    role_codigo BIGINT PRIMARY KEY,
    role_nome VARCHAR(255)
);

CREATE TABLE usuario_role (
    usuario_codigo BIGINT NOT NULL,
    role_codigo BIGINT NOT NULL,
    PRIMARY KEY (usuario_codigo, role_codigo),
    FOREIGN KEY (usuario_codigo) REFERENCES usuario(usuario_codigo) ON DELETE CASCADE,
    FOREIGN KEY (role_codigo) REFERENCES tb_roles(role_codigo) ON DELETE CASCADE
);

-- Inserir roles na tabela tb_roles
INSERT INTO tb_roles (role_codigo, role_nome) VALUES (1, 'ADMIN');
INSERT INTO tb_roles (role_codigo, role_nome) VALUES (2, 'BASIC');

