package WFM;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


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
		// Single and double click events
		fTree.addMouseListener(new MouseAdapter() 
		{
		    public void mouseClicked(MouseEvent e) {
		    	
		        int selRow = fTree.getClosestRowForLocation(e.getX(), e.getY());
		        
    			Desktop desktop = Desktop.getDesktop();
    			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fTree.getLastSelectedPathComponent();
    			
    			if (node == null)
    			{
    				fTree.setSelectionRow(selRow);
    				// Right-click does not select
    				node = (DefaultMutableTreeNode) fTree.getLastSelectedPathComponent();
    			}
    			
    			
    			FileNode fNode = (FileNode) node.getUserObject();
    			File file = fNode.getFile();

		        if(selRow != -1) {
		            if(e.getClickCount() == 1 && !SwingUtilities.isRightMouseButton(e)) {
						App.setLastSelected(node);
						// So we know what FileManagerFrame to update
						App.setSelectedFrame(getThis());
		            }
		            else if(e.getClickCount() == 2 && !SwingUtilities.isRightMouseButton(e)) {
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
		            else if(SwingUtilities.isRightMouseButton(e))
		            {
						App.setLastSelected(node);
						// So we know what FileManagerFrame to update
						App.setSelectedFrame(getThis());
						
		            	JPopupMenu popUp = new JPopupMenu();
		            	
		        		JMenuItem rename = new JMenuItem("Rename");
		        		JMenuItem copy = new JMenuItem("Copy");
		        		JMenuItem paste = new JMenuItem("Paste");
		        		JMenuItem delete = new JMenuItem("Delete");
		        		
		        		rename.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								App.rename();
							}
		        			});
		        		
		        		copy.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								App.setCopied((DefaultMutableTreeNode) fTree.getLastSelectedPathComponent());
							}
		        			});
		        		
		        		paste.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								DefaultMutableTreeNode node = (DefaultMutableTreeNode) fTree.getLastSelectedPathComponent();
								String location = ((FileNode) ((DefaultMutableTreeNode) node.getParent()).getUserObject()).getFile().getPath();
								App.paste(location);
							}
		        			});
		        		
		        		delete.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								App.delete();
							}
		        			});
		        		
		        		popUp.add(rename);
		        		popUp.add(copy);
		        		popUp.add(paste);
		        		popUp.add(delete);
		        		
		            	popUp.show(e.getComponent(), e.getX(), e.getY());
		            	popUp.setVisible(true);
		        		}
		            
		        }
		    }
		});
		
		fTree.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		fTree.setToggleClickCount(2);
		
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
		
		File[] lst = f.listFiles();
		for (int i = 0; i < lst.length; i++)
		{
			File fi = lst[i];
			if (fi.canRead() && fi.getName().charAt(0) != '$' && !fi.isHidden() && fi.canExecute())
			{
				DefaultMutableTreeNode temp = new DefaultMutableTreeNode(new FileNode(lst[i]));
				file.add(temp);
			}
		}
		
		return file;
	}
	
	
	private void updateFileTree()
	{
		DefaultTreeModel treemodel = new DefaultTreeModel(fileRoot);
		fTree.setModel(treemodel);
		fTree.repaint();
	}
	
	public void updateAfterNodeChanges(DefaultMutableTreeNode nd, DefaultMutableTreeNode newNode, String op)
	{
		App.setLastSelected(null);
		File ls = ((FileNode) nd.getUserObject()).getFile();
		if (ls.isDirectory())
		{
			// Need to update the FileTree and
			if (op == "Delete")
			{
				// Check if directory no longer exists
				if (!ls.exists())
				{				
					// For fTree
					DefaultTreeModel model = (DefaultTreeModel) fTree.getModel();
					nd.removeFromParent();
					model.reload();
					
					if (fancy)
					{
						fancy = false;
						detailedFormat();
					}
					else
					{
						fTree.repaint();
					}
					
					// For dirTree
					model = (DefaultTreeModel) dirTree.getModel();
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
					
					TreePath dirNodePath = null;
					String s = nd.toString();
					
				    Enumeration<TreeNode> e = root.depthFirstEnumeration();
				    while (e.hasMoreElements()) {
				        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				        if (node.toString().equalsIgnoreCase(s)) {
				            dirNodePath = new TreePath(node.getPath());
				            break;
				        }
				    }
				    
				    
				    dirTree.setSelectionPath(dirNodePath);
					DefaultMutableTreeNode dirNode = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();
					model.removeNodeFromParent(dirNode);
					model.reload();
					dirTree.repaint();
				}
				// Do nothing if it still exists
			}
			else if(op == "Rename")
			{
				// Delete old node, add new one
			}
			else
			{
				// Rename or Copy
				// Need to update fTree and dirTree
				// For fTree
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) nd.getParent();
				fileRoot = buildFileNodes(root);
				
				if (fancy)
				{
					fancy = false;
					detailedFormat();
				}
				else
				{
					updateFileTree();
				}
				
				// For dirTree
				// Add new node
				// For dirTree
				// Need to implement
			}
		}
		else
		{
			// Is a file (only operate on fTree), simply update
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) nd.getParent();

			fileRoot = buildFileNodes(root);
			
			if (fancy)
			{
				fancy = false;
				detailedFormat();
			}
			else
			{
				updateFileTree();
			}
		}
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
}
