import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import javax.swing.UIManager;

public class ImageSplitter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6670552391327121522L;
	private JPanel contentPane;
	private JTextField path;
	private JFormattedTextField sizeX;
	private JFormattedTextField sizeY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					ImageSplitter frame = new ImageSplitter();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public ImageSplitter() throws IOException 
	{
		setIconImage(ImageIO.read(new File("icon.png")));
		setTitle("Image Splitter");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 376, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Panel panel = new Panel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPath = new JLabel("Path : ");
		panel.add(lblPath, BorderLayout.WEST);
		
		JButton btnBrowse = new JButton("Browse");
		panel.add(btnBrowse, BorderLayout.EAST);
		btnBrowse.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(new JFrame("Chose an image"));
				File file = fc.getSelectedFile();
			
				try 
				{
					if(ImageIO.read(file) != null)
					{
						path.setText(fc.getSelectedFile().getAbsolutePath());
					}
					else 
						throw new Exception("Not an image");
				} 
				catch (Exception e1) 
				{
					JOptionPane.showMessageDialog(null, "The selected file is not an image!", "Error", 0);
				}
			}
		});
		
		path = new JTextField();
		panel.add(path, BorderLayout.CENTER);
		path.setColumns(10);
		
		Panel panel_1 = new Panel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Panel panel_2 = new Panel();
		panel_1.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblSizeX = new JLabel("Size X :");
		panel_2.add(lblSizeX);
		
		// Creating a formatter so that those fields are always valid
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(1);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formatter.setCommitsOnValidEdit(true);
	    
		sizeX = new JFormattedTextField(formatter);
		sizeX.setText("1");
		panel_2.add(sizeX);
		sizeX.setColumns(10);
		
		JLabel lblSizeY = new JLabel("Size Y :");
		panel_2.add(lblSizeY);
		
		sizeY = new JFormattedTextField(formatter);
		sizeY.setText("1");
		panel_2.add(sizeY);
		sizeY.setColumns(10);
		
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setValue(0);
		bar.setStringPainted(true);
		panel_1.add(bar, BorderLayout.SOUTH);
		
		JButton btnProcess = new JButton("Process");
		panel_1.add(btnProcess, BorderLayout.CENTER);
		btnProcess.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					if(path.getText().isEmpty())
						throw new FilePathException("not file path specified");
					
					if(sizeX.getText().isEmpty() || sizeY.getText().isEmpty())
						throw new SizeFieldException("No size values");

					// Creating an image
					BufferedImage img = null;
					img = ImageIO.read(new File(path.getText()));
					
					// Creating folder where to create sub-images
					File fileName = new File(path.getText());
					File folder = new File("img/" + fileName.getName());
					folder.mkdir();
					
					// Creating all the sub-images
					int width = img.getWidth() / Integer.parseInt(sizeX.getText());
					int height = img.getHeight() / Integer.parseInt(sizeY.getText());
					int subCount = 1;
					
					//bar.setMaximum(width*height);
					
					for(int j = 0 ; j < height ; j++)
					{
						for(int i = 0 ; i < width ; i++)
						{	
							File outputfile = new File("img/" + folder.getName() + "/sub" + subCount + ".png");
							outputfile.createNewFile();
							BufferedImage sub = img.getSubimage(i * Integer.parseInt(sizeX.getText()), 
																j * Integer.parseInt(sizeY.getText()), 
																Integer.parseInt(sizeX.getText()), 
																Integer.parseInt(sizeY.getText()));
							ImageIO.write(sub, "png", outputfile);
							subCount++;
						}
						
						
						float per = ((float) subCount / (width*height) * 100);
						bar.setValue((int) per);
					}
					
					JOptionPane.showMessageDialog(null, "The image has been successfuly splitted!", "Info", 1);
					bar.setValue(0);
				}
				catch(SizeFieldException sfe)
				{
					JOptionPane.showMessageDialog(null, "Size values are not or partially specified!", "Error", 0);
				}
				catch(FilePathException fpe)
				{	
					JOptionPane.showMessageDialog(null, "Path to image is not specified!", "Error", 0);
				} 
				catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(null, "An error occured while attempting to read file!", "Error", 0);
				}
			}
		});
	}
}