package WFM;

import java.io.File;
import java.util.ArrayList;

public class FileNode {
	private File file;
	private ArrayList<FileNode> fileChildren;
	FileNode(File f)
	{
		file = f;
		fileChildren = new ArrayList<FileNode>();
	}
	
	public File getFile()
	{
		return file;
	}
	
	public ArrayList<FileNode> getChildren()
	{
		return fileChildren;
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
	
	public boolean isDirectory()
	{
		return file.isDirectory();
	}
}
