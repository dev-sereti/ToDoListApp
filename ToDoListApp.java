import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Task implements Serializable {
    private String title;
    private String description;
    private String priority;
    private LocalDate dueDate;
    private boolean isComplete;

    public Task(String title, String description, String priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isComplete = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void markAsComplete() {
        this.isComplete = true;
    }

    @Override
    public String toString() {
        return "Task [" +
                "Title='" + title + '\'' +
                ", Priority='" + priority + '\'' +
                ", Due Date=" + dueDate +
                ", Completed=" + (isComplete ? "Yes" : "No") +
                ']';
    }
}

public class ToDoListApp {
    private static final String DATA_FILE = "tasks.dat";
    private List<Task> tasks;

    public ToDoListApp() {
        tasks = loadTasks();
    }

    public void addTask(String title, String description, String priority, LocalDate dueDate) {
        tasks.add(new Task(title, description, priority, dueDate));
        saveTasks();
    }

    public void viewTasks(boolean showCompleted) {
        tasks.stream()
                .filter(task -> task.isComplete() == showCompleted)
                .sorted(Comparator.comparing(Task::getDueDate))
                .forEach(System.out::println);
    }

    public void updateTask(String title, String newTitle, String newDescription, String newPriority, LocalDate newDueDate) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setPriority(newPriority);
                task.setDueDate(newDueDate);
                saveTasks();
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void deleteTask(String title) {
        tasks.removeIf(task -> task.getTitle().equalsIgnoreCase(title));
        saveTasks();
    }

    public void markTaskAsComplete(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                task.markAsComplete();
                saveTasks();
                return;
            }
        }
        System.out.println("Task not found.");
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private List<Task> loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        ToDoListApp app = new ToDoListApp();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- To-Do List Menu ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Pending Tasks");
            System.out.println("3. View Completed Tasks");
            System.out.println("4. Update Task");
            System.out.println("5. Delete Task");
            System.out.println("6. Mark Task as Complete");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter priority (High, Medium, Low): ");
                    String priority = scanner.nextLine();
                    System.out.print("Enter due date (YYYY-MM-DD): ");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine());
                    app.addTask(title, description, priority, dueDate);
                    break;
                case 2:
                    app.viewTasks(false);
                    break;
                case 3:
                    app.viewTasks(true);
                    break;
                case 4:
                    System.out.print("Enter task title to update: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new description: ");
                    String newDescription = scanner.nextLine();
                    System.out.print("Enter new priority: ");
                    String newPriority = scanner.nextLine();
                    System.out.print("Enter new due date (YYYY-MM-DD): ");
                    LocalDate newDueDate = LocalDate.parse(scanner.nextLine());
                    app.updateTask(oldTitle, newTitle, newDescription, newPriority, newDueDate);
                    break;
                case 5:
                    System.out.print("Enter task title to delete: ");
                    String deleteTitle = scanner.nextLine();
                    app.deleteTask(deleteTitle);
                    break;
                case 6:
                    System.out.print("Enter task title to mark as complete: ");
                    String completeTitle = scanner.nextLine();
                    app.markTaskAsComplete(completeTitle);
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
