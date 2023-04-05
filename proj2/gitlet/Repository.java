package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File LOG = join(GITLET_DIR, "log");
    public static final File Global_Log = join(GITLET_DIR, "global_log");
    public static final File Objects = join(GITLET_DIR, "objects");
    public static final File Staging_add = join(GITLET_DIR, "staging_add");
    public static final File Staging_rem = join(GITLET_DIR, "staging_remove");
    public static final File Commitee = join(GITLET_DIR, "commitee");
    public static final File HEAD = join(GITLET_DIR, "head");
    public static final File master = join(GITLET_DIR, "master");
    public static final File Branches = join(GITLET_DIR, "branches");
    /* TODO: fill in the rest of this class. */

    /**
     * Helper Methods
     */
    public static void setupPersistence() {
        try {
            if (!GITLET_DIR.exists()) {
                GITLET_DIR.mkdir();
            }
            if (!LOG.exists()) {
                LOG.createNewFile();
            }
            if (!Global_Log.exists()) {
                Global_Log.createNewFile();
            }
            if (!Objects.exists()) {
                Objects.mkdir();
            }
            if (!Staging_add.exists()) {
                Staging_add.mkdir();
            }
            if (!Staging_rem.exists()) {
                Staging_rem.mkdir();
            }
            if (!HEAD.exists()) {
                HEAD.createNewFile();
            }
            if (!Branches.exists()) {
                Branches.mkdir();
            }
            if (!Commitee.exists()) {
                Commitee.mkdir();
            }
            if (!master.exists()) {
                master.createNewFile();
            }

        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
    }

    public static boolean checkIfRepository() {
        return GITLET_DIR.exists();
    }

    public static boolean checkIfNotExistAdd() {
        if (Staging_add.delete()) {
            Staging_add.mkdir();
            return true;
        }
        return false;
    }

    /**
     * Commit-related Helper Methods
     */
    public static Commit returnCommitByHead() {
        String index = Utils.readContentsAsString(HEAD);
        File commit = join(Commitee, index);
        try {
            Commit com = Utils.readObject(commit, gitlet.Commit.class);
            return com;
        } catch (IllegalArgumentException e){
            System.err.println("读取对象发生异常：" + e.getMessage());
            return null;
        }
    }

    public static Commit returnCommitByIndex(String index) {
        File commit = join(Commitee, index);
        if (!commit.exists()) {
            System.out.println("No commit with that id exists.");
            return null;
        }
        try {
            Commit com = Utils.readObject(commit, gitlet.Commit.class);
            return com;
        } catch (IllegalArgumentException e){
            System.err.println("读取对象发生异常：" + e.getMessage());
            return null;
        }
    }

    public static void CommitSaveandUpdatePointer(Commit com, File f) {
        String index = com.getHash();
        File tobesaved = Utils.join(Commitee, index);
        Fileinitialize(tobesaved);
        Utils.writeObject(tobesaved, com);
        Utils.writeContentsSafe(HEAD, index);
    }

    public static String addHelperGetFileIndex(String name, Commit com) {
        if (com.treeDirectory != null) {
            Map<String, String> map = com.treeDirectory.returnMap();
            String index = map.get(name);
            return index;
        }
        return null;
    }

    public static void LogHelperRecordCommit(Commit com, File toberecord) {
        String identifier = "===" + "\n";
        String last = Utils.readContentsAsString(toberecord);
        String firstline = "commit" + " " + com.getHash() + "\n";
        String secondline = "Data:" + com.getTimestamp() + "\n";
        String thirdline = com.getMessage();
        String finalres = last + identifier + firstline + secondline + thirdline + "\n";
        Utils.writeContentsSafe(toberecord, finalres);
    }

    public static void Fileinitialize(File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
    }

    public static boolean checkIfFileEqualGivenCommit(String name, Commit com) {
        File cur = join(CWD, name);
        if (addHelperGetFileIndex(name, com) != null) {
            String oriindex = addHelperGetFileIndex(name, com);

            try {
                byte[] bytee = Utils.readContents(cur);
                String curindex = Utils.sha1(bytee);
                // 处理读取到的对象
                if (oriindex.equals(curindex)) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("读取对象发生异常：" + e.getMessage());
            }

        }
        return false;
    }

    /**
     * To be sloved : Only able to process 2 branches
     */
    public static boolean checkIfAtBranch() {
        File flag = join(GITLET_DIR, "flag");
        Fileinitialize(flag);
        String val = Utils.readContentsAsString(flag);
        if (val.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Below are the commands gitlet supports
     */
    //Initialize the repo and create the first commit
    public static void init() {
        Commit initial_commit = new Commit("initial commit", null);
        initial_commit.calcHash();
        CommitSaveandUpdatePointer(initial_commit, HEAD);
        LogHelperRecordCommit(initial_commit, Global_Log);
        LogHelperRecordCommit(initial_commit, LOG);
    }

    public static void add(String name) {
        File tobeadd = join(CWD, name);
        // if file not exists,exit
        if (!tobeadd.exists()) {
            Utils.error("File does not exist.");
            System.exit(0);
        }
        //if the file to be added is identical to the current commit,then exit
        if (checkIfFileEqualGivenCommit(name, returnCommitByHead())) {
            return;
        }
        //copy the file to the staging area
        File copyAdd = join(Staging_add, name);
        try {
            Files.copy(tobeadd.toPath(), copyAdd.toPath());
        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
    }

    public static void commit(String message) {
        // copy the last commit to the new commit
        Commit last = returnCommitByHead();
        Commit now = last;
        now.parent = last.getHash();
        now.message = message;
        /** to be solved : the representation of timestamp */
        //Deal with the files
        File[] files = Staging_add.listFiles();
        File[] files1 = Staging_rem.listFiles();

        now.changeTreeDirectoryAdd(files);
        now.changeTreeDirectoryRemove(files1);
        now.calcHash();
        LogHelperRecordCommit(now, Global_Log);
        CommitSaveandUpdatePointer(now, HEAD);
        // record the commit in Log
        if (!checkIfAtBranch()) {
            LogHelperRecordCommit(now, LOG);
            File mainpointer = master;
            Utils.writeContentsSafe(mainpointer, now.getHash());
        } else {
            File Branchlog = join(GITLET_DIR, "branchlog");
            LogHelperRecordCommit(now, Branchlog);
            File[] branch = Branches.listFiles();
            for (File file : branch) {
                Utils.writeContentsSafe(file, now.getHash());
                CommitSaveandUpdatePointer(now, file);
            }
        }
        //clear the staging area after commit
        for (File file : files) {
            file.delete();
        }
        for (File file : files1) {
            file.delete();
        }
    }

    public static void log() {
        if (!checkIfAtBranch()) {
            String res = Utils.readContentsAsString(LOG);
            System.out.println(res);
        } else {
            File toberead = join(GITLET_DIR, "branchlog");
            String res = Utils.readContentsAsString(toberead);
            System.out.println(toberead);
        }
    }

    public static void globalLog() {
        String res = Utils.readContentsAsString(Global_Log);
        System.out.println(res);
    }

    public static void checkoutHEAD(String fileName) {
        Commit com = returnCommitByHead();
        Map<String, String> map = com.treeDirectory.returnMap();
        if (map.containsKey(fileName)) {
            Blob b = Blob.returnBlobByIndex(map.get(fileName));
            File tooverride = join(CWD, fileName);
            Fileinitialize(tooverride);
            Utils.writeContentsSafe(tooverride, b.getByte());
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void checkoutBeforeCommit(String commitindex, String filename) {
        Commit com = returnCommitByIndex(commitindex);
        if (com == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Map<String, String> map = com.treeDirectory.returnMap();
        if (map.containsKey(filename)) {
            Blob b = Blob.returnBlobByIndex((String) map.get(filename));
            File tooverride = join(CWD, filename);
            Fileinitialize(tooverride);
            Utils.writeContentsSafe(tooverride, b.getByte());
        } else {
            System.out.println("File does not exist in that commit.");
        }

    }

    public static void HPDeleteCWDAndReadCommit(Commit com) {
        File[] files = CWD.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitlet")) {
                file.delete();
            }
        }
        // iterate the map returned by the head commit and save the file to the CWD
        Map<String, String> map = com.treeDirectory.returnMap();
        map.forEach((key, value) -> {
            Blob b = Blob.returnBlobByIndex(value);
            File tooverride = join(CWD, key);
            Fileinitialize(tooverride);
            Utils.writeContentsSafe(tooverride, b.getByte());
        });


    }

    /**
     * 用途 ： 切换到新的分支
     * 实现方式： 由两部分构成，第一部分是设置flag来对是否是branch进行锚定
     * 第二部分，将要切换的分支的节点的文件覆盖当前的文件，还要更新head指针
     */
    public static void checkoutNewBranch(String branchname) {
        File flag = join(GITLET_DIR, "flag");
        Fileinitialize(flag);
        String HeadIndex = Utils.readContentsAsString(HEAD);
        if (!branchname.equals("master")) {
            File branch = join(Branches, branchname);
            if (!branch.exists()) {
                Utils.exitWithMessage("No such branch exists.");
            } else if (checkIfAtBranch()) {
                Utils.exitWithMessage("No need to checkout the current branch.");
            } else {
                String index = Utils.readContentsAsString(branch);
                HPDeleteCWDAndReadCommit(returnCommitByIndex(index));
                Utils.writeContentsSafe(flag, "true");
                //Update the head pointer
                Utils.writeContentsSafe(HEAD, index);
                Utils.writeContentsSafe(flag, "true");
            }
        } else {
            if (Utils.readContentsAsString(flag).equals("false")) {
                Utils.exitWithMessage("No need to checkout the current branch.");
            }
            Utils.writeContentsSafe(flag, "false");
            String index = Utils.readContentsAsString(master);
            HPDeleteCWDAndReadCommit(returnCommitByIndex(index));
            //Update the head pointer
            Utils.writeContentsSafe(HEAD, index);
        }


    }

    //Either remove it from the staging are or remove it from last commit
    public static void rm(String filename) {
        File Stagetobedeleted = join(Staging_add, filename);
        Commit com = returnCommitByHead();
        Map<String, String> map1 = com.treeDirectory.returnMap();
        //if the file has already been added to the staging area, just remove it from the staging area
        if (Stagetobedeleted.exists()) {
            Stagetobedeleted.delete();
            return;
        }
        // add files(to be deleted) to the Staging area of remove so that status could track this
        else if (map1.containsKey(filename)) {
            //map1.remove(filename);
            //com.treeDirectory.map = map1;
            //File commitChanged = join(Commitee,com.getHash());
            //Utils.writeObject(commitChanged,com);
            File CWDtobedelete = join(CWD, filename);
            File copyAdd = join(Staging_rem, filename);
            try {
                Files.copy(CWDtobedelete.toPath(), copyAdd.toPath());
            } catch (IOException excp) {
                System.out.println(excp.getMessage());
            }
            CWDtobedelete.delete();
        } else {
            Utils.exitWithMessage("No reason to remove the file.");
        }

    }

    public static void find(String message) {
        File[] files = Commitee.listFiles();
        int fla = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                String name = file.getName();
                Commit com = returnCommitByIndex(name);
                if (com.getMessage().equals(message)) {
                    System.out.println(name);
                    fla += 1;
                }
            } else {                                   // Why Commitee may have multiple structures???
                File[] filess = file.listFiles();
                for (File filee : filess) {
                    String name = filee.getName();
                    Commit com = returnCommitByIndex(name);
                    if (com.getMessage().equals(message)) {
                        System.out.println(name);
                        fla += 1;
                    }
                }
            }
        }
        if (fla == 0) {
            System.out.println("Found no commit with that message.");
        }

    }

    public static void status() {
        List<String> cwdfilenames = new ArrayList<>();
        List<String> strnames = Utils.plainFilenamesIn(CWD);
        for (String name : strnames) {
            cwdfilenames.add(name);
        }
        // Branches
        System.out.println("=== Branches ===");
        System.out.println("*master");
        //Print out names of each branch
        File[] branchfiles = Branches.listFiles();
        for (File f : branchfiles) {
            System.out.println(f.getName());
        }
        //Staged files
        System.out.println("=== Staged Files ===");
        File[] stagedFiles = Staging_add.listFiles();
        for (File file : stagedFiles) {
            System.out.println(file.getName());
            cwdfilenames.remove(file.getName());
        }

        //Removed Files
        System.out.println("=== Removed Files ===");
        File[] removedFiles = Staging_rem.listFiles();
        for (File file : removedFiles) {
            System.out.println(file.getName());
        }

        // Modifications Not Staged For Commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (cwdfilenames != null) {
            for (int i = 0, len = cwdfilenames.size(); i < len; i++) {
                String each = cwdfilenames.get(i);
                if (checkIfFileEqualGivenCommit(each, returnCommitByHead())) {
                    cwdfilenames.remove(each);
                    len -= 1;
                    i -= 1;
                } else if (addHelperGetFileIndex(each, returnCommitByHead()) != null) {
                    cwdfilenames.remove(each);
                    len -= 1;
                    i -= 1;
                    System.out.println(each);
                }
            }
        }

        //Untracked Files
        System.out.println("=== Untracked Files ===");
        for (String filename : cwdfilenames) {
            System.out.println(filename);
        }
    }

    public static void branch(String branchname) {
        //创建 三个文件，flag,branch，branchlog。前者表示该branch是否激活，中者显示branch所指向的commit，后者表示该branch提交的记录
        File flagForIfChangeBranch = join(GITLET_DIR, "flag");
        File branch = join(Branches, branchname);
        File branchlog = join(GITLET_DIR, "branchlog");
        if (branch.exists()) {
            Utils.exitWithMessage("A branch with that name already exists.");
        }
        Fileinitialize(flagForIfChangeBranch);
        Fileinitialize(branch);
        Fileinitialize(branchlog);
        // 将branch和head指向同一个commit
        String index = returnCommitByHead().getHash();
        Utils.writeContentsSafe(branch, index);

    }

    public static void removeBranch(String branchname) {
        File b = join(Branches, branchname);
        if (!b.exists()) {
            Utils.exitWithMessage("A branch with that name does not exist.");
        } else if (checkIfAtBranch()) {
            Utils.exitWithMessage("Cannot remove the current branch.");
        } else {
            b.delete();
            File flagForIfChangeBranch = join(GITLET_DIR, "flag");
            File branchlog = join(GITLET_DIR, "branchlog");
            branchlog.delete();
            flagForIfChangeBranch.delete();
        }


    }

    public static void HPDeleteStagingarea() {
        File[] addedfiles = Staging_add.listFiles();
        File[] deletedfiles = Staging_rem.listFiles();
        for (File f : addedfiles) {
            f.delete();
        }
        for (File f : deletedfiles) {
            f.delete();
        }
    }

    /**
     * Restore all files of the specified commit and delete files that tracked by other commits
     * REmove files from the staging area
     *
     * @param commitindex
     */

    public static void reset(String commitindex) {
        returnCommitByIndex(commitindex);//check if exists
        //Restore all files of the specified commit and delete files that tracked by other commit
        Commit tobereset = returnCommitByIndex(commitindex);
        HPDeleteCWDAndReadCommit(tobereset);/**To be fixed:Also delete files not being tracked*/
        File[] addedfiles = Staging_add.listFiles();
        File[] deletedfiles = Staging_rem.listFiles();
        //REmove files from the staging area
        HPDeleteStagingarea();
        //Update branch commit and HEAD
        Utils.writeContentsSafe(HEAD, tobereset.getHash());
        if (checkIfAtBranch()) {
            File[] branch = Branches.listFiles();
            for (File f : branch) {
                Utils.writeContentsSafe(f, tobereset.getHash());
            }
        } else {
            Utils.writeContentsSafe(master, tobereset.getHash());
        }
    }


}
