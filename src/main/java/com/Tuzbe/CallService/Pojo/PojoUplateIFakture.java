package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoUplateIFakture {

	String datum;
	String naziv;
	String dugovanje;
	String potrazivanje;
	
	
	public PojoUplateIFakture(){}
	
	public PojoUplateIFakture(String datum, String naziv, String dugovanje, String potrazivanje) {
		this.datum = datum;
		this.naziv = naziv;
		this.dugovanje = dugovanje;
		this.potrazivanje = potrazivanje;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getDugovanje() {
		return dugovanje;
	}
	public void setDugovanje(String dugovanje) {
		this.dugovanje = dugovanje;
	}
	public String getPotrazivanje() {
		return potrazivanje;
	}
	public void setPotrazivanje(String potrazivanje) {
		this.potrazivanje = potrazivanje;
	}
	
}
