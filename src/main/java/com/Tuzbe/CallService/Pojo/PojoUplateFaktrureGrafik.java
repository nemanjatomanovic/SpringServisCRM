package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoUplateFaktrureGrafik {

	String godina;
	String mjesec;
	String dugovanje;
	
	public PojoUplateFaktrureGrafik(){}
	public PojoUplateFaktrureGrafik(String godina, String mjesec, String dugovanje) {
		this.godina = godina;
		this.mjesec = mjesec;
		this.dugovanje = dugovanje;
	}
	public String getGodina() {
		return godina;
	}
	public void setGodina(String godina) {
		this.godina = godina;
	}
	public String getMjesec() {
		return mjesec;
	}
	public void setMjesec(String mjesec) {
		this.mjesec = mjesec;
	}
	public String getDugovanje() {
		return dugovanje;
	}
	public void setDugovanje(String dugovanje) {
		this.dugovanje = dugovanje;
	}
	
	
	
	
}
