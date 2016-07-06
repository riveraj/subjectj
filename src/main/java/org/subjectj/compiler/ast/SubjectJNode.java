package org.subjectj.compiler.ast;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public class SubjectJNode implements ASTNode {

	private final IncludeNode include;

	public SubjectJNode(IncludeNode include) {
		this.include = include;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	public IncludeNode getIncludeNode() {
		return this.include;
	}
}
