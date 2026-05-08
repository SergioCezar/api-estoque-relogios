package com.example.desafiorelogio.repository;

import com.example.desafiorelogio.entity.Relogio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;


//JpaSpecificationExecutor é uma propriedade que evita que tenha vários métodos padrões do JPA (findby)
public interface RelogioRepository extends JpaRepository <Relogio, UUID>, JpaSpecificationExecutor<Relogio> {
}
