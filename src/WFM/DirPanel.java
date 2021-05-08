package WFM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DirPanel extends JPanel{
	private JScrollPane sp = new JScrollPane();
	private JTree dirTree = new JTree();
	private FileTree root;
	
	DirPanel(JTree tree)
	{
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) dirTree.getCellRenderer();
        renderer.setLeafIcon(renderer.getClosedIcon());
        
		dirTree = tree;
		this.setLayout(new BorderLayout());
		
		sp.setSize(100, 100);
		sp.setViewportView(dirTree);
		this.add(sp, BorderLayout.CENTER);
		
		Dimension minimum = new Dimension(100, 0);
		this.setMinimumSize(minimum);
	}
}
