package io.lightningbug.lightningbug_maven_plugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitAnalysis {

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
		test();
	}
	
	public static void testForBug() {
		try (Repository repo = openRepository()) {
			final String[] list = new File(".").list();
		    Collection<File> files = FileUtils.listFiles(FileUtils.getFile("."), new String[] { "java" }, true);
			for (File file : files) {
				if (!(file.isDirectory())) {
					AnyObjectId commitID = repo.resolve("HEAD");
					System.out.println("Commit ID " + commitID);
					BlameCommand b = new Git(repo).blame();
					BlameResult result = b.setFilePath(file.getPath()).call();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void test() {
		try (Repository repo = openRepository()) {
			final String[] list = new File(".").list();
			Config cfg = repo.getConfig();
			String name = cfg.getString("user", null, "name");
            String email = cfg.getString("user", null, "email");
            String url = cfg.getString("remote", "origin", "url");
            System.out.println(name + ": " + email + ": " + url);
			cfg.getSections().forEach(section->{System.out.println(section + "-> ");cfg.getSubsections(section).forEach(System.out::println);});
			System.out.println(cfg.getString(ConfigConstants.CONFIG_BRANCH_SECTION, "master", ConfigConstants.CONFIG_KEY_REMOTE));
			
		    Collection<File> files = FileUtils.listFiles(FileUtils.getFile("."), new String[] { "java" }, true);
			for (String s : list) {
				System.out.println(s);
			}
			if (list == null) {
				throw new IllegalStateException("Did not find any files at " + new File(".").getAbsolutePath());
			}

			for (File file : files) {
				if (!(file.isDirectory())) {
					System.out.println("Blaming " + file.getPath());
					AnyObjectId commitID = repo.resolve("HEAD");
					System.out.println("Commit ID " + commitID);
					BlameCommand b = new Git(repo).blame();
		 //           b.setStartCommit(commitID);
					final BlameResult result = b.setFilePath(file.getPath()).call();
					final RawText rawText = result.getResultContents();
					for (int i = 0; i < rawText.size(); i++) {
						final PersonIdent sourceAuthor = result.getSourceAuthor(i);
						final RevCommit sourceCommit = result.getSourceCommit(i);
						System.out.println(sourceAuthor.getName() + (sourceCommit != null
								? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName()
								: "") + ": " + rawText.getString(i));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the blame of each line in a specified file
	 * 
	 * @param file	Must not be null or a directory or else the method returns an empty list
	 * @return		List of all of the lines of code blamed
	 */
	public static List<String> getBlame(File file) {
		List<String> blameLines = new ArrayList<String>();
		if (file!= null && file.exists() && file.isFile()) {
			try (Git git = openGit()) {
				final BlameResult result = git.blame().setFilePath(file.getPath())
						.setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
				if (result != null) {
					final RawText rawText = result.getResultContents();
					for (int i = 0; i < rawText.size(); i++) {
						final PersonIdent sourceAuthor = result.getSourceAuthor(i);
						final RevCommit sourceCommit = result.getSourceCommit(i);

						final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm");
						blameLines.add(sourceAuthor.getName() + sourceCommit.getAuthorIdent().getEmailAddress()
								+ (sourceCommit != null
										? " - " + DATE_FORMAT.format(((long) sourceCommit.getCommitTime()) * 1000)
												+ " - " + sourceCommit.getName()
										: "")
								+ ": " + rawText.getString(i));
					}
				} else {
					System.out.println("Showing null");
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (GitAPIException ex) {
				ex.printStackTrace();
			}
		}
		return blameLines;
	}

}
