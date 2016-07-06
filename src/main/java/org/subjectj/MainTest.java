package org.subjectj;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.subjectj.compiler.parser.ANTLR4DemoParser;

public class MainTest {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(args[0]);
		ANTLR4DemoParser.parse(path);
	}
}
