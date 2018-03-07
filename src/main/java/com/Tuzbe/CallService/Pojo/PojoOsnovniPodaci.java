package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoOsnovniPodaci {
	String naziv;
	String kontakti;
	Long korisnikId;
	Double dug;
	String datum;
	String loyality;
	public PojoOsnovniPodaci() {
	}

	public PojoOsnovniPodaci(String loyality,String datum,Double dug, String naziv, String kontakti, Long korisnikId) {
		this.naziv = naziv;
		this.kontakti = kontakti;
		this.korisnikId = korisnikId;
		this.dug = dug;
		this.datum = datum;
		this.loyality = loyality;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getKontakti() {
		return kontakti;
	}

	public void setKontakti(String kontakti) {
		this.kontakti = kontakti;
	}

	public Double getDug() {
		return dug;
	}

	public void setDug(Double dug) {
		this.dug = dug;
	}

	public Long getKorisnikId() {
		return korisnikId;
	}

	public void setKorisnikId(Long korisnikId) {
		this.korisnikId = korisnikId;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getLoyality() {
		return loyality;
	}

	public void setLoyality(String loyality) {
		this.loyality = loyality;
	}
	
	

}
