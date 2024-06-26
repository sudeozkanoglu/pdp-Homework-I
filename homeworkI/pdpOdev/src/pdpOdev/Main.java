/**
*
* @author Sude Özkanoğlu - sude.ozkanoglu@ogr.sakarya.edu.tr
* @since 10 Mart 2024
* <p>
* Kullanıcının girmiş olduğu GitHub repository linkinin kurallara uygun bir link olup olmadığını kontrol eden ve 
* uygunsa bu repo'yu klonlayan sınıfa gerekli parametreleri yollayan Main.java class'ı eğer link kurallara uygun değilse try-catch methoduyla
* kullanıcıya hata vermektedir.
* </p>
*/

package pdpOdev;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {

	public static void main(String[] args) throws Exception {
		
		try {
    		
    		Scanner in = new Scanner(System.in);
    		String regex = "((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?";
    		System.out.println("GitHub Repo Linkini Giriniz:");
    		String cloneURL = in.next();
    		Pattern ifade = Pattern.compile(regex);
    		Matcher control = ifade.matcher(cloneURL);
    		
    		if(control.find())
    		{
    			System.out.println("Erisim Onaylandi - Repo Linkiniz Klonlaniyor...");
    			String localPath = System.getProperty("user.dir") + "/klonlanan_Repo" + "/" + (int) (Math.random() * 10000);
    	        
    			Fetch fetch = new Fetch();  
    	        ProcessClasses process = new ProcessClasses();
    			fetch.setFile(new File(localPath));
    			fetch.setUrl(cloneURL);
    	        fetch.cloneRepository();
    	        process.getAllJavaFiles(fetch.getFile());
    			
    		}else {
    			System.out.println("Repo Linkiniz Erisilebilir Degil !");
    		}
    		in.close();
    	}catch (Exception e) {
    		throw new Exception("Repo Linkiniz Kurallara Uygun Degil !" + e.getMessage());
    	}
	}

}