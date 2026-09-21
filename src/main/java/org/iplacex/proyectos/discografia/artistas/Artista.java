package org.iplacex.proyectos.discografia.artistas;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "artistas")
public class Artista {

    @Id
    public String _id;

    public String nombre;
    public List<String> estilos;
    public int anioFundacion;
    public boolean estaActivo;

    public Artista() {
    }

    public Artista(String _id, String nombre, List<String> estilos, int anioFundacion, boolean estaActivo) {
        this._id = _id;
        this.nombre = nombre;
        this.estilos = estilos;
        this.anioFundacion = anioFundacion;
        this.estaActivo = estaActivo;
    }

}
