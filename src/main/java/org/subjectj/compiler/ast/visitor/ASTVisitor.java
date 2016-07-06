package org.subjectj.compiler.ast.visitor;

import org.subjectj.compiler.ast.DirectoryExcludeNode;
import org.subjectj.compiler.ast.DirectoryNode;
import org.subjectj.compiler.ast.DirectoryRecursiveExcludeNode;
import org.subjectj.compiler.ast.DirectoryRecursiveNode;
import org.subjectj.compiler.ast.ExcludeNode;
import org.subjectj.compiler.ast.FileNode;
import org.subjectj.compiler.ast.IncludeNode;
import org.subjectj.compiler.ast.SubjectJNode;

public interface ASTVisitor {

	public void visit(DirectoryNode node);

	public void visit(DirectoryExcludeNode node);

	public void visit(DirectoryRecursiveNode node);

	public void visit(DirectoryRecursiveExcludeNode node);

	public void visit(FileNode node);

	public void visit(ExcludeNode node);

	public void visit(IncludeNode node);

	public void visit(SubjectJNode node);
}
