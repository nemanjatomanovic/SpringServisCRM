package com.Tuzbe.CallService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.ini4j.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Tuzbe.CallService.Izvjestaji.IzvjestajOpomena;
import com.Tuzbe.CallService.Pojo.PojoAktivniNalozi;
import com.Tuzbe.CallService.Pojo.PojoDetaljiKor;
import com.Tuzbe.CallService.Pojo.PojoNultiITrenutni;
import com.Tuzbe.CallService.Pojo.PojoOprema;
import com.Tuzbe.CallService.Pojo.PojoOsnovniPodaci;
import com.Tuzbe.CallService.Pojo.PojoUplateFaktrureGrafik;
import com.Tuzbe.CallService.Pojo.PojoUplateIFakture;

@RestController
public class Kontroler {

	UpitDb upit;
	IzvjestajOpomena izvjestajOpomena;
	Util util;

	@Autowired
	public Kontroler(UpitDb upit, IzvjestajOpomena izvjestajOpomena, Util util) {
		this.upit = upit;
		this.izvjestajOpomena = izvjestajOpomena;
		this.util = util;

	}
	//test21
	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/detalji", method = RequestMethod.POST)
	public List<PojoDetaljiKor> getZaTuzbu(@RequestParam("korisnikId") Long korId) throws SQLException {
		upit.selectDetalje(korId);

		return upit.getListaDetalja();

	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/korisniciZaTuzbu", method = RequestMethod.POST)
	public List<PojoOsnovniPodaci> getkorisnikZaTuzbu() throws SQLException {
		upit.selectOsnovnePodatke();
		return upit.getListaPodataka();

	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/listaOpreme", method = RequestMethod.POST)
	public List<PojoOprema> getOpremu(@RequestParam("korisnikId") Long korId) throws SQLException {
		upit.selectOpremu(korId);
		return upit.getListaOpreme();

	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/uplateIFakture", method = RequestMethod.POST)
	public List<PojoUplateIFakture> getUplateFakture(@RequestParam("korisnikId") Long korId) throws SQLException {
		upit.selectUplateFakture(korId);
		return upit.getListaUplataFaktura();
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/uplateIFaktureGrafik", method = RequestMethod.POST)
	public List<PojoUplateFaktrureGrafik> getUplateFaktureGraf(@RequestParam("korisnikId") Long korId)
			throws SQLException {
		upit.selectUplateFaktureGrafik(korId);
		return upit.getListgrafika();
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/izvjestajOpomene", method = RequestMethod.POST)
	public void printIzvjestaj(@RequestParam("korisnikId") Long korisnikId, @RequestParam("tipNaloga") Long tipNaloga)
			throws SQLException {
		izvjestajOpomena.printaj(upit.selectPodatkeZaOpomeneReport(korisnikId, tipNaloga), Long.toString(korisnikId),
				Long.toString(upit.getNultiNalog(korisnikId, tipNaloga)));
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/insertCRMNalog", method = RequestMethod.POST)
	public boolean insertCRMNalog(@RequestParam(value = "korisnikId") Long korisnikId,
			@RequestParam(value = "tipNaloga") Long tipNaloga, @RequestParam(value = "aktivan") boolean aktivan,
			@RequestParam(value = "radnik") String radnik, @RequestParam(value = "parentId") Long parentId)
			throws ClassNotFoundException, InvalidFileFormatException, SQLException, IOException {

		return upit.insertCRMNalog(tipNaloga, aktivan, radnik, korisnikId, parentId);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/kreirajIzvjestajSaTerena", method = RequestMethod.POST)
	public boolean kreirajIzvjestajSaTerena(@RequestParam("korisnikId") Long korisnikId,
			@RequestParam("sadrzaj") String sadrzaj, @RequestParam("tipNaloga") Long tipNaloga)
			throws IOException, SQLException {
		return util.upisIzvjestajaSaTerena(korisnikId, upit.getNultiNalog(korisnikId, tipNaloga), sadrzaj);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/faktureZaDug", method = RequestMethod.POST)
	public List<String> listaFakturaZaDug(@RequestParam("korisnikId") Long korisnikId) {
		return upit.selectFaktureZaDug(korisnikId);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/zatvoriSveNaloge", method = RequestMethod.POST)
	public boolean zatvoriSveNaloge(@RequestParam("korisnikId") Long korisnikId) {
		return upit.zatvoriSveNaloge(korisnikId);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/zatvoriTrenutniNalog", method = RequestMethod.POST)
	public boolean zatvoriTrenutniNalog(@RequestParam("korisnikId") Long korisnikId) throws SQLException {
		return upit.zatvoriTrenutniNalog(korisnikId);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/insertCRMNalogStavka", method = RequestMethod.POST)
	public boolean insertPomNalog(@RequestParam("tipNaloga") Long tipNaloga, @RequestParam("aktivan") boolean aktivan,
			@RequestParam("radnik") String radnik, @RequestParam("korisnikId") Long korisnikId)
			throws ClassNotFoundException, InvalidFileFormatException, SQLException, IOException {

		return upit.insertCRMNalog(tipNaloga, aktivan, radnik, korisnikId, upit.getParentNalog(korisnikId));
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/kreirajDosije", method = RequestMethod.POST)
	public boolean dosijeKorisnik(@RequestParam("korisnikId") Long korisnikId,
			@RequestParam("tipNaloga") Long tipNaloga) throws IOException, SQLException {
		upit.selectFaktureZaDug(korisnikId);
		return util.dosijeX(upit.faktureZaDug, Long.toString(upit.getNultiNalog(korisnikId, tipNaloga)), korisnikId);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/getPoslednjiNalogId", method = RequestMethod.POST)
	public Long poslednjiNalog(@RequestParam("korisnikId") Long korisnikId, @RequestParam("tipNaloga") Long tipNaloga)
			throws SQLException {
		return upit.getPoslednjiNalogId(korisnikId, tipNaloga);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/getNultiNalog", method = RequestMethod.POST)
	public Long getNultiNalog(@RequestParam("korisnikId") Long korisnikId, @RequestParam("tipNaloga") Long tipNaloga)
			throws SQLException {
		return upit.getNultiNalog(korisnikId, tipNaloga);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/insertOpomena", method = RequestMethod.POST)
	public boolean insertOpomenu(@RequestParam("korisnikId") Long korisnikId, @RequestParam("tipNaloga") Long tipNaloga,
			@RequestParam("idStatusa") Long idStatusa)
			throws ClassNotFoundException, InvalidFileFormatException, SQLException, IOException {
		return upit.insertOpomena(upit.getPoslednjiNalogId(korisnikId, tipNaloga), idStatusa);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/prenesiNalog", method = RequestMethod.POST)
	public boolean prenosNaloga(@RequestParam("prethodniKorisnik") String prethodniKorisnik,
			@RequestParam("trenutniKorisnik") String trenutniKorisnik, @RequestParam("korisnikId") Long korisnikId,
			@RequestParam("tipNaloga") Long tipNaloga, @RequestParam("opis") String opis,
			@RequestParam("userName") String userName)
			throws ClassNotFoundException, InvalidFileFormatException, SQLException, IOException {
		Long naloogId = upit.getPoslednjiNalogId(korisnikId, tipNaloga);

		upit.prenosNaloga(prethodniKorisnik, trenutniKorisnik, naloogId, opis);
		return upit.updateNaloga(naloogId, trenutniKorisnik);
	}

	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/sviAktivniNalozi", method = RequestMethod.POST)
	public List<PojoAktivniNalozi> getNiT(@RequestParam("userName") String userName) throws SQLException {
		upit.getAktivne(userName);
		return upit.getAktivniNalozi();
	}
	
	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/statusNaloga", method = RequestMethod.POST)
	public String getStatusNaloga(@RequestParam("korisnikId")Long korisnikId, @RequestParam("tipNaloga")Long tipNaloga) throws SQLException {
		return upit.provjeriStatus(upit.getPoslednjiNalogId(korisnikId, tipNaloga));
	}
	
	@CrossOrigin(origins = "http://192.168.0.125")
	@RequestMapping(value = "/generisiTuzbeniZahtjev", method = RequestMethod.POST)
	public void generisi() throws SQLException, IOException {
		util.napraviZahtjev();
	}
}
