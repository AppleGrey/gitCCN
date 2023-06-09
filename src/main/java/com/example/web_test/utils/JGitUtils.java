package com.example.web_test.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JGitUtils {
    public static Git openRpo(String dir){
        Git git = null;
        try {
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(Paths.get(dir, ".git").toFile())
                    .build();
            git = new Git(repository);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return git;
    }

    public static List<String> getFiles(String warePath, String branch) throws IOException, GitAPIException {
        Git git = Git.open(new File(warePath));//打开仓库
        git.checkout().setName(branch).call();
        ObjectId head = git.getRepository().resolve("HEAD^{tree}");//获取文件树

        List<String> fileList = new ArrayList<>();

        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
            treeWalk.addTree(head);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                fileList.add(treeWalk.getPathString());
            }
        }

        return fileList;
    }
}
