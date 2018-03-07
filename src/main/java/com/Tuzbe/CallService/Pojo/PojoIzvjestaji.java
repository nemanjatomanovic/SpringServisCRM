package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoIzvjestaji {

	String brojRadnogNaloga;
	String imePrezime;
	String adresa;
	String danDuga;
	Double dug;
	Long pozivNaBroj;
	String datum;

	public PojoIzvjestaji() {}

	public PojoIzvjestaji(String datum,String brojRadnogNaloga, String imePrezime, String adresa, String danDuga, Double dug,
			Long pozivNaBroj) {
		this.brojRadnogNaloga = brojRadnogNaloga;
		this.imePrezime = imePrezime;
		this.adresa = adresa;
		this.danDuga = danDuga;
		this.dug = dug;
		this.pozivNaBroj = pozivNaBroj;
		this.datum = datum;
	}

	public String getBrojRadnogNaloga() {
		return brojRadnogNaloga;
	}

	public void setBrojRadnogNaloga(String brojRadnogNaloga) {
		this.brojRadnogNaloga = brojRadnogNaloga;
	}

	public String getImePrezime() {
		return imePrezime;
	}

	public void setImePrezime(String imePrezime) {
		this.imePrezime = imePrezime;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getDanDuga() {
		return danDuga;
	}

	public void setDanDuga(String danDuga) {
		this.danDuga = danDuga;
	}

	public Double getDug() {
		return dug;
	}

	public void setDug(Double dug) {
		this.dug = dug;
	}

	public Long getPozivNaBroj() {
		return pozivNaBroj;
	}

	public void setPozivNaBroj(Long pozivNaBroj) {
		this.pozivNaBroj = pozivNaBroj;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	
}
