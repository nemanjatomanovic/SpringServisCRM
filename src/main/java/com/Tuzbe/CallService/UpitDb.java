package com.Tuzbe.CallService;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Tuzbe.CallService.Pojo.PojoAktivniNalozi;
import com.Tuzbe.CallService.Pojo.PojoDetaljiKor;
import com.Tuzbe.CallService.Pojo.PojoIzvjestaji;
import com.Tuzbe.CallService.Pojo.PojoLoyality;
import com.Tuzbe.CallService.Pojo.PojoNultiITrenutni;
import com.Tuzbe.CallService.Pojo.PojoOprema;
import com.Tuzbe.CallService.Pojo.PojoOsnovniPodaci;
import com.Tuzbe.CallService.Pojo.PojoUplateFaktrureGrafik;
import com.Tuzbe.CallService.Pojo.PojoUplateIFakture;

@Component
public class UpitDb {
	// String iniFile = "/var/lib/tomcat8/webapps/podaciOKonekcijiNaBaze.ini";
	String iniFile = "/opt/apache-tomcat-8.0.41/webapps/KonfFile/podaciOKonekcijiNaBaze.ini";
	// String iniFile = "C:\\Users\\Nemanja\\Desktop\\podaciOKonekcijiNaBaze.ini";
	String crta = Character.toString((char) 92);
	List<String> listaKorId = new ArrayList<String>();
	List<String> listaZbirni = new ArrayList<>();
	List<PojoDetaljiKor> listaDetalja = new ArrayList<>();
	List<PojoOsnovniPodaci> listaPodataka = new ArrayList<>();
	List<PojoOprema> listaOpreme = new ArrayList<>();
	List<PojoUplateIFakture> listaUplataFaktura = new ArrayList<>();
	List<PojoUplateFaktrureGrafik> listgrafika = new ArrayList<>();
	List<PojoIzvjestaji> listaIzvjestaja = new ArrayList<>();
	List<String> faktureZaDug = new ArrayList<>();
	List<PojoAktivniNalozi> aktivniNalozi = new ArrayList<>();
	Map<Long, String> mapaLoyality = new HashMap<>();
	List<String> listaBrojeva = new ArrayList<>();
	List<String> pomLista = new ArrayList<>();

	// <<<<<<< DEPENDENCY INJECTION >>>>>>>
	PojoDetaljiKor pojoDetaljiKor;
	PojoOsnovniPodaci pojoOsnovniPodaci;
	PojoOprema pojoOprema;
	PojoUplateIFakture pojoUplateFakture;
	PojoLoyality pojoLoyality;
	PojoUplateFaktrureGrafik pojoUplateGrafik;
	PojoIzvjestaji pojoIzvjestaji;
	PojoAktivniNalozi pojoAktivni;
	@Autowired
	public UpitDb(PojoIzvjestaji pojoIzvjestaji, PojoUplateFaktrureGrafik pojoUplateGrafik, PojoLoyality pojoLoyality,
			PojoUplateIFakture pojoUplateFakture, PojoDetaljiKor pojoDetaljiKor, PojoOsnovniPodaci pojoOsnovniPodaci,
			PojoOprema pojoOprema, PojoAktivniNalozi pojoAktivni) {
		this.pojoDetaljiKor = pojoDetaljiKor;
		this.pojoOsnovniPodaci = pojoOsnovniPodaci;
		this.pojoOprema = pojoOprema;
		this.pojoUplateFakture = pojoUplateFakture;
		this.pojoLoyality = pojoLoyality;
		this.pojoIzvjestaji = pojoIzvjestaji;
		this.pojoAktivni = pojoAktivni;
	}

	public UpitDb() {
	}

	public void selectStatusPoUgovoru() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement("select DISTINCT(KorisnikId) as korisnik from ugvUgovor"
					+ " where PrivremeniStatusIskljucenjaId is not null and DatumRaskida is null and DatumPrivremenogIskljucenja > dateadd(month, -11, getdate()) ");

			rs = stmt.executeQuery();

			while (rs.next()) {
				listaKorId.add(rs.getString("korisnik"));
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void selectDetalje(Long korisnik) throws SQLException {

		listaDetalja.clear();
		listaKorId.clear();
		listaZbirni.clear();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			selectStatusPoUgovoru();

			getZbirniId(korisnik);
			for (int j = 0; j < getListaZbirni().size(); j++) {
				String sql = "select sum( (COALESCE(sgsNalog.iznosDug,0) - COALESCE(sgsNalog.iznosPot,0))) as DugKorisnika ,sgsNalog.zbirniRacun_Id,vwProvisioning.UgovorId,vwProvisioning.NazivProizvoda"
						+ " from dbo.vwProvisioning"
						+ " LEFT JOIN dbo.ugvUgovor ON dbo.vwProvisioning.UgovorId=ugvUgovor.Id"
						+ " LEFT JOIN dbo.sgsNalog on ugvUgovor.ZbirniRacunId=sgsNalog.zbirniRacun_Id"
						+ " where vwProvisioning.UgovorId in (select id from dbo.ugvUgovor"
						+ " where ugvUgovor.ZbirniRacunId = ? and ugvUgovor.StatusUgovoraId=3)"
						+ " GROUP by sgsNalog.zbirniRacun_Id,vwProvisioning.UgovorId,vwProvisioning.NazivProizvoda";
				stmt = conn.prepareStatement(sql);
				System.out.println(sql);
				stmt.setString(1, getListaZbirni().get(j));

				rs = stmt.executeQuery();
				System.out.println(rs.getStatement().toString());
				while (rs.next()) {
					pojoDetaljiKor = new PojoDetaljiKor();
					pojoDetaljiKor.setIznosDuga(rs.getDouble("DugKorisnika"));
					pojoDetaljiKor.setPaket(rs.getString("NazivProizvoda"));
					pojoDetaljiKor.setZbirniId(rs.getLong("zbirniRacun_Id"));
					pojoDetaljiKor.setUgovorId(rs.getLong("UgovorId"));
					listaDetalja.add(pojoDetaljiKor);
				}

			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void selectOsnovnePodatke() throws SQLException {

		String rez = selectTuzeneKorisnike();
		listaPodataka.clear();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			String sql;
			stmt = conn.createStatement();
			if (rez.length() < 1) {
				sql = "SELECT sum( (COALESCE(n.iznosDug,0) - COALESCE(n.iznosPot,0)))  as DugKorisnika ,korZbirniRacun.KorisnikId,(korKorisnik.Ime+' '+korKorisnik.Prezime) as ImePrezime, korKorisnik.Naziv,korKorisnik.Telefon,korKorisnik.MobilniTelefon\r\n"
						+ "FROM sgsNalog n\r\n"
						+ "LEFT JOIN dbo.korZbirniRacun ON n.zbirniRacun_Id=korZbirniRacun.Id\r\n"
						+ "LEFT join dbo.korKorisnik on korZbirniRacun.KorisnikId=korKorisnik.Id\r\n"
						+ "WHERE n.zbirniracun_id in (select DISTINCT (id) \r\n" + "from dbo.korZbirniRacun\r\n"
						+ "where korZbirniRacun.KorisnikId in (select DISTINCT(KorisnikId) as korisnik from ugvUgovor where PrivremeniStatusIskljucenjaId is not null and DatumRaskida is null and DatumPrivremenogIskljucenja >  dateadd(month, -11, getdate()))) and n.status=2\r\n"
						+ "group by korZbirniRacun.KorisnikId,korKorisnik.Ime,korKorisnik.Prezime,korKorisnik.Naziv,korKorisnik.Telefon,korKorisnik.MobilniTelefon\r\n"
						+ "having sum( (COALESCE(n.iznosDug,0) - COALESCE(n.iznosPot,0)))>0";
			} else {
				sql = "SELECT sum( (COALESCE(n.iznosDug,0) - COALESCE(n.iznosPot,0)))  as DugKorisnika ,korZbirniRacun.KorisnikId,(korKorisnik.Ime+' '+korKorisnik.Prezime) as ImePrezime, korKorisnik.Naziv,korKorisnik.Telefon,korKorisnik.MobilniTelefon\r\n"
						+ "FROM sgsNalog n\r\n"
						+ "LEFT JOIN dbo.korZbirniRacun ON n.zbirniRacun_Id=korZbirniRacun.Id\r\n"
						+ "LEFT join dbo.korKorisnik on korZbirniRacun.KorisnikId=korKorisnik.Id\r\n"
						+ "WHERE n.zbirniracun_id in (select DISTINCT (id) \r\n" + "from dbo.korZbirniRacun\r\n"
						+ "where korZbirniRacun.KorisnikId in (select DISTINCT(KorisnikId) as korisnik from ugvUgovor where PrivremeniStatusIskljucenjaId is not null and KorisnikId not in("
						+ rez
						+ ") and DatumRaskida is null and DatumPrivremenogIskljucenja >  dateadd(month, -11, getdate()))) and n.status=2\r\n"
						+ "group by korZbirniRacun.KorisnikId,korKorisnik.Ime,korKorisnik.Prezime,korKorisnik.Naziv,korKorisnik.Telefon,korKorisnik.MobilniTelefon\r\n"
						+ "having sum( (COALESCE(n.iznosDug,0) - COALESCE(n.iznosPot,0)))>0";
			}

			System.out.println(sql);

			rs = stmt.executeQuery(sql);
			selectLoyality();
			while (rs.next()) {
				pojoOsnovniPodaci = new PojoOsnovniPodaci();

				if (rs.getString("ImePrezime") == null) {
					pojoOsnovniPodaci.setNaziv(rs.getString("Naziv"));
				} else {
					if (rs.getString("ImePrezime").length() < 3) {
						pojoOsnovniPodaci.setNaziv(rs.getString("Naziv"));
					} else {
						pojoOsnovniPodaci.setNaziv(rs.getString("ImePrezime"));
					}

				}
				if (rs.getString("Telefon") == null && rs.getString("MobilniTelefon") == null) {
					pojoOsnovniPodaci.setKontakti("");
				} else if (rs.getString("Telefon") != null && rs.getString("MobilniTelefon") != null) {
					pojoOsnovniPodaci.setKontakti(rs.getString("Telefon") + " " + rs.getString("MobilniTelefon"));

				} else if (rs.getString("Telefon") != null && rs.getString("MobilniTelefon") == null) {
					pojoOsnovniPodaci.setKontakti(rs.getString("Telefon"));
				} else if (rs.getString("Telefon") == null && rs.getString("MobilniTelefon") != null) {
					pojoOsnovniPodaci.setKontakti(rs.getString("MobilniTelefon"));
				}

				pojoOsnovniPodaci.setKorisnikId(rs.getLong("KorisnikId"));
				pojoOsnovniPodaci.setDatum(selectDatumIskljucenja(rs.getLong("KorisnikId")));
				pojoOsnovniPodaci.setDug(rs.getDouble("DugKorisnika"));
				if (getMapaLoyality().get(rs.getLong("KorisnikId")) == null) {
					pojoOsnovniPodaci.setLoyality("");
				} else {
					pojoOsnovniPodaci.setLoyality(getMapaLoyality().get(rs.getLong("KorisnikId")));
				}

				listaPodataka.add(pojoOsnovniPodaci);

			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void getZbirniId(Long korisnikId) throws SQLException {
		listaZbirni.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement("Select id from dbo.korZbirniRacun where korZbirniRacun.KorisnikId = ?");

			stmt.setLong(1, korisnikId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				listaZbirni.add(rs.getString("id"));
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

	}

	public String selectDatumIskljucenja(Long korisnikId) throws SQLException {
		String datum = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement(
					"select top 1 ugvUgovor.DatumPrivremenogIskljucenja as DatumIskljucenja from dbo.ugvUgovor where ugvUgovor.KorisnikId=? and ugvUgovor.StatusUgovoraId=3 and ugvUgovor.PrivremeniStatusIskljucenjaId is not null");
			stmt.setLong(1, korisnikId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				datum = rs.getString("DatumIskljucenja");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

		return datum;

	}

	public void selectOpremu(Long korisnikId) throws SQLException {

		listaOpreme.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement(
					"select vwUgovorZaduzenjePoSN.ugovor,predmet as nazivPaketa,vwUgovorZaduzenjePoSN.r_naziv as nazivProizvoda,"
							+ "sn,mac,vwUgovorZaduzenjePoSN.adresaInstalacije as adresa,ugvUgovor.DatumRealizacije as datumAktiviranja from dbo.vwUgovorZaduzenjePoSN"
							+ " LEFT join dbo.ugvUgovor on vwUgovorZaduzenjePoSN.ugovor=ugvUgovor.Id where ugvUgovor.KorisnikId=?");
			stmt.setLong(1, korisnikId);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {

				pojoOprema = new PojoOprema();

				pojoOprema.setAdresa(rs.getString("adresa"));
				pojoOprema.setDatumRealizacije(rs.getString("datumAktiviranja"));
				if (rs.getString("mac") == null) {
					pojoOprema.setMac("");
				} else {
					pojoOprema.setMac(rs.getString("mac"));
				}
				pojoOprema.setNazivOpreme(rs.getString("nazivProizvoda"));
				pojoOprema.setPaket(rs.getString("nazivPaketa"));
				pojoOprema.setSn(rs.getString("sn"));
				pojoOprema.setUgovor(rs.getLong("ugovor"));
				listaOpreme.add(pojoOprema);

			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void selectLoyality() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));
			stmt = conn.prepareStatement("select korisnik_id,rejting from korisnik_lojaliti");
			System.out.println(stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
				mapaLoyality.put(rs.getLong("korisnik_id"), rs.getString("rejting"));
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void selectUplateFakture(Long korisnikId) throws SQLException {

		listaUplataFaktura.clear();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement(
					"select datum,sgsNalog.dokumentNaziv,sgsNalog.iznosDug,sgsNalog.iznosPot,sgsNalog.zbirniRacun_Id from dbo.sgsNalog where sgsNalog.dokument_id in (305,94) and sgsNalog.zbirniRacun_Id in ("
							+ " select id from dbo.korZbirniRacun where korZbirniRacun.KorisnikId=?) ORDER by sgsNalog.zbirniRacun_Id");
			stmt.setLong(1, korisnikId);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {

				if (rs.getDouble("iznosDug") == 0.0 && rs.getDouble("iznosPot") == 0.0) {

				} else {

					pojoUplateFakture = new PojoUplateIFakture();
					pojoUplateFakture.setNaziv(rs.getString("dokumentNaziv"));
					pojoUplateFakture.setDatum(rs.getString("datum"));
					if (rs.getString("iznosDug") == null) {
						pojoUplateFakture.setDugovanje("");
					} else {
						pojoUplateFakture.setDugovanje(rs.getString("iznosDug"));
					}
					if (rs.getString("iznosPot") == null) {
						pojoUplateFakture.setPotrazivanje("");
					} else {
						pojoUplateFakture.setPotrazivanje(rs.getString("iznosPot"));
					}

					listaUplataFaktura.add(pojoUplateFakture);
				}

			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public void selectUplateFaktureGrafik(Long korisnikId) throws SQLException {

		listgrafika.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));
			stmt = conn.prepareStatement(
					"SELECT  year(datum) as godina,month(datum) as mjesec,(sum (sgsNalog.iznosDug)-sum (sgsNalog.iznosPot)) as saldoMjesec"
							+ " FROM sgsNalog WHERE zbirniracun_id in (select id from dbo.korZbirniRacun"
							+ " where korZbirniRacun.KorisnikId=? and sgsNalog.dokument_id in (305,94)) group by year(datum),month(datum) order by year(datum) desc ,month(datum) desc");
			stmt.setLong(1, korisnikId);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
				pojoUplateGrafik = new PojoUplateFaktrureGrafik();

				pojoUplateGrafik.setDugovanje(rs.getString("saldoMjesec"));
				pojoUplateGrafik.setGodina(rs.getString("godina"));
				pojoUplateGrafik.setMjesec(rs.getString("mjesec"));

				listgrafika.add(pojoUplateGrafik);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
	}

	public boolean insertCRMNalog(Long tipNaloga, boolean aktivan, String radnik, Long korisnikId, Long parentId)
			throws ClassNotFoundException, SQLException, InvalidFileFormatException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean uspjelo = false;
		int count = 0;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Timestamp ts = Timestamp.valueOf(dtf.format(now));

			stmt = conn.prepareStatement(
					"INSERT INTO public.crm_nalozi(nalog_type,is_active,parent_id,date_start,user_name,date_stop,customer_id)"
							+ "VALUES(?,?,?,?,?,null,?)");
			stmt.setLong(1, tipNaloga);
			stmt.setBoolean(2, aktivan);
			stmt.setLong(3, parentId);
			stmt.setTimestamp(4, ts);
			stmt.setString(5, radnik);
			stmt.setLong(6, korisnikId);
			System.out.println(stmt);
			count = stmt.executeUpdate();

			uspjelo = (count > 0) ? true : false;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
		}
		return uspjelo;
	}

	public List<PojoIzvjestaji> selectPodatkeZaOpomeneReport(Long korisnik, Long tipNaloga) throws SQLException {
		listaIzvjestaja.clear();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));
			stmt = conn.prepareStatement(
					"SELECT sum( (COALESCE(n.iznosDug,0) - COALESCE(n.iznosPot,0)))  as DugKorisnika ,(korKorisnik.Ime + ' ' +korKorisnik.Prezime) as ImePrezime,korKorisnik.Naziv,vwProvisioning.AdresaInstalacije as adresa,ugvUgovor.DatumPrivremenogIskljucenja"
							+ " FROM sgsNalog n left join dbo.korKorisnik on ?=korKorisnik.Id left join dbo.vwProvisioning on ?=vwProvisioning.KorisnikId"
							+ " left join dbo.ugvUgovor on ?=ugvUgovor.KorisnikId"
							+ " WHERE n.zbirniracun_id in (select id from dbo.korZbirniRacun"
							+ " where korZbirniRacun.KorisnikId=?) and n.status=2"
							+ " group by korKorisnik.Ime,korKorisnik.Prezime,vwProvisioning.AdresaInstalacije,korKorisnik.Naziv,ugvUgovor.DatumPrivremenogIskljucenja");

			stmt.setLong(1, korisnik);
			stmt.setLong(2, korisnik);
			stmt.setLong(3, korisnik);
			stmt.setLong(4, korisnik);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			while (rs.next()) {
				pojoIzvjestaji = new PojoIzvjestaji();
				String datum1[] = dateFormat.format(date).split("-");
				pojoIzvjestaji.setDatum(datum1[2] + "." + datum1[1] + "." + datum1[0]);
				pojoIzvjestaji.setAdresa(rs.getString("adresa").trim());
				pojoIzvjestaji.setBrojRadnogNaloga(Long.toString(getPoslednjiNalogId(korisnik, tipNaloga)));
				if (rs.getString("DatumPrivremenogIskljucenja") != null) {
					pojoIzvjestaji.setDanDuga(datum1[2] + "." + datum1[1] + "." + datum1[0]);
				} else {
					pojoIzvjestaji.setDanDuga("");
				}
				pojoIzvjestaji.setDug(rs.getDouble("DugKorisnika"));
				if (rs.getString("ImePrezime") != null) {
					pojoIzvjestaji.setImePrezime(rs.getString("ImePrezime"));
				} else {
					pojoIzvjestaji.setImePrezime(rs.getString("Naziv"));
				}

				pojoIzvjestaji.setPozivNaBroj(korisnik);
				listaIzvjestaja.add(pojoIzvjestaji);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}
		return listaIzvjestaja;
	}

	public List<String> selectFaktureZaDug(Long korisnikId) {
		faktureZaDug.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement(
					"select fktZbirnaFaktura.Id,(ROUND(CAST(fktZbirnaFaktura.IznosFaktureSaPdv AS FLOAT),2)) AS Dug\r\n"
							+ "from dbo.fktZbirnaFaktura\r\n" + "where \r\n" + "fktZbirnaFaktura.ZbirniRacunId in\r\n"
							+ "(select id \r\n" + "from dbo.korZbirniRacun\r\n"
							+ "where korZbirniRacun.KorisnikId=?)\r\n" + "and \r\n"
							+ "fktZbirnaFaktura.DatumPocetka in (\r\n" + "select  datum\r\n" + "from \r\n" + "\r\n"
							+ "(select * \r\n" + "FROM sgsNalog n\r\n" + "WHERE n.zbirniracun_id in (select id \r\n"
							+ "from dbo.korZbirniRacun\r\n" + "where korZbirniRacun.KorisnikId=?)\r\n"
							+ "and n.dokument_id=94\r\n" + "and n.status=2) as tabela\r\n" + "where datum> =\r\n"
							+ "(select top(1) datum\r\n" + "FROM sgsNalog n\r\n"
							+ "WHERE n.zbirniracun_id in (select id \r\n" + "from dbo.korZbirniRacun\r\n"
							+ "where korZbirniRacun.KorisnikId=?)\r\n"
							+ "and n.dokument_id=94 and n.iznosTrenutnogDuga=0\r\n" + "and n.status=2 \r\n"
							+ "order by datum desc \r\n" + "\r\n" + ")   and iznosDug!=0\r\n"
							+ ")and fktZbirnaFaktura.IznosFaktureSaPdv>0");
			stmt.setLong(1, korisnikId);
			stmt.setLong(2, korisnikId);
			stmt.setLong(3, korisnikId);
			System.out.println(stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				faktureZaDug.add(rs.getString("id"));

			}
			conn.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return faktureZaDug;

	}

	public boolean zatvoriSveNaloge(Long korisnikId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean uspjelo = false;
		int count = 0;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("update crm_nalozi set is_active=false,date_stop=NOW()\r\n"
					+ "where id in (select id from (WITH RECURSIVE rec (id) as (\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id,crm_nalozi.is_active from crm_nalozi where crm_nalozi.nalog_type=1 and crm_nalozi.customer_id=?\r\n"
					+ "UNION ALL\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id,crm_nalozi.is_active from rec,crm_nalozi where crm_nalozi.parent_id = rec.id\r\n"
					+ ") SELECT * FROM rec ) as tabela order by nalog_type desc\r\n" + ") and is_active=true");
			stmt.setLong(1, korisnikId);
			System.out.println(stmt);
			count = stmt.executeUpdate();

			uspjelo = (count > 0) ? true : false;

			conn.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return uspjelo;

	}

	public boolean zatvoriTrenutniNalog(Long korisnikId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean uspjelo = false;
		int count = 0;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("update crm_nalozi set is_active=false,date_stop=NOW()\r\n"
					+ "where id in (select id from (WITH RECURSIVE rec (id) as (\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from crm_nalozi where crm_nalozi.nalog_type=1 and crm_nalozi.customer_id=? and is_active=true\r\n"
					+ "UNION ALL\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from rec,crm_nalozi where   crm_nalozi.parent_id = rec.id\r\n"
					+ ") SELECT * FROM rec ) as tabela order by nalog_type desc\r\n" + "limit 1)");
			stmt.setLong(1, korisnikId);
			System.out.println(stmt);
			count = stmt.executeUpdate();

			uspjelo = (count > 0) ? true : false;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
		}

		return uspjelo;

	}

	public Long getParentNalog(Long korisnikId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long parentId = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("select id from (WITH RECURSIVE rec (id) as (\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from crm_nalozi where crm_nalozi.nalog_type=1 and crm_nalozi.customer_id=? and is_active=true \r\n"
					+ "UNION ALL\r\n"
					+ "SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from rec,crm_nalozi where   crm_nalozi.parent_id = rec.id\r\n"
					+ ") SELECT * FROM rec ) as tabela order by nalog_type desc\r\n" + "limit 1");

			stmt.setLong(1, korisnikId);
			rs = stmt.executeQuery();
			System.out.println(stmt);
			while (rs.next()) {
				parentId = rs.getLong("id");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

		return parentId;
	}

	public Long getPoslednjiNalogId(Long korisnikId, Long tipNaloga) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long poslednjiId = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("select id from (WITH RECURSIVE rec (id) as (\r\n"
					+ "  SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from crm_nalozi where crm_nalozi.nalog_type=? and crm_nalozi.customer_id=? \r\n"
					+ "  UNION ALL\r\n"
					+ "  SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from rec,crm_nalozi where crm_nalozi.parent_id = rec.id\r\n"
					+ "  ) SELECT * FROM rec ) as tabela order by nalog_type desc \r\n" + "limit 1  ");

			stmt.setLong(1, tipNaloga);
			stmt.setLong(2, korisnikId);
			System.out.println(stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				poslednjiId = rs.getLong("id");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

		return poslednjiId;
	}

	public String selectTuzeneKorisnike() {
		String rezultat = "";
		String pomVar = "";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("select distinct(crm_nalozi.customer_id) as id" + " from public.crm_nalozi"
					+ " where crm_nalozi.parent_id=0 and crm_nalozi.nalog_type=1 and crm_nalozi.is_active=true");
			System.out.println(stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				rezultat = rezultat + rs.getString("id") + ",";

			}
			conn.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rezultat.length() < 1) {
			pomVar = rezultat;
		} else {
			pomVar = rezultat.substring(0, rezultat.length() - 1);
		}
		return pomVar;

	}

	public Long getNultiNalog(Long korisnikId, Long tipNaloga) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long nulti = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement(
					"WITH RECURSIVE rec (id) as (SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from crm_nalozi where crm_nalozi.nalog_type=? and crm_nalozi.customer_id=? UNION ALL SELECT crm_nalozi.id,crm_nalozi.nalog_type,crm_nalozi.date_start,crm_nalozi.parent_id from rec,crm_nalozi where crm_nalozi.id = rec.parent_id ) SELECT id FROM rec order by id limit 1");

			stmt.setLong(1, tipNaloga);
			stmt.setLong(2, korisnikId);
			System.out.println("---------  " + stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				nulti = rs.getLong("id");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

		return nulti;
	}

	public boolean insertOpomena(Long idNaloga, Long idStatusa)
			throws ClassNotFoundException, SQLException, InvalidFileFormatException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean uspjelo = false;
		int count = 0;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn
					.prepareStatement("INSERT INTO public.crm_status_naloga(id_naloga,id_statusa) \r\n" + " VALUES(?,?)");
			stmt.setLong(1, idNaloga);
			stmt.setLong(2, idStatusa);
			System.out.println(stmt);
			count = stmt.executeUpdate();

			uspjelo = (count > 0) ? true : false;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
		}
		return uspjelo;
	}

	
	public String provjeriStatus(Long idNaloga) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String status = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement("select id_statusa from public.crm_status_naloga where id_naloga = ?");
			stmt.setLong(1, idNaloga);
			rs = stmt.executeQuery();

			while (rs.next()) {
				status = rs.getString("id_statusa");
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

		return status;
	}
	public void prenosNaloga(String prethodniKorisnik, String trenutniKorisnik, long idNaloga, String opis) throws ClassNotFoundException, SQLException, InvalidFileFormatException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Timestamp ts = Timestamp.valueOf(dtf.format(now));

			String prvi = "INSERT INTO public.crm_prenos_naloga(predao_nalog,preuzeo_nalog,id_naloga,datum_prenosa,opis)\r\n"
					+ " VALUES(?,?,?,?,?)";
			stmt = conn.prepareStatement(prvi);

			stmt.setString(1, prethodniKorisnik);
			stmt.setString(2, trenutniKorisnik);
			stmt.setLong(3, idNaloga);
			stmt.setTimestamp(4, ts);
			stmt.setString(5, opis);


			System.out.println("---- " + prvi);;
			stmt.executeUpdate();;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
		}
	}

	public boolean updateNaloga(long idNaloga, String trenutni)
			throws ClassNotFoundException, SQLException, InvalidFileFormatException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		;
		boolean uspjelo = false;
		int count = 0;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Timestamp ts = Timestamp.valueOf(dtf.format(now));

			stmt = conn
					.prepareStatement("update crm_nalozi\r\n" + " set user_name=?\r\n" + " where crm_nalozi.id=?\r\n");

			stmt.setString(1, trenutni);
			stmt.setLong(2, idNaloga);

			System.out.println(stmt);
			count = stmt.executeUpdate();
			uspjelo = (count > 0) ? true : false;

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
		}
		return uspjelo;
	}

	public void selectNeaktivneBrojeve() {
		listaBrojeva.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			stmt = conn.prepareStatement(
					"select BrojTelefona from dbo.vwProvisioning where vwProvisioning.StatusUgovora = 'Aktivan' and vwProvisioning.BrojTelefona is not null and vwProvisioning.PrivremeniStatusIskljucenja is null");

			System.out.println(stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				listaBrojeva.add(rs.getString("BrojTelefona"));

			}
			conn.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectBrojevekojihNemaUSobisu(List<String> brojevi) {
		pomLista.clear();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					ini.get("sobis", "sobis.url") + crta + crta + ini.get("sobis", "sobis.db"),
					ini.get("sobis", "sobis.user"), ini.get("sobis", "sobis.password"));

			for (int i = 0; i < brojevi.size(); i++) {
				if (!(brojevi.get(i).replaceAll("'", "") == null)
						|| !(brojevi.get(i).replaceAll("'", "").toLowerCase() == "null")) {
					stmt = conn.prepareStatement(
							"select vwProvisioning.BrojTelefona from dbo.vwProvisioning where vwProvisioning.BrojTelefona =? and vwProvisioning.StatusUgovora = 'Aktivan'");
					stmt.setString(1, brojevi.get(i));
					// System.out.println(stmt);
					rs = stmt.executeQuery();

					if (!rs.next()) {
						pomLista.add(brojevi.get(i).replace("'", ""));
						// System.out.println(brojTel);
						System.out.println("Redni broj: " + i);
					}
				}
			}

			conn.close();
			stmt.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAktivne(String userName) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		aktivniNalozi.clear();

		try {
			Ini ini = new Ini(new File(iniFile));
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(ini.get("erminesCRM", "erminesCRM.url"),
					ini.get("erminesCRM", "erminesCRM.user"), ini.get("erminesCRM", "erminesCRM.password"));

			stmt = conn.prepareStatement(
					"select crm_nalozi.customer_id,crm_nalozi.is_active,crm_nalozi.nalog_type as Poslednji_tip,crm_nalog_type.description as poslednjiNalog,tabela.nalog_type as 							nulti_tip,tabela.description as nulti\r\n"
							+ "from public.crm_nalozi\r\n"
							+ "left JOIN public.crm_nalog_type on crm_nalozi.nalog_type=crm_nalog_type.id\r\n"
							+ "left join \r\n"
							+ "(select nalog_type ,crm_nalog_type.description,crm_nalozi.customer_id\r\n"
							+ "from public.crm_nalozi\r\n"
							+ "left JOIN public.crm_nalog_type on crm_nalozi.nalog_type=crm_nalog_type.id\r\n"
							+ "where crm_nalozi.id \r\n" + "IN (SELECT min(crm_nalozi.id)\r\n" + "from\r\n"
							+ "public.crm_nalozi\r\n"
							+ "group by crm_nalozi.customer_id) and crm_nalozi.is_active=true) as tabela on crm_nalozi.customer_id =tabela.customer_id\r\n"
							+ "where crm_nalozi.id \r\n" + "IN (SELECT MAX(crm_nalozi.id)\r\n" + "from\r\n"
							+ "public.crm_nalozi\r\n"
							+ "group by crm_nalozi.customer_id) and crm_nalozi.is_active=true and crm_nalozi.user_name=? ORDER by crm_nalozi.customer_id");

			stmt.setString(1, userName);
			System.out.println("---------  " + stmt);
			rs = stmt.executeQuery();

			while (rs.next()) {
				pojoAktivni = new PojoAktivniNalozi();
				
				pojoAktivni.setAktivan(rs.getBoolean("is_active"));
				pojoAktivni.setKorisnikId(rs.getLong("customer_id"));
				pojoAktivni.setNazivNalogaNulti(rs.getString("nulti"));
				pojoAktivni.setNazivNalogaTrenutni(rs.getString("poslednjinalog"));
				pojoAktivni.setTipNalogaTrenutni(rs.getLong("poslednji_tip"));
				pojoAktivni.setTipNultog(rs.getLong("nulti_tip"));
				
				aktivniNalozi.add(pojoAktivni);
			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
			stmt.close();
			rs.close();
		}

	}

	public List<String> getListaKorId() {
		return listaKorId;
	}

	public List<String> getListaZbirni() {
		return listaZbirni;
	}

	public List<PojoDetaljiKor> getListaDetalja() {
		return listaDetalja;
	}

	public List<PojoOsnovniPodaci> getListaPodataka() {
		return listaPodataka;
	}

	public List<PojoOprema> getListaOpreme() {
		return listaOpreme;
	}

	public List<PojoUplateIFakture> getListaUplataFaktura() {
		return listaUplataFaktura;
	}

	public PojoLoyality getPojoLoyality() {
		return pojoLoyality;
	}

	public Map<Long, String> getMapaLoyality() {
		return mapaLoyality;
	}

	public List<PojoUplateFaktrureGrafik> getListgrafika() {
		return listgrafika;
	}

	public List<String> getListaBrojeva() {
		return listaBrojeva;
	}

	public List<String> getPomLista() {
		return pomLista;
	}

	public List<PojoAktivniNalozi> getAktivniNalozi() {
		return aktivniNalozi;
	}

	

}
