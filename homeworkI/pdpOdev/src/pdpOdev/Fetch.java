/**
*
* @author Sude Özkanoğlu - sude.ozkanoglu@ogr.sakarya.edu.tr
* @since 10 Mart 2024
* <p>
* Main.java class'ından gelen parametreler ile repo'yu dinamik bir şekilde klonlayan Fetch.java class'ı klonlama anında bir hata oluştuğunda 
* try-catch methoduyla kullanıcıya hata mesajını döndürmektedir.
* </p>
*/

package pdpOdev;

import java.io.File;

import org.eclipse.jgit.api.Git;

public class Fetch {
	
	private String repositoryURL;
	private File file;
	private File[] allFiles;
	
	public void cloneRepository()
	{
		try {
			Git result = Git.cloneRepository().setURI(repositoryURL).setDirectory(file).call();
			System.out.println("Repo Klonlandi : " + result.getRepository().getDirectory());
			
		}catch(Exception e){
			System.out.println("Hata Var !" + e.getMessage());
		}
		
		allFiles = file.listFiles();
	}
	
	public void setFile(File f)
	{
		this.file = f;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public File[] getFiles()
	{
		return allFiles;
	}
	
	public void setUrl(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
}

