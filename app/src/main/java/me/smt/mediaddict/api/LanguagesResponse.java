package me.smt.mediaddict.api;

import java.util.List;
import me.smt.mediaddict.model.Language;

/**
 * Método que se utiliza para deserializar la respuesta
 * de las peticiones REST relacionadas con los lenguajes
 * de las películas.
 */
public class LanguagesResponse {
    /**
     * Atributo que se encarga de almacenar un conjunto de lenguajes.
     */
    private List<Language> languages;

    /**
     * Método que se encarga de obtener la lista de lenguajes.
     * @return List<Language> lista de lenguajes.
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Método que se encarga de modificar la lista de lenguajes.
     * @param languages lista de lenguajes.
     */
    public void setGenres(List<Language> languages) {
        this.languages = languages;
    }

}