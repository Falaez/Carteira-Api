package br.com.alura.carteira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.carteira.modelo.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

}
