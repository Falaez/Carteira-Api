create table usuarios(
	id bigint not null auto_increment,
	nome varchar(255) not null,
	login varchar(30) not null,
	senha varchar(10) not null,
	primary key(id)
);
