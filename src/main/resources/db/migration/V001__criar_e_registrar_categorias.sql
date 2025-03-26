create table categoria(
	codigo BigINT(20) PRIMARY KEY AUTO_INCREMENT,
	NOME VARCHAR(50) NOT NULL
	
)Engine=innodb default charset=utf8;

INSERT INTO categoria (nome) values("Lazer");
INSERT INTO categoria (nome) values("Alimentação");
INSERT INTO categoria (nome) values("Supermercado");
INSERT INTO categoria (nome) values("Farmacia");
INSERT INTO categoria (nome) values("Outros");