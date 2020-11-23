package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Clase que actúa como modelo de datos dando forma a una
 * película obtenida a través de la API.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class Movie {
    /**
     * Atributo que indica el ID de la película.
     */
    @SerializedName("id")
    @Expose
    private int id;

    /**
     * Atributo que indica el título de la película.
     */
    @SerializedName("title")
    @Expose
    private String title;

    /**
     * Atributo que indica la ruta del póster de la película.
     */
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    /**
     * Atributo que indica la fecha de salida de la película.
     */
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    /**
     * Atributo que indica la media de votos de la película.
     */
    @SerializedName("vote_average")
    @Expose
    private float rating;

    /**
     * Atributo que indica el total de votos sobre la película.
     */
    @SerializedName("vote_count")
    @Expose
    private float voteCount;

    /**
     * Atributo que indica los IDs de géneros asociados a la película.
     */
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;

    /**
     * Atributo que indica la sinopsis de la película.
     */
    @SerializedName("overview")
    @Expose
    private String overview;

    /**
     * Atributo que indica las ganancias de la película.
     */
    @SerializedName("revenue")
    @Expose
    private double revenue;

    /**
     * Atributo que indica el path al backdrop de la película.
     */
    @SerializedName("backdrop_path")
    @Expose
    private String backdrop;

    /**
     * Atributo que el lenguaje original de la película.
     */
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    /**
     * Atributo que indica el título de la película en idioma original.
     */
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    /**
     * Atributo que indica la popularidad de la película.
     */
    @SerializedName("popularity")
    @Expose
    private double popularity;

    /**
     * Atributo que indica el estado de la película. Ej: Released.
     */
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * Atributo que indica el tagline o eslogan de la película.
     */
    @SerializedName("tagline")
    @Expose
    private String tagline;

    /**
     * Atributo que indica la página oficial de la película.
     */
    @SerializedName("homepage")
    @Expose
    private String homepage;

    /**
     * Atributo que indica el presupuesto de la película.
     */
    @SerializedName("budget")
    @Expose
    private double budget;

    /**
     * Atributo que indica la duración de la película.
     */
    @SerializedName("runtime")
    @Expose
    private double runtime;

    /**
     * Atributo que indica el enlace a la web de IMDB.
     */
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    /**
     * Atributo que indica las compañías de producción
     * asociadas a la película.
     */
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> companies;

    /**
     * Atributo que indica la lista de géneros asociada
     * a la película.
     */
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    /**
     * Atributo que indica la lista de países de producción
     * de la película.
     */
    @SerializedName("production_countries")
    @Expose
    private List<ProductionCountry> countries;

    /**
     * Atributo que indica los idiomas hablados.
     */
    @SerializedName("spoken_languages")
    @Expose
    private List<Language> languages;

    /**
     * Atributo para almacenar los IDs en tipo String.
     */
    private String GenreIdString;

    /**
     * Constructor de la clase.
     */
    public Movie() {
    }

    /**
     * Método que devuelve el ID de la película.
     * @return int id de la película.
     */
    public int getId() {
        return id;
    }

    /**
     * Método que modifica el ID de la película.
     * @param id id de la película.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método que devuelve el título de la película.
     * @return String título de la película.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Método que modifica el título de la película.
     * @param title título de la película.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Método que devuelve la ruta al posterpath de la película
     * @return String posterpath de la película.
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Método que modifica el posterpath de la película.
     * @param posterPath posterpath de la película.
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Método que devuelve la fecha de lanzamiento de la película.
     * @return String fecha de lanzamiento.
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Método que modifica la fecha de estreno de la película.
     * @param releaseDate fecha de estreno.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Método que devuelve la puntuación de la película.
     * @return float puntuación de la película.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Método que modifica la puntuació nde la película.
     * @param rating puntuación.
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Método que devuelve el total de votos de la película.
     * @return float total de votos de la película.
     */
    public float getVoteCount() {
        return voteCount;
    }

    /**
     * Método que modifica el total de votos de la película.
     * @param voteCount total de votos.
     */
    public void setVoteCount(float voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * Método que devuelve un listado con los IDs de
     * los géneros asociados a la película.
     * @return List<Integer> listado de ids.
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     * Método que modifica el listado de IDs de los géneros
     * de la película.
     * @param genreIds
     */
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    /**
     * Método que devuelve la sinopsis de la película.
     * @return String sinopsis de la película.
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Método que modifica la sinopsis de la película.
     * @param overview sinopsis.
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * Método que devuelve el enlace al backdrop de la película.
     * @return String backdrop de la película.
     */
    public String getBackdrop() {
        return backdrop;
    }

    /**
     * Método que modifica el backdrop de la película.
     * @param backdrop backdrop.
     */
    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    /**
     * Método que devuelve un listado con los géneros
     * asociados a la película.
     * @return List<Genre> géneros de la película.
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Método que modifica el listado de géneros de la película.
     * @param genres lsitado de géneros.
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Método que devuelve el listado de productoras de la película.
     * @return List<ProductionCompany> listado de productoras.
     */
    public List<ProductionCompany> getCompanies() {
        return companies;
    }

    /**
     * Método que modifica el listado de compañías de la película.
     * @param companies listado de compañías.
     */
    public void setCompanies(List<ProductionCompany> companies) {
        this.companies = companies;
    }

    /**
     * Método que devuelve un listado con los países de
     * las productoras de la película.
     * @return List<ProductionCountry> listado de países.
     */
    public List<ProductionCountry> getCountries() {
        return countries;
    }

    /**
     * Método que modifica el listado de los países de las productoras
     * de la película.
     * @param countries listado de países.
     */
    public void setCountries(List<ProductionCountry> countries) {
        this.countries = countries;
    }

    /**
     * Método que devuelve las ganacias de la película.
     * @return double ganancias de la película.
     */
    public double getRevenue() {
        return revenue;
    }

    /**
     * Método que modifica las ganancias de la película.
     * @param revenue ganancias.
     */
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    /**
     * Método que devuelve el lenguaje original de la película.
     * @return String lenguaje original de la película.
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * Método que modifica el idioma original de la película.
     * @param originalLanguage idioma original.
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     * Método que devuelve el título original de la película.
     * @return String título original de la película.
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Método que modifica el título original de la película.
     * @param originalTitle título original de la película.
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * Método que devuelve el estado de la película.
     * @return String estado de la película.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Método que modifica el estado de la película.
     * @param status estado.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Método que devuelve el eslogan de la película.
     * @return String eslogan de la película.
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * Método que modifica el eslogan de la película.
     * @param tagline eslogan.
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     * Método que devuelve la URL a la web oficial de la película.
     * @return String URL.
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Método que modifica la web oficial de la película.
     * @param homepage web oficial.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * Método que devuelve el enlace a IMDB de la película.
     * @return String enlace a IMDB.
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * Método que modifica el enlace a IMDB.
     * @param imdbId enlace.
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * Método que devuelve el listado de lenguajes de de la película.
     * @return String listado de lenguajes.
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Método que modifica el bloque de lenguajes.
     * @param languages bloque de lenguajes.
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Método que devuelve la popularidad de la película.
     * @return double popularidad de la película.
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     * Método que modifica la popularidad de la película.
     * @param popularity popularidad.
     */
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     * Método que devuelve el presupuesto de de la película.
     * @return double presupuesto de la película.
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Método que modifica el presupuesto de la película.
     * @param budget presupuesto.
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * Método que devuelve la duración de la película.
     * @return double duración de la película.
     */
    public double getRuntime() {
        return runtime;
    }

    /**
     * Método que modifica la duración de la película.
     * @param runtime duración.
     */
    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    /**
     * Método que devuelve los IDs en modo String.
     * @return IDs.
     */
    public String getGenreIdString() {
        return GenreIdString;
    }

    /**
     * Método que modifica los IDs en modo String.
     * @param genreIdString IDs.
     */
    public void setGenreIdString(String genreIdString) {
        GenreIdString = genreIdString;
    }

}
