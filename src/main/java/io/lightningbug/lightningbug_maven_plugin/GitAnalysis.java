package io.lightningbug.lightningbug_maven_plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitAnalysis {
	private static final Pattern WINDOWS_FILE_SEPARATOR = Pattern.compile("[\\\\]");
	private static final Pattern LEADING_DOT_SLASH = Pattern.compile("[.][\\\\]");

	public static Git openGit() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repo = builder.readEnvironment().findGitDir().build();
		return Git.open(openRepository().getDirectory());
	}

	public static Repository openRepository() throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		return builder.readEnvironment().findGitDir().build();
	}

	public static void main(String[] args) {
		getBlameForProject();
	}

	public static void testForBug() {
		Pattern windowsFilesystemSeparators = Pattern.compile("[\\\\]");
		Pattern leadingDotSlash = Pattern.compile("[.][\\\\]");
		try (Git git = openGit()) {
			Collection<File> files = FileUtils.listFiles(FileUtils.getFile("./src"), new String[] { "java" }, true);
			for (File file : files) {
				getFileBlame(git, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getBlameForProject() {
		List<String> blameLines = new ArrayList<String>();
		
		try (Git git = openGit()) {
			Collection<File> files = FileUtils.listFiles(FileUtils.getFile("./src"), new String[] { "java" }, true);
			for (File file : files) {
				getFileBlame(git, file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blameLines;
	}

	/**
	 * Gets the blame of each line in a specified file
	 * 
	 * @param file Must not be null or a directory or else the method returns an
	 *             empty list
	 * @return List of all of the lines of code blamed
	 */
	public static void getFileBlame(Git git, File file)
			throws GitAPIException {
		if (!(file.isDirectory())) {
			BlameCommand b = git.blame();
			String filePath = LEADING_DOT_SLASH.matcher(file.getPath()).replaceFirst("");
			filePath = WINDOWS_FILE_SEPARATOR.matcher(filePath).replaceAll("/");
			final BlameResult result = b.setFilePath(filePath).call();
			RawText rawText = result.getResultContents();
			for (int i = 0; i < rawText.size(); i++) {
				PersonIdent sourceAuthor = result.getSourceAuthor(i);
				RevCommit sourceCommit = result.getSourceCommit(i);
				System.out.println(sourceAuthor.getName() + (sourceCommit != null
						? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName()
						: "") + ": " + rawText.getString(i));
			}
		}
	}
}
