import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas on 13.12.2015.
 */
public class SGModel {

    private SpritesheetGenerator controller = null;

    private List<BufferedImage> sprites = null;
    private List<Node<File>> directories = null;

    private Node tree = null;
    private int customFilterValue = 30; //global custom file filter for all spritesheets equally.

    private int currentDirectoryIndex = 0;
    private String globalExportDirectory = "";



    public enum FileFilterState {
        ALL,
        EVEN,
        ODD,
        CUSTOM;

        private static FileFilterState[] vals = values();

        public FileFilterState next() {
            return vals[(this.ordinal()+1)%vals.length];
        }
    }

    private FileFilterState ffstate = null;

    private List<Integer> heights = null;
    private List<Integer> widths = null;


    public SGModel( SpritesheetGenerator controller ) {
        this.controller = controller;
        sprites = new ArrayList<BufferedImage>();
        heights = new ArrayList<>();
        widths = new ArrayList<>();
        directories = new ArrayList<>();
        ffstate = FileFilterState.ALL;
    }

    public void setGlobalExportDirectory(String directory) {
        this.globalExportDirectory = directory;
    }

    public String getGlobalExportDirectory() {
        return this.globalExportDirectory;
    }

    public int nextState() {
        ffstate = ffstate.next();
        return ffstate.ordinal();
    }

    public FileFilterState getFfstate() {
        return ffstate;
    }

    public void reset() {
        sprites = new ArrayList<BufferedImage>();
        heights = new ArrayList<>();
        widths = new ArrayList<>();
        directories = new ArrayList<>();
        //ffstate = FileFilterState.ALL;
    }

    public void releaseFiles() {
        for( Node<File> directory : directories ) {
            if( directory.hasFiles() ) directory.clearFiles();
        }
    }

    public List<BufferedImage> getSprites() {
        return sprites;
    }

    public void setSprites( List<BufferedImage> sprites ) {
        this.sprites = sprites;
    }

    public void addSprite( BufferedImage sprite ) {
        if( sprite!= null ) sprites.add( sprite );
    }

    public void addHeight( int height ) {
        heights.add( height );
    }

    public void addWidth( int width ) {
        widths.add( width );
    }

    public void addDirectory( Node<File> directory ) {
        if( directory!=null ) this.directories.add( directory );
    }

    public List<Node<File>> getDirectories() {
        return directories;
    }

    public boolean heightsAreEqual() {
        if( heights!=null && !heights.isEmpty() ) {
            int mHeight = heights.get(0);
            for (int height : heights) {
                if( mHeight!=height ) return false;
            }
            return true;
        }
        return false;
    }

    public boolean widthsAreEqual() {
        if( widths != null && !widths.isEmpty() ) {
            int mWidth = widths.get(0);
            for( int width : widths ) {
                if( mWidth != width ) return false;
            }
            return true;
        }
        return false;
    }

    public boolean hasSprites() {
        if( sprites!=null && !sprites.isEmpty() ) {
            return true;
        }
        return false;
    }

    public int getTotalWidth() {
        return getWidthTo( sprites.size() );
    }

    /**
     * Cumulative width until that index.
     * @param index
     * @return
     */
    public int getWidthTo( int index ) {
        int totalWidth = 0;
        if( index > sprites.size() ) index = sprites.size();
        if( sprites != null && !sprites.isEmpty() ) {
            for( int i=0; i<index; i++ ) {
                totalWidth += sprites.get(i).getWidth();
            }
            return totalWidth;
        }
        return -1;
    }

    public int getHeight() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getHeight();
        }
        return -1;
    }

    public int getType() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getType();
        }
        return -1;
    }

    public int getWidth() {
        if( sprites != null && !sprites.isEmpty() ) {
            return sprites.get(0).getWidth();
        }
        return -1;
    }

    public void setCustomFilterValue( int value ) {
        this.customFilterValue = value;
    }

    public int getCustomFilterValue() {
        return this.customFilterValue;
    }

    public Node<File> getTree() {
        return tree;
    }

    public void setTree( Node<File> tree ) {
        this.tree = tree;
    }

    public void setCurrentDirectoryIndex( int index ) {
        this.currentDirectoryIndex = index;
    }

    public int getCurrentDirectoryIndex() {
        return currentDirectoryIndex;
    }

    /**
     * Increases the currentDirectoryIndex.
     * Returns false, if the current Index is already the last element.
     * @return
     */
    public boolean incDirectoryIndex() {
        if( currentDirectoryIndex == directories.size()-1 ) return false;
        currentDirectoryIndex++;
        return true;
    }

    public Node<File> getCurrentDirectory() {
        if(directories==null || directories.isEmpty()) return null;
        return directories.get(currentDirectoryIndex);
    }

    public void decDirectoryIndex() {
        if( currentDirectoryIndex > 0 ) currentDirectoryIndex--;
    }

}
