package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoAktivniNalozi {

	Long tipNalogaTrenutni;
	String nazivNalogaTrenutni;
	Long korisnikId;
	Long tipNultog;
	String nazivNalogaNulti;
	boolean aktivan;
	public Long getTipNalogaTrenutni() {
		return tipNalogaTrenutni;
	}
	public void setTipNalogaTrenutni(Long tipNalogaTrenutni) {
		this.tipNalogaTrenutni = tipNalogaTrenutni;
	}
	public String getNazivNalogaTrenutni() {
		return nazivNalogaTrenutni;
	}
	public void setNazivNalogaTrenutni(String nazivNalogaTrenutni) {
		this.nazivNalogaTrenutni = nazivNalogaTrenutni;
	}
	public Long getKorisnikId() {
		return korisnikId;
	}
	public void setKorisnikId(Long korisnikId) {
		this.korisnikId = korisnikId;
	}
	public Long getTipNultog() {
		return tipNultog;
	}
	public void setTipNultog(Long tipNultog) {
		this.tipNultog = tipNultog;
	}
	public String getNazivNalogaNulti() {
		return nazivNalogaNulti;
	}
	public void setNazivNalogaNulti(String nazivNalogaNulti) {
		this.nazivNalogaNulti = nazivNalogaNulti;
	}
	public boolean getAktivan() {
		return aktivan;
	}
	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	
	
}
