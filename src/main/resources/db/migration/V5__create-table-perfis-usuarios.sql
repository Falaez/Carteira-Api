create table perfis_usuarios(
	usuario_id bigint not null,
	perfil_id bigint not null,
	
	primary key(usuario_id, perfil_id),
	foreign key (usuario_id) references usuarios(id),
	foreign key (perfil_id) references perfis(id)
);