package org.subjectj.compiler.ast;

import java.nio.file.Path;
import java.util.Collection;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public class ExcludeNode implements ASTNode {

	private final Collection<Path> paths;

	public ExcludeNode(Collection<Path> paths) {
		this.paths = paths;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	public Collection<Path> getPaths() {
		return this.paths;
	}
}
