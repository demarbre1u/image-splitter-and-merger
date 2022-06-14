import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.languages.ComboBoxLanguage;

import util.Metodos;

public class MergeImage {

	public void mergeImage(String path, String output, int rows, int cols) {

		try {

			if (rows + cols != 2) {

				int chunks = rows * cols;

				File[] imgFiles;

				int chunkWidth, chunkHeight;

				int type;

				if (ImgSplitter.archivosDrag.isEmpty()) {

					LinkedList<String> lista = new LinkedList<String>();

					Metodos.renombrarConCeros(path, "images");

					lista = Metodos.directorio(path + Metodos.saberSeparador(), "images", false);

					imgFiles = new File[lista.size()];

					for (int i = 0; i < lista.size(); i++) {

						imgFiles[i] = new File(lista.get(i));

					}

				}

				else {

					imgFiles = new File[ImgSplitter.archivosDrag.size()];

					for (int i = 0; i < ImgSplitter.archivosDrag.size(); i++) {

						imgFiles[i] = ImgSplitter.archivosDrag.get(i);

					}

				}

				BufferedImage[] buffImages = new BufferedImage[chunks];

				for (int i = 0; i < chunks; i++) {

					buffImages[i] = ImageIO.read(imgFiles[i]);
				}

				type = buffImages[0].getType();

				chunkWidth = buffImages[0].getWidth();

				chunkHeight = buffImages[0].getHeight();

				BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

				int num = 0;

				for (int i = 0; i < rows; i++) {

					for (int j = 0; j < cols; j++) {

						finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);

						num++;

					}

				}

				String extension = Metodos.extraerExtension(path);

				ImageIO.write(finalImg, extension, new File(output));

				if (!extension.equals(ImgSplitter.format.getSelectedItem().toString().toLowerCase())) {

					Metodos.convertImg(output, ImgSplitter.format.getSelectedItem().toString().toLowerCase());

					Metodos.eliminarFichero(output);

				}

				ImgSplitter.alert.mensaje(
						ComboBoxLanguage.translateString("finish_conversion", ImgSplitter.idioma.getLanguage()), 2, 15);

				Metodos.abrirCarpeta(output.substring(0, output.lastIndexOf(Metodos.saberSeparador())));

			}

		}

		catch (IndexOutOfBoundsException e) {

			ImgSplitter.alert.mensaje(ComboBoxLanguage.translateString("msg_collage", ImgSplitter.idioma.getLanguage()),
					1, 16);

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

}
