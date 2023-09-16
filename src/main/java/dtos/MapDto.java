package dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MapDto {

    private final String mapName;
    private final String alias;
    private final String urlPhoto;
    private final LocalDate releaseDate;
    private final LocalDateTime importedDate;
    private final String mapDescription;
    private final boolean downloaded;
    private final boolean official;
    private final Long idWorkShop;

    public MapDto(String alias, String urlPhoto, LocalDate releaseDate, LocalDateTime importedDate, String mapDescription, boolean downloaded, String mapName, boolean official, Long idWorkShop) {
        super();
        this.alias = alias;
        this.urlPhoto = urlPhoto;
        this.releaseDate = releaseDate;
        this.importedDate = importedDate;
        this.mapDescription = mapDescription;
        this.downloaded = downloaded;
        this.mapName = mapName;
        this.official = official;
        this.idWorkShop = idWorkShop;
    }

    public String getMapName() {
        return mapName;
    }

    public String getAlias() {
        return alias;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public LocalDateTime getImportedDate() {
        return importedDate;
    }

    public String getMapDescription() {
        return mapDescription;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isOfficial() {
        return official;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }
}
