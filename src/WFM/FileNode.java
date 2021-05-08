package WFM;

import java.io.File;
import java.util.ArrayList;

public class FileNode {
	private File file;
	private boolean explored;
	FileNode(File f)
	{
		file = f;
		explored = false;
	}
	
	FileNode(FancyFileNode f)
	{
		file = f.getFile();
		explored = false;
	}
	
	public File getFile()
	{
		return file;
	}
	
	@Override
	public String toString()
	{
		if (file.getName().equals(""))
		{
			return file.getPath();
		}
		
		return file.getName();
	}
	
	public void setExplored()
	{
		explored = true;
	}
	
	public boolean isExplored()
	{
		return explored;
	}
	
	public boolean isDirectory()
	{
		return file.isDirectory();
	}
}
