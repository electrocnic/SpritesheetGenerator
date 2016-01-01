import java.awt.image.BufferedImage;
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
    private boolean isActive = true;

    public enum FileType {
        FILE,
        DIRECTORY;
    }

    public Node( T data, FileType type ) {
        this( null, data, type );
    }

    public Node( Node<File> parent, T data, FileType type ) {
        this( parent, data, type, null, null, 1, 0, "" );
    }

    public Node( Node<File> parent, T data, FileType type, List<Node> files, List<Node<File>> directories, int customFilterValue, int totalFileSize, String destinationPath ) {
        this.data = data;
        this.type = type;
        this.parent = parent;
        this.files = new ArrayList<Node>();
        if(files!=null) {
           for( Node file : files ) {
               this.files.add( file );
           }
        }
        this.directories = new ArrayList<Node<File>>();
        if(directories!=null) {
            for( Node<File> directory : directories ) {
                this.directories.add( directory );
            }
        }
        this.customFilterValue = customFilterValue;
        this.totalFileSize = totalFileSize;
        this.destinationPath = destinationPath;
    }

    public Node( Node<T> node ) {
        this(node.parent, node.data, node.type, node.files, node.directories, node.customFilterValue, node.totalFileSize, node.destinationPath);
    }

    public void addFile( Node node ) {
        if( node != null ) {
            if (files == null) files = new ArrayList<Node>();
            node.setParent( this );
            files.add(new Node(node));
        }
    }

    public void addFile( T file ) {
        if( file != null ) {
            if (files == null) files = new ArrayList<Node>();
            files.add(new Node((Node<File>) this, file, FileType.FILE));
        }
    }

    public void addDirectory( File directory ) {
        if( directory != null ) {
            if (directories == null) directories = new ArrayList<Node<File>>();
            directories.add(new Node((Node<File>) this, directory, FileType.DIRECTORY));
        }
    }

    public void addDirectory(Node node) {
        if (node != null) {
            if (directories == null) directories = new ArrayList<Node<File>>();
            node.setParent(this);
            directories.add(new Node(node));
        }
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

    public Node<BufferedImage> getFileAt( int index ) {
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

    public void updateCustomFilterValueToALL() {
        if( files!=null ) {
            this.customFilterValue = files.size();
        }
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

    public void setActive( boolean isActive ) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean hasSubDirectories() {
        return (directories != null && !directories.isEmpty());
    }

    public void setParent(Node<File> parent) {
        this.parent = parent;
    }

    public boolean hasFiles() {
        return ( files != null && !files.isEmpty() );
    }

    public int getTotalWidth() {
        int width =0;
        for( Node<BufferedImage> node : files ) {
            width += node.getData().getWidth();
        }
        return width;
    }

    public int getHeight() {
        if( files!=null && !files.isEmpty() ) return files.get(0).getHeight(); //TODO resolve empty array error?!
        else return 0;
    }
}

