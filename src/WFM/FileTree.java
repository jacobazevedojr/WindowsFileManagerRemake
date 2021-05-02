package WFM;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTree{
	private DefaultMutableTreeNode root;
	String rootPath;
	
	FileTree(String path)
	{
		rootPath = path;
		root = new DefaultMutableTreeNode(new FileNode(new File(path)));
		
		// Append the required children to root
		DFS(root);
	}
	
	public DefaultMutableTreeNode getRoot()
	{
		return root;
	}
	
	// Depth first search w/ depth of 1. Populates the given node with children at most 2 subdirects deep
	// Ex: Root -> sub -> sub2
	public void DFS(DefaultMutableTreeNode leaf)
	{
		// Assuming the node stores Strings
		//String path = leaf.getUserObject();
		
		FileNode node = (FileNode) (leaf.getUserObject());
		File file = new File(node.getFile().getAbsolutePath());
		File[] files;
		files = file.listFiles();
		
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		//DecimalFormat dformat = new DecimalFormat("#,###");
		
		for (File f : files)
		{
			File[] tempFileList = f.listFiles();
			if(f.isDirectory() && f.canRead() && f.getName().charAt(0) != '$' && !f.isHidden() && tempFileList != null)
			{
				// Should create an object with the file contents here
				// NEED TO ADJUST HERE
				DefaultMutableTreeNode temp = new DefaultMutableTreeNode(new FileNode(f));
				leaf.add(temp);
				
				// Explore the temp nodes subdirectory(s)
				// Add them as children
				
				
				for (File fTemp : tempFileList)
				{
					// Only appends directories to the file tree
					if (fTemp.isDirectory() && fTemp.canRead() && fTemp.getName().charAt(0) != '$' && !fTemp.isHidden() && fTemp.listFiles() != null)
					{
						temp.add(new DefaultMutableTreeNode(new FileNode(fTemp)));
					}
					// File is not a directory, add to the temp Nodes list of children
					else if (fTemp.isFile() && fTemp.canExecute() && !fTemp.isHidden() && fTemp.canWrite() && fTemp.canRead())
					{
						System.out.println("Yes " + node.toString() + ": " + fTemp.getName());
						node.getChildren().add(new FileNode(fTemp));
					}
				}
			}
			else
			{
				
			}
			// For initializing the FileTree
			//System.out.println(f.getAbsolutePath() 
			//		+ " Date: " + formatter.format(f.lastModified()) + " Size: " + dformat.format(f.length()) + " bytes");
		}
	}
}
