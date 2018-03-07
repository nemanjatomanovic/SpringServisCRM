package com.Tuzbe.CallService.Pojo;

import org.springframework.stereotype.Component;

@Component
public class PojoDetaljiKor {
	String paket;
	Long zbirniId;
	Long ugovorId;
	Double iznosDuga;

	public PojoDetaljiKor() {}

	public PojoDetaljiKor(String paket, Long zbirniId, Long ugovorId, Double iznosDuga) {
		super();
		this.paket = paket;
		this.zbirniId = zbirniId;
		this.ugovorId = ugovorId;
		this.iznosDuga = iznosDuga;
	}

	public Double getIznosDuga() {
		return iznosDuga;
	}

	public void setIznosDuga(Double iznosDuga) {
		this.iznosDuga = iznosDuga;
	}

	public String getPaket() {
		return paket;
	}

	public void setPaket(String paket) {
		this.paket = paket;
	}

	public Long getZbirniId() {
		return zbirniId;
	}

	public void setZbirniId(Long zbirniId) {
		this.zbirniId = zbirniId;
	}

	public Long getUgovorId() {
		return ugovorId;
	}

	public void setUgovorId(Long ugovorId) {
		this.ugovorId = ugovorId;
	}

}
