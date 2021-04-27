package WFM;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

public class FileManagerFrame extends JInternalFrame
{
	JSplitPane splitpane;
	public FileManagerFrame()
	{
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new DirPanel(), new FilePanel());
		splitpane.revalidate();
		
		this.setSize(400, 400);
		this.setVisible(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setTitle("Temp!!! MUST CHANGE");
		this.setIconifiable(true);
		
		// Adding SplitPane
		this.getContentPane().add(splitpane);
	}
}
