
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import componente.PopupAlerts;
import radio_button.RadioButtonCustom;
import roundedButtonsWithImage.ButtonRoundedWithImage;
import simple.chooser.DemoJavaFxStage;
import spinner.Spinner;
import util.Metodos;

@SuppressWarnings("all")

public class ImgSplitter extends javax.swing.JFrame {

	JTextField path;

	private JTextField destination;

	RadioButtonCustom split;

	RadioButtonCustom merge;

	String archivo;

	Spinner sizeX;

	Spinner sizeY;

	boolean carpeta;

	boolean error;

	public static PopupAlerts alert;

	public ImgSplitter() throws IOException {

		setIconImage(Toolkit.getDefaultToolkit().getImage(ImgSplitter.class.getResource("/images/icon.png")));

		setTitle("Image Splitter And Merger");

		initComponents();

		this.setVisible(true);

	}

	public void initComponents() throws IOException {

		archivo = "";

		error = false;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		ButtonRoundedWithImage btnNewButton = new ButtonRoundedWithImage();

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				error = false;

				if (split.isSelected()) {

					try {

						if (path.getText().isEmpty()) {

							error = true;

							alert.mensaje("no file path specified", 3, 16);

						}

						if (sizeX.getValue().toString().isEmpty() || sizeY.getValue().toString().isEmpty()) {

							error = true;

							alert.mensaje("No size values", 3, 16);

						}

						if (destination.getText().isEmpty()) {

							error = true;

							alert.mensaje("no destination path specified", 3, 16);

						}

						if (!error && !(sizeX.getValor() == 1 && sizeY.getValor() == 1)) {

							BufferedImage img = null;

							img = ImageIO.read(new File(path.getText()));

							File fileName = new File(path.getText());

							File folder = new File(destination.getText() + "/" + fileName.getName());

							folder.mkdir();

							int column = sizeX.getValor();

							int row = sizeY.getValor();

							int width = img.getWidth() / column;

							int height = img.getHeight() / row;

							int subCount = 1;

							String carpeta = destination.getText() + "\\"
									+ folder.getName().substring(0, folder.getName().lastIndexOf("."));

							Metodos.crearCarpeta(carpeta);

							for (int j = 0; j < row; j++) {

								for (int i = 0; i < column; i++) {

									int size1 = ("" + (row * column)).length();

									int size2 = ("" + subCount).length();

									String placeholder = "";

									for (int h = 0; h < (size1 - size2); h++) {

										placeholder += "0";

									}

									File outputfile = new File(destination.getText() + "\\"
											+ folder.getName().substring(0, folder.getName().lastIndexOf(".")) + "\\sub"
											+ placeholder + subCount + ".png");

									outputfile.createNewFile();

									BufferedImage sub = img.getSubimage(i * width, j * height, width, height);

									ImageIO.write(sub, "png", outputfile);

									subCount++;

								}

							}

							alert.mensaje("The image has been successfuly splitted!", 2, 16);

							Metodos.abrirCarpeta(carpeta);

						}

					}

					catch (RasterFormatException rfe) {

						alert.mensaje("Values for row or column are too big!", 1, 16);

					}

					catch (Exception rfe) {

						rfe.printStackTrace();

					}

				}

				else {

					try {

						new MergeImage().mergeImage(path.getText() + Metodos.saberSeparador(),
								destination.getText() + Metodos.saberSeparador() + "0_Output_merge_"
										+ Metodos.obtenerParametrosApi("test.png").get(0),
								sizeY.getValor(), sizeX.getValor());

					}

					catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}

		});

		btnNewButton.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/icon.png")));

		ButtonRoundedWithImage btnBrowse = new ButtonRoundedWithImage();

		btnBrowse.setBackground(SystemColor.inactiveCaptionBorder);

		btnBrowse.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

					DemoJavaFxStage test = new DemoJavaFxStage();

					LinkedList<File> lista = new LinkedList<File>();

					if (split.isSelected()) {

						carpeta = false;

					}

					else {

						carpeta = true;

					}

					String ruta = "";

					if (!path.getText().isEmpty()) {

						ruta = path.getText();

					}

					lista = test.showOpenFileDialog(ruta, carpeta, "images");

					path.setText(lista.get(0).toString());

				}

				catch (Exception e1) {

				}

			}

		});

		btnBrowse.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/folder.png")));

		btnBrowse.setText("Browse");

		path = new JTextField();

		path.setColumns(10);

		JLabel lblSizeX = new JLabel("Column :");
		lblSizeX.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblSizeX.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/height.png")));

		lblSizeX.setHorizontalAlignment(SwingConstants.CENTER);

		sizeX = new Spinner();

		sizeX.getEditor().addKeyListener(new KeyAdapter() {

			@Override

			public void keyReleased(KeyEvent e) {

				sizeX.ponerFiltro();

			}

		});

		sizeX.setMinValor(1);

		sizeX.setLabelText("Columns");

		sizeX.setValue(1);

		sizeY = new Spinner();

		sizeY.getEditor().addKeyListener(new KeyAdapter() {

			@Override

			public void keyReleased(KeyEvent e) {

				sizeY.ponerFiltro();

			}

		});

		sizeY.setLabelText("Rows");

		sizeY.setValue(1);

		sizeY.setMinValor(1);

		JLabel lblSizeY = new JLabel("Row :");

		lblSizeY.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblSizeY.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/width.png")));

		lblSizeY.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblDestination = new JLabel("Destination :");

		lblDestination.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblDestination.setHorizontalAlignment(SwingConstants.CENTER);

		destination = new JTextField();

		destination.setColumns(10);

		JLabel lblPath = new JLabel("Path : ");
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblPath.setHorizontalAlignment(SwingConstants.CENTER);

		ButtonRoundedWithImage btnBrowse_1 = new ButtonRoundedWithImage();
		btnBrowse_1.setBackground(SystemColor.inactiveCaptionBorder);
		btnBrowse_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

					DemoJavaFxStage test = new DemoJavaFxStage();

					LinkedList<File> lista = new LinkedList<File>();

					String ruta = "";

					if (!destination.getText().isEmpty()) {

						ruta = destination.getText();

					}

					lista = test.showOpenFileDialog(ruta, true, "");

					destination.setText(lista.get(0).toString());

				}

				catch (Exception e1) {

				}

			}

		});

		btnBrowse_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/folder.png")));

		btnBrowse_1.setText("Browse");

		split = new RadioButtonCustom();

		split.setFont(new Font("Tahoma", Font.PLAIN, 16));

		split.setSelected(true);

		split.setHorizontalAlignment(SwingConstants.LEFT);

		split.setText("Split");

		split.addMouseListener(new MouseAdapter() {

			@Override

			public void mousePressed(MouseEvent e) {

				if (!split.isSelected()) {

					merge.setSelected(false);

					if (!archivo.isEmpty()) {

						path.setText(archivo);

						archivo = "";

					}

				}

				else {

					merge.setSelected(true);

					comprobarMerge();

				}

			}

		});

		merge = new RadioButtonCustom();

		merge.setFont(new Font("Tahoma", Font.PLAIN, 16));

		merge.setHorizontalAlignment(SwingConstants.LEFT);

		merge.setText("Merge");

		merge.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (!merge.isSelected()) {

					split.setSelected(false);

					comprobarMerge();

				}

				else {

					split.setSelected(true);

				}

			}

		});

		ButtonRoundedWithImage btnBrowse_1_1 = new ButtonRoundedWithImage();
		btnBrowse_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(1);
				sizeX.setValor(2);
			}
		});
		btnBrowse_1_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/1.png")));
		btnBrowse_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_1 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(2);
				sizeX.setValor(2);
			}
		});

		btnBrowse_1_1_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/2.png")));

		btnBrowse_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_1.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_2 = new ButtonRoundedWithImage();

		btnBrowse_1_1_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(2);
				sizeX.setValor(3);
			}

		});

		btnBrowse_1_1_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/3.png")));

		btnBrowse_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_2.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_3 = new ButtonRoundedWithImage();

		btnBrowse_1_1_3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(3);

				sizeX.setValor(3);

			}

		});

		btnBrowse_1_1_3.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/4.png")));
		btnBrowse_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_3.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_1_1 = new ButtonRoundedWithImage();

		btnBrowse_1_1_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(2);

				sizeX.setValor(1);

			}

		});

		btnBrowse_1_1_1_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/5.png")));
		btnBrowse_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_1.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_1_2 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(4);

				sizeX.setValor(1);

			}

		});

		btnBrowse_1_1_1_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/7.png")));

		btnBrowse_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_1_2.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_1_2_1 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(3);
				sizeX.setValor(1);
			}
		});
		btnBrowse_1_1_1_2_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/6.png")));
		btnBrowse_1_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_2_1.setBackground(SystemColor.inactiveCaptionBorder);

		ButtonRoundedWithImage btnBrowse_1_1_1_2_2 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/8.png")));
		btnBrowse_1_1_1_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(3);
				sizeX.setValor(2);
			}
		});
		btnBrowse_1_1_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_2_2.setBackground(SystemColor.inactiveCaptionBorder);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/crop.png")));

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/merge_files.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout
								.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(
										Alignment.TRAILING).addGroup(
												layout.createSequentialGroup().addGroup(layout.createParallelGroup(
														Alignment.LEADING)
														.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
																.addComponent(lblPath, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(lblDestination, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
														.addGroup(layout.createSequentialGroup().addGap(3).addComponent(
																lblSizeY)))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(Alignment.TRAILING)
																.addComponent(path, Alignment.LEADING,
																		GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
																.addGroup(layout.createSequentialGroup().addGap(6)
																		.addComponent(sizeY, GroupLayout.PREFERRED_SIZE,
																				114, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.UNRELATED)
																		.addComponent(lblSizeX,
																				GroupLayout.DEFAULT_SIZE, 133,
																				Short.MAX_VALUE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(sizeX, GroupLayout.PREFERRED_SIZE,
																				122, GroupLayout.PREFERRED_SIZE)
																		.addGap(8))
																.addComponent(destination, Alignment.LEADING,
																		GroupLayout.DEFAULT_SIZE, 397,
																		Short.MAX_VALUE)))
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout
														.createSequentialGroup()
														.addComponent(btnBrowse_1_1, GroupLayout.PREFERRED_SIZE, 53,
																GroupLayout.PREFERRED_SIZE)
														.addGap(18)
														.addComponent(btnBrowse_1_1_1, GroupLayout.PREFERRED_SIZE, 54,
																GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(
																btnBrowse_1_1_2, GroupLayout.PREFERRED_SIZE, 53,
																GroupLayout.PREFERRED_SIZE))
														.addGroup(layout.createSequentialGroup()
																.addComponent(btnBrowse_1_1_1_1,
																		GroupLayout.PREFERRED_SIZE, 54,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(btnBrowse_1_1_1_2_1,
																		GroupLayout.PREFERRED_SIZE, 54,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(17).addComponent(btnBrowse_1_1_1_2,
																		GroupLayout.PREFERRED_SIZE, 54,
																		GroupLayout.PREFERRED_SIZE)))
												.addGap(18)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(
																btnBrowse_1_1_3, GroupLayout.PREFERRED_SIZE, 53,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnBrowse_1_1_1_2_2, GroupLayout.PREFERRED_SIZE,
																54, GroupLayout.PREFERRED_SIZE))
												.addGap(87)))
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addGroup(layout.createSequentialGroup().addGap(2).addComponent(
																lblNewLabel, GroupLayout.PREFERRED_SIZE, 58,
																GroupLayout.PREFERRED_SIZE))
														.addGroup(layout.createSequentialGroup().addGap(10)
																.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE,
																		53, GroupLayout.PREFERRED_SIZE)))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(split, GroupLayout.DEFAULT_SIZE, 93,
																Short.MAX_VALUE)
														.addComponent(merge, GroupLayout.PREFERRED_SIZE, 68,
																GroupLayout.PREFERRED_SIZE)))
										.addGroup(layout.createSequentialGroup().addGap(56).addComponent(btnNewButton,
												GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup().addGap(18)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 112,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(btnBrowse_1, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup()
				.addGap(36)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(path, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPath, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
								.addGap(18))
						.addGroup(layout.createSequentialGroup()
								.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDestination, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(destination, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(33)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addComponent(split, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 40,
														GroupLayout.PREFERRED_SIZE)
												.addGap(12)))
								.addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 57,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(merge, GroupLayout.PREFERRED_SIZE, 28,
														GroupLayout.PREFERRED_SIZE)
												.addGap(9)))
								.addGap(18)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(layout.createSequentialGroup().addGap(59).addGroup(layout
								.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblSizeY, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(sizeX, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblSizeX, GroupLayout.PREFERRED_SIZE, 52,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(sizeY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
								.addGap(28)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(btnBrowse_1_1_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
										.addComponent(btnBrowse_1_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 45,
												Short.MAX_VALUE)
										.addComponent(btnBrowse_1_1_2, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnBrowse_1_1_3, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE))
								.addGap(18)
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnBrowse_1_1_1_2_2, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnBrowse_1_1_1_2_1, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnBrowse_1_1_1_1, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnBrowse_1_1_1_2, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE))))
				.addGap(36)));

		getContentPane().setLayout(layout);

		setSize(new Dimension(690, 455));

		setLocationRelativeTo(null);

	}

	protected void comprobarMerge() {

		Pattern p = Pattern.compile("\\.[a-zA-Z0-9]{3}$");

		Matcher m = p.matcher(path.getText());

		if (m.find()) {

			archivo = path.getText();

			path.setText(archivo.substring(0, archivo.lastIndexOf(Metodos.saberSeparador())));

		}

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void stateChanged(ChangeEvent e) {

	}
}
