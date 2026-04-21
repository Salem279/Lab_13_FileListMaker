import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {

    private static ArrayList<String> list = new ArrayList<>();
    private static String currentFileName = "";
    private static boolean needsToBeSaved = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            displayList();

            String cmd = SafeInput.getRegExString(
                    in,
                    "Choose A-add, D-delete, I-insert, M-move, O-open, S-save, C-clear, V-view, Q-quit",
                    "[AaDdIiMmOoSsCcVvQq]"
            ).toUpperCase();

            try {
                switch (cmd) {
                    case "A":
                        addItem(in);
                        break;
                    case "D":
                        deleteItem(in);
                        break;
                    case "I":
                        insertItem(in);
                        break;
                    case "M":
                        moveItem(in);
                        break;
                    case "O":
                        openFile(in);
                        break;
                    case "S":
                        saveFile(in);
                        break;
                    case "C":
                        clearList(in);
                        break;
                    case "V":
                        viewList();
                        break;
                    case "Q":
                        done = quitProgram(in);
                        break;
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye.");
        in.close();
    }

    private static void displayList() {
        System.out.println("\n-----------------------------------");
        if (currentFileName.isEmpty()) {
            System.out.println("Current file: [none]");
        } else {
            System.out.println("Current file: " + currentFileName);
        }

        System.out.println("Unsaved changes: " + needsToBeSaved);

        if (list.isEmpty()) {
            System.out.println("[empty list]");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
        System.out.println("-----------------------------------");
    }

    private static void viewList() {
        System.out.println("\nList Contents:");
        if (list.isEmpty()) {
            System.out.println("[empty]");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
    }

    private static void addItem(Scanner in) {
        System.out.print("Enter item to add: ");
        String item = in.nextLine();
        list.add(item);
        needsToBeSaved = true;
        System.out.println("Item added.");
    }

    private static void deleteItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }

        viewList();
        int itemNum = SafeInput.getRangedInt(in, "Enter item number to delete", 1, list.size());
        list.remove(itemNum - 1);
        needsToBeSaved = true;
        System.out.println("Item deleted.");
    }

    private static void insertItem(Scanner in) {
        int position = SafeInput.getRangedInt(in, "Enter position to insert item", 1, list.size() + 1);
        System.out.print("Enter item to insert: ");
        String item = in.nextLine();
        list.add(position - 1, item);
        needsToBeSaved = true;
        System.out.println("Item inserted.");
    }

    private static void moveItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }

        viewList();
        int from = SafeInput.getRangedInt(in, "Enter item number to move", 1, list.size());
        int to = SafeInput.getRangedInt(in, "Enter new position", 1, list.size());

        String movedItem = list.remove(from - 1);
        list.add(to - 1, movedItem);

        needsToBeSaved = true;
        System.out.println("Item moved.");
    }

    private static void openFile(Scanner in) throws IOException {
        if (needsToBeSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(in, "You have unsaved changes. Save first?");
            if (saveFirst) {
                saveFile(in);
            }
        }

        System.out.print("Enter file name to open (.txt): ");
        String fileName = in.nextLine();

        Path file = Path.of("src", fileName);
        list = new ArrayList<>(Files.readAllLines(file));
        currentFileName = fileName;
        needsToBeSaved = false;

        System.out.println("File loaded.");
    }

    private static void saveFile(Scanner in) throws IOException {
        if (currentFileName.isEmpty()) {
            System.out.print("Enter file name to save as (.txt): ");
            currentFileName = in.nextLine();
        }

        Path file = Path.of("src", currentFileName);
        Files.write(file, list);
        needsToBeSaved = false;

        System.out.println("File saved.");
    }

    private static void clearList(Scanner in) {
        boolean confirm = SafeInput.getYNConfirm(in, "Clear the entire list?");
        if (confirm) {
            list.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static boolean quitProgram(Scanner in) throws IOException {
        if (needsToBeSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before quitting?");
            if (saveFirst) {
                saveFile(in);
            }
        }

        return SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
    }
}