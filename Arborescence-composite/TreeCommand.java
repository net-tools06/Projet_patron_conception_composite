import java.io.File;

// Component
interface FileSystemComponent {
    void display(String indent);
}

// Leaf
class FileLeaf implements FileSystemComponent {
    private String name;

    public FileLeaf(String name) {
        this.name = name;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "|-- " + name);
    }
}

// Composite
class DirectoryComposite implements FileSystemComponent {
    private String name;
    private FileSystemComponent[] children;

    public DirectoryComposite(String name) {
        this.name = name;
    }

    public void addChildren(FileSystemComponent... children) {
        this.children = children;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "+-- " + name);
        indent += "    ";
        for (FileSystemComponent child : children) {
            child.display(indent);
        }
    }
}

// Client
public class TreeCommand {
    public static void main(String[] args) {
        String currentDirectory = System.getProperty("user.dir");
        DirectoryComposite root = buildFileSystemTree(currentDirectory);
        root.display("");
    }

    private static DirectoryComposite buildFileSystemTree(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + path);
        }

        DirectoryComposite directory = new DirectoryComposite(file.getName());
        File[] files = file.listFiles();
        if (files != null) {
            FileSystemComponent[] children = new FileSystemComponent[files.length];
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    children[i] = buildFileSystemTree(files[i].getPath());
                } else {
                    children[i] = new FileLeaf(files[i].getName());
                }
            }
            directory.addChildren(children);
        }

        return directory;
    }
}