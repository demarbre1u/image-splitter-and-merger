import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import util.Metodos;

public class MergeImage {

	public void mergeImage(String path, String output, int rows, int cols) {

		try {

			int chunks = rows * cols;

			int chunkWidth, chunkHeight;

			int type;

			File[] imgFiles = new File[chunks];

			LinkedList<String> lista = new LinkedList<String>();

			Metodos.renombrarConCeros(path, "images");

			lista = Metodos.directorio(path, "images", true, true);

			for (int i = 0; i < chunks; i++) {

				imgFiles[i] = new File(lista.get(i));

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

			ImageIO.write(finalImg, "png", new File(output));

			ImgSplitter.alert.mensaje("Conversion terminada", 2, 20);

			Metodos.abrirCarpeta(output.substring(0, output.lastIndexOf(Metodos.saberSeparador())));

		}

		catch (IndexOutOfBoundsException e) {

			ImgSplitter.alert.mensaje("Faltan imagenes para el collage", 1, 16);

		}

		catch (Exception e) {

		}
	}

}
