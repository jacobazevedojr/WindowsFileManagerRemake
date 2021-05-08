package WFM;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

// Test
public class App extends JFrame {
	// For copy and paste
	static DefaultMutableTreeNode lastSelected;
	static FileManagerFrame lastSelectedFrame;
	static DefaultMutableTreeNode copied;
	private static TreePath treePath;
	
	// Cascading Positions
	int lastX;
	int lastY;
	
	// Panels
	JPanel panel;
	JPanel northPanel;
	
	// Buttons
	JButton ok;
	JButton bye;
	
	// Menu
	JMenuBar menu;
	
	// Toolbars
	JToolBar toolbar;
	JToolBar drivebar;
	JToolBar statusbar;
	
	// Desktop Pane
	private JDesktopPane desktop;
	
	// File Manager (Internal Frames)
	FileManagerFrame myf, myf2;
	
	String currentDrive;
	
	public App()
	{
		lastX = 0;
		lastY = 0;
		
		// Initializing outermost panel
		panel = new JPanel();
		// Initialize top panel
		northPanel = new JPanel();
		
		// Menu
		menu = new JMenuBar();
		
		// Status
		statusbar = new JToolBar();
		
		// Tool Bar
		toolbar = new JToolBar();
		
		// Desktop
		setDesktop(new JDesktopPane());
		
		// File Manager Frame
		// Should create C:\ by default, and additional FileManagerFrames
		// Will be created through the Toolbar
		File[] files = File.listRoots();
		myf = new FileManagerFrame(files[0].toString());
	}
	
	public void go()
	{

		this.setTitle("CECS 277 File Manager");
		// Layouts
		panel.setLayout(new BorderLayout());
		northPanel.setLayout(new BorderLayout());

		// Add Components
		buildMenu();
		northPanel.add(menu, BorderLayout.NORTH);
		buildToolbar();
		northPanel.add(toolbar, BorderLayout.SOUTH);
		
		// Desktop
		panel.add(getDesktop(), BorderLayout.CENTER);
		
		// Internal Frames
		getDesktop().add(myf);
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		// Status Bar
		buildStatusBar();
		updateStatusBar(new File("C:\\"));
		panel.add(statusbar, BorderLayout.SOUTH);
		
		// Panel into JFrame
		this.add(panel);
		
		// Properties of JFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(700, 500);
	}
	
	public static void setLastSelected(DefaultMutableTreeNode f)
	{
		lastSelected = f;
	}
	
	public static DefaultMutableTreeNode getLastSelected()
	{
		return lastSelected;
	}
	
	public static void setSelectedFrame(FileManagerFrame f)
	{
		lastSelectedFrame = f;
	}
	
	public static FileManagerFrame getSelectedFrame()
	{
		return lastSelectedFrame;
	}
	
	public static void rename()
	{
		if (lastSelected != null)
		{
			File ls = ((FileNode)lastSelected.getUserObject()).getFile();
			String path = ls.getParent();
			
			dataDlg dlg = new dataDlg("Rename", path, ls.getName(), ls);
			dlg.setVisible(true);
		}
	}
	
	public static void delete()
	{
		if (lastSelected != null)
		{
			File ls = ((FileNode)lastSelected.getUserObject()).getFile();
			
			dltDlg dlg = new dltDlg(ls);
			dlg.setVisible(true);
		}
	}
	
	public void copy()
	{
		if (lastSelected != null)
		{
			File ls = ((FileNode)lastSelected.getUserObject()).getFile();
			String path = ls.getParent();
			
			dataDlg dlg = new dataDlg("Copy", path, ls.getName(), ls);
			dlg.setVisible(true);
		}
	}
	
	public static void paste(String location)
	{
		File file = ((FileNode) copied.getUserObject()).getFile();
		String newName = location;
		
		if(newName.charAt(newName.length() - 1) != '\\')
		{
			newName += "\\";
		}
		
		newName += file.getName();
		
		File newFile = new File(newName);

		try {
			Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		App.updateTree(App.getSelectedFrame(), App.getLastSelected(), null, "Paste");
	}
	
	public static void updateTree(FileManagerFrame frame, DefaultMutableTreeNode node, DefaultMutableTreeNode newNode, String op)
	{	
		frame.updateAfterNodeChanges(node, newNode, op);
	}
	
	private void buildStatusBar() {
		// Still need to update depending on the selected directory
		JLabel drive = new JLabel();
		JLabel freeSpace = new JLabel();
		JLabel usedSpace = new JLabel();
		JLabel totalSpace = new JLabel();
		
		statusbar.add(drive);
		statusbar.addSeparator();
		statusbar.add(freeSpace);
		statusbar.addSeparator();
		statusbar.add(usedSpace);
		statusbar.addSeparator();
		statusbar.add(totalSpace);
	}
	
	private void updateStatusBar(File drive)
	{
		String driveStr = drive.toString();
		String free = String.format("%.0f", ((9.3132 * Math.pow(10, -10)) * drive.getFreeSpace()));
		String used = String.format("%.0f", ((9.3132 * Math.pow(10, -10)) * (drive.getTotalSpace() - drive.getFreeSpace())));
		String total = String.format("%.0f", ((9.3132 * Math.pow(10, -10)) * drive.getTotalSpace()));
		
		((JLabel) statusbar.getComponent(0)).setText("Current Drive: " + driveStr + " ");
		((JLabel) statusbar.getComponent(2)).setText("Free Space: " + free + " GB ");
		((JLabel) statusbar.getComponent(4)).setText("Used Space: " + used + " GB ");
		((JLabel) statusbar.getComponent(6)).setText("Total Space: " + total + " GB");
		
		statusbar.repaint();
	}

	private void buildToolbar() {
		// Buttons
		JButton details = new JButton("Details");
		JButton simple = new JButton("Simple");
		
		details.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FileManagerFrame fmf = ((FileManagerFrame) getDesktop().getSelectedFrame());
				if (fmf != null)
				{
					fmf.detailedFormat();
				}
			}
		});
		
		simple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FileManagerFrame fmf = ((FileManagerFrame) getDesktop().getSelectedFrame());
				if (fmf != null)
				{
					fmf.simpleFormat();
				}
			}
		});		
		File[] files = File.listRoots();
		// Drop down
		JComboBox<File> dropDown = new JComboBox<File>(files);
		
		toolbar.addSeparator(new Dimension(150, 0));
		toolbar.add(dropDown);
		toolbar.addSeparator();
		toolbar.add(details);
		toolbar.addSeparator();
		toolbar.add(simple);
		toolbar.addSeparator(new Dimension(150, 0));
		
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
	}

	private void buildMenu() {
		JMenu fileMenu, treeMenu, windowMenu, helpMenu;
		fileMenu = new JMenu("File");
		treeMenu = new JMenu("Tree");
		windowMenu = new JMenu("Window");
		helpMenu = new JMenu("Help");
		
		// JMenu Buttons
		JMenuItem rename = new JMenuItem("Rename");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem about = new JMenuItem("About");
		JMenuItem run = new JMenuItem("Run");
		JMenuItem expand = new JMenuItem("Expand");
		JMenuItem collapse = new JMenuItem("Collapse");
		JMenuItem newWin = new JMenuItem("New");
		JMenuItem cascade = new JMenuItem("Cascade");
		
		// Action Listeners
		exit.addActionListener(new ExitAL());
		about.addActionListener(new AboutAL());
		
		newWin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) toolbar.getComponent(1);
		        File drive = (File) cb.getSelectedItem();
		        openNewFileFrame(drive);
		        
		        // Update statusbar
		        updateStatusBar(drive);
			}
		});
		
		cascade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) toolbar.getComponent(1);
		        File drive = (File) cb.getSelectedItem();
		        openNewFileFrameCascade(drive);
		        
		        // Update statusbar
		        updateStatusBar(drive);
			}
		});
		
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.getDesktop();
				File file = ((FileNode) lastSelected.getUserObject()).getFile();
				
				try {
					desktop.open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				delete();
			}
		});
		
		rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				rename();
			}
		});
		
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				copy();
			}
		});
		
		expand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JTree tree = lastSelectedFrame.getDirTree();
				tree.expandPath(treePath);
			}
		});
		
		collapse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JTree tree = lastSelectedFrame.getDirTree();
				tree.collapsePath(treePath);
			}
		});
		
		// Adding buttons
		fileMenu.add(rename);
		fileMenu.add(copy);
		fileMenu.add(delete);
		fileMenu.add(run);
		fileMenu.add(exit);
		
		helpMenu.add(about);
		
		treeMenu.add(expand);
		treeMenu.add(collapse);
		
		windowMenu.add(newWin);
		windowMenu.add(cascade);
		
		// Add drop downs
		menu.add(fileMenu);
		menu.add(treeMenu);
		menu.add(windowMenu);
		menu.add(helpMenu);
	}
	
	private void openNewFileFrameCascade(File drive)
	{
		FileManagerFrame newFrame = new FileManagerFrame(drive.toString());
		
		getDesktop().add(newFrame);
		
		// When I close a window, I need to specify where to open the new cascading windows
		lastX += 30;
		lastY += 30;
		
		newFrame.setLocation(lastX, lastY);
		getDesktop().setComponentZOrder(newFrame, 0);
	}
	
	private void openNewFileFrame(File drive)
	{
		FileManagerFrame newFrame = new FileManagerFrame(drive.toString());
		
		getDesktop().add(newFrame);
		
		lastX = 0;
		lastY = 0;
		
		newFrame.setLocation(lastX, lastY);
		getDesktop().setComponentZOrder(newFrame, 0);
	}

	private class ExitAL implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	private class AboutAL implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e)
		{
			AboutDlg dlg = new AboutDlg();
			dlg.setVisible(true);
		}
	}
	
	private class RDAL implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand().equals("Run"))
			{
				System.out.println("Running the program");
			}
			else if (e.getActionCommand().equals("Debug"))
			{
				System.out.println("Debugging the program");
			}
			else
			{
				System.out.println("ERROR");
			}
		}
	}

	public static void setCopied(DefaultMutableTreeNode node) {
		copied = node;
	}

	public JDesktopPane getDesktop() {
		return desktop;
	}

	public void setDesktop(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public static void setDirPath(TreePath path) {
		treePath = path;
	}
}


