package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Park.
 */
@Entity
@Table(name = "park")
public class Park implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "park_name")
    private String parkName;

    @Column(name = "park_address")
    private String parkAddress;

    @Column(name = "longtitude")
    private Long longtitude;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "verified")
    private Boolean verified;

    @OneToMany(mappedBy = "park")
    @JsonIgnoreProperties(value = { "reports", "media", "park" }, allowSetters = true)
    private Set<Equipement> equipements = new HashSet<>();

    @OneToMany(mappedBy = "park")
    @JsonIgnoreProperties(value = { "park", "equipement", "report" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @OneToMany(mappedBy = "park")
    @JsonIgnoreProperties(value = { "media", "equipement", "park" }, allowSetters = true)
    private Set<Report> reports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Park id(Long id) {
        this.id = id;
        return this;
    }

    public String getParkName() {
        return this.parkName;
    }

    public Park parkName(String parkName) {
        this.parkName = parkName;
        return this;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkAddress() {
        return this.parkAddress;
    }

    public Park parkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
        return this;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

    public Long getLongtitude() {
        return this.longtitude;
    }

    public Park longtitude(Long longtitude) {
        this.longtitude = longtitude;
        return this;
    }

    public void setLongtitude(Long longtitude) {
        this.longtitude = longtitude;
    }

    public Long getLatitude() {
        return this.latitude;
    }

    public Park latitude(Long latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public Park verified(Boolean verified) {
        this.verified = verified;
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Set<Equipement> getEquipements() {
        return this.equipements;
    }

    public Park equipements(Set<Equipement> equipements) {
        this.setEquipements(equipements);
        return this;
    }

    public Park addEquipement(Equipement equipement) {
        this.equipements.add(equipement);
        equipement.setPark(this);
        return this;
    }

    public Park removeEquipement(Equipement equipement) {
        this.equipements.remove(equipement);
        equipement.setPark(null);
        return this;
    }

    public void setEquipements(Set<Equipement> equipements) {
        if (this.equipements != null) {
            this.equipements.forEach(i -> i.setPark(null));
        }
        if (equipements != null) {
            equipements.forEach(i -> i.setPark(this));
        }
        this.equipements = equipements;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public Park media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Park addMedia(Media media) {
        this.media.add(media);
        media.setPark(this);
        return this;
    }

    public Park removeMedia(Media media) {
        this.media.remove(media);
        media.setPark(null);
        return this;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setPark(null));
        }
        if (media != null) {
            media.forEach(i -> i.setPark(this));
        }
        this.media = media;
    }

    public Set<Report> getReports() {
        return this.reports;
    }

    public Park reports(Set<Report> reports) {
        this.setReports(reports);
        return this;
    }

    public Park addReport(Report report) {
        this.reports.add(report);
        report.setPark(this);
        return this;
    }

    public Park removeReport(Report report) {
        this.reports.remove(report);
        report.setPark(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        if (this.reports != null) {
            this.reports.forEach(i -> i.setPark(null));
        }
        if (reports != null) {
            reports.forEach(i -> i.setPark(this));
        }
        this.reports = reports;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Park)) {
            return false;
        }
        return id != null && id.equals(((Park) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Park{" +
            "id=" + getId() +
            ", parkName='" + getParkName() + "'" +
            ", parkAddress='" + getParkAddress() + "'" +
            ", longtitude=" + getLongtitude() +
            ", latitude=" + getLatitude() +
            ", verified='" + getVerified() + "'" +
            "}";
    }
}
