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
    private int customFilterValue = 0; //local custom file filter, only for this spritesheet.
    private int totalFileSize = 0;
    private String destinationPath = "";
    private boolean isActive = true;
    private boolean lastAlwaysActive = true;
    private boolean firstAlwaysActive = true;
    private List<Integer> customIndizes = null;

    private boolean customFilterChanged = false;
    private boolean customDirectoryChanged = false;

    private String pathHead = "";
    private String pathBody = "";



    public enum FileType {
        FILE,
        DIRECTORY;
    }

    public Node( T data, FileType type ) {
        this( null, data, type );
    }

    public Node( Node<File> parent, T data, FileType type ) {
        this( parent, data, type, null, null, 0, 0, "" );
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
        this.customIndizes = new ArrayList<>();
    }

    public Node( Node<T> node ) {
        this(node.parent, node.data, node.type, node.files, node.directories, node.customFilterValue, node.totalFileSize, node.destinationPath);
    }

    public String getPathBody() {
        return pathBody;
    }

    public void setPathBody(String pathBody) {
        this.pathBody = pathBody;
    }

    public String getPathHead() {
        return pathHead;
    }

    public void setPathHead(String pathHead) {
        this.pathHead = pathHead;
    }

    public boolean isCustomDirectoryChanged() {
        return customDirectoryChanged;
    }

    public void setCustomDirectoryChanged(boolean customDirectoryChanged) {
        this.customDirectoryChanged = customDirectoryChanged;
    }

    public void clearFiles() {
        this.files = new ArrayList<>();
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
        if( value > files.size() ) this.customFilterValue = files.size();
        else if (value < 0) this.customFilterValue=0;
        else this.customFilterValue = value;
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

    /**
     * Recalculates the custom Filter dependent cumulative width of the sprites in this directory.
     * Thus it also resets and updates the indizes list.
     * @return
     */
    public int getCustomWidth() {
        int cumulativeWidth = 0;
        customIndizes = new ArrayList<>();
        if( this.type==FileType.DIRECTORY && hasFiles() ) {
            float startOffset = 0;
            float customIndex = 0;
            float nte = (float) files.size() / (float) customFilterValue;
            if (nte < 1) nte = 1;

            startOffset = (nte - 1) / 2;
            customIndex += startOffset;

            List<BufferedImage> images = new ArrayList<>();
            BufferedImage image=null;
            if (firstAlwaysActive && customIndex >= 1) {
                image = (BufferedImage) files.get(0).getData();
                images.add( image );
                cumulativeWidth+=image.getWidth();
                customIndizes.add( 0 );
            }
            boolean lastAdded = false;
            int i = (int)customIndex;
            for ( ; i < getFileAmount(); ) {
                System.out.println("Active Spritesheet now loads the individual images: " + i + ",  nte = " + nte + ",  customIndex = " + customIndex);
                image = getFileAt(i).getData();
                customIndizes.add( i );
                images.add(image);
                if (i == getFileAmount() - 1) lastAdded = true;
                cumulativeWidth += image.getWidth();
                customIndex += nte;
                i = (int) customIndex;
            }
            if( lastAlwaysActive() && !lastAdded ) {
                image = getFileAt(getFileAmount() - 1).getData();
                customIndizes.add( getFileAmount() - 1 );
                images.add( image );
                cumulativeWidth += image.getWidth();
            }
        }
        return cumulativeWidth;
    }

    public List<Integer> getCustomIndizes() {
        getCustomWidth();
        return customIndizes;
    }

    public int getHeight() {
        if( this.type==FileType.DIRECTORY ) {
            if (files != null && !files.isEmpty()) {
                BufferedImage image = (BufferedImage) files.get(0).getData();
                return image.getHeight();
            }
        }else if( this.type==FileType.FILE ) {
            BufferedImage image = (BufferedImage)data;
            if( image!=null ) return image.getHeight();
        }
        return 0;
    }

    public boolean firstAlwaysActive() {
        return firstAlwaysActive;
    }

    public void setFirstAlwaysActive( boolean isActive ) {
        this.firstAlwaysActive = isActive;
    }

    public boolean lastAlwaysActive() {
        return lastAlwaysActive;
    }

    public void setLastAlwaysActive( boolean isActive ) {
        this.lastAlwaysActive = isActive;
    }

    public boolean isCustomFilterChanged() {
        return customFilterChanged;
    }

    public void setCustomFilterChanged(boolean customFilterChanged) {
        this.customFilterChanged = customFilterChanged;
    }
}

