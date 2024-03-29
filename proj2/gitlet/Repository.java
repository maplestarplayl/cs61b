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

    public static boolean checkIfNotExistAddAndRem() {
        if (Staging_add.delete()) {
            Staging_add.mkdir();
            if (Staging_rem.delete()){
                Staging_rem.mkdir();
                return true;
            }
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
            System.err.println("Exception of reading Objects:" + e.getMessage());
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
            System.err.println("Exception of reading Objects:" + e.getMessage());
            return null;
        }
    }
    public static Commit returnCommitByBranch(){
        File[] branchname = Branches.listFiles();
        for (File f :branchname){
            String branchindex = Utils.readContentsAsString(f);
            return returnCommitByIndex(branchindex);
        }
        return null;
    }
    public static Commit returnCommitBySpiltPoint(){
        File spiltpoint = Utils.join(GITLET_DIR,"spiltpoint");
        String spiltpointindex = Utils.readContentsAsString(spiltpoint);
        return returnCommitByIndex(spiltpointindex);
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
    public static boolean checkIfFileInCommit(String filename,Commit com){
        Map<String,String> map = com.treeDirectory.returnMap();
        return map.containsKey(filename);
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
    public static void Directoryitialize(File f){
        if (!f.exists()){
            f.mkdir();
        }
        return;
    }

    public static boolean checkIfFileEqualGivenCommit(String name, Commit com) {
        File cur = join(CWD, name);
        if (addHelperGetFileIndex(name, com) != null) {
            String oriindex = addHelperGetFileIndex(name, com);

            try {
                byte[] bytee = Utils.readContents(cur);
                String curindex = Utils.sha1(bytee);

                if (oriindex.equals(curindex)) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Exception of reading Objects:" + e.getMessage());
            }

        }
        return false;
    }

    /**
     * To be sloved : Only able to process 2 branches
     */
    public static boolean checkIfAtBranch() {
        File flag = join(GITLET_DIR, "flag");


        if (!flag.exists()){
            return false;
        }
        else if (Utils.readContentsAsString(flag).equals("true")) {
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
            System.out.println(res);
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
     * Usage : checkout new branch
     * Method of implementation : Be made up of two parts, the first is to set a flag to make sure if at branch
     * Second part,override the files in the CWD with the checkout-commit's files,also need to update the pointer
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
        if (!checkIfAtBranch()){
            System.out.println("*master");
            //Print out names of each branch
            File[] branchfiles = Branches.listFiles();
            for (File f : branchfiles) {
                System.out.println(f.getName());
            }
        }else {
            System.out.println("master");
            File[] branchfiles = Branches.listFiles();
            for (File f : branchfiles) {
                System.out.println("*"+f.getName());
            }
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
        //Create 4 Files，flag,branch，branchlog,spiltpoint.The first represents branch is actived,secong shows branch's commit,third shows branch's commit log，Finally shows spiltpoint's commit
        File flagForIfChangeBranch = join(GITLET_DIR, "flag");
        File branch = join(Branches, branchname);
        File branchlog = join(GITLET_DIR, "branchlog");
        File spiltpoint = join(GITLET_DIR,"spiltpoint");
        if (branch.exists()) {
            Utils.exitWithMessage("A branch with that name already exists.");
        }
        Fileinitialize(flagForIfChangeBranch);
        Fileinitialize(branch);
        Fileinitialize(branchlog);
        Fileinitialize(spiltpoint);
        // 将branch和head指向同一个commit
        String index = returnCommitByHead().getHash();
        Utils.writeContentsSafe(branch, index);
        Utils.writeContentsSafe(spiltpoint,index);

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
            File spiltpoint = join(GITLET_DIR,"spiltpoint");
            branchlog.delete();
            flagForIfChangeBranch.delete();
            spiltpoint.delete();
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

    /**Helper Method
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

    public static boolean HPcheckIfHeadAtBranch(String branchname){
        Commit Head = returnCommitByHead();
        Commit branch = returnCommitByBranch();
        Commit cur = branch;
        while (cur.parent != null){
            if (cur != Head){
                Commit parent = returnCommitByIndex(cur.parent);
                cur = parent;
                continue;
            }
            return true;
        }
        return false;
    }
    public static boolean HPcheckFileIfInSpilt(File f){
        Commit spiltpoint = returnCommitBySpiltPoint();
        Map<String,String> spiltpointmap = spiltpoint.treeDirectory.returnMap();
        if (spiltpointmap.containsKey(f.getName())){
            return true;
        }
        return false;
    }
    //Return filehash if file modified else return null
    public static String checkFileIfModified(String filename,Commit com){
        Commit spiltpoint = returnCommitBySpiltPoint();
        Map<String,String> spiltpointmap = spiltpoint.treeDirectory.returnMap();
        Map<String,String> commitmap = com.treeDirectory.returnMap();
        if (commitmap.get(filename) != spiltpointmap.get(filename)){
            return commitmap.get(filename);
        }
        return null;
    }
    public static void merge(String branchname) {
        //deal with the failure cases
        File branch = Utils.join(Branches, branchname);
        if (!branch.exists()){
            Utils.exitWithMessage("A branch with that name does not exist.");
        }
        if (HPcheckIfHeadAtBranch(branchname)){
            Utils.exitWithMessage("Cannot merge a branch with itself.");
        }
        if (!checkIfNotExistAddAndRem()){
            Utils.exitWithMessage("You have uncommitted changes.");
        }
        //7 cases of merge
        //
        //Get 3 commits by helper methods
        Commit Head = returnCommitByHead();
        Commit branches = returnCommitByBranch();
        Commit spiltpoint = returnCommitBySpiltPoint();

        //Process files in Head first:Case 2,3,4,6
        File[] CWDfiles = CWD.listFiles();
        Set<String> filenameset = new HashSet<>();
        for (File f:CWDfiles){
            if (f.isDirectory()){
                continue;
            }
            filenameset.add(f.getName());
        }
        Map<String,String> Headmap = Head.treeDirectory.returnMap();
        Map<String,String> branchmap = branches.treeDirectory.returnMap();
        for (String filename : Headmap.keySet()){
            File file = join(CWD,filename);
            if (file.isDirectory()){
                continue;
            }
            String headfilehash = checkFileIfModified(filename,Head);
            //Case : 4 Not in spilt nor branch but in Head
            if (!HPcheckFileIfInSpilt(file) && !branchmap.containsKey(filename)){
                Repository.add(filename);
                filenameset.remove(file.getName());
            //Case (1) and 8 :Head unmodified
            } else if(headfilehash == null &&branchmap.containsKey(filename)){
                //Case 8:Both unmodified
                String branchfilehash = checkFileIfModified(filename,branches);
                if (branchfilehash == null){
                    Repository.add(filename);
                    filenameset.remove(file.getName());
                }
                else{
                    ;
                }
            //Case 6:Head unmodified but not present in branch
            } else if (headfilehash == null &&!branchmap.containsKey(filename)){
                File copyRem = join(Staging_rem, filename);
                filenameset.remove(file.getName());
                try {
                    Files.copy(file.toPath(), copyRem.toPath());
                } catch (IOException excp) {
                    System.out.println(excp.getMessage());
                }
            //Case 2 and 3:Head modified but branch unmodified/modified
            } else if (headfilehash != null && branchmap.containsKey(filename)){
                String branchfilehash = checkFileIfModified(filename,branches);
                //Case 2:Head modified but branch unmodified
                if (branchfilehash == null){
                    Repository.add(filename);
                    filenameset.remove(file.getName());
                }
                //Case 3:Both modified in same way
                else if(branchfilehash.equals(headfilehash)){
                    Repository.add(filename);
                    filenameset.remove(file.getName());
                }
                /**To be continued: add content into the conflicted file
                   Case 3 :Both modified but in different way
                 */
                else{
                    File conflictflag = Utils.join(Branches,"conflictflag");
                    //Check if file has been committed
                    if (conflictflag.exists() && checkIfFileInCommit(filename,Head)){
                        ;
                    }
                    else{
                        Fileinitialize(conflictflag);
                        Utils.exitWithMessage("Encountered a merge conflict.");
                    }
                }
             //Case 9 : Head modified but not present in other
            } else if (headfilehash != null && !branchmap.containsKey(filename)){
                File copyRem = join(Staging_rem, filename);
                filenameset.remove(file.getName());
                try {
                    Files.copy(file.toPath(), copyRem.toPath());
                } catch (IOException excp) {
                    System.out.println(excp.getMessage());
                }
            }
        }

        //Process branch then
        //Case 1 : Modified in branch but not in Head
        Repository.checkoutNewBranch(branchname);
        for (String filename :filenameset){
            Repository.add(filename);
        }


        Set<File> fileset = new HashSet<>();
        File[] CWDfile = CWD.listFiles();
        fileset.addAll(Arrays.asList(CWDfile));
        //Remove files appeared in Head
        fileset.removeIf(f -> Headmap.containsKey(f.getName()));
        for (File f:fileset){
            if (f.isDirectory()){
                continue;
            }
            //Case 5:not in spilt nor Head but in branch
            if (!HPcheckFileIfInSpilt(f)){
                Repository.add(f.getName());
            }
            //Case 7:unmodified in branch but not present in Head
            else if (checkFileIfModified(f.getName(),branches) == null){
                ;
            }
            //Case 10: modified in branch but not present in Head
            else {
                Repository.add(f.getName());
            }
        }
        Repository.checkoutNewBranch("master");
        Repository.commit("This is a merge commit.");

    }
}
