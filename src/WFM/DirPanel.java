package WFM;

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
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		dirTree.setPreferredSize(new Dimension(1000, 0));
		
		sp.setSize(100, 100);
		sp.setViewportView(dirTree);
		this.add(sp);
		
		Dimension minimum = new Dimension(150, 0);
		this.setMinimumSize(minimum);
	}
}
