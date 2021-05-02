package WFM;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

// Test
public class App extends JFrame {
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
	JDesktopPane desktop;
	
	// File Manager (Internal Frames)
	FileManagerFrame myf, myf2;
	
	String currentDrive;
	
	public App()
	{
		// Initializing outermost panel
		panel = new JPanel();
		// Initialize top panel
		northPanel = new JPanel();
		
		// Menu
		menu = new JMenuBar();
		
		// Status
		statusbar = new JToolBar();
		
		// Desktop
		desktop = new JDesktopPane();
		
		// File Manager Frame
		myf = new FileManagerFrame("C:\\");
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
		//northPanel.add(toolbar, BorderLayout.SOUTH);
		
		// Desktop
		panel.add(desktop, BorderLayout.CENTER);
		
		// Internal Frames
		desktop.add(myf);
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		// Status Bar
		buildStatusBar();
		panel.add(statusbar, BorderLayout.SOUTH);
		
		// Panel into JFrame
		this.add(panel);
		
		// Properties of JFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(700, 500);
	}
	
	private void buildStatusBar() {
		JLabel size = new JLabel("Size in GBs: ");
		statusbar.add(size);
	}

	private void buildToolbar() {
		return;
	}

	private void buildMenu() {
		JMenu fileMenu, treeMenu, windowMenu, helpMenu;
		fileMenu = new JMenu("file");
		treeMenu = new JMenu("tree");
		windowMenu = new JMenu("window");
		helpMenu = new JMenu("help");
		
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem about = new JMenuItem("About");
		JMenuItem run = new JMenuItem("Run");
		JMenuItem debug = new JMenuItem("Debug");
		
		// Action Listeners
		exit.addActionListener(new ExitAL());
		about.addActionListener(new AboutAL());
		run.addActionListener(new RDAL());
		debug.addActionListener(new RDAL());
		
		fileMenu.add(exit);
		helpMenu.add(about);
		treeMenu.add(run);
		treeMenu.add(debug);
		
		menu.add(fileMenu);
		menu.add(treeMenu);
		menu.add(windowMenu);
		menu.add(helpMenu);
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
}


