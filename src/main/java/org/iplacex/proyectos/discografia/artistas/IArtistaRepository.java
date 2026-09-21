package org.iplacex.proyectos.discografia.artistas;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IArtistaRepository extends MongoRepository<Artista, String> {

}
