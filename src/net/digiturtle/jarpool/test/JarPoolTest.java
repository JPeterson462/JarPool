package net.digiturtle.jarpool.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.digiturtle.jarpool.JarPool;

public class JarPoolTest {
	
	public static void main(String[] args) throws IOException {
		JarPool pool = new JarPool(null);
		pool.runJar(1, "program1.jar", "Hey 100 3.14");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(pool.getJarInputStream(1)))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				System.out.println("OUT> " + line);
			}
		}
		pool.shutdown();
	}

}
