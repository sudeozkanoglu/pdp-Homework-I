/**
*
* @author Sude Özkanoğlu - sude.ozkanoglu@ogr.sakarya.edu.tr
* @since 10 Mart 2024
* <p>
* ProcessClasses.java class'ı klonlanan repo içerisindeki .java uzantılı classlara ait olan 'sınıf adını' ve bu sınıfla ilgili ödev isterlerini 
* (Java Doc - Yorum Satır Sayısı - Kod Satır Sayısı - LOC - Fonksiyon Sayısı - Yorum Sapma Yüzdesi) hesaplamaktadır.
* </p>
*/

package pdpOdev;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProcessClasses {
	
	private int counter = 0;
	private int javaDocCounter = 0;
	private int multipleCommentCounter = 0;
	private int singleCommentCounter = 0;
	private int whiteSpaceCounter = 0;
	private int methodCounter = 0;
	private int singleOuterCommentCounter = 0;
	
	private boolean javaDocCond = false;
    private boolean multipleCommentCond = false;
	
    private final String methodRegex = "(public|protected|private)\\s+(static\\s+)?(void|[\\w\\<\\>\\[\\],\\s]+)\\s+(\\w+)\\s*\\([^\\)]*\\)\\s*(\\{?|[^;])";
	private final String constructorRegex = "^\\s*public\\s+([a-zA-Z_]\\w*)\\s*\\(([^)]*)\\)\\s*\\{?";
	private final String classRegex ="public\\s+class\\s+([a-zA-Z_$][a-zA-Z\\d_$]*)";
	
	public void readClass(String path, String className)
	{
		Pattern pattern = Pattern.compile(methodRegex);
        Pattern patternConstructor = Pattern.compile(constructorRegex);
        
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path)))
		{
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
            	checkLines(pattern,patternConstructor, line);
            }
        } catch (IOException e)
		{
            e.printStackTrace();
            
        }
		printResult(className);
	}
	
	public void printResult(String className)
	{
		System.out.println("-----------------------------");
		System.out.println("Sınıf: " + className);
    	System.out.println("JavaDoc: " + javaDocCounter );
    	System.out.println("Yorum Satır Sayısı: " + (multipleCommentCounter + singleCommentCounter));
    	System.out.println("Kod Satır Sayısı: " + (counter - whiteSpaceCounter - singleOuterCommentCounter - javaDocCounter - multipleCommentCounter));
    	System.out.println("LOC: " + counter);
    	System.out.println("Method Sayısı: " + methodCounter);
    	System.out.println("Yorum Sapma Yüzdesi: " + "%" + String.format("%.2f",calculateCommentRate()));
    	
    	counter = 0;
    	javaDocCounter = 0;
    	multipleCommentCounter = 0;
    	whiteSpaceCounter = 0;
    	methodCounter = 0;
    	singleCommentCounter = 0;
    	singleOuterCommentCounter = 0;
	}
	
	public void checkLines(Pattern pattern, Pattern constructorPattern, String line)
	{
		
		Matcher matcher = pattern.matcher(line);
		Matcher constructorMatcher = constructorPattern.matcher(line);
		
		//Yapıcı method bulma
		if(constructorMatcher.find()  && !matcher.find())
		{
			methodCounter++;
		}
		
        if(matcher.find())
        {
        	methodCounter++;
        }
    	
    	// Boş satır sayısı hesabı
    	if(line.isBlank() == true)
    	{
    		whiteSpaceCounter++;
    	}
    	
    	// Tekli yorum satırı sayısı
    	if(line.trim().startsWith("//"))
    	{	
    		singleCommentCounter++;
    		singleOuterCommentCounter++;
    	}
    	
    	if(line.trim().contains("//") && !line.trim().startsWith("//"))
    		singleCommentCounter++;
    		
    	// Çoklu yorum satırı sayısı
    	if(line.trim().startsWith("/*") && !line.trim().startsWith("/**"))
    	{
    		multipleCommentCond = true;
    	}
    	
    	if(multipleCommentCond && !line.trim().startsWith("/*") && !line.trim().startsWith("*/"))
    	{
    		multipleCommentCounter++;
    	}
    	
    	if(multipleCommentCond && line.trim().startsWith("*/"))
    	{
    		multipleCommentCond = false;
    		singleOuterCommentCounter += 2;
    	}
    	
    	// Javadoc yorum satırı sayısı
    	if(line.trim().startsWith("/**"))
    	{
    		javaDocCond = true;
    	}
    	
    	if(javaDocCond && !line.trim().startsWith("/**") && !line.trim().startsWith("*/"))
    	{
    		javaDocCounter++;
    	}
    	
    	if(javaDocCond && line.trim().startsWith("*/"))
    	{
    		javaDocCond = false;
    		singleOuterCommentCounter += 2;
    	}

    	//LOC sayısı
    	counter++;
	}
	
	public double calculateCommentRate()
	{
		double yg = javaDocCounter + multipleCommentCounter + singleCommentCounter;
		yg *= 8;
		yg /= 10;
		yg /= methodCounter;
		
		double yh = counter - whiteSpaceCounter - singleOuterCommentCounter - javaDocCounter - multipleCommentCounter;
		yh /= methodCounter;
		yh *= 3;
		yh /= 10;
		
		double ans = ((100 * yg) / yh)-100;
		
		return ans;
	}
	
	public void getAllJavaFiles(File path)	
	{
	    File[] allFiles = path.listFiles();
	    
	    for(int i = 0; i < allFiles.length; i++)
	    {
	    	if (allFiles[i].isDirectory())
	        {
	        	getAllJavaFiles(allFiles[i]);
	        } 
	        else if (allFiles[i].getName().endsWith(".java") && allFiles[i].isFile())
	        {
	        	Pattern pattern = Pattern.compile(classRegex);
	        	int lastSlashIndex = allFiles[i].getAbsolutePath().lastIndexOf("\\");
	            String baseSubstring = allFiles[i].getAbsolutePath().substring(lastSlashIndex + 1);
	            boolean isClass = false;
	            
	            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(allFiles[i].getAbsolutePath())))
	    		{
	                String line;
	                while ((line = bufferedReader.readLine()) != null && !isClass)
	                {
	                	Matcher matcher = pattern.matcher(line);
	                	if(matcher.find())
	                		isClass = true;
	                }
	            } catch (IOException e)
	    		{
	            	System.out.println("File Kontrol Kisminda Hata Var! " + e.getMessage());
	            }
	            
	            if(isClass)
	            {
	            	readClass(allFiles[i].getAbsolutePath(), baseSubstring);
	            }
	        }
	    }
    }
}
