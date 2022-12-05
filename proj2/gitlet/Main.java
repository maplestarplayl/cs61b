package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.exitWithMessage("Please enter a command.");
        }
        Repository.setupPersistence();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs(args,1);
                if (Repository.checkIfRepository()){
                    Utils.message("A Gitlet version-control system already exists in the current directory.");
                    return;
                }
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs(args,2);
                String name = args[1];
                validateAtaRepo();
                Repository.add(name);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length == 1){
                    Utils.error("Please enter a commit message.");
                    System.out.println("Please enter a commit message.");
                    return;
                }
                validateNumArgs(args,2);
                validateAtaRepo();
                String message = args[1];
                if (Repository.checkIfNotExistAdd()){
                    System.out.println("No changes added to the commit.");
                    return;
                }
                Repository.commit(message);
                break;
            case "log":
                validateNumArgs(args,1);
                validateAtaRepo();
                Repository.log();
                break;
            case "checkout":
                validateAtaRepo();
                if (args.length == 3 ){
                    validateNumArgs(args,3);
                    //System.out.println("123");
                    Repository.checkoutHEAD(args[2]);}
                else if (args.length == 4){
                    validateNumArgs(args,4);
                    //System.out.println("124");
                    Repository.checkooutBeforeCommit(args[1],args[3]);
                }
                //else{
                    //System.out.println(args.length);
                //}
                //break;
                // to be sloved: checkout [branch name]
        }
    }



    public static void validateNumArgs( String[] args, int n) {
        if (args.length != n) {
            Utils.exitWithMessage("Incorrect operands.");
        }
    }
    public static void validateAtaRepo(){
        if (!Repository.checkIfRepository()){
            Utils.exitWithMessage("Not in an initialized Gitlet directory.");
        }
    }
}
