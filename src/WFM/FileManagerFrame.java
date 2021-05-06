package WFM;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class FileManagerFrame extends JInternalFrame
{
	// Specifies whether or not to display simple or detailed
	private boolean fancy;
	
	private JSplitPane splitpane;
	private FileTree fileTree;
	private JTree dirTree;
	private JTree fTree;
	private DefaultMutableTreeNode fileRoot;
	public FileManagerFrame(String root)
	{
		// Simple
		fancy = false;
		
		fileTree = new FileTree(root);
		
		dirTree = buildTree(fileTree.getRoot());
		dirTree.addTreeSelectionListener(new DirTreeSL());
		
		// Build new fileTree w/ DMTN
		fileRoot = buildFileNodes(fileTree.getRoot());
		fTree = buildTree(fileRoot);
		fTree.addTreeSelectionListener(new FileTreeSL());
		fTree.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirPanel(dirTree), new FilePanel(fTree));
		splitpane.revalidate();
		
		this.setSize(400, 400);
		this.setVisible(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setTitle(root);
		this.setIconifiable(true);
		
		// Adding SplitPane
		this.getContentPane().add(splitpane);
	}
	
	public void simpleFormat()
	{
		if (fancy)
		{
			// Rebuild the tree with "FancyFileNodes" with a diff toString overloading
			fileRoot = toSimple(fileRoot);
			fancy = false;
			updateFileTree();
		}
	}
	
	public void detailedFormat()
	{
		if (!fancy)
		{
			// Rebuild the tree with "FancyFileNodes" with a diff toString overloading
			fileRoot = toDetailed(fileRoot);
			fancy = true;
			updateFileTree();
		}
		
	}
	
	private DefaultMutableTreeNode toSimple(DefaultMutableTreeNode root)
	{
		DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(new FileNode((((FileNode) root.getUserObject())).getFile()));
		
		for (int i = 0; i < root.getChildCount(); i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
			newRoot.add(new DefaultMutableTreeNode(new FileNode((FancyFileNode) child.getUserObject())));		
		}
		
		return newRoot;
	}
	
	private DefaultMutableTreeNode toDetailed(DefaultMutableTreeNode root)
	{
		DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(new FileNode((((FileNode) root.getUserObject())).getFile()));
		
		for (int i = 0; i < root.getChildCount(); i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
			newRoot.add(new DefaultMutableTreeNode(new FancyFileNode((FileNode) child.getUserObject())));
		}
		
		return newRoot;
	}
	
	private FileManagerFrame getThis()
	{
		return this;
	}
	
	private JTree buildTree(DefaultMutableTreeNode root)
	{
		JTree tree = new JTree();

		DefaultTreeModel treemodel = new DefaultTreeModel(root);
		tree.setModel(treemodel);
		
		return tree;
	}
	
	private DefaultMutableTreeNode buildFileNodes(DefaultMutableTreeNode leaf)
	{
		File f = ((FileNode) leaf.getUserObject()).getFile();
		
		DefaultMutableTreeNode file = new DefaultMutableTreeNode(new FileNode(f));
		
		// Add all file children under the selected node
		ArrayList<FileNode> lst = ((FileNode) leaf.getUserObject()).getChildren();
		for (int i = 0; i < lst.size(); i++)
		{
			DefaultMutableTreeNode temp = new DefaultMutableTreeNode(lst.get(i));
			file.add(temp);
		}
		
		return file;
	}
	
	
	private void updateFileTree()
	{
		
		DefaultTreeModel treemodel = new DefaultTreeModel(fileRoot);
		fTree.setModel(treemodel);
		fTree.repaint();
	}
	
	private class DirTreeSL implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			// Sometimes when I close a top directory that has some sub directories under it, it returns null for 
			// tree.getLastSelec..., doesn't break program yet... probably should fix
			
			// Returns the object stored in the selected node
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
			
			// If any nodes under the selected node are not explored
			if (node != null)
			{
				FileNode temp = (FileNode) node.getUserObject();
				if (!temp.isExplored())
				{				
					for (Enumeration<TreeNode> enumer = node.children(); enumer.hasMoreElements();)
					{
						DefaultMutableTreeNode n = (DefaultMutableTreeNode) enumer.nextElement();
						fileTree.DFS(n);
					}
					
					temp.setExplored();
					
					dirTree.repaint();
				}
				
				// Display the fileChildren of the selected node in the FilePanel
				
				// Should improve this to store the tree in the FileNode object, but for now, this will work 
				// (And may be better for memory to only store one tree of fileChildren at a time)
				
				fileRoot = buildFileNodes(node);
				
				if (fancy)
				{
					fancy = false;
					detailedFormat();
				}
				else
				{
					updateFileTree();
				}
				
				// Update the title of the FileManagerFrame
				getThis().setTitle(((FileNode) node.getUserObject()).getFile().getAbsolutePath());
			}
		}
	}
	
	private class FileTreeSL implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			// Should allow for executions
			Desktop desktop = Desktop.getDesktop();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fTree.getLastSelectedPathComponent();
			FileNode fNode = (FileNode) node.getUserObject();
			File file = fNode.getFile();
			if (file.canExecute() && file.canRead() && file.canWrite())
			{
				try
				{
					desktop.open(file);
				}
				catch (IOException ex)
				{
					System.out.println(ex.toString());
				}				
			}
		}
	}
}
