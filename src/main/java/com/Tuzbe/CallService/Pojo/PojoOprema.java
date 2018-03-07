package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoOprema {
	Long ugovor;
	String paket;
	String nazivOpreme;
	String sn;
	String mac;
	String adresa;
	String datumRealizacije;
	
	public PojoOprema(){}
	
	public PojoOprema(Long ugovor, String paket, String nazivOpreme, String sn, String mac, String adresa,
			String datumRealizacije) {
		this.ugovor = ugovor;
		this.paket = paket;
		this.nazivOpreme = nazivOpreme;
		this.sn = sn;
		this.mac = mac;
		this.adresa = adresa;
		this.datumRealizacije = datumRealizacije;
	}
	public Long getUgovor() {
		return ugovor;
	}
	public void setUgovor(Long ugovor) {
		this.ugovor = ugovor;
	}
	public String getPaket() {
		return paket;
	}
	public void setPaket(String paket) {
		this.paket = paket;
	}
	public String getNazivOpreme() {
		return nazivOpreme;
	}
	public void setNazivOpreme(String nazivOpreme) {
		this.nazivOpreme = nazivOpreme;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getDatumRealizacije() {
		return datumRealizacije;
	}
	public void setDatumRealizacije(String datumRealizacije) {
		this.datumRealizacije = datumRealizacije;
	}
	
	
	
	
}
