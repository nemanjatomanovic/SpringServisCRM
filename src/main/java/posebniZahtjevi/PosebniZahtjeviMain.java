package posebniZahtjevi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.Tuzbe.CallService.UpitDb;

public class PosebniZahtjeviMain {

	
	public static void main(String[] args) throws IOException {
//		GetSubscriber getSub = new GetSubscriber();
		UpitDb upit = new UpitDb();
//		upit.selectNeaktivneBrojeve();
		List<String>listaBrojeva = new ArrayList<>();
		//List<String>
		int brojac = 0;
//		for (int i = 0; i < upit.getListaBrojeva().size(); i++) {
//			if (!upit.getListaBrojeva().get(i).startsWith("6")) {
//				if (getSub.provjera(upit.getListaBrojeva().get(i).substring(2), upit.getListaBrojeva().get(i).substring(0, 2))==false) {
//					brojac ++;
//					System.out.println("Nema na centrail " + upit.getListaBrojeva().get(i).trim()+" " + brojac);
//				}
//			}
//			
//		}
		
		String nizBrojeva="";
		String fileName = "C:\\Users\\Nemanja\\Desktop\\telefoni.dat";
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			
			String line;
			while ((line = br.readLine()) != null) {
				String[] pomNiz = line.split(",");
				//System.out.println(pomNiz[12].replaceAll("'", ""));
				if (pomNiz[12].replaceAll("'", "").trim().length()<8) {
					listaBrojeva.add("'55"+pomNiz[12].trim().replace("'", "")+"'");
					//upit.selectBrojevekojihNemaUSobisu("'55"+pomNiz[12].trim().replace("'", "")+"'");
				}
				else {
				
					listaBrojeva.add(pomNiz[12].trim());
					//upit.selectBrojevekojihNemaUSobisu(pomNiz[12].trim().replace("'", ""));
				}
//				
				brojac++;
				System.out.println(brojac);
//				if (pomNiz[12].replaceAll("'", "").trim().length()<8) {
//					nizBrojeva = nizBrojeva+"'55"+pomNiz[12].substring(1, pomNiz[12].length())+",";
//				}else {
//					nizBrojeva = nizBrojeva+ pomNiz[12]+",";
//				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Faza prikupljanja zavrsena.");
		System.out.println("Pocinje faza upita");
		
		upit.selectBrojevekojihNemaUSobisu(listaBrojeva);
		
		for (int i = 0; i < upit.getPomLista().size(); i++) {
			System.out.println(upit.getPomLista().get(i));
		}
//		upit.selectBrojevekojihNemaUSobisu(nizBrojeva.substring(0,nizBrojeva.length()-1));
//		System.out.println(upit.getPomLista().size());

	}
}
