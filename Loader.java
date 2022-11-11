package net.byAqua3.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
	public static void main(String[] args) throws Exception {
		File file = File.createTempFile("", null,
				new File(System.getProperty("java.io.tmpdir")));
		JarFile zipFile = new JarFile(new File(%outputFile%));
		ZipEntry entry = zipFile.getEntry("lib" + File.separator + "byAqua3" + File.separator
				+ %encryptFile%);
		InputStream fileInput = zipFile.getInputStream(entry);
		OutputStream fileOutput = new FileOutputStream(file);
		int fileTemp = 0;
		while ((fileTemp = fileInput.read()) != -1) {
			fileOutput.write(fileTemp ^ %encryptKey%);
		}
		fileInput.close();
		fileOutput.flush();
		fileOutput.close();
		zipFile.close();
		file.deleteOnExit();
		Method method_addurl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		method_addurl.setAccessible(true);
		method_addurl.invoke(ClassLoader.getSystemClassLoader(), file.toURI().toURL());
		Class<?> clazz = Class.forName(%jarMain%);
		Method method_main = clazz.getMethod("main", String[].class);
		method_main.invoke(clazz.newInstance(), new Object[] { args });
	}
}
