package com.Tuzbe.CallService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

@Component
public class Util {

	public boolean upisIzvjestajaSaTerena(Long korisnikId, Long nalogId, String sadrzaj) throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;
		Path path = Paths.get("/opt/apache-tomcat-8.0.41/webapps/crm_files/" + korisnikId + "/" + nalogId);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		try {

			File file = new File(path + "/" + Long.toString(nalogId) + ".txt");

			if (file.createNewFile()) {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write(sadrzaj);
				bw.close();
				fw.close();
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}// kraj UpisIzvjestajaSaTerena

	@SuppressWarnings("deprecation")
	public boolean dosijeX(List<String> listaFaktura, String nalogId, Long korisnikId) throws IOException {
		

			try {
				Path path2 = Paths.get("/opt/apache-tomcat-8.0.41/webapps/crm_files/" + korisnikId + "/" + nalogId);
				PDFMergerUtility PDFmerger = new PDFMergerUtility();
				PDFmerger.setDestinationFileName(path2.toString() + "/" + nalogId + ".pdf");
				File fajl;
				URL url;
				InputStream in = null;

				//pokupi sve
				for (int i = 0; i < listaFaktura.size(); i++) {
					try {
						url = new URL(
								"http://192.168.10.21/ReportServer/Pages/ReportViewer.aspx?/iTisReports/Zbirna%20Faktura%20sa%20logom&rs:Command=Render&rs:Format=PDF&rc:Parameters=false&FakturaId="
										+ listaFaktura.get(i).toString());
						in = url.openStream();
						Files.copy(in, Paths.get(path2 + "/" + listaFaktura.get(i).toString() + ".pdf"),
								StandardCopyOption.REPLACE_EXISTING);
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				url = new URL("http://192.168.0.94:82/" + korisnikId + ".pdf");
				in = url.openStream();
				Files.copy(in, Paths.get(path2 + "/" + korisnikId + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
				in.close();
				//prikupio sve
				
				
				//sastavi sve
				PDFmerger.addSource(path2 + "/" + korisnikId + ".pdf");
				for (int i = 0; i < listaFaktura.size(); i++) {
					try {
						PDFmerger.addSource(path2 + "/" + listaFaktura.get(i).toString()+".pdf");
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}

				PDFmerger.mergeDocuments();
				//sastavio sve
				
				
				//brisi sve

				for (int i = 0; i < listaFaktura.size(); i++) {
					try {
						fajl = new File(path2 + "/" + listaFaktura.get(i).toString()+".pdf");
						System.out.println(path2+"/"+listaFaktura.get(i).toString());
						fajl.delete();
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				fajl = new File(path2 + "/" + korisnikId + ".pdf");
				fajl.delete();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			//obrisao sve

		} // kraj Dosije: Korisnik
	
		public void napraviZahtjev() throws IOException {
			Path path = Paths.get("/opt/apache-tomcat-8.0.41/webapps/crm_files/test/");
			Files.createDirectories(path);
			XWPFDocument document = new XWPFDocument(); 
			FileOutputStream out = new FileOutputStream(new File("/opt/apache-tomcat-8.0.41/webapps/crm_files/test/test.docx"));
			
			XWPFParagraph paragraph = document.createParagraph();
			
			
			paragraph.setAlignment(ParagraphAlignment.BOTH);
			XWPFRun run1 = paragraph.createRun();
			run1.setFontFamily("Times New Roman");
			run1.setFontSize(12);
		      run1.setText("BOSNA I HERCEGOVINA \n" + 
		      			"REPUBLIKA SRPSKA \n" + 
		      			"OSNOVNI SUD U BIJELJINI \n \n \n");
		      
		      XWPFRun run2 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run2 = paragraph.createRun();
		      run2.setFontFamily("Times New Roman");
		      run2.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run2.setText("Tužilac : \" Telrad Net\" doo Bijeljina, koga zastupa Damir Zečević \n" +
		    		  	  "Tuženi : Boris Lipovac iz Bijeljine, ulica Dušana Radovića-V 30 \n \n ");
		      		      
		     
		      XWPFRun run3 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run3 = paragraph.createRun();
		      run3.setFontFamily("Times New Roman");
		      run3.setFontSize(12);
		      run3.setItalic(true);
		      run3.setBold(true);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run3.setText("Radi : naplate dospjelog potraživanja \n");
		     
		      
		      
		      XWPFRun run4 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run4 = paragraph.createRun();
		      run4.setFontFamily("Times New Roman");
		      run4.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run4.setText("Stvarna nadležnost suda propisana je po članu 27 ZPP-a ( \"Službeni Glasnik RS\", broj 58/03,85/03,74/05,63/07,49/09,61/13) i članu 30. Zakona o sudovima RS ( \"Službeni Glasnik RS\" , broj 37/12,14/14,44/15,39/16, 100/17) .\r\n" + 
		      		"Mjesna nadležnost po članu 28 i 29 ZPP-a ( \"Službeni Glasnik RS\" broj: 58/03,85/03,74/05,63/07,49/09,61/13) \r\n" + 
		      		"U skladu sa članom 190.ZOO te članu 55. ZPP \n");

		      
		      XWPFRun run5 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run5 = paragraph.createRun();
		      run5.setFontFamily("Times New Roman");
		      run5.setFontSize(12);
		      run5.setBold(true);
		      paragraph.setAlignment(ParagraphAlignment.CENTER);
		      run5.setText("T U Ž B A");
		      
		      XWPFRun run6 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run6 = paragraph.createRun();
		      run6.setFontFamily("Times New Roman");
		      run6.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run6.setText("Tužilac i tuženi su dana 28.04.2018.godine ,pod rednim brojem protokola 86902 , potpisali Ugovor o zasnivanju pretplatničkog odnosa na ime Boris Lipovac , za Analognu kablovsku televiziju, čija mjesečna pretplata iznosi 14,00KM ( četrnaestkonvertibilnihmaraka) na neodređen rok trajanja, pod rednim brojm protokola 86903 - Ugovor o zasnivanju pretplatičkog odnosa- usluga Kablovski internet- STRAT -sa ugovornom obavezom u trajanju od 12 mjeseci sa mjesečnom pretplatom od 25.74KM ( dvadesetpetkonvertibilnimmaraka 74/100) i pod rednim brojem protokola 86904 - Tarifni paket S plus gde je zasnovana Ugovorna obaveza na period od 24.mjeseca u iznosu od 17.55 KM (sedamnaestkonvertibilnihmaraka 55/100) \n");
		      
		      
		      XWPFRun run7 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run7 = paragraph.createRun();
		      run7.setFontFamily("Times New Roman");
		      run7.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run7.setText("DOKAZ: predmetni ugovori \n");
		      
		      
		      XWPFRun run8 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run8 = paragraph.createRun();
		      run8.setFontFamily("Times New Roman");
		      run8.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run8.setText("Takođe neminovno je navesti i činjenicu da je 24.05.2106.godine Tuženi potpisao i Revers 4256/17 na ime 1180 MODEM EPC3208 docsis 3.0 u vrijednosti od 170,00KM ( stosedamdesetkonvertibilnihmaraka) \n");
		      
		      XWPFRun run9 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run9 = paragraph.createRun();
		      run9.setFontFamily("Times New Roman");
		      run9.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run9.setText("DOKAZ: fotokopija predmetnog reversa \n");
		      
		      XWPFRun run10 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run10 = paragraph.createRun();
		      run10.setFontFamily("Times New Roman");
		      run10.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run10.setText("U skladu sa predmetnim ugovorima Tužilac je ispostavio mjesečne fakture Tuženom u periodu od 26.01.2018 \n");
		      
		      XWPFRun run11 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run11 = paragraph.createRun();
		      run11.setFontFamily("Times New Roman");
		      run11.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run11.setText("DOKAZ: mjesečne fakture \n");
		      
		      XWPFRun run12 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run12 = paragraph.createRun();
		      run12.setFontFamily("Times New Roman");
		      run12.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run12.setText("Dana 29.11.2016.godine Tuženi potpisuje sa Tužiocem Reprogram - tkz novi Ugovor pod brojem protokola 658224, kojim se Tuženi obavezuje sa će izmiriti dugovanje po osnovu osnovnog Ugovora koje iznosi 328,49KM (tristotinedvadesetosamkonvertibilnihmaraka 49/100) i to u tri mjesečne rate počevši od 29.11.2016.godine, u mjesečnom iznosu od 109,75KM (stodevetkonvertibilnihmaraka 75/100) i da će redovno izmirivati dospjele obaveze po osnovu redovnog pružanja usluga. \n");
		      
		      XWPFRun run13 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run13 = paragraph.createRun();
		      run13.setFontFamily("Times New Roman");
		      run13.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run13.setText("DOKAZ: Reprogram (Ugovor pod brojem protokola 658224) \n");
		      
		      
		      XWPFRun run14 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run14 = paragraph.createRun();
		      run14.setFontFamily("Times New Roman");
		      run14.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run14.setText("Na osnovnu navedenih faktura, Tuženi je na ime predmetnog duga uplatio iznos od 124,00 KM, preostalo dugovanje iznosi 254,23 KM. \n");
		      
		      XWPFRun run15 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run15 = paragraph.createRun();
		      run15.setFontFamily("Times New Roman");
		      run15.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run15.setText("DOKAZ: Izvještaj o promjenama na stanju računa na dan 15.01.2018, analitička katrica . \n");
		      
		      XWPFRun run16 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run16 = paragraph.createRun();
		      run16.setFontFamily("Times New Roman");
		      run16.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run16.setText("Međutim iz gore navedenih faktura i iz analitičke kartice se jasno vidi da je tuženi uplatio dio svoga predmetnog dugovanja te samo djelimično ispunio svoju ugovornu obavezu. \n");
		      
		      
		      XWPFRun run17 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run17 = paragraph.createRun();
		      run17.setFontFamily("Times New Roman");
		      run17.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run17.setText("Tužilac je u više navrata pokušao da postigne neki vid sporazuma sa tuženim, ali s obzirom da nije bilo neke pozitivne reakcije od strane tuženoga, neophodno je bilo pokrenuti sudski postupak radi izmirenja preostalog potraživanja.  \n");
		      
		      XWPFRun run18 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run18 = paragraph.createRun();
		      run18.setFontFamily("Times New Roman");
		      run18.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.LEFT);
		      run18.setText("DOKAZ: Opomena pred tužbu \n");
		      
		      XWPFRun run19 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run19 = paragraph.createRun();
		      run19.setFontFamily("Times New Roman");
		      run19.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run19.setText("Tužilac predlaže da sud u slučaju nedostavljanja odgovora na tužbu, donese Presudu zbog propuštanja u skladu sa članom 182. ZPP-a. \n");
		    
		      XWPFRun run20 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run20 = paragraph.createRun();
		      run20.setFontFamily("Times New Roman");
		      run20.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run20.setText("Obzirom da tuženi neće dobrovoljno da izvrši svoju obavezu, tužilac predlaže da sud nakon sprovedenog postupka donese slijedeću:  \n");
		      
		      XWPFRun run21 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run21 = paragraph.createRun();
		      run21.setFontFamily("Times New Roman");
		      run21.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.CENTER);
		      run21.setText("P R E S U D U \n");
		      
		      XWPFRun run22 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run22 = paragraph.createRun();
		      run22.setFontFamily("Times New Roman");
		      run22.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run22.setText("Usvaja se tužbeni zahtjev tužioca. Obavezuje se tuženi Boris Lipovac da tužiocu \" Telrad Net\" doo Bijeljina na ime glavnog duga isplati iznos od 321.29 KM sa zakonskom zateznom kamatom počevši od 21.01.2018 pa do konačne isplate, vrati predmetnu opremu ili isplati vrijednost iste, kao i da tužiocu nadoknadi troškove parničnog postupka , sve u roku od 15 dana od dana pravosnažnosti presude, pod prijetnjom prinudnog izvršenja. \n");
		      
		      
		      XWPFRun run23 = paragraph.createRun();
		      paragraph = document.createParagraph();
		      run23 = paragraph.createRun();
		      run23.setFontFamily("Times New Roman");
		      run23.setFontSize(12);
		      paragraph.setAlignment(ParagraphAlignment.BOTH);
		      run23.setText("Tužilac \r\n" + 
		      		"	____________________\r\n" + 
		      		"\"Telrad Net\" doo Bijeljina\n");
		      
		    
		      document.write(out);
		      out.close();
		      document.close();
			
		}
		}
