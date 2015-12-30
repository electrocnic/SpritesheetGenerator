import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 30.12.2015.
 */
public class Node<T> {
    private T data;
    private Node<File> parent;
    private List<Node> files=null;
    private List<Node<File>> directories=null;
    private FileType type = null;

    public enum FileType {
        FILE,
        DIRECTORY;
    }

    public Node( T data, FileType type ) {
        this( null, data, type );
    }

    public Node( Node<File> parent, T data, FileType type ) {
        this.data = data;
        this.type = type;
        this.parent = parent;
    }

    public void addNode( Node node ) {
        if( files==null ) files = new ArrayList<Node>();
        files.add( new Node((Node<File>) this, node.getData(), FileType.FILE ));
    }

    public void addDirectory( File directory ) {
        if( directories==null ) directories = new ArrayList<Node<File>>();
        directories.add( new Node<>((Node<File>) this, directory, FileType.DIRECTORY ));
    }

    public T getData() {
        return data;
    }

    public Node<File> getParent() {
        return parent;
    }

    public FileType getType() {
        return type;
    }
}

