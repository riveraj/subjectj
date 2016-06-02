package edu.rit.cs.weaver;

import java.io.IOException;

import edu.rit.cs.weaver.build.ClassBuilder;
import edu.rit.cs.weaver.build.ClassUpdateBuilder;
import edu.rit.cs.weaver.map.ClassMap;

public class WeaverDriver {

	public static void main(String[] args) {
		// Weaving the BarkSpeak class...
		ClassMap classMap = new ClassMap();

		try {
			classMap.add("BarkingDog", "BarkingDog.class");
			classMap.add("SpeakingDog", "SpeakingDog.class");
			classMap.add("TestMain", "TestMain.class");

			ClassBuilder builder = new ClassBuilder(classMap, "BarkSpeak");
			builder.composeClass("BarkingDog", "SpeakingDog").write("BarkSpeak.class");

			ClassUpdateBuilder updater = new ClassUpdateBuilder(classMap.get("TestMain"));
			updater.updateClass("BarkingDog", "BarkSpeak").updateClass("SpeakingDog", "BarkSpeak")
					.updateMethod("speak", "bark").write("TestMain.class");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Weaving the TicTacToe project...
		classMap = new ClassMap();

		try {
			classMap.add("rules_legal", "rules_legal/TTTBoard.class");
			classMap.add("setup", "setup/TTTBoard.class");
			classMap.add("rules_win", "rules_win/TTTBoard.class");
			classMap.add("ui", "ui/TTTCommands.class");

			ClassBuilder builder = new ClassBuilder(classMap, "setup/TTTBoard");
			builder.composeClass("rules_legal", "setup")
					.mergeMethods("rules_legal", "<init>", "setup", "<init>", "<init>")
					.mergeMethods("rules_legal", "markSquare", "setup", "markSquare", "markSquare")
					.write("setup/TTTBoard.class");

			classMap.add("setup", "setup/TTTBoard.class");

			builder = new ClassBuilder(classMap, "setup/TTTBoard");
			builder.composeClass("rules_win", "setup").mergeMethods("rules_win", "<init>", "setup", "<init>", "<init>")
					.mergeMethods("setup", "markSquare", "rules_win", "markSquare", "markSquare")
					.write("setup/TTTBoard.class");

			classMap.add("setup", "setup/TTTBoard.class");

			ClassUpdateBuilder updater = new ClassUpdateBuilder(classMap.get("setup"));
			updater.updateClass("rules_legal/TTTBoard", "setup/TTTBoard")
					.updateClass("rules_win/TTTBoard", "setup/TTTBoard").write("setup/TTTBoard.class");

			updater = new ClassUpdateBuilder(classMap.get("ui"));
			updater.updateClass("rules_legal/TTTBoard", "setup/TTTBoard")
					.updateClass("rules_win/TTTBoard", "setup/TTTBoard").write("ui/TTTCommands.class");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
