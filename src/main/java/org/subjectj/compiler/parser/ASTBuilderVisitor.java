package org.subjectj.compiler.parser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.subjectj.compiler.ast.ASTNode;
import org.subjectj.compiler.ast.DirectoryExcludeNode;
import org.subjectj.compiler.ast.DirectoryNode;
import org.subjectj.compiler.ast.DirectoryRecursiveExcludeNode;
import org.subjectj.compiler.ast.DirectoryRecursiveNode;
import org.subjectj.compiler.ast.ExcludeNode;
import org.subjectj.compiler.ast.FileNode;
import org.subjectj.compiler.ast.IncludeNode;
import org.subjectj.compiler.parser.SubjectJParser.DirectoryContext;
import org.subjectj.compiler.parser.SubjectJParser.FileContext;
import org.subjectj.compiler.parser.SubjectJParser.PathContext;

public class ASTBuilderVisitor extends SubjectJBaseVisitor<ASTNode> {

	@Override
	public ASTNode visitDirectory(SubjectJParser.DirectoryContext context) {
		Path path = Paths.get(context.name().getText());
		return new DirectoryNode(path);
	}

	@Override
	public ASTNode visitDirectoryExclude(SubjectJParser.DirectoryExcludeContext context) {
		Path path = Paths.get(context.name().getText());
		ASTNode node = context.exclude().accept(this);
		return new DirectoryExcludeNode(path, node);
	}

	@Override
	public ASTNode visitDirectoryRecursive(SubjectJParser.DirectoryRecursiveContext context) {
		Path path = Paths.get(context.name().getText());
		return new DirectoryRecursiveNode(path);
	}

	@Override
	public ASTNode visitDirectoryRecursiveExclude(SubjectJParser.DirectoryRecursiveExcludeContext context) {
		Path path = Paths.get(context.name().getText());
		ASTNode node = context.exclude().accept(this);
		return new DirectoryRecursiveExcludeNode(path, node);
	}

	@Override
	public ASTNode visitFile(SubjectJParser.FileContext context) {
		Path path = Paths.get(context.name().getText());
		return new FileNode(path);
	}

	@Override
	public ASTNode visitExclude(SubjectJParser.ExcludeContext context) {
		Collection<Path> paths = new ArrayList<>();

		for (DirectoryContext directory : context.directory()) {
			paths.add(Paths.get(directory.name().getText()));
		}

		for (FileContext file : context.file()) {
			paths.add(Paths.get(file.name().getText()));
		}

		return new ExcludeNode(paths);
	}

	@Override
	public ASTNode visitInclude(SubjectJParser.IncludeContext context) {
		Collection<ASTNode> includes = new ArrayList<>();

		for (PathContext path : context.path()) {
			ASTNode include = path.accept(this);
			includes.add(include);
		}

		return new IncludeNode(includes);
	}

	@Override
	public ASTNode visitPath(SubjectJParser.PathContext context) {
		return context.getChild(0).accept(this);
	}

	@Override
	public ASTNode visitSubject(SubjectJParser.SubjectContext context) {
		return context.include().accept(this);
	}
}
