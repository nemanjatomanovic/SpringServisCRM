package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoLoyality {

	Long korisnikId;
	Double rating;
	String napomena;
	
	public PojoLoyality(){}
	
	public PojoLoyality(Long korisnikId, Double rating, String napomena) {
		this.korisnikId = korisnikId;
		this.rating = rating;
		this.napomena = napomena;
	}
	public Long getKorisnikId() {
		return korisnikId;
	}
	public void setKorisnikId(Long korisnikId) {
		this.korisnikId = korisnikId;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public String getNapomena() {
		return napomena;
	}
	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}
	
	
	
}
