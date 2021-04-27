package WFM;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FileTree {
	
	FileTree(String path)
	{
		File file = new File(path);
		File[] files;
		files = file.listFiles();
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dformat = new DecimalFormat("#,###");
		for (File f : files)
		{
			// For initializing the FileTree
			System.out.println(f.getAbsolutePath() 
					+ " Date: " + formatter.format(f.lastModified()) + " Size: " + dformat.format(f.length()) + " bytes");
		}
	}
}
