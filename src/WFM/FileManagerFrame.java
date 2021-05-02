package WFM;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class FileManagerFrame extends JInternalFrame
{
	private JSplitPane splitpane;
	private FileTree fileTree;
	private JTree tree;
	public FileManagerFrame(String root)
	{
		fileTree = new FileTree(root);
		
		tree = buildTree();
		tree.addTreeSelectionListener(new TreeSL());
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirPanel(tree), new FilePanel());
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
	
	private JTree buildTree()
	{
		DefaultTreeModel treemodel = new DefaultTreeModel(fileTree.getRoot());
		JTree tree = new JTree();
		tree.setModel(treemodel);
		
		return tree;
	}
	
	private class TreeSL implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			
			// If the current node doesn't have children, DFS 2 layers down
			System.out.println(node.toString());
			if (!node.children().hasMoreElements())
			{
				fileTree.DFS(node);
			}
			
			System.out.println(((FileNode) node.getUserObject()).getChildren());
			tree.repaint();
		}
	}
}
