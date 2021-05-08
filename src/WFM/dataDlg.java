package WFM;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class dataDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField toField;
	private JTextField fromField;
	private String to;
	
	/**
	 * Create the dialog.
	 */
	public dataDlg(String title, String dir, String fromName, File file) {
		this.setTitle(title);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Current Directory:");
			lblNewLabel.setBounds(29, 35, 109, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_2 = new JLabel(dir);
			lblNewLabel_2.setBounds(148, 35, 247, 14);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("From:");
			lblNewLabel_1.setBounds(59, 117, 79, 14);
			contentPanel.add(lblNewLabel_1);
		}
		{
			fromField = new JTextField(fromName);
			fromField.setBounds(148, 114, 247, 20);
			fromField.setEditable(false);
			contentPanel.add(fromField);
			fromField.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("To:");
			lblNewLabel_1.setBounds(65, 142, 73, 14);
			contentPanel.add(lblNewLabel_1);
		}
		{
			toField = new JTextField();
			toField.setBounds(148, 139, 247, 20);
			contentPanel.add(toField);
			toField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						to = toField.getText();
						setVisible(false);
						
						if (title == "Rename")
						{
							String newName = file.getParent();
							if(newName.charAt(newName.length() - 1) != '\\')
							{
								newName += "\\";
							}
							newName += to;
							
							boolean worked = file.renameTo(new File(newName));
							App.updateTree(App.getSelectedFrame(), App.getLastSelected(), null, "Rename");
						}
						else if (title == "Copy")
						{
							String newName = file.getParent();
							if(newName.charAt(newName.length() - 1) != '\\')
							{
								newName += "\\";
							}
							newName += to;
							
							File newFile = new File(newName);
							try {
								Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							App.updateTree(App.getSelectedFrame(), App.getLastSelected(), null, "Copy");
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						to = fromName;
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
}
