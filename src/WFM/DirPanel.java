package WFM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class DirPanel extends JPanel{
	private JScrollPane sp = new JScrollPane();
	private JTree dirTree = new JTree();
	
	DirPanel()
	{
		this.setLayout(new BorderLayout());
		dirTree.setPreferredSize(new Dimension(150, 500));
		
		sp.setSize(100, 100);
		sp.setViewportView(dirTree);
		this.add(sp, BorderLayout.CENTER);
		
		Dimension minimum = new Dimension(100, 0);
		this.setMinimumSize(minimum);
	}
}
