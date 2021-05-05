package WFM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FilePanel extends JPanel{
	private JScrollPane sp = new JScrollPane();
	private JTree fileTree;
	
	FilePanel(JTree fTree)
	{
		fileTree = fTree;
		this.setLayout(new BorderLayout());
		sp.setViewportView(fileTree);
		this.add(sp, BorderLayout.CENTER);
		
		Dimension minimum = new Dimension(100, 0);
		this.setMinimumSize(minimum);
	}
}
