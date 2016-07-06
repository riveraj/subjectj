package org.subjectj.compiler.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.subjectj.compiler.ast.ASTNode;
import org.subjectj.compiler.ast.visitor.SimplePrintingVisitor;

public class ANTLR4DemoParser {

	public static void parse(Path path) throws IOException {
		try (Reader file = Files.newBufferedReader(path)) {
			SubjectJLexer lexer = new SubjectJLexer(new ANTLRInputStream(file));
			SubjectJParser parser = new SubjectJParser(new CommonTokenStream(lexer));
			parser.addErrorListener(new BaseErrorListener() {
				@Override
				public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
						int charPositionInLine, String msg, RecognitionException e) {
					throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
				}
			});
			ASTNode tree = parser.subject().accept(new ASTBuilderVisitor());
			tree.accept(new SimplePrintingVisitor());
		}
	}
}
