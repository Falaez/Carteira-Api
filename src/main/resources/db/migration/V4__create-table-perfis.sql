create table perfis(
	id bigint not null auto_increment primary key,
	nome varchar(100) not null
);


insert into perfis values (1, 'ROLE_ADMIN');
insert into perfis values (2, 'ROLE_COMUN');

