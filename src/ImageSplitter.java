import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class ImageSplitter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6670552391327121522L;
	private JPanel contentPane;
	private JTextField path;
	private JFormattedTextField sizeX;
	private JFormattedTextField sizeY;
	private JTextField destination;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try 
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} 
		catch (Throwable e) 
		{
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
		setBounds(100, 100, 376, 160);
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
						throw new FilePathException("Not an image");
				} 
				catch (FilePathException e1) 
				{
					JOptionPane.showMessageDialog(null, "The selected file is not an image!", "Error", 0);
				} 
				catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(null, "An error has occured while attempting to read the file!", "Error", 0);
				}
				catch(IllegalArgumentException iae) {}
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

		JLabel lblSizeX = new JLabel("Column :");
		panel_2.add(lblSizeX);

		// Creating a formatter so that those fields are always valid
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1);
		formatter.setMaximum(999);
		formatter.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);

		sizeX = new JFormattedTextField(formatter);
		sizeX.setText("1");
		panel_2.add(sizeX);
		sizeX.setColumns(10);

		JLabel lblSizeY = new JLabel("Row :");
		panel_2.add(lblSizeY);

		sizeY = new JFormattedTextField(formatter);
		sizeY.setText("1");
		panel_2.add(sizeY);
		sizeY.setColumns(10);

		JButton btnProcess = new JButton("Process");
		panel_1.add(btnProcess, BorderLayout.SOUTH);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDestination = new JLabel("Destination :");
		panel_3.add(lblDestination, BorderLayout.WEST);
		
		destination = new JTextField();
		panel_3.add(destination, BorderLayout.CENTER);
		destination.setColumns(10);
		
		JButton btnBrowse_1 = new JButton("Browse");
		panel_3.add(btnBrowse_1, BorderLayout.EAST);
		btnBrowse_1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    fc.setAcceptAllFileFilterUsed(false);
				fc.showOpenDialog(new JFrame("Chose a destination folder"));

				destination.setText(fc.getSelectedFile().getAbsolutePath());
			}
		});
		
		btnProcess.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					if(path.getText().isEmpty())
						throw new FilePathException("no file path specified");

					if(sizeX.getText().isEmpty() || sizeY.getText().isEmpty())
						throw new SizeFieldException("No size values");
					
					if(destination.getText().isEmpty())
						throw new FileDestinationException("no destination path specified");

					// Creating an image
					BufferedImage img = null;
					img = ImageIO.read(new File(path.getText()));

					// Creating folder where to create sub-images
					File fileName = new File(path.getText());
					File folder = new File(destination.getText() + "/" + fileName.getName());
					folder.mkdir();

					// Creating all the sub-images
					int column = Integer.parseInt(sizeX.getText());
					int row = Integer.parseInt(sizeY.getText());
					int width = img.getWidth() / column;
					int height = img.getHeight() / row;
					int subCount = 1;

					for(int j = 0 ; j < row ; j++)
					{
						for(int i = 0 ; i < column ; i++)
						{	
							int size1 = ("" + (row * column)).length();
							int size2 = ("" + subCount).length();
							String placeholder = "";
							for(int h = 0 ; h < (size1 - size2) ; h++)
							{
								placeholder += "0";
							}

							File outputfile = new File(destination.getText() + "/" + folder.getName() + "/sub" + placeholder + subCount + ".png");
							outputfile.createNewFile();
							BufferedImage sub = img.getSubimage(i * width, 
									j * height, 
									width,
									height);
							ImageIO.write(sub, "png", outputfile);
							subCount++;
						}
					}

					JOptionPane.showMessageDialog(null, "The image has been successfuly splitted!", "Info", 1);
				}
				catch(SizeFieldException sfe)
				{
					JOptionPane.showMessageDialog(null, "Size values are not or partially specified!", "Error", 0);
				}
				catch(FilePathException fpe)
				{	
					JOptionPane.showMessageDialog(null, "Path to image is not specified!", "Error", 0);
				} 
				catch(FileDestinationException fde)
				{
					JOptionPane.showMessageDialog(null, "Destination path is not specified!", "Error", 0);
				}
				catch (IOException e1) 
				{
					JOptionPane.showMessageDialog(null, "An error occured while attempting to read file!", "Error", 0);
				}
				catch(RasterFormatException rfe)
				{
					JOptionPane.showMessageDialog(null, "Values for row or column are too big!", "Error", 0);
				}
			}
		});
	}
}