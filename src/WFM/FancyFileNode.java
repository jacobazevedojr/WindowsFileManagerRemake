package WFM;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class FancyFileNode extends FileNode {
	
	FancyFileNode(FileNode fn)
	{
		super(fn.getFile());
	}
	
	@Override
	public String toString()
	{
		File f = this.getFile();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat dformat = new DecimalFormat("#,###");
		
		String name = this.getFile().getName();
		String date = formatter.format(f.lastModified());
		String size = dformat.format(f.length());
		
		return String.format("%-30s  %10s  %15s bytes", name, date, size);
	}
}
