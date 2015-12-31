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
    private int customFilterValue = 1; //local custom file filter, only for this spritesheet.
    private int totalFileSize = 0;
    private String destinationPath = "";

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
        files = new ArrayList<Node>();
        directories = new ArrayList<Node<File>>();
    }

    public void addFile( Node node ) {
        if( files==null ) files = new ArrayList<Node>();
        files.add(new Node((Node<File>) this, node.getData(), FileType.FILE));
    }

    public void addFile( T file ) {
        if( files==null ) files = new ArrayList<Node>();
        files.add(new Node((Node<File>) this, file, FileType.FILE));
    }

    public void addDirectory( File directory ) {
        if( directories==null ) directories = new ArrayList<Node<File>>();
        directories.add( new Node((Node<File>) this, directory, FileType.DIRECTORY ));
    }

    public void addDirectory( Node node ) {
        if( directories==null ) directories = new ArrayList<Node<File>>();
        directories.add( new Node((Node<File>) this, node.getData(), FileType.DIRECTORY ));
    }

    public void setData( T data ) {
        this.data = data;
    }

    public void setType( FileType type ) {
        this.type = type;
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

    public Node getFileAt( int index ) {
        if( index < files.size() ) {
            return files.get(index);
        }else return null;
    }

    public Node<File> getDirectoryAt( int index ) {
        if( index < directories.size() ) {
            return directories.get( index );
        }else return null;
    }

    /**
     * Sets the size of this node. The size of this node represents only the total size of all FILES in this node,
     * not including subdirectories. If this node IS already a file, this value represents the size of this one file.
     * @param size
     */
    public void setFileSize( int size ) {
        this.totalFileSize = size;
    }

    /**
     * Gets the size of this node. The size of this node represents only the total size of all FILES in this node,
     * not including subdirectories. If this node IS already a file, this value represents the size of this one file.
     * @return
     */
    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setCustomFilterValue( int value ) {
        this.customFilterValue = value;
    }

    public int getCustomFilterValue() {
        return this.customFilterValue;
    }

    public int getFileAmount() {
        return files.size();
    }

    public int getDirectoryAmount() {
        return directories.size();
    }

    /**
     * Gets the path of the directory, where the final spritesheet for this node's elements should be located.
     * @return
     */
    public String getDestinationPath() {
        return destinationPath;
    }

    /**
     * Sets the path of the directory, where the final spritesheet for this node's elements should be located.
     * @param destinationPath
     */
    public void setDestinationPath( String destinationPath ) {
        this.destinationPath = destinationPath;
    }
}

