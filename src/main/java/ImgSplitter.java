
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

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

import com.languages.ComboBoxLanguage;
import com.languages.ComboBoxLanguage.Language;

import combo_suggestion.ComboBoxSuggestion;
import componente.PopupAlerts;
import drag_and_drop.DragAndDrop;
import drag_and_drop.UtilDragAndDrop;
import javaswingdev.message.MessageDialog;
import radio_button.RadioButtonCustom;
import roundedButtonsWithImage.ButtonRoundedWithImage;
import simple.chooser.DemoJavaFxStage;
import spinner.Spinner;
import swing.EventSwitchSelected;
import util.Metodos;

@SuppressWarnings("all")

public class ImgSplitter extends javax.swing.JFrame {

	private JTextField path;

	private JLabel lblSizeX;

	private JTextField destination;

	private ButtonRoundedWithImage btnBrowse_1_2_2;

	private RadioButtonCustom split;

	private ButtonRoundedWithImage btnBrowse;

	private RadioButtonCustom merge;

	private JLabel lblPath;

	private Spinner sizeX;

	private JLabel lblDestination;

	private Spinner sizeY;

	private JLabel lblSizeY;

	private ButtonRoundedWithImage btnBrowse_1;

	private ButtonRoundedWithImage btnNewButton;

	private JLabel convert;

	boolean carpeta;

	boolean error;

	boolean subcarpeta;

	private JLabel lblNewLabel_2;

	private swing.SwitchButton btnBrowse_1_1_4;

	private DragAndDrop drgndrpDragAndDrop;

	private String salida;

	static ComboBoxLanguage idioma;

	private String archivoTxt;

	private String carpetaSalida;

	static PopupAlerts alert;

	int contador = 1;

	public static ComboBoxSuggestion<Object> format;

	public static ArrayList<File> archivosDrag;

	public ImgSplitter() throws IOException {

		setIconImage(Toolkit.getDefaultToolkit().getImage(ImgSplitter.class.getResource("/images/icon.png")));

		setTitle("Image Splitter And Merger");

		initComponents();

		this.setVisible(true);

	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

		MessageDialog obj = new MessageDialog(this);

		ComboBoxLanguage.translateMessageDialog(obj, "title_clear_message", "msg_clear_message", idioma.getLanguage());

		if (obj.getMessageType() == MessageDialog.MessageType.OK) {

			archivosDrag.clear();

		}

	}

	void convertir() {

		error = false;

		File fileName = new File(path.getText());

		carpetaSalida = destination.getText();

		if (subcarpeta) {

			try {

				carpetaSalida = destination.getText() + Metodos.saberSeparador()
						+ fileName.getName().substring(0, fileName.getName().lastIndexOf("."));

			}

			catch (Exception e1) {

				carpetaSalida = destination.getText() + Metodos.saberSeparador() + "Output_" + fileName.getName();

			}

			new File(carpetaSalida).mkdir();

		}

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

					int column = sizeX.getValor();

					int row = sizeY.getValor();

					int width = img.getWidth() / column;

					int height = img.getHeight() / row;

					int subCount = 1;

					for (int j = 0; j < row; j++) {

						for (int i = 0; i < column; i++) {

							int size1 = ("" + (row * column)).length();

							int size2 = ("" + subCount).length();

							String placeholder = "";

							for (int h = 0; h < (size1 - size2); h++) {

								placeholder += "0";

							}

							File outputfile = new File(carpetaSalida + Metodos.saberSeparador() + "_sub" + placeholder
									+ subCount + "." + format.getSelectedItem().toString().toLowerCase());

							outputfile.createNewFile();

							BufferedImage sub = img.getSubimage(i * width, j * height, width, height);

							ImageIO.write(sub, format.getSelectedItem().toString().toLowerCase(), outputfile);

							subCount++;

						}

					}

					alert.mensaje("The image has been successfuly splitted!", 2, 16);

					Metodos.abrirCarpeta(carpetaSalida);

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

				new MergeImage().mergeImage(path.getText(),
						carpetaSalida + Metodos.saberSeparador() + "Output_merge_" + Metodos
								.obtenerParametrosApi("test." + Metodos.extraerExtension(path.getText())).get(0),
						sizeY.getValor(), sizeX.getValor());

			}

			catch (Exception e1) {

				e1.printStackTrace();

			}

		}

	}

	public void initComponents() throws IOException {

		format = new ComboBoxSuggestion<Object>();

		format.setEditable(false);

		format.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PNG", "JPG" }));

		btnBrowse_1_2_2 = new ButtonRoundedWithImage();

		btnBrowse_1_2_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				jButton1ActionPerformed(e);

			}

		});

		archivosDrag = new ArrayList();

		lblPath = new JLabel("Path : ");

		error = false;

		btnBrowse_1_1_4 = new swing.SwitchButton();

		btnBrowse_1_1_4.addEventSelected(new EventSwitchSelected() {

			public void onSelected(boolean selected) {

				subcarpeta = selected;

				if (selected) {

					btnBrowse_1_1_4.setBackground(Color.decode("#019CE1"));
				}

				else {

					btnBrowse_1_1_4.setBackground(new Color(250, 250, 250));
				}

			}

		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		convert = new JLabel("New label");

		convert.setHorizontalAlignment(SwingConstants.CENTER);

		convert.setFont(new Font("Tahoma", Font.PLAIN, 16));

		btnNewButton = new ButtonRoundedWithImage();

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				convertir();

			}

		});

		btnNewButton.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/icon.png")));

		btnBrowse = new ButtonRoundedWithImage();

		btnBrowse.setBackground(Color.WHITE);

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

					try {

						format.setSelectedItem(Metodos.extraerExtension(path.getText()).toUpperCase());
					}

					catch (Exception e1) {

					}
				}

				catch (Exception e1) {

				}

			}

		});

		btnBrowse.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/folder.png")));

		btnBrowse.setText("Browse");

		path = new JTextField();

		path.setColumns(10);

		lblSizeX = new JLabel("Column :");

		lblSizeX.setFont(new Font("Tahoma", Font.PLAIN, 16));

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

		sizeX.setValue(2);

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

		lblSizeY = new JLabel("Row :");

		lblSizeY.setFont(new Font("Tahoma", Font.PLAIN, 16));

		lblSizeY.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/width.png")));

		lblSizeY.setHorizontalAlignment(SwingConstants.CENTER);

		lblDestination = new JLabel("Destination :");

		lblDestination.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblDestination.setHorizontalAlignment(SwingConstants.CENTER);

		destination = new JTextField();

		destination.setColumns(10);

		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblPath.setHorizontalAlignment(SwingConstants.CENTER);

		btnBrowse_1 = new ButtonRoundedWithImage();

		btnBrowse_1.setBackground(Color.WHITE);

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

				}

				else {

					merge.setSelected(true);

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

		btnBrowse_1_1.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_1 = new ButtonRoundedWithImage();

		btnBrowse_1_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(2);

				sizeX.setValor(2);

			}

		});

		btnBrowse_1_1_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/2.png")));

		btnBrowse_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_1.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_2 = new ButtonRoundedWithImage();

		btnBrowse_1_1_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(2);
				sizeX.setValor(3);
			}

		});

		btnBrowse_1_1_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/3.png")));

		btnBrowse_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_2.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_3 = new ButtonRoundedWithImage();

		btnBrowse_1_1_3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(3);

				sizeX.setValor(3);

			}

		});

		btnBrowse_1_1_3.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/4.png")));
		btnBrowse_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_3.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_1_1 = new ButtonRoundedWithImage();

		btnBrowse_1_1_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(2);

				sizeX.setValor(1);

			}

		});

		btnBrowse_1_1_1_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/5.png")));
		btnBrowse_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_1.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_1_2 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				sizeY.setValor(4);

				sizeX.setValor(1);

			}

		});

		btnBrowse_1_1_1_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/7.png")));

		btnBrowse_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_1_2.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_1_2_1 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(3);
				sizeX.setValor(1);
			}
		});
		btnBrowse_1_1_1_2_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/6.png")));
		btnBrowse_1_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_2_1.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_1_1_2_2 = new ButtonRoundedWithImage();
		btnBrowse_1_1_1_2_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/8.png")));
		btnBrowse_1_1_1_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sizeY.setValor(3);
				sizeX.setValor(2);
			}
		});
		btnBrowse_1_1_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBrowse_1_1_1_2_2.setBackground(Color.WHITE);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/crop.png")));

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/merge_files.png")));

		btnBrowse_1_1_4.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_1_4.setBackground(SystemColor.inactiveCaptionBorder);

		lblNewLabel_2 = new JLabel("Create subfolder in destination");

		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));

		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);

		drgndrpDragAndDrop = new DragAndDrop("", "");

		drgndrpDragAndDrop.setBackground(Color.WHITE);

		try {

			new UtilDragAndDrop(drgndrpDragAndDrop, drgndrpDragAndDrop.dragBorder, true,
					new UtilDragAndDrop.Listener() {

						public void filesDropped(java.io.File[] archivos) {

							int i = 0;

							String extension = "";

							for (File f : archivos) {

								if (i == 0) {

									archivosDrag.add(f);

									extension = Metodos.extraerExtension(f.getAbsolutePath());

								} else {

									if (Metodos.extraerExtension(f.getAbsolutePath()).equals(extension)) {

										archivosDrag.add(f);

									}

								}

								i++;

							}

							Collections.sort(archivosDrag);

							salida = archivosDrag.get(0).getAbsolutePath().substring(0,
									archivosDrag.get(0).getAbsolutePath().lastIndexOf(Metodos.saberSeparador()));

							path.setText(archivosDrag.get(0).getAbsolutePath());

							if (destination.getText().isEmpty()) {

								destination.setText(salida);

							}

							try {

								format.setSelectedItem(
										Metodos.extraerExtension(archivosDrag.get(0).getAbsolutePath()).toUpperCase());
							}

					catch (Exception e) {

							}

						}

					});

		}

		catch (Exception e1) {

		}

		Language[] list = new Language[4];

		list[0] = Language.ENGLISH;

		list[1] = Language.ESPAÑOL;

		list[2] = Language.DEUTSCH;

		list[3] = Language.FRANÇAIS;

		archivoTxt = Metodos.rutaActual() + "Languages" + Metodos.saberSeparador() + "Default_Language.txt";

		try {

			String startLanguage = Metodos.leerArchivo(archivoTxt).get(0);

			int index = Integer.parseInt(startLanguage);

			idioma = new ComboBoxLanguage(this, Language.ENGLISH, list);

			idioma.setLanguage(index);

			idioma.box.setSelectedIndex(index);

		}

		catch (Exception e) {

			idioma = new ComboBoxLanguage(this, Language.ENGLISH, list);

		}

		traducir();

		idioma.box.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (contador % 2 == 0) {

					try {

						Metodos.escribirFichero(archivoTxt,
								new ArrayList<String>(Arrays.asList("" + idioma.box.getSelectedIndex())));

					}

					catch (Exception e1) {

					}

					traducir();

				}

				contador++;

				if (contador == 2) {

					contador = 0;

				}

			}

		});

		ButtonRoundedWithImage btnBrowse_1_2 = new ButtonRoundedWithImage();

		btnBrowse_1_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					Metodos.abrirCarpeta(path.getText());

				}

				catch (IOException e1) {

				}

			}

		});

		btnBrowse_1_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/abrir.png")));

		btnBrowse_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_2.setBackground(Color.WHITE);

		ButtonRoundedWithImage btnBrowse_1_2_1 = new ButtonRoundedWithImage();

		btnBrowse_1_2_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					Metodos.abrirCarpeta(destination.getText());

				}

				catch (IOException e1) {

				}

			}

		});

		btnBrowse_1_2_1.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/abrir.png")));

		btnBrowse_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnBrowse_1_2_1.setBackground(Color.WHITE);

		btnBrowse_1_2_2.setIcon(new ImageIcon(ImgSplitter.class.getResource("/images/clean.png")));

		btnBrowse_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		btnBrowse_1_2_2.setBackground(Color.WHITE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout
						.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
								.addContainerGap().addGroup(layout
										.createParallelGroup(Alignment.LEADING).addComponent(lblSizeY,
												GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
										.addComponent(lblPath, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
										.addComponent(lblDestination, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(btnBrowse_1_1, GroupLayout.PREFERRED_SIZE, 53,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(btnBrowse_1_1_1_1, GroupLayout.PREFERRED_SIZE, 54,
														GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout
										.createSequentialGroup()
										.addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout
												.createSequentialGroup()
												.addComponent(btnBrowse_1_1_1_2_1, GroupLayout.PREFERRED_SIZE, 54,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnBrowse_1_1_1, GroupLayout.PREFERRED_SIZE, 54,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnBrowse_1_1_1_2, GroupLayout.PREFERRED_SIZE, 54,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnBrowse_1_1_1_2_2, GroupLayout.PREFERRED_SIZE, 54,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(btnBrowse_1_1_2, GroupLayout.PREFERRED_SIZE, 53,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(
														btnBrowse_1_1_3, GroupLayout.PREFERRED_SIZE, 61,
														GroupLayout.PREFERRED_SIZE))
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addGroup(layout.createSequentialGroup()
																.addComponent(sizeY, GroupLayout.PREFERRED_SIZE, 84,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(lblSizeX, GroupLayout.PREFERRED_SIZE, 144,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(sizeX, GroupLayout.PREFERRED_SIZE, 89,
																		GroupLayout.PREFERRED_SIZE))
														.addGroup(layout.createSequentialGroup()
																.addComponent(btnBrowse_1_1_4,
																		GroupLayout.PREFERRED_SIZE, 41,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE,
																		315, GroupLayout.PREFERRED_SIZE))))
										.addGap(18)
										.addGroup(layout.createParallelGroup(Alignment.LEADING)
												.addComponent(convert, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
														127, GroupLayout.PREFERRED_SIZE)
												.addGroup(Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE,
																		60, GroupLayout.PREFERRED_SIZE)
																.addGap(31))
												.addComponent(format, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
										.addGroup(layout.createSequentialGroup().addGroup(layout
												.createParallelGroup(Alignment.TRAILING)
												.addComponent(path, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 301,
														Short.MAX_VALUE)
												.addComponent(
														drgndrpDragAndDrop, GroupLayout.PREFERRED_SIZE, 301,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(
														destination, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
												.addGap(18)
												.addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout
														.createSequentialGroup()
														.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
																.addComponent(btnBrowse_1, GroupLayout.DEFAULT_SIZE,
																		135, Short.MAX_VALUE)
																.addComponent(btnBrowse, GroupLayout.DEFAULT_SIZE, 135,
																		Short.MAX_VALUE))
														.addGap(18)
														.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
																.addComponent(btnBrowse_1_2_1, 0, 0, Short.MAX_VALUE)
																.addComponent(btnBrowse_1_2, GroupLayout.PREFERRED_SIZE,
																		46, Short.MAX_VALUE)))
														.addGroup(layout.createSequentialGroup()
																.addComponent(btnBrowse_1_2_2,
																		GroupLayout.PREFERRED_SIZE, 199,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED)))))
								.addGap(419))
						.addGroup(layout.createSequentialGroup().addGap(163)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(idioma, GroupLayout.PREFERRED_SIZE, 214,
														GroupLayout.PREFERRED_SIZE)
												.addGap(180))
										.addGroup(layout.createSequentialGroup()
												.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 55,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(split, GroupLayout.PREFERRED_SIZE, 163,
														GroupLayout.PREFERRED_SIZE)
												.addGap(12)))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(merge, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 57,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18))
								.addGroup(layout.createSequentialGroup().addGap(26)
										.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(split, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(lblNewLabel, Alignment.TRAILING,
														GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE).addComponent(
												idioma, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)))
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addComponent(merge, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addGap(28)))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(10)
										.addComponent(btnBrowse_1_2_2, GroupLayout.PREFERRED_SIZE, 42,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12))
								.addComponent(drgndrpDragAndDrop, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(11)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE)
												.addComponent(path, GroupLayout.PREFERRED_SIZE, 28,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblPath, GroupLayout.PREFERRED_SIZE, 28,
														GroupLayout.PREFERRED_SIZE)))
								.addComponent(btnBrowse_1_2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
						.addGap(24)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(destination, GroupLayout.PREFERRED_SIZE, 29,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDestination, GroupLayout.PREFERRED_SIZE, 40,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(btnBrowse_1_2_1, GroupLayout.PREFERRED_SIZE, 45,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBrowse_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
						.addGap(27)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(convert, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblNewLabel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnBrowse_1_1_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGap(16)
						.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout
								.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSizeY, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(sizeY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSizeX).addComponent(sizeX, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
						.addGap(17)
						.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(format, 0, 0, Short.MAX_VALUE)
								.addComponent(btnBrowse_1_1_3, 0, 0, Short.MAX_VALUE)
								.addComponent(btnBrowse_1_1_2, 0, 0, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(btnBrowse_1_1_1_2_1, 0, 0, Short.MAX_VALUE)
												.addComponent(btnBrowse_1_1_1, 0, 0, Short.MAX_VALUE)
												.addComponent(btnBrowse_1_1_1_2_2, 0, 0, Short.MAX_VALUE)
												.addComponent(btnBrowse_1_1_1_2, GroupLayout.PREFERRED_SIZE, 54,
														Short.MAX_VALUE)
												.addComponent(btnBrowse_1_1, 0, 0, Short.MAX_VALUE)
												.addComponent(btnBrowse_1_1_1_1, GroupLayout.PREFERRED_SIZE, 50,
														Short.MAX_VALUE))))
						.addContainerGap(20, Short.MAX_VALUE)));

		getContentPane().setLayout(layout);

		setSize(new Dimension(700, 540));

		setLocationRelativeTo(null);

	}

	private void traducir() {

		ComboBoxLanguage.translateDragAndDrop(drgndrpDragAndDrop, "draganddrop", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(lblPath, "path", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(lblDestination, "destination", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(lblNewLabel_2, "subfolder", idioma.getLanguage());

		ComboBoxLanguage.translateSpinner(sizeY, "rows", idioma.getLanguage());

		ComboBoxLanguage.translateSpinner(sizeX, "columns", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(convert, "convert", idioma.getLanguage());

		ComboBoxLanguage.translateRadioButtonCustom(split, "split", idioma.getLanguage());

		ComboBoxLanguage.translateRadioButtonCustom(merge, "merge", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(lblSizeY, "row", idioma.getLanguage());

		ComboBoxLanguage.translateJlabel(lblSizeX, "column", idioma.getLanguage());

		ComboBoxLanguage.translateButtonRoundedWithImage(btnBrowse, "browse", idioma.getLanguage());

		ComboBoxLanguage.translateButtonRoundedWithImage(btnBrowse_1, "browse", idioma.getLanguage());

		ComboBoxLanguage.translateButtonRoundedWithImage(btnBrowse_1_2_2, "clear", idioma.getLanguage());

		sizeX.setLabelText(ComboBoxLanguage.translateString("columns", idioma.getLanguage()));

		sizeY.setLabelText(ComboBoxLanguage.translateString("rows", idioma.getLanguage()));

	}

}
